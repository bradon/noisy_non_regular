package edu.monash.bthal2.repeatedPD.newDPDA;

import java.util.ArrayList;
import java.util.Stack;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.repeatedgames.evolution.Action;
import com.evolutionandgames.repeatedgames.evolution.RepeatedStrategy;

/**
 * Agent that plays Repeated Strategy using a Deterministic Pushdown Austomata
 * model
 * 
 * @author Bradon Hall
 * 
 */
public class DPDA implements Agent, RepeatedStrategy {

	static int MAX_PATH_SIZE = 10;

	final static int NULL_MARKER = -1;
	final static int STACK_MARKER = -2;
	final static int MAX_PATH_LENTH = 10; // Path before we assume non halting

	final static Action DEFAULT_ACTION = Action.DEFECT; // Perform if halting
	final static int[] STACK_ALPHABET = { 0, NULL_MARKER, STACK_MARKER };

	final static char EMPTY_INPUT = 'l';
	final static char R = 'R';
	final static char S = 'S';
	final static char T = 'T';
	final static char P = 'P';
	final static char[] INPUT_ALPHABET = { R, T, S, P, EMPTY_INPUT };
	final static Action ACTION_ON_HALT = Action.DEFECT;
	private State currentState;

	private boolean halted = false;

	/**
	 * @return the states
	 */
	public ArrayList<State> getStates() {
		return states;
	}

	public DPDA(String definition) {
		// Split into states
		String[] stateStrings = definition.split(Parser.STATE_SEPERATOR);
		// Build states first
		for (String str : stateStrings) {
			// Allow comments/whitespace
			// Comments not preserved on conversion to DPDA
			if (str != "" && str.substring(0, 2) != "\\") {
				states.add(new State());
			}
		}
		for (String str : stateStrings) {
			if (str != "" && str.substring(0, 2) != "\\") {
				// Split into parts of the state definition
				String[] currentStateStrings = str
						.split(Parser.TRANSITION_SEPERATOR);
				// Get the state basics
				// Index 0 state number, 1 Default, 2 Final/Non
				// 1 is currently ignored (static final)
				String[] stateBasics = currentStateStrings[0]
						.split(Parser.STATE_INTERNAL_SEPERATOR);
				int sourceState = Integer.valueOf(stateBasics[0]);
				if (stateBasics[2].equals(Parser.FINAL.F.toString())) {
					// Is final - flip state
					states.get(sourceState).flip();
				}
				// Build transitions
				for (int i = 1; i < currentStateStrings.length; i++) {
					String[] transitionParts = currentStateStrings[i]
							.split(Parser.TRANSITION_INTERNAL_SEPERATOR);
					if (transitionParts[0].length() != 1) {
						throw new RuntimeException("Multiple Char read");
					}
					char read = transitionParts[0].charAt(0);
					int pop = Integer.valueOf(transitionParts[1]);
					int push = Integer.valueOf(transitionParts[2]);
					State destination = states.get(Integer
							.valueOf(transitionParts[3]));
					Transition newTransition = new Transition(read, pop, push,
							destination);
					states.get(sourceState).getTransitions().add(newTransition);
				}
			}
			reset();
		}
	}

	public DPDA() {
		stack.push(DPDA.STACK_MARKER);
	}

	// Will print out sparse table
	public String printTable() {

		// What is javas string builder?
		String returnString = "";
		for (State state : states) {

			for (Transition transition : state.getTransitions()) {
				if (state.stateAction() == Action.COOPERATE) {
					returnString = returnString + "F,";
				} else {
					returnString = returnString + "N,";
				}
				returnString = returnString + states.indexOf(state) + ","
						+ states.indexOf(transition.destination) + ","
						+ transition.read + "," + transition.pop + ","
						+ transition.push + "\n";
			}

		}
		return returnString;
	}

	// I recall issues with clone() and have avoided for now
	public DPDA copyDPDA() {
		// Create new DPDA
		DPDA copy = new DPDA();
		// Copy states (not transitions)
		for (State state : states) {
			State newState = new State();
			if (state.stateAction() == Action.COOPERATE) {
				newState.flip();
			}
			copy.getStates().add(newState);
		}
		// Copy transitions
		for (State state : states) {
			for (Transition transition : state.getTransitions()) {
				State destinationState = copy.getStates().get(
						states.indexOf(transition.destination));
				int sourceState = states.indexOf(state);
				Transition newTransition = new Transition(transition.read,
						transition.pop, transition.push, destinationState);
				copy.getStates().get(sourceState).getTransitions()
						.add(newTransition);
			}
		}
		return copy;
	}

	public String toString() {
		// Machine readable
		String returnString = "";
		for (State state : states) {
			returnString = returnString + states.indexOf(state)
					+ Parser.STATE_INTERNAL_SEPERATOR;
			if (DEFAULT_ACTION == Action.COOPERATE) {
				returnString = returnString + Parser.DEFAULT.C;
			} else {
				returnString = returnString + Parser.DEFAULT.D;
			}
			if (state.stateAction() == Action.COOPERATE) {
				returnString = returnString + Parser.STATE_INTERNAL_SEPERATOR
						+ Parser.FINAL.F;
			} else {
				returnString = returnString + Parser.STATE_INTERNAL_SEPERATOR
						+ Parser.FINAL.N;
			}
			for (Transition transition : state.getTransitions()) {

				returnString = returnString + Parser.TRANSITION_SEPERATOR
						+ transition.read
						+ Parser.TRANSITION_INTERNAL_SEPERATOR + transition.pop
						+ Parser.TRANSITION_INTERNAL_SEPERATOR
						+ transition.push
						+ Parser.TRANSITION_INTERNAL_SEPERATOR
						+ states.indexOf(transition.destination);
			}
			returnString = returnString + Parser.STATE_SEPERATOR;
		}
		return returnString;
	}

	private ArrayList<State> states = new ArrayList<State>();
	private Stack<Integer> stack = new Stack<Integer>();

	public State getCurrentState() {
		if (currentState != null) {
			return currentState;
		} else {
			if (states.size() > 0) {
				return states.get(0);
			}
		}
		return null;
	}

	/**
	 * Action at current DPDA state
	 */
	public Action currentAction() {
		if (halted) {
			return DEFAULT_ACTION;
		}
		if (getCurrentState() != null) {
			return getCurrentState().stateAction();
		} else {
			return null;
		}
	}

	/**
	 * This method updates the current state based on the input actions.
	 */
	public void next(Action focal, Action other) {
		currentState = getCurrentState();

		if (currentState == null) {
			throw new RuntimeException("This is a problem!");
		}
		if (halted) { // If halted no action required
			return;
		}

		// Determine from actions what the input string is
		char historyMove = determineHistoryMove(focal, other);
		// System.out.println("Stack " + stack.peek() + " move " + historyMove);
		boolean hasExhaustedInputString = false;
		int path_size = 0;
		while (path_size < MAX_PATH_SIZE) {

			path_size++;

			Transition transition;
			if (stack.isEmpty()) {
				transition = currentState.validTransitions(historyMove,
						DPDA.NULL_MARKER);
			} else {
				transition = currentState.validTransitions(historyMove,
						stack.peek());
			}
			if (transition == null) {
				if (!hasExhaustedInputString) {
					// Did not consume input
					System.out.println("DPDA did not consume input");
					System.out.println(toString());
					System.out.println("Stack: " + stack);
					throw new RuntimeException("DPDA did not consume input");
				}
				// If no valid transitions exist we stop
				return;
			}
			// Transition valid, follow it
			if (transition.read != DPDA.EMPTY_INPUT) {
				// Consume input
				historyMove = DPDA.EMPTY_INPUT;
				hasExhaustedInputString = true;
			}
			currentState = transition.destination;
			if (hasExhaustedInputString) {
				// If we have consumed input we only need to find an
				// accepting configuration then we stop
				if (currentState.stateAction() == Action.COOPERATE) {
					return;
				}
			}
		}
		// Record Max Path Exceeded
		halted = true;
	}

	/**
	 * Convert tuple of Actions to Input Alphabet Char
	 * 
	 * @param focal
	 * @param other
	 * @return
	 */
	private char determineHistoryMove(Action focal, Action other) {
		if (focal == Action.DEFECT && other == Action.DEFECT) {
			return P;
		} else if (focal == Action.DEFECT && other == Action.COOPERATE) {
			return T;
		} else if (focal == Action.COOPERATE && other == Action.DEFECT) {
			return S;
		} else if (focal == Action.COOPERATE && other == Action.COOPERATE) {
			return R;
		} else {
			// Unreachable
			throw new RuntimeException("This is a problem!");
		}
	}

	/**
	 * Reset DPDA state
	 * 
	 * Clears the stack and resets the current state
	 */
	public void reset() {
		halted = false;
		stack.clear();
		stack.push(DPDA.STACK_MARKER);
		currentState = null;
		currentState = getCurrentState();
	}

	/**
	 * Hash code to identify unique machines
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + states.indexOf(currentState);
		for (State state : states) {
			result = prime * result + state.stateAction().hashCode();
			for (Transition transition : state.getTransitions()) {
				result = prime * result + transition.pop;
				result = prime * result + transition.push;
				result = prime * result + transition.read;
				result = prime * result
						+ states.indexOf(transition.destination);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DPDA)) {
			return false;
		}

		DPDA other = (DPDA) obj;
		if (hashCode() == other.hashCode()) {
			return true;
		}
		// TODO: Clean up this comparison
		if (currentState == null) {
			if (other.currentState != null) {
				return false;
			}
		} else if (!currentState.equals(other.currentState)) {
			return false;
		}
		if (stack == null) {
			if (other.stack != null) {
				return false;
			}
		} else if (!stack.equals(other.stack)) {
			return false;
		}
		if (states == null) {
			if (other.states != null) {
				return false;
			}
		} else if (!states.equals(other.states)) {
			return false;
		}
		return true;
	}

}

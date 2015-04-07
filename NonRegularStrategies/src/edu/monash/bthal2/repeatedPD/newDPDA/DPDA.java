package edu.monash.bthal2.repeatedPD.newDPDA;

import java.util.ArrayList;
import java.util.Stack;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.repeatedgames.evolution.Action;
import com.evolutionandgames.repeatedgames.evolution.RepeatedStrategy;

public class DPDA implements Agent, RepeatedStrategy {

	// TODO: Define behaviour in null cases

	static int MAX_PATH_SIZE = 10;

	final static int EMPTY_STACK = -1;

	final static int[] STACK_ALPHABET = { 0, EMPTY_STACK };

	final static char EMPTY_INPUT = 'l';
	final static char R = 'R';
	final static char S = 'S';
	final static char T = 'T';
	final static char P = 'P';
	final static char[] INPUT_ALPHABET = { R, T, S, P, EMPTY_INPUT };

	private State currentState;

	/**
	 * @return the states
	 */
	public ArrayList<State> getStates() {
		return states;
	}

	public DPDA copyDPDA() {
		// Create new DPDA
		DPDA copy = new DPDA();
		// Copy states (not transitions)
		for (State state : states) {
			State newState = new State();
			if (state.currentAction() == Action.COOPERATE) {
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
		// What is javas string builder?
		String returnString = "";
		for (State state : states) {
			if (state.currentAction() == Action.COOPERATE) {
				returnString = returnString + "Final State ";
			}
			for (Transition transition : state.getTransitions()) {

				returnString = returnString + states.indexOf(state) + " to "
						+ states.indexOf(transition.destination) + " read "
						+ transition.read + " pop " + transition.pop + " push "
						+ transition.push + "\n";
			}
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

	public Action currentAction() {
		if (getCurrentState() != null) {
			return getCurrentState().currentAction();
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

		// Determine from actions what the input string is
		char historyMove = determineHistoryMove(focal, other);
		boolean hasExhaustedInputString = false;
		int path_size = 0;
		while (path_size < MAX_PATH_SIZE) {

			path_size++;

			// TODO: verify one valid transition
			Transition transition;
			// TODO: define stack behavior clearly. Is stack marker enforced?
			if (stack.isEmpty()) {
				transition = currentState.validTransition(historyMove,
						DPDA.EMPTY_STACK);
			} else {
				transition = currentState.validTransition(historyMove,
						stack.peek());
			}
			if (transition == null) {
				if (!hasExhaustedInputString) {
					// Did not consume input
					// Should never occur (IF we force to always have a
					// transition)
					System.out.println("DPDA did not consume input");
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
				if (currentState.currentAction() == Action.COOPERATE) {
					return;
				}
			}
		}
		throw new RuntimeException("Max path exceeded - halting issue?");
	}

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
			throw new RuntimeException("This is a problem!");
		}
	}

	public void reset() {
		stack.clear();
		currentState = null;
		currentState = getCurrentState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentState == null) ? 0 : currentState.hashCode());
		result = prime * result + ((stack == null) ? 0 : stack.hashCode());
		result = prime * result + ((states == null) ? 0 : states.hashCode());
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

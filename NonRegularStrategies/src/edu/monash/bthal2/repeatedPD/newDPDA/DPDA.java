package edu.monash.bthal2.repeatedPD.newDPDA;

import java.util.ArrayList;
import java.util.Stack;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.repeatedgames.evolution.Action;
import com.evolutionandgames.repeatedgames.evolution.RepeatedStrategy;

public class DPDA implements Agent, RepeatedStrategy {
	// TODO: Define behaviour in null cases

	// TODO: copy mechanism: ensure hash preservation
	static char empty_input = 'l';
	static int empty_stack = -1;
	static int stack_marker = -2;
	State currentState;
	ArrayList<State> states = new ArrayList<State>();
	Stack<Integer> stack = new Stack<Integer>();

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

	@Override
	public Action currentAction() {
		if (getCurrentState() != null) {
			return getCurrentState().currentAction();
		} else {
			return null;
		}
	}

	@Override
	public void next(Action focal, Action other) {
		if (currentState == null) {
			return;
		}
		char input;
		if (focal == Action.DEFECT) {
			if (other == Action.DEFECT) {
				input = 'P';
			} else {
				input = 'T';
			}
		} else {
			if (other == Action.DEFECT) {
				input = 'S';
			} else {
				input = 'R';
			}
		}
		boolean has_read = false;
		int max_path = 10;
		int path_size = 0;
		while (true) {
			if (path_size > max_path) {
				System.out.println("DPDA appeared not to halt");
				return;
			}
			path_size++;

			// TODO: verify one valid transition
			Transition transition;
			// TODO: define stack behavior clearly. Is stack marker enforced?
			if (stack.isEmpty()) {
				transition = currentState.valid_transition(input,
						DPDA.empty_stack);
			} else {
				transition = currentState.valid_transition(input, stack.peek());
			}
			if (transition == null) {
				if (!has_read) {
					// Did not consume input
					// Should never occur (IF we force to always have a
					// transition)
					System.out.println("DPDA did not consume input");
				}
				// If no valid transitions exist we stop
				return;
			}
			// Transition valid, follow it
			if (transition.read != DPDA.empty_input) {
				// Consume input
				input = DPDA.empty_input;
				has_read = true;
			}
			currentState = transition.destination;
			if (has_read) {
				// If we have consumed input we only need to find an
				// accepting configuration then we stop
				if (currentState.currentAction() == Action.COOPERATE) {
					return;
				}
			}
		}

	}

	@Override
	public void reset() {
		stack.clear();
		currentState = null;
		currentState = getCurrentState();
	}

}

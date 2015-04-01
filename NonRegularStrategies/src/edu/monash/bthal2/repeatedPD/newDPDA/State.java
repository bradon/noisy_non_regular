package edu.monash.bthal2.repeatedPD.newDPDA;

import java.util.ArrayList;

import com.evolutionandgames.repeatedgames.evolution.Action;

public class State {
	ArrayList<Transition> transitions = new ArrayList<Transition>();
	private Action action = Action.DEFECT;

	public Action currentAction() {
		return action;
	}

	public void flip() {
		if (action == Action.DEFECT) {
			action = Action.COOPERATE;
		} else {
			action = Action.DEFECT;
		}
	}

	public Transition valid_transition(char read, int stack_top) {
		// Loop through transitions until we find one that works
		// Return the one that works
		for (Transition transition : transitions) {
			if (read == transition.read || transition.read == DPDA.empty_input) {
				if (stack_top == transition.pop
						|| transition.pop == DPDA.empty_stack) {
					return transition;
				}
			}
		}
		return null;

	}
}
package edu.monash.bthal2.repeatedPD.newDPDA;

import java.util.ArrayList;

import com.evolutionandgames.repeatedgames.evolution.Action;

/**
 * State of a DPDA
 * 
 * @author Bradon Hall
 * 
 */
public class State {
	private Action action = Action.DEFECT;
	private ArrayList<Transition> transitions = new ArrayList<Transition>();

	/**
	 * Action returned by this state
	 * 
	 * @return
	 */
	public Action stateAction() {
		return action;
	}

	/**
	 * Change action returned by this state
	 */
	public void flip() {
		if (action == Action.DEFECT) {
			action = Action.COOPERATE;
		} else {
			action = Action.DEFECT;
		}
	}

	/**
	 * Looks at all the transitions that could result from the current
	 * configuration, and returns the first valid one.
	 * 
	 * @param read
	 * @param stack_top
	 * @return
	 */
	public Transition validTransition(char read, int stack_top) {
		for (Transition transition : getTransitions()) {
			if (read == transition.read || transition.read == DPDA.EMPTY_INPUT) {
				if (stack_top == transition.pop
						|| transition.pop == DPDA.NULL_MARKER) {
					return transition;
				}
			}
		}
		throw new RuntimeException("No valid transitions");
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
		if (!(obj instanceof State)) {
			return false;
		}
		State other = (State) obj;
		if (action != other.action) {
			return false;
		}
		if (getTransitions() == null) {
			if (other.getTransitions() != null) {
				return false;
			}
		} else if (!getTransitions().equals(other.getTransitions())) {
			return false;
		}
		return true;
	}

	/**
	 * Transitions from this State
	 * 
	 * @return Transitions
	 */
	public ArrayList<Transition> getTransitions() {
		return transitions;
	}

	/**
	 * Set the transitions array
	 * 
	 * @param transitions
	 */
	public void setTransitions(ArrayList<Transition> transitions) {
		this.transitions = transitions;
	}
}
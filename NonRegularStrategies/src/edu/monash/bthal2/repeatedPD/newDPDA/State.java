package edu.monash.bthal2.repeatedPD.newDPDA;

import java.util.ArrayList;

import com.evolutionandgames.repeatedgames.evolution.Action;

public class State {
	
	private Action action = Action.DEFECT;
	private ArrayList<Transition> transitions = new ArrayList<Transition>();
	
	

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

	/**
	 * Looks at all the transitions that could result from the current configuration, and returns the first
	 * valid one. 
	 * @param read
	 * @param stack_top
	 * @return
	 */
	public Transition validTransition(char read, int stack_top) {
		//TODO: Build all transitions and throw an error if there is more than one valid. 
		for (Transition transition : getTransitions()) {
			if (read == transition.read || transition.read == DPDA.EMPTY_INPUT) {
				if (stack_top == transition.pop
						|| transition.pop == DPDA.EMPTY_STACK) {
					return transition;
				}
			}
		}
		return null;

	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result
				+ ((getTransitions() == null) ? 0 : getTransitions().hashCode());
		return result;
	}

	/* (non-Javadoc)
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

	public ArrayList<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(ArrayList<Transition> transitions) {
		this.transitions = transitions;
	}
}
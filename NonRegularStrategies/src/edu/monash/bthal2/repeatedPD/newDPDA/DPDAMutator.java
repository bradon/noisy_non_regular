package edu.monash.bthal2.repeatedPD.newDPDA;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentMutator;
import com.evolutionandgames.jevodyn.utils.Random;

public class DPDAMutator implements AgentMutator {

	@Override
	public Agent mutate(Agent arg0) {
		// Build a mutation chain

		// Apply mutations
		return null;
	}

	public void addState(DPDA dpda) {
		// Add a state to the DPDA and make it reachable
		// TODO: Random should use the seeded random method
		State newState = new State();
		dpda.states.add(newState);
		int random_state_index = Random.nextInt(dpda.states.size());
		int random_transition_index = Random.nextInt(dpda.states
				.get(random_state_index).transitions.size());
		dpda.states.get(random_state_index).transitions
				.get(random_transition_index).destination = newState;

		// TODO: generate random deterministic transitions
	}

	public void removeState(DPDA dpda) {
		// TODO: seeded random
		int random_state = Random.nextInt(dpda.states.size());
		State removedState = dpda.states.get(random_state);
		dpda.states.remove(removedState);
		for (State state : dpda.states) {
			for (Transition transition : state.transitions) {
				if (transition.destination == removedState) {
					// Assign a new destination randomly
					// TODO: Avoid self-empty transitions?
					random_state = Random.nextInt(dpda.states.size());
					transition.destination = dpda.states.get(random_state);
				}
			}
		}
	}

	public void change(DPDA dpda) {
		// Simple version: take existing transitions, change destination/push
		// randomly
		// 'Better?' version: take existing transitions, change all factors
		// randomly
		// while maintaining determinisim
	}
}

package edu.monash.bthal2.repeatedPD.newDPDA;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentMutator;
import com.evolutionandgames.jevodyn.utils.Random;

public class DPDAMutator implements AgentMutator {

	public DPDAMutator(double mutationProbabilityPerState,
			double addStatesProbability, double removeStatesProbability,
			double addTransitionProbability,
			double removeTransitionProbability, double changeReadProbability,
			double changePopProbability, double changePushProbability,
			double changeDestinationProbability, double flipState,
			double flipMachineResultProbability) {
		// TODO Auto-generated constructor stub
	}

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

		// TODO: Probabilities
		// First: Does it read?
		if (Random.nextBoolean()) {
			// Reads - Generate for all 4 read cases
			for (char input : DPDA.input_alphabet) {
				if (input != DPDA.empty_input) {

					if (Random.nextBoolean()) {
						// Pops
						for (int pop : DPDA.stack_alphabet) {
							if (pop != DPDA.empty_stack) {
								// Add transition
								int newDestination = Random.nextInt(dpda.states
										.size());
								int newPush = DPDA.stack_alphabet[Random
										.nextInt(DPDA.stack_alphabet.length)];
								Transition newTransition = new Transition(
										input, pop, newPush,
										dpda.states.get(newDestination));
								newState.transitions.add(newTransition);
							}
						}
					} else {
						// Does not pop
						// Add transition
						int newDestination = Random.nextInt(dpda.states.size());
						int newPush = DPDA.stack_alphabet[Random
								.nextInt(DPDA.stack_alphabet.length)];
						Transition newTransition = new Transition(input,
								DPDA.empty_stack, newPush,
								dpda.states.get(newDestination));
						newState.transitions.add(newTransition);
					}

				}
			}

		} else {
			// Does not read
			if (Random.nextBoolean()) {
				// Pops
				for (int pop : DPDA.stack_alphabet) {
					if (pop != DPDA.empty_stack) {
						// Add transition
						int newDestination = Random.nextInt(dpda.states.size());
						int newPush = DPDA.stack_alphabet[Random
								.nextInt(DPDA.stack_alphabet.length)];
						Transition newTransition = new Transition(
								DPDA.empty_input, pop, newPush,
								dpda.states.get(newDestination));
						newState.transitions.add(newTransition);
					}
				}
			} else {
				// Does not pop
				// Add transition
				int newDestination = Random.nextInt(dpda.states.size());
				int newPush = DPDA.stack_alphabet[Random
						.nextInt(DPDA.stack_alphabet.length)];
				Transition newTransition = new Transition(DPDA.empty_input,
						DPDA.empty_stack, newPush,
						dpda.states.get(newDestination));
				newState.transitions.add(newTransition);
			}

		}
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

		// Change types: Flip state, change push, change destination
		int random_state_index = Random.nextInt(dpda.states.size());
		State random_state = dpda.states.get(random_state_index);
		// Chance of flip, transition change
		// Select uniformly? Probably harder to analyse?
		int random_change = Random.nextInt(random_state.transitions.size() + 1);
		if (random_change == random_state.transitions.size()) {
			// 1/transitions+1 chance of flip
			random_state.flip();
		} else {
			Transition transition = random_state.transitions.get(random_change);
			if (Random.nextBoolean()) {
				// Switch destination
				// Self mutation possible (no change)
				transition.destination = dpda.states.get(Random
						.nextInt(dpda.states.size()));
			} else {
				// Switch push
				// Self mutation possible (no change)
				transition.push = DPDA.stack_alphabet[Random
						.nextInt(DPDA.stack_alphabet.length)];
			}
		}
	}
}

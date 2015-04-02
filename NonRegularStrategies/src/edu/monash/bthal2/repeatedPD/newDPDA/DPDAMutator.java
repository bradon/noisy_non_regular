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
		dpda.getStates().add(newState);
		int random_state_index = Random.nextInt(dpda.getStates().size());
		int random_transition_index = Random.nextInt(dpda.getStates()
				.get(random_state_index).getTransitions().size());
		dpda.getStates().get(random_state_index).getTransitions()
				.get(random_transition_index).destination = newState;

		// TODO: generate random deterministic transitions

		// TODO: Probabilities
		// First: Does it read?
		if (Random.nextBoolean()) {
			// Reads - Generate for all 4 read cases
			for (char input : DPDA.INPUT_ALPHABET) {
				if (input != DPDA.EMPTY_INPUT) {

					if (Random.nextBoolean()) {
						// Pops
						for (int pop : DPDA.STACK_ALPHABET) {
							if (pop != DPDA.EMPTY_STACK) {
								// Add transition
								int newDestination = Random.nextInt(dpda
										.getStates().size());
								int newPush = DPDA.STACK_ALPHABET[Random
										.nextInt(DPDA.STACK_ALPHABET.length)];
								Transition newTransition = new Transition(
										input, pop, newPush, dpda.getStates()
												.get(newDestination));
								newState.getTransitions().add(newTransition);
							}
						}
					} else {
						// Does not pop
						// Add transition
						int newDestination = Random.nextInt(dpda.getStates()
								.size());
						int newPush = DPDA.STACK_ALPHABET[Random
								.nextInt(DPDA.STACK_ALPHABET.length)];
						Transition newTransition = new Transition(input,
								DPDA.EMPTY_STACK, newPush, dpda.getStates()
										.get(newDestination));
						newState.getTransitions().add(newTransition);
					}

				}
			}

		} else {
			// Does not read
			if (Random.nextBoolean()) {
				// Pops
				for (int pop : DPDA.STACK_ALPHABET) {
					if (pop != DPDA.EMPTY_STACK) {
						// Add transition
						int newDestination = Random.nextInt(dpda.getStates()
								.size());
						int newPush = DPDA.STACK_ALPHABET[Random
								.nextInt(DPDA.STACK_ALPHABET.length)];
						Transition newTransition = new Transition(
								DPDA.EMPTY_INPUT, pop, newPush, dpda
										.getStates().get(newDestination));
						newState.getTransitions().add(newTransition);
					}
				}
			} else {
				// Does not pop
				// Add transition
				int newDestination = Random.nextInt(dpda.getStates().size());
				int newPush = DPDA.STACK_ALPHABET[Random
						.nextInt(DPDA.STACK_ALPHABET.length)];
				Transition newTransition = new Transition(DPDA.EMPTY_INPUT,
						DPDA.EMPTY_STACK, newPush, dpda.getStates().get(
								newDestination));
				newState.getTransitions().add(newTransition);
			}

		}
	}

	public void removeState(DPDA dpda) {
		// TODO: seeded random
		int random_state = Random.nextInt(dpda.getStates().size());
		State removedState = dpda.getStates().get(random_state);
		dpda.getStates().remove(removedState);
		for (State state : dpda.getStates()) {
			for (Transition transition : state.getTransitions()) {
				if (transition.destination == removedState) {
					// Assign a new destination randomly
					// TODO: Avoid self-empty transitions?
					random_state = Random.nextInt(dpda.getStates().size());
					transition.destination = dpda.getStates().get(random_state);
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
		int random_state_index = Random.nextInt(dpda.getStates().size());
		State random_state = dpda.getStates().get(random_state_index);
		// Chance of flip, transition change
		// Select uniformly? Probably harder to analyse?
		int random_change = Random
				.nextInt(random_state.getTransitions().size() + 1);
		if (random_change == random_state.getTransitions().size()) {
			// 1/transitions+1 chance of flip
			random_state.flip();
		} else {
			Transition transition = random_state.getTransitions().get(
					random_change);
			if (Random.nextBoolean()) {
				// Switch destination
				// Self mutation possible (no change)
				transition.destination = dpda.getStates().get(
						Random.nextInt(dpda.getStates().size()));
			} else {
				// Switch push
				// Self mutation possible (no change)
				transition.push = DPDA.STACK_ALPHABET[Random
						.nextInt(DPDA.STACK_ALPHABET.length)];
			}
		}
	}
}

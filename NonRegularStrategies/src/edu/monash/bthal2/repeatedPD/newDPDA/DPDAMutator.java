package edu.monash.bthal2.repeatedPD.newDPDA;

import java.util.ArrayList;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentMutator;
import com.evolutionandgames.jevodyn.utils.Random;

public class DPDAMutator implements AgentMutator {

	private static final double mutationProbabilityPerState = 0.1;

	private static double[] distrubutionOfEvents = { 0.3, 0.3, 0.4 };

	// New sig: Add, Change, Delete pr +??
	public DPDAMutator(double mutationProbabilityPerState,
			double addProbability, double removeProbability,
			double changeProbability) {
		distrubutionOfEvents = new double[3];
		distrubutionOfEvents[0] = addProbability;
		distrubutionOfEvents[1] = removeProbability;
		distrubutionOfEvents[2] = changeProbability;
	}

	public DPDAMutator() {

	}

	/**
	 * Types of Mutations that occur
	 * 
	 * @author Bradon Hall
	 * 
	 */
	private enum MutationEvent {
		ADD, REMOVE, CHANGE
	}

	public Agent mutate(Agent arg0) {
		// Copy?

		// Build a mutation chain
		ArrayList<MutationEvent> mutationChain = buildMutationChain(((DPDA) arg0)
				.getStates().size());
		// Apply mutations
		for (MutationEvent mutation : mutationChain) {
			switch (mutation) {
			case ADD:
				addState((DPDA) arg0);
				break;
			case CHANGE:
				change((DPDA) arg0);
				break;
			case REMOVE:
				removeState((DPDA) arg0);
				break;
			default:
				break;
			}
		}
		return arg0;
	}

	public void generateRandomTransitions(DPDA dpda, State state) {
		// Possible change: add set destination/pop
		// For switch from pop to non-pop and vice versa

		// TODO: Probabilities
		// First: Does it read?
		if (Random.nextBoolean()) {
			// Reads - Generate for all 4 read cases
			for (char input : DPDA.INPUT_ALPHABET) {
				if (input != DPDA.EMPTY_INPUT) {

					if (Random.nextBoolean()) {
						// Pops
						for (int pop : DPDA.STACK_ALPHABET) {
							if (pop != DPDA.NULL_MARKER) {
								// Add transition
								int newDestination = Random.nextInt(dpda
										.getStates().size());
								int newPush;
								if (pop != DPDA.STACK_MARKER) {

									newPush = DPDA.STACK_ALPHABET[Random
											.nextInt(DPDA.STACK_ALPHABET.length - 1)];
								} else {
									newPush = DPDA.STACK_MARKER;
								}
								Transition newTransition = new Transition(
										input, pop, newPush, dpda.getStates()
												.get(newDestination));
								state.getTransitions().add(newTransition);
							}
						}
					} else {
						// Does not pop
						// Add transition
						int newDestination = Random.nextInt(dpda.getStates()
								.size());
						int newPush = DPDA.STACK_ALPHABET[Random
								.nextInt(DPDA.STACK_ALPHABET.length - 1)];
						Transition newTransition = new Transition(input,
								DPDA.NULL_MARKER, newPush, dpda.getStates()
										.get(newDestination));
						state.getTransitions().add(newTransition);
					}

				}
			}

		} else {
			// Does not read
			if (Random.nextBoolean()) {
				// Pops
				for (int pop : DPDA.STACK_ALPHABET) {
					if (pop != DPDA.NULL_MARKER) {
						// Add transition
						int newDestination = Random.nextInt(dpda.getStates()
								.size());
						int newPush;
						if (pop != DPDA.STACK_MARKER) {

							newPush = DPDA.STACK_ALPHABET[Random
									.nextInt(DPDA.STACK_ALPHABET.length - 1)];
						} else {
							newPush = DPDA.STACK_MARKER;
						}
						Transition newTransition = new Transition(
								DPDA.EMPTY_INPUT, pop, newPush, dpda
										.getStates().get(newDestination));
						state.getTransitions().add(newTransition);
					}
				}
			} else {
				// Does not pop
				// Add transition
				int newDestination = Random.nextInt(dpda.getStates().size());
				int newPush = DPDA.STACK_ALPHABET[Random
						.nextInt(DPDA.STACK_ALPHABET.length)];
				Transition newTransition = new Transition(DPDA.EMPTY_INPUT,
						DPDA.NULL_MARKER, newPush, dpda.getStates().get(
								newDestination));
				state.getTransitions().add(newTransition);
			}

		}

	}

	public void addState(DPDA dpda) {
		// Add a state to the DPDA and make it reachable
		State newState = new State();
		int random_state_index;
		if (dpda.getStates().size() > 0) {
			random_state_index = Random.nextInt(dpda.getStates().size());
		} else {
			random_state_index = 0;
			dpda.getStates().add(newState);
			generateRandomTransitions(dpda, newState);
			return;
		}

		int random_transition_index = Random.nextInt(dpda.getStates()
				.get(random_state_index).getTransitions().size());
		dpda.getStates().get(random_state_index).getTransitions()
				.get(random_transition_index).destination = newState;
		dpda.getStates().add(newState);

		// TODO: Probabilities
		// First: Does it read?
		generateRandomTransitions(dpda, newState);
	}

	public void removeState(DPDA dpda) {
		if (dpda.getStates().size() < 2) {
			return;
		}

		int random_state = Random.nextInt(dpda.getStates().size());
		State removedState = dpda.getStates().get(random_state);
		dpda.getStates().remove(removedState);
		for (State state : dpda.getStates()) {
			for (Transition transition : state.getTransitions()) {
				if (transition.destination == removedState) {
					// Assign a new destination randomly
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
				if (transition.push != DPDA.STACK_MARKER) {
					transition.push = DPDA.STACK_ALPHABET[Random
							.nextInt(DPDA.STACK_ALPHABET.length - 1)];
				}
			}
		}
	}

	/**
	 * Build a chain of mutation events to be applied to the automata. One event
	 * is added per state with probability mutationProbabilityPerState.
	 * Therefore the expected size of a chain is size of the automata times the
	 * probability of mutation per state.
	 * 
	 * - From JG's FSA
	 * 
	 * @param size
	 * @return
	 */
	private ArrayList<MutationEvent> buildMutationChain(int size) {
		ArrayList<MutationEvent> mutationEvents = new ArrayList<MutationEvent>();
		for (int i = 0; i < size; i++) {
			if (Random.bernoulliTrial(mutationProbabilityPerState)) {
				int eventType = Random
						.simulateDiscreteDistribution(distrubutionOfEvents);
				switch (eventType) {
				case 0:
					mutationEvents.add(MutationEvent.ADD);
					break;
				case 1:
					mutationEvents.add(MutationEvent.REMOVE);
					break;
				case 2:
					mutationEvents.add(MutationEvent.CHANGE);
					break;
				}
			}
		}
		return mutationEvents;
	}
}

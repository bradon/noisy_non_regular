package edu.monash.bthal2.repeatedPD.newDPDA;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentBasedPopulation;
import com.evolutionandgames.agentbased.AgentBasedPopulationFactory;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulationImpl;

public class DPDAFactory implements AgentBasedPopulationFactory {

	private int popSize;

	public DPDAFactory(int popSize) {
		this.popSize = popSize;
	}

	public AgentBasedPopulation createPopulation() {
		Agent[] agents = new DPDA[popSize];
		for (int i = 0; i < popSize; i++) {
			agents[i] = ExampleStrategies.allD();
		}
		return new ExtensivePopulationImpl(agents);
	}

	public static class ExampleStrategies {
		public static DPDA tft() {
			DPDA dpda = new DPDA();
			State acceptState = new State();
			acceptState.flip();
			State rejectState = new State();

			// Read C
			Transition selfAcceptR = new Transition(DPDA.R, DPDA.STACK_MARKER,
					DPDA.STACK_MARKER, acceptState);
			Transition selfAcceptT = new Transition(DPDA.T, DPDA.STACK_MARKER,
					DPDA.STACK_MARKER, acceptState);
			Transition transitionT = new Transition(DPDA.T, DPDA.STACK_MARKER,
					DPDA.STACK_MARKER, acceptState);
			Transition transitionR = new Transition(DPDA.R, DPDA.STACK_MARKER,
					DPDA.STACK_MARKER, acceptState);
			acceptState.getTransitions().add(selfAcceptR);
			acceptState.getTransitions().add(selfAcceptT);
			rejectState.getTransitions().add(transitionT);
			rejectState.getTransitions().add(transitionR);

			// Read D
			Transition selfS = new Transition(DPDA.S, DPDA.STACK_MARKER,
					DPDA.STACK_MARKER, rejectState);
			Transition selfP = new Transition(DPDA.P, DPDA.STACK_MARKER,
					DPDA.STACK_MARKER, rejectState);
			Transition transitionS = new Transition(DPDA.S, DPDA.STACK_MARKER,
					DPDA.STACK_MARKER, rejectState);
			Transition transitionP = new Transition(DPDA.P, DPDA.STACK_MARKER,
					DPDA.STACK_MARKER, rejectState);
			acceptState.getTransitions().add(transitionS);
			acceptState.getTransitions().add(transitionP);
			rejectState.getTransitions().add(selfS);
			rejectState.getTransitions().add(selfP);
			dpda.getStates().add(acceptState);
			dpda.getStates().add(rejectState);
			dpda.reset();

			return dpda;
		}

		public static Agent allD() {
			DPDA dpda = new DPDA();
			State state = new State();
			dpda.getStates().add(state);
			state.getTransitions().add(
					new Transition(DPDA.P, DPDA.NULL_MARKER, DPDA.NULL_MARKER,
							state));
			state.getTransitions().add(
					new Transition(DPDA.R, DPDA.NULL_MARKER, DPDA.NULL_MARKER,
							state));
			state.getTransitions().add(
					new Transition(DPDA.S, DPDA.NULL_MARKER, DPDA.NULL_MARKER,
							state));
			state.getTransitions().add(
					new Transition(DPDA.T, DPDA.NULL_MARKER, DPDA.NULL_MARKER,
							state));
			return dpda;
		}
	}
}

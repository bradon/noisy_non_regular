package edu.monash.bthal2.repeatedPD.newDPDA;

import com.evolutionandgames.agentbased.AgentBasedPopulation;
import com.evolutionandgames.agentbased.AgentBasedPopulationFactory;

public class DPDAFactory implements AgentBasedPopulationFactory {

	public DPDAFactory(int populationSize) {
		// TODO Auto-generated constructor stub
	}

	public AgentBasedPopulation createPopulation() {
		// TODO Auto-generated method stub
		return null;
	}

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

}

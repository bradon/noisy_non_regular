package edu.monash.bthal2.repeatedPD.newDPDA;

import com.evolutionandgames.repeatedgames.evolution.Action;

public class Temp {
	public static void main(String[] args) {
		DPDA dpda = new DPDA();
		State state = new State();
		Transition transition = new Transition('R', DPDA.empty_stack,
				DPDA.empty_stack, state);
		state.transitions.add(transition);
		dpda.states.add(state);
		state.flip();
		State state2 = new State();
		dpda.states.add(state2);
		state.transitions.add(new Transition('T', DPDA.empty_stack,
				DPDA.empty_stack, state));
		state.transitions.add(new Transition('S', DPDA.empty_stack,
				DPDA.empty_stack, state2));
		state.transitions.add(new Transition('P', DPDA.empty_stack,
				DPDA.empty_stack, state2));
		state2.transitions.add(new Transition('R', DPDA.empty_stack,
				DPDA.empty_stack, state));
		state2.transitions.add(new Transition('T', DPDA.empty_stack,
				DPDA.empty_stack, state));
		state2.transitions.add(new Transition('S', DPDA.empty_stack,
				DPDA.empty_stack, state2));
		state2.transitions.add(new Transition('P', DPDA.empty_stack,
				DPDA.empty_stack, state2));
		System.out.println("Default");
		System.out.println(dpda.currentAction());
		dpda.next(Action.COOPERATE, Action.COOPERATE);
		System.out.println("R");
		System.out.println(dpda.currentAction());
		dpda.reset();
		dpda.next(Action.COOPERATE, Action.DEFECT);
		System.out.println("S");
		System.out.println(dpda.currentAction());
	}
}

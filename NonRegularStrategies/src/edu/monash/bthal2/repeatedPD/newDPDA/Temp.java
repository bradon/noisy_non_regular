package edu.monash.bthal2.repeatedPD.newDPDA;

import java.util.Stack;

public class Temp {

	public static void main(String[] args) {
		char[] input_alphabet = {'R','T','S','P','l'};
		int[] stack_alphabet = {0, 255, -1};
		State state = new State(input_alphabet, stack_alphabet);
		state.alter_transition('P', 0, 2);
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(0);
		System.out.println(state.get_next('P', stack));
	} 
}

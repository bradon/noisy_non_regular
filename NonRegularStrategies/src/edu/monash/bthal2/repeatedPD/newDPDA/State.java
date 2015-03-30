package edu.monash.bthal2.repeatedPD.newDPDA;

import java.util.Stack;

public class State {

	// Each state contains a column of transitions
	// One option: Limit to either empty or specified
	// Another option: Default to specified, allow both
	// Using latter, but enforcing in mutations

	private char[] input_alphabet;
	private int[] stack_alphabet;
	int transitions[];

	public State(char[] input_alphabet, int[] stack_alphabet) {
		this.input_alphabet = input_alphabet;
		this.stack_alphabet = stack_alphabet;
		// stack alphabet * input_alphabet transitions.
		transitions = new int[(stack_alphabet.length * input_alphabet.length)];
		for (int i = 0; i < transitions.length; i++) {
			transitions[i] = -1;
		}
	}

	public void alter_transition(char input, int stack_top, int destination) {
		this.alter_transition(
				this.index_input_alphabet(input) * this.stack_alphabet.length
						+ this.index_stack_alphabet(stack_top), destination);
	}

	public int get_next(char input, Stack<Integer> stack) {
		// Default: Transition found
		int destination = this.transitions[this.index_input_alphabet(input)
				* this.stack_alphabet.length
				+ this.index_stack_alphabet(stack.peek())];
		if (destination != -1) {
			return destination;
		}
		// Next case: Follow Non-Pop route
		destination = this.transitions[this.index_input_alphabet(input)
				* this.stack_alphabet.length + this.stack_alphabet.length - 1];
		if (destination != -1) {
			return destination;
		}
		// Next case: Follow No-Read Pop route
		destination = this.transitions[(this.input_alphabet.length - 1)
				* this.stack_alphabet.length
				+ this.index_stack_alphabet(stack.peek())];
		if (destination != -1) {
			return destination;
		}
		// Final case: Follow No-Read No-Pop route
		return this.transitions[this.transitions.length - 1];
	}

	public void alter_transition(int index, int destination) {
		this.transitions[index] = destination;
	}

	// Part of the idea of doing States in a table form is they will become
	// A part of the DPDA itself: States dont need to be an object
	// For now, thing about it as an object though
	private int index_input_alphabet(char letter) {
		for (int i = 0; i < input_alphabet.length; i++) {
			if (input_alphabet[i] == letter) {
				return i;
			}
		}
		return -1;
	}

	private int index_stack_alphabet(int letter) {
		for (int i = 0; i < stack_alphabet.length; i++) {
			if (stack_alphabet[i] == letter) {
				return i;
			}
		}
		return -1;
	}
}

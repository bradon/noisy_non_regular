package edu.monash.bthal2.repeatedPD.newDPDA;

/**
 * Transition to a State in a DPDA
 * 
 * @author Bradon Hall
 * 
 */
public class Transition {
	public int pop;
	public int push;
	public State destination;
	public char read;

	public Transition(char read, int pop, int push, State destination) {
		this.pop = pop;
		this.read = read;
		this.destination = destination;
		this.push = push;
	}
}

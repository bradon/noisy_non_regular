package edu.monash.bthal2.repeatedPD.newDPDA;

import static org.junit.Assert.*;

import org.junit.Test;

import com.evolutionandgames.jevodyn.utils.Random;
import com.evolutionandgames.repeatedgames.evolution.Action;

public class DPDATest {
	//@Test
	public void randomDPDATest() {
		Random.seed();
		DPDA dpda = randomDPDA();
		System.out.println(dpda);
		//System.out.println(dpda.getStates());
		//DPDAMutator mutator = new DPDAMutator();
		//mutator.addState(dpda);
		//System.out.println(dpda);
	}

	//@Test
	public void testCopyDPDA() {
		// A copy of a DPDA should have the same hash as a DPDA
		DPDA dpda = randomDPDA();

		System.out.println("Hash Original: " + dpda.hashCode());
		DPDA copy = dpda.copyDPDA();
		System.out.println("Hash Original: " + dpda.hashCode());
		System.out.println("Copy Hash " + copy.hashCode());
		System.out.println("Original :");
		System.out.println(dpda.toString());
		System.out.println("Copy :");
		System.out.println(copy.toString());
		System.out.println("Size :" + copy.getStates().size());
		assertTrue(dpda.hashCode() == copy.hashCode());

	}

	@Test
	public void testRandomInput() {
		DPDA dpda = randomDPDA();
		Random.seed();
		for (int i = 0; i < 100; i++) {
			Action focal;
			Action other;
			if (Random.nextBoolean()) {
				focal = Action.COOPERATE;
			} else {
				focal = Action.DEFECT;
			}
			if (Random.nextBoolean()) {
				other = Action.COOPERATE;
			} else {
				other = Action.DEFECT;
			}
			dpda.next(focal, other);
			System.out.println(dpda.currentAction());
		}
	}

	public DPDA randomDPDA() {
		Random.seed();
		DPDA dpda = new DPDA();
		DPDAMutator mutator = new DPDAMutator();
		mutator.addState(dpda);
		// Mutate 100 times, to generate a random machine
		for (int i = 0; i < 100; i++) {
			mutator.mutate(dpda);
		}
		return dpda;
	}
}

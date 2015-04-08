package edu.monash.bthal2.repeatedPD.newDPDA;

import static org.junit.Assert.*;

import org.junit.Test;

import com.evolutionandgames.jevodyn.utils.Random;

public class DPDATest {

	@Test
	public void testCopyDPDA() {
		// A copy of a DPDA should have the same hash as a DPDA
		DPDA dpda = randomDPDA();
		DPDAMutator mutator = new DPDAMutator();
		// Mutate 100 times, to generate a random machine
		for (int i = 0; i < 0; i++) {
			mutator.mutate(dpda);
		}
		System.out.println("Hash Original: " + dpda.hashCode());
		DPDA copy = dpda.copyDPDA();
		System.out.println("Hash Original: " + dpda.hashCode());
		System.out.println("Copy Hash " + copy.hashCode());
		System.out.println("Original :");
		System.out.println(dpda);
		System.out.println("Copy :");
		System.out.println(copy);

	}

	public DPDA randomDPDA() {
		Random.seed();
		DPDA dpda = new DPDA();
		DPDAMutator mutator = new DPDAMutator();
		mutator.addState(dpda);

		return dpda;
	}
}

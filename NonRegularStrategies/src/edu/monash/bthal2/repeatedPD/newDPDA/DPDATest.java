package edu.monash.bthal2.repeatedPD.newDPDA;

import static org.junit.Assert.*;

import org.junit.Test;

import com.evolutionandgames.jevodyn.utils.Random;

public class DPDATest {

	@Test
	public void testCopyDPDA() {
		// A copy of a DPDA should have the same hash as a DPDA

	}

	@Test
	public void randomDPDA() {
		Random.seed();
		DPDA dpda = new DPDA();
		DPDAMutator mutator = new DPDAMutator();
		mutator.addState(dpda);
	}
}

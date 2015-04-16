package edu.monash.bthal2.repeatedPD.newDPDA;

import static org.junit.Assert.*;

import org.junit.Test;

import com.evolutionandgames.jevodyn.utils.Random;
import com.evolutionandgames.repeatedgames.evolution.Action;

public class DPDAFactoryTest {

	@Test
	public void testTft() {
		Random.seed();
		DPDA tft = DPDAFactory.ExampleStrategies.tft();
		Action previous = Action.COOPERATE;
		for (int i = 0; i < 10; i++) {
			Action focal = tft.currentAction();
			Action opponent;
			if (Random.nextBoolean()) {
				opponent = Action.COOPERATE;
			} else {
				opponent = Action.DEFECT;
			}
			System.out.println("TFT " + focal + " RAND " + opponent);
			tft.next(focal, opponent);
			assertTrue(previous == focal);
			previous = opponent;
		}
	}

}

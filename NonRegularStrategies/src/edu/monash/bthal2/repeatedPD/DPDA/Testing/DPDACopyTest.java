package edu.monash.bthal2.repeatedPD.DPDA.Testing;
import org.junit.Test;

import edu.monash.bthal2.repeatedPD.DPDA.DPDA;
import edu.monash.bthal2.repeatedPD.DPDA.DPDAFactory;


public class DPDACopyTest {
	@Test
	public void copyBasicStrategies() {
		DPDA original = DPDAFactory.ExampleStrategies.allD();
		DPDA copy=original.copy();
	}
}

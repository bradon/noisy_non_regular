package edu.monash.bthal2.repeatedPD.newDPDA;

public class Parser {
	final static String STATE_SEPERATOR = "A";

	public static enum DEFAULT {
		C, D
	}

	public static enum FINAL {
		F, N
	}

	final static String TRANSITION_SEPERATOR = ":";
	final static String TRANSITION_INTERNAL_SEPERATOR = ";";
	final static String STATE_INTERNAL_SEPERATOR = ";";

}

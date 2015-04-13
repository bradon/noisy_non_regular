package edu.monash.bthal2.repeatedPD;

import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import edu.monash.bthal2.repeatedPD.Simulation.DPDAPayoffSimulation;
import edu.monash.bthal2.repeatedPD.Simulation.newDPDATimeSeriesSimulation;

/**
 * Initializing class for running as jar
 * 
 * @author bradon
 * 
 */
public class App {
	@Parameter(names = { "-file", "-f" }, description = "Name of the json file")
	private String file;

	enum SimulationType {
		PAYOFF, TIMESERIES
	}

	@Parameter(names = { "-type", "-t" }, description = "Type of simulation", required = true)
	private SimulationType type;

	@Parameter(names = { "-json", "-j" }, description = "Show example JSON file", required = false)
	private static boolean showJson = false;

	@Parameter(names = { "-neutral" }, description = "Initialise with mixture of ALLC and ALLD")
	private static boolean neutralPopulation = false;

	public static void main(String[] args) throws IOException {

		App app = new App();
		// Parsing
		JCommander commander = new JCommander(app);
		try {
			commander.parse(args);
		} catch (ParameterException e) {
			System.out.println(e.getMessage());
			commander.usage();
			return;
		}

		switch (app.type) {
		case PAYOFF:
			if (showJson) {
				System.out.println(DPDAPayoffSimulation.exampleJson());
			} else {
				DPDAPayoffSimulation.runOncePayoff(app.file, neutralPopulation);
			}
			break;
		case TIMESERIES:
			if (showJson) {
				System.out.println(newDPDATimeSeriesSimulation.exampleJson());

			} else {
				newDPDATimeSeriesSimulation.runApp(app.file, neutralPopulation);
			}
			break;
		default:
			System.out.println("Simulation not implemented");
			break;

		}
	}
}

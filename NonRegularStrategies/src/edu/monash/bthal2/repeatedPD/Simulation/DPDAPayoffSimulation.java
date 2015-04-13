package edu.monash.bthal2.repeatedPD.Simulation;

import java.io.File;
import java.io.IOException;

import com.evolutionandgames.agentbased.AgentBasedSimulation;
import com.evolutionandgames.agentbased.extensive.AgentBasedWrightFisherProcessWithAssortment;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulation;
import com.evolutionandgames.jevodyn.utils.Random;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGame;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGamePayoffCalculator;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.monash.bthal2.repeatedPD.newDPDA.DPDA;
import edu.monash.bthal2.repeatedPD.newDPDA.DPDAFactory;
import edu.monash.bthal2.repeatedPD.newDPDA.DPDAMutator;

public class DPDAPayoffSimulation extends PayoffSimulation {
	protected double flipMachineResultProbability; // change C on accept or D on
													// accept
	protected double addProbability;
	protected double removeProbability;
	protected double changeProbability;

	protected double mutationProbabilityPerState;

	public double numericalRunOnce(String filename) throws IOException {
		DPDAPayoffSimulation app = DPDAPayoffSimulation.loadFromFile(filename);
		double totalPayoff = app.simulation.estimateTotalPayoff(
				app.burningTimePerEstimate, app.timeStepsPerEstimate,
				app.numberOfEstimates, app.reportEveryTimeSteps, app.seed,
				app.factory);

		double averageTotalPayoff = totalPayoff / (double) app.populationSize;

		for (int i = 0; i < 10; i++) {
			DPDA test = (DPDA) app.process.getPopulation().getAgent(
					Random.nextInt(app.populationSize));
			System.out.println(test.toString());
		}
		return averageTotalPayoff;
	}

	// RunOnce?
	public static void runOncePayoff(String filename, boolean neutralPop)
			throws IOException {
		DPDAPayoffSimulation app = DPDAPayoffSimulation.loadFromFile(filename,
				neutralPop);
		double totalPayoff = app.simulation.estimateTotalPayoff(
				app.burningTimePerEstimate, app.timeStepsPerEstimate,
				app.numberOfEstimates, app.reportEveryTimeSteps, app.seed,
				app.factory);
		double averageTotalPayoff = totalPayoff / (double) app.populationSize;
		File file = new File(app.outputFile);
		// Payoff should match expected- first test to see if correct
		Files.append(
				Double.toString(app.continuationProbability) + ", "
						+ Double.toString(app.r) + ", "
						+ Double.toString(averageTotalPayoff) + "\r\n", file,
				Charsets.UTF_8);
	}

	public static void runOncePayoff(String filename) throws IOException {
		runOncePayoff(filename, false);
	}

	public static DPDAPayoffSimulation loadFromFile(String string,
			boolean setNeutralPopulation) throws IOException {
		File file = new File(string);
		Gson gson = new Gson();
		String json = Files.toString(file, Charsets.UTF_8);
		DPDAPayoffSimulation sim = gson.fromJson(json,
				DPDAPayoffSimulation.class);
		sim.init();
		return sim;
	}

	public static DPDAPayoffSimulation loadFromFile(String string)
			throws IOException {
		return loadFromFile(string, false);
	}

	public static void generatesTimeSeries(String filename) throws IOException {
		DPDAPayoffSimulation app = DPDAPayoffSimulation.loadFromFile(filename);
		double totalPayoff = app.simulation.estimateTotalPayoff(
				app.burningTimePerEstimate, app.timeStepsPerEstimate,
				app.numberOfEstimates, app.reportEveryTimeSteps, app.seed,
				app.factory);
		double averageTotalPayoff = totalPayoff / (double) app.populationSize;
		File file = new File(app.outputFile);
		// Payoff should match expected- first test to see if correct
		Files.append(
				Double.toString(app.continuationProbability) + ", "
						+ Double.toString(app.r) + ", "
						+ Double.toString(averageTotalPayoff) + "\r\n", file,
				Charsets.UTF_8);
	}

	private void init() {

		// Refactor- some of this code can be generalized
		// this.factory = new RepeatedStrategyPopulationFactory(populationSize,
		// DPDAFactory.ExampleStrategies.allD());
		this.factory = new DPDAFactory(populationSize);

		// Will need mutation parameters
		this.mutator = new DPDAMutator();

		this.population = (ExtensivePopulation) factory.createPopulation();
		this.repeatedGame = new RepeatedGame(this.reward, this.sucker,
				this.temptation, this.punishment, this.continuationProbability);

		this.payoffCalculator = new RepeatedGamePayoffCalculator(
				this.repeatedGame, this.mistakeProbability, false);

		this.process = new AgentBasedWrightFisherProcessWithAssortment(
				population, payoffCalculator, mapping, intensityOfSelection,
				mutator, r);
		this.simulation = new AgentBasedSimulation(this.process);
	}

	public static String exampleJson() {
		DPDAPayoffSimulation app = new DPDAPayoffSimulation();
		prepareJsonPayoffSimulation(app);
		app.mutationProbabilityPerState = 0.001;
		app.addProbability = 0.3;
		app.removeProbability = 0.3;
		app.changeProbability = 0.4;

		String json = new GsonBuilder().setPrettyPrinting().create()
				.toJson(app);
		return json;

	}
}

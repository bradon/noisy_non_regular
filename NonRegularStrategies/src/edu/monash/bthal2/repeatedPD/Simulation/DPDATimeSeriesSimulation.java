package edu.monash.bthal2.repeatedPD.Simulation;

import java.io.File;
import java.io.IOException;

import com.evolutionandgames.agentbased.AgentBasedSimulation;
import com.evolutionandgames.agentbased.extensive.AgentBasedWrightFisherProcessWithAssortment;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulation;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGame;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGamePayoffCalculator;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.monash.bthal2.repeatedPD.newDPDA.DPDAFactory;
import edu.monash.bthal2.repeatedPD.newDPDA.DPDAMutator;

public class DPDATimeSeriesSimulation extends TimeSeriesSimulation {
	protected double addStatesProbability;
	protected double removeStatesProbability;
	protected double changeProbability;
	protected double mutationProbabilityPerState;

	public void init() {
		// Refactor- some of this code can be generalized
		// this.factory = new RepeatedStrategyPopulationFactory(populationSize,
		// DPDAFactory.ExampleStrategies.allD());
		this.factory = new DPDAFactory(populationSize);

		// Will need mutation parameters
		// mutationProbabilityPerState, addingStatesProbability,
		// removingStatesProbability, addTransitionProbability,
		// removeTransitionProbability, changingReadProbability,
		// changingPopProbability, changingPushProbability,
		// changingDestinationProbability, flipState
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

	private static DPDATimeSeriesSimulation loadFromFile(String filename,
			boolean setNeutralPopulation) throws IOException {
		File file = new File(filename);
		Gson gson = new Gson();
		String json = Files.toString(file, Charsets.UTF_8);
		DPDATimeSeriesSimulation sim = gson.fromJson(json,
				DPDATimeSeriesSimulation.class);
		sim.init();
		return sim;
	}

	public static DPDATimeSeriesSimulation loadFromFile(String string)
			throws IOException {
		return loadFromFile(string, false);
	}

	public static void runApp(String filename, boolean setNeutralPopulation)
			throws IOException {
		DPDATimeSeriesSimulation app = DPDATimeSeriesSimulation.loadFromFile(
				filename, setNeutralPopulation);
		app.simulation.simulateTimeSeries(app.numberOfTimeSteps,
				app.reportEveryTimeSteps, app.seed, app.outputFile);
		app.simulation.simulateTimeSeries(app.numberOfTimeSteps,
				app.reportEveryTimeSteps, app.seed, app.outputFile, null);
	}

	public static void runApp(String filename) throws IOException {
		runApp(filename, false);
	}

	/**
	 * @return
	 */
	public static String exampleJson() {
		DPDATimeSeriesSimulation app = new DPDATimeSeriesSimulation();
		prepareJsonTimeSeries(app);
		app.mutationProbabilityPerState = 0.001;
		app.addStatesProbability = 0.01;
		app.removeStatesProbability = 0.05;
		app.changeProbability = 0.05;
		String json = new GsonBuilder().setPrettyPrinting().create()
				.toJson(app);
		return json;
	}
}

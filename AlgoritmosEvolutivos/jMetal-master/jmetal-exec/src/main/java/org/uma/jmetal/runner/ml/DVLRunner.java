package org.uma.jmetal.runner.ml;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.ml.DVL;
import org.uma.jmetal.algorithm.ml.datasetgenerator.DataSetGenerator;
import org.uma.jmetal.algorithm.ml.datasetgenerator.lhs.LHSGenerator;
import org.uma.jmetal.algorithm.ml.estimator.Estimator;
import org.uma.jmetal.algorithm.ml.estimator.Iterative;
import org.uma.jmetal.algorithm.ml.evaluator.Evaluator;
import org.uma.jmetal.algorithm.ml.evaluator.HyperVolume;
import org.uma.jmetal.algorithm.ml.popGenerator.ML;
import org.uma.jmetal.algorithm.ml.popGenerator.PopGenerator;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.runner.multiobjective.NSGAIIIRunner;
import org.uma.jmetal.solution.DoubleSolution;

public class DVLRunner {
	public static void main(String[] args) {
		DoubleProblem problem = new DTLZ2(12, 3);
		Evaluator evaluator = new HyperVolume(problem);
		Algorithm<List<DoubleSolution>> algorithm = NSGAIIIRunner.geraNSGA(problem,  10, null);
		DataSetGenerator dataSetGenerator = new LHSGenerator(problem, algorithm, 500);
		PopGenerator popGenerator = new ML(algorithm, "experimento1", dataSetGenerator.getLower(), dataSetGenerator.getUpper());
		Estimator estimator = new Iterative(500, 92, 0.001,10, evaluator, popGenerator, problem);
		DVL dvl = new DVL(dataSetGenerator, estimator);
		ArrayList<DoubleSolution> res = dvl.execute();
		System.out.println(evaluator.evaluate(res));
	}
}

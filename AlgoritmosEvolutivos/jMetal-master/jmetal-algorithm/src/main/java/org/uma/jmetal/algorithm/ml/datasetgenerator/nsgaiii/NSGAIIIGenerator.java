package org.uma.jmetal.algorithm.ml.datasetgenerator.nsgaiii;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.ml.datasetgenerator.DataSetGenerator;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;

public class NSGAIIIGenerator extends DataSetGenerator {

	public NSGAIIIGenerator(DoubleProblem problem, Algorithm<List<DoubleSolution>> algorithm, int ini_dataset_len) {
		super(problem, algorithm, ini_dataset_len);
	}

	@Override
	public void gen_pop() {
		new AlgorithmRunner.Executor(algorithm).execute();
	}

}

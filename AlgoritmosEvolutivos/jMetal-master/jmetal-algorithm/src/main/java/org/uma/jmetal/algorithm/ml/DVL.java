package org.uma.jmetal.algorithm.ml;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.ml.datasetgenerator.DataSetGenerator;
import org.uma.jmetal.algorithm.ml.estimator.Estimator;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public class DVL implements AlgorithmML{
	private DataSetGenerator dataSetGenerator;
	private Estimator estimator;
	public DVL(DataSetGenerator dataSetGenerator, Estimator estimator) {
		this.estimator = estimator;
		this.dataSetGenerator = dataSetGenerator;
	}
	@Override
	public ArrayList<DoubleSolution> execute() {
		dataSetGenerator.gen_pop();
		return estimator.execute();
		
	}
	
	
}

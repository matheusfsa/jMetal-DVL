package org.uma.jmetal.algorithm.ml.estimator;

import java.util.ArrayList;

import org.uma.jmetal.solution.DoubleSolution;

public interface Estimator {
	public ArrayList<DoubleSolution> execute();
}

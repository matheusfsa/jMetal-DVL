package org.uma.jmetal.algorithm.ml.evaluator;

import java.util.ArrayList;

import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public abstract class Evaluator {
	protected DoubleProblem problem;
	public Evaluator(DoubleProblem problem) {
		this.problem = problem;
	}
	public abstract double evaluate(ArrayList<DoubleSolution> pop);
}

package org.uma.jmetal.algorithm.ml.datasetgenerator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public abstract class DataSetGenerator {
	protected DoubleProblem problem;
	protected Algorithm<List<DoubleSolution>> algorithm;
	protected ArrayList<Double> upper;
	protected ArrayList<Double> lower;
	protected int ini_dataset_len;
	public DataSetGenerator(DoubleProblem problem,Algorithm<List<DoubleSolution>> algorithm, int ini_dataset_len) {
		this.problem = problem;
		this.ini_dataset_len = ini_dataset_len;
		this.algorithm = algorithm;
		setConstraints();
	}
	public abstract void gen_pop();
	private void setConstraints() {
		int n = problem.getNumberOfVariables();
		upper = new ArrayList<>();
		lower = new ArrayList<>();
		for(int i = 0; i < n; i++) {
			upper.add(problem.getUpperBound(i));
			lower.add(problem.getLowerBound(i));
		}
	}
	public DoubleProblem getProblem() {
		return problem;
	}
	public void setProblem(DoubleProblem problem) {
		this.problem = problem;
	}
	public ArrayList<Double> getUpper() {
		return upper;
	}
	public void setUpper(ArrayList<Double> upper) {
		this.upper = upper;
	}
	public ArrayList<Double> getLower() {
		return lower;
	}
	public void setLower(ArrayList<Double> lower) {
		this.lower = lower;
	}
	public int getIni_dataset_len() {
		return ini_dataset_len;
	}
	public void setIni_dataset_len(int ini_dataset_len) {
		this.ini_dataset_len = ini_dataset_len;
	}
	public Algorithm<List<DoubleSolution>> getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(Algorithm<List<DoubleSolution>> algorithm) {
		this.algorithm = algorithm;
	}
}

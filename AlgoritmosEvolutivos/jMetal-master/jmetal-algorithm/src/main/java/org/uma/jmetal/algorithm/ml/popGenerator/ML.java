package org.uma.jmetal.algorithm.ml.popGenerator;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.DoubleSolution;

public class ML implements PopGenerator {
	private Algorithm<List<DoubleSolution>> algorithm;
	private String estrategia;
	private ArrayList<Double> lower;
	private ArrayList<Double> upper;
	
	public ML(Algorithm<List<DoubleSolution>> algorithm, String estrategia, ArrayList<Double> lower,
			ArrayList<Double> upper) {
		this.algorithm = algorithm;
		this.estrategia = estrategia;
		this.lower = lower;
		this.upper = upper;
	}

	@Override
	public ArrayList<DoubleSolution> generate(int size) {
		return (ArrayList<DoubleSolution>) algorithm.gera_pop(estrategia, lower, upper,size);
	}

}

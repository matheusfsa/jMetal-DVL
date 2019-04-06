package org.uma.jmetal.algorithm.ml.popGenerator;

import java.util.ArrayList;

import org.uma.jmetal.solution.DoubleSolution;

public interface PopGenerator {
	public ArrayList<DoubleSolution> generate(int size);
}

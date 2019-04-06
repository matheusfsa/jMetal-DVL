package org.uma.jmetal.algorithm.ml.datasetgenerator.lhs;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.ml.datasetgenerator.DataSetGenerator;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.user;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSO;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;

public class LHSGenerator extends DataSetGenerator {
	private  static String urlTreino = "http://127.0.0.1:5003/treinamento";
	public LHSGenerator(DoubleProblem problem, Algorithm<List<DoubleSolution>> algorithm, int ini_dataset_len) {
		super(problem, algorithm, ini_dataset_len);
	}

	@Override
	public void gen_pop() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		user userObject = new user(
			    "NSGA-III",
			    "sol_lhs",
			    new ArrayList(),
			    new ArrayList(),
			    algorithm.getPontos(),
			    ini_dataset_len,
			    problem.getNumberOfVariables(),
			    upper,
			    lower
			);
		@SuppressWarnings("rawtypes")
		ArrayList new_sol = SMPSO.http(urlTreino, userObject);
		ArrayList<DoubleSolution> pop = new ArrayList<>();
		for(int j = 0; j< new_sol.size(); j++) {
		    DoubleSolution ds = new DefaultDoubleSolution(problem);
		    @SuppressWarnings("unchecked")
			Double[] new_sol_d = (Double[]) ((List<Double>) new_sol.get(j)).toArray(new Double[new_sol.size()]);
		    for (int l = 0; l < problem.getNumberOfVariables(); l++) 
				ds.setVariableValue(l, new_sol_d[l]);
		    problem.evaluate(ds);
			pop.add(ds);
		}
		algorithm.add_pop(pop);

	}

}

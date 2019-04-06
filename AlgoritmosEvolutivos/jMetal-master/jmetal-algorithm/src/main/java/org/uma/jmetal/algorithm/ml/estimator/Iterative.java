package org.uma.jmetal.algorithm.ml.estimator;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.ml.evaluator.Evaluator;
import org.uma.jmetal.algorithm.ml.popGenerator.PopGenerator;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

public class Iterative implements Estimator {
	private double tol; 
	private int max_ite;
	private Evaluator evaluator;
	private PopGenerator popGenerator;
	private DoubleProblem problem;
	private int max_pop_size;
	private SolutionListEvaluator<DoubleSolution> ev_sols;
	private int ini_dataset_size;
	public Iterative(int ini_dataset_size,int max_pop_size,double tol, int max_ite, Evaluator evaluator, 
			PopGenerator popGenerator, DoubleProblem problem) {
		this.ini_dataset_size = ini_dataset_size;
		this.tol = tol;
		this.max_ite = max_ite;
		this.evaluator = evaluator;
		this.popGenerator = popGenerator;
		this.max_pop_size = max_pop_size;
		this.problem = problem;
		ev_sols = new SequentialSolutionListEvaluator<DoubleSolution>(); 
	}
	public ArrayList<DoubleSolution> getRandom(){
		ArrayList<DoubleSolution> population = new ArrayList<>(max_pop_size);
	    for (int i = 0; i < max_pop_size; i++) {
	    	DoubleSolution newIndividual = problem.createSolution();
	      population.add(newIndividual);
	    }
	    return (ArrayList<DoubleSolution>) ev_sols.evaluate(population, problem);
	}
	@Override
	public ArrayList<DoubleSolution> execute() {
		double diff = 1.0;
		int iteracoes = 0;
		ArrayList<DoubleSolution> pop = null;
		ArrayList<DoubleSolution> pop_res = null;
		ArrayList<DoubleSolution> pop_res_t = new ArrayList<>();
		double hv_ant = 0;
		double hv = 0;
		double max = 0;
		pop = popGenerator.generate(ini_dataset_size);
		pop_res = pop;
		int size = ini_dataset_size;
		for(int i = 0; i < max_ite && diff > tol; i++) {
			hv = evaluator.evaluate(pop);
			diff = Math.abs(hv - hv_ant);
			if(hv >= 0 && hv  <= 1 && hv > max) {
				max = hv;
				pop_res = pop;
			}	
			hv_ant = hv;
			size += pop.size();
			iteracoes = i+1;
			if(i<max_ite && diff > tol) {
				pop = popGenerator.generate(size);
			}
		}
		return pop_res;

	}

}

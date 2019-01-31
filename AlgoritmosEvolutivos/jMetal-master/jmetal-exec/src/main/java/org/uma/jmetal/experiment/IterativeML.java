package org.uma.jmetal.experiment;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.impl.AbstractEvolutionaryAlgorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.user;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSO;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.multiobjective.dtlz.*;
import org.uma.jmetal.problem.multiobjective.wfg.*;
import org.uma.jmetal.runner.multiobjective.NSGAIIIRunner;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;

public class IterativeML {
	private DoubleProblem problem;
	private Algorithm<List<DoubleSolution>> algorithm;
	private ArrayList<Double> upper;
	private ArrayList<Double> lower;
	public double hv_pop;
	public double hv_rand;
	public double hv_alg;
	public int iteracoes;
	private  static String urlTreino = "http://127.0.0.1:5003/treinamento";
	public IterativeML(DoubleProblem problem, Algorithm<List<DoubleSolution>> algorithm) {
		this.problem = problem;
		this.algorithm = algorithm;
		setConstraints();
	}
	
	public DoubleProblem getProblem() {
		return problem;
	}

	public void setProblem(DoubleProblem problem) {
		this.problem = problem;
		setConstraints();
	}

	public Algorithm<List<DoubleSolution>> getAlgorithm() {
		return algorithm;
	}
	
	public void setAlgorithm(Algorithm<List<DoubleSolution>> algorithm) {
		this.algorithm = algorithm;
	}
	public void setConstraints() {
		int n = problem.getNumberOfVariables();
		upper = new ArrayList<>();
		lower = new ArrayList<>();
		for(int i = 0; i < n; i++) {
			upper.add(problem.getUpperBound(i));
			lower.add(problem.getLowerBound(i));
		}
	}	
	public void geraPopLHS(int n_pop){
		@SuppressWarnings({ "unchecked", "rawtypes" })
		user userObject = new user(
			    "NSGA-III",
			    "sol_lhs",
			    new ArrayList(),
			    new ArrayList(),
			    algorithm.getPontos(),
			    n_pop,
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
	 public void send_pop(String nome, List<DoubleSolution> population) {
		    ArrayList<double[]> objetivos = new ArrayList<>();
			int n_obj = population.get(0).getNumberOfObjectives();
		    int n_var = population.get(0).getNumberOfVariables();
		    for(int i = 0; i < population.size(); i++) {
		    	double[] solucao = new double[n_var];
		    	double[] objetivo = new double[n_obj];
		    	for(int j = 0; j < n_obj; j++) {
		    		objetivo[j] = (double) population.get(i).getObjective(j);
		    	}
		    	objetivos.add(objetivo);
		    }
		    user userObject = new user(
				    nome,
				    "add_sols",
				    new ArrayList(),
				    objetivos,
				    new ArrayList(),
				    0,
				    0,
				    upper,
				    lower
				);
		    SMPSO.http(urlTreino, userObject);
		    	
		  }
	public ArrayList<DoubleSolution> geraPopML(int n_pop){
		return (ArrayList<DoubleSolution>) algorithm.gera_pop("experimento", lower, upper,n_pop);
	}
	public ArrayList<DoubleSolution> getRandom(){
		return (ArrayList<DoubleSolution>) algorithm.getInitialPop();
	}
	public ArrayList<DoubleSolution> ini_pop_gen(int ini_pop_size, double tol, int max_ite){
		geraPopLHS(ini_pop_size);
		return fit(ini_pop_size, tol, max_ite);
	}
	public ArrayList<DoubleSolution> pop_gen(int ini_pop_size, double tol, int max_ite){
		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
		ArrayList<DoubleSolution> res = (ArrayList<DoubleSolution>)algorithm.getResult();
		hv_alg = HyperVolume.hv(problem, res);
		return fit(ini_pop_size, tol, max_ite);
	}
	public ArrayList<DoubleSolution> fit(int ini_pop_size, double tol, int max_ite) {
		double diff = 1.0;
		ArrayList<DoubleSolution> pop = null;
		ArrayList<DoubleSolution> pop_res = null;
		ArrayList<Double> hvs = new ArrayList<>(); 
		ArrayList<Double> hvs_random = new ArrayList<>(); 
		double hv_ant = 0;
		double hv = 0;
		double max = 0;
		//geraPopLHS(ini_pop_size);
		pop = geraPopML(ini_pop_size);
		pop_res = pop;
		int size = ini_pop_size;
		for(int i = 0; i < max_ite && Math.abs(diff) > tol; i++) {
			//System.out.println("a: " + i);
			//System.out.println(pop.get(0));
			//System.out.println(getRandom().get(0));
			hv = HyperVolume.hv(problem, pop);
			hvs_random.add(HyperVolume.hv(problem, getRandom()));
			diff = Math.abs(hv - hv_ant);
			hvs.add(hv);
			if(hv >= 0 && hv  <= 1 && hv > max) {
				max = hv;
				pop_res = pop;
			}
				
			hv_ant = hv;
			algorithm.add_pop(pop);
			size += pop.size();
			pop = geraPopML(size);
			iteracoes = i+1;
		}
		hv_rand = hvs_random.get(0);
		System.out.println("alg = " + hvs.toString());
		//System.out.println("rdm = " + hvs_random.toString());
		//System.out.println(max);
		hv_pop = max;
		return pop_res;
	}
	public static void main(String[] args) {
		int[] objs = {3};
		int[] vars = {12};
		int n_int = 0;
		double hv = 0;
		
		int it = 1; 
		for (int m : objs) {
			if(m == 3)
				n_int = 1099;
			else if(m == 5)
				n_int = 476;
			else if(m == 10)
				n_int = 454;
			DoubleProblem problem = new DTLZ4(12, m);
			double media = 0;
			double media2 = 0;
			ArrayList<Double> res_alg =  new ArrayList<>();
			ArrayList<Double> res_rand=  new ArrayList<>();
			
			for(int i = 0; i < it; i++) {
				//System.out.println(i);
				Algorithm<List<DoubleSolution>> algorithm = NSGAIIIRunner.geraNSGA(problem, 61, null);
				//System.out.println(res);
				IterativeML iml = new IterativeML(problem, algorithm);
				List<DoubleSolution> pop = iml.ini_pop_gen(1000, 0.001,50);
				iml.send_pop("Estimativas",pop);
				//System.out.println(pop.size());9
				//System.out.println(pop.get(0));
				System.out.println(iml.iteracoes);
				media += iml.hv_pop/it;
				n_int = (iml.iteracoes*440 + 1000)/440;
				System.out.println(n_int);
				algorithm = NSGAIIIRunner.geraNSGA(problem, n_int, null);
				AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute() ;
				ArrayList<DoubleSolution> population = (ArrayList<DoubleSolution>)algorithm.getResult();
				iml.send_pop("NSGAIII",population);
			    res_alg.add(iml.hv_pop);
			    res_rand.add(iml.hv_rand);
			    System.out.println("NSGAIII = " + HyperVolume.hv(problem, population));
			}
			System.out.println("alg"+ m +" = " + res_alg.toString());
			System.out.println("rand"+ m + " = " + res_rand.toString());
			//System.out.println(m + " objetivos");
			//System.out.println(media);
			//System.out.println(media2);
			
		}
		
		

	}

}

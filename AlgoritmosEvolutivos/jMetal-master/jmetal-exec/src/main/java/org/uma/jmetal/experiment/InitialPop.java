package org.uma.jmetal.experiment;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.user;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSO;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.generator.MLGenerator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.multiobjective.dtlz.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

public class InitialPop {
	public static double getRefPoint(String problemName) {
		double REFPOINT = 0;
		if (problemName.equals("CDTLZ2"))
			REFPOINT = 2;
		else if (problemName.equals("DTLZ1"))
			REFPOINT = 1.5;
		else if (problemName.equals("DTLZ2"))
			REFPOINT = 2;
		else if (problemName.equals("DTLZ3"))
			REFPOINT = 11;
		else if (problemName.equals("DTLZ4"))
			REFPOINT = 2;
		else if (problemName.equals("DTLZ5"))
			REFPOINT = 2;
		else if (problemName.equals("DTLZ6"))
			REFPOINT = 2;
		else if (problemName.equals("DTLZ7"))
			REFPOINT = 2;
		else if (problemName.equals("SDTLZ1"))
			REFPOINT = 1.5;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF01"))
			REFPOINT = 5.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF02"))
			REFPOINT = 50.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF03"))
			REFPOINT = 50.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF04"))
			REFPOINT = 50.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF05"))
			REFPOINT = 50.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF06"))
			REFPOINT = 50.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF07"))
			REFPOINT = 50.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF08"))
			REFPOINT = 50.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF09"))
			REFPOINT = 50.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF10"))
			REFPOINT = 50.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF11"))
			REFPOINT = 50.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF12"))
			REFPOINT = 50.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF13"))
			REFPOINT = 50.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF14"))
			REFPOINT = 50.0;
		else if (problemName.equals("org.uma.jmetal.problem.multiobjective.maf.MaF15"))
			REFPOINT = 50.0;
		else
			REFPOINT = 1.6;
		return REFPOINT;
	  }
	private  static String urlTreino = "http://127.0.0.1:5003/treinamento";
	public static ArrayList<DoubleSolution>  criaPop(DoubleProblem problem, Algorithm<List<DoubleSolution>> algorithm,ArrayList<Double> upper, ArrayList<Double> lower) {
		  @SuppressWarnings({ "rawtypes", "unchecked" })
		  user userObject = new user(
				    "NSGA-III",
				    "sol_lhs",
				    new ArrayList(),
				    new ArrayList(),
				    algorithm.getPontos(),
				    100000,
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
		    	for (int l = 0; l < problem.getNumberOfVariables(); l++) {
					ds.setVariableValue(l, new_sol_d[l]);
					
				}
		    	problem.evaluate(ds);
				pop.add(ds);
			}
		    algorithm.add_pop(pop);
		    pop = (ArrayList<DoubleSolution>) algorithm.gera_pop("experimento", lower, upper,pop.size());
		    return pop;
	}
	public static void main(String[] args) {
		Algorithm<List<DoubleSolution>> algorithm;
		int[] objs = {3, 5, 10};
	    int[] vars = {12, 14, 19};
	    int n_int = 0;
	    for (int n : vars) {
			for (int m : objs) {
				DTLZ2 dtlz2 = new DTLZ2(n, m);
				System.out.println("Variáveis: " + n);
				System.out.println("Objetivos: " + m);
				ArrayList<Double> upper = new ArrayList<>();
				ArrayList<Double> lower = new ArrayList<>();
				for(int i = 0; i < n; i++) {
					upper.add(dtlz2.getUpperBound(i));
					lower.add(dtlz2.getLowerBound(i));
				}
				List<DoubleSolution> pop = null;
				double crossoverProbability = 0.9 ;
			    double crossoverDistributionIndex = 30.0 ;
			    CrossoverOperator<DoubleSolution> crossover;
			    MutationOperator<DoubleSolution> mutation;
			    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
			    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;
		
			    double mutationProbability = 1.0 / dtlz2.getNumberOfVariables() ;
			    double mutationDistributionIndex = 20.0 ;
			    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;
			    
			    
			    selection = new BinaryTournamentSelection<DoubleSolution>();
			    double REFPOINT = 2.0;
			    double hyp_ref[] = new double[dtlz2.getNumberOfObjectives()];
				double normalizacao = 1;
				if (REFPOINT == 1.6) {
					for (int i = 0; i < dtlz2.getNumberOfObjectives(); i++) {
		
						hyp_ref[i] = 2 * (i + 1) + 1;
		
						normalizacao = hyp_ref[i] * normalizacao;
					}
				} else {
					for (int i = 0; i < dtlz2.getNumberOfObjectives(); i++) {
						hyp_ref[i] = REFPOINT;
		
						normalizacao = hyp_ref[i] * normalizacao;
					}
				}
				Point referencePoint = new ArrayPoint(hyp_ref);
				int iteracoes = 20;
				int tam = iteracoes/2;
				double k[] = new double[iteracoes];
			    double k2[] = new double[iteracoes];
			
			    if(m == 3)
					n_int = 1099;
				else if(m == 5)
					n_int = 476;
				else if(m == 10)
					n_int = 454;
				for(int i = 0; i < iteracoes; i++) {
					algorithm = new NSGAIIIBuilder<>(dtlz2)
				    		.setCrossoverOperator(crossover)
				    		.setMutationOperator(mutation)
				    		.setSelectionOperator(selection)
				    		.setGeneratorOperator(new MLGenerator())
				    		.setMaxIterations(n_int)
				    		.setPopulation(pop)
				    		.build();
					AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute() ;
					if(i%2 == 0)
						pop = InitialPop.criaPop(dtlz2, algorithm, upper, lower);
					else
						pop = null;
				    new SolutionListOutput(algorithm.getResult())
			        .setSeparator("\t")
			        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
			        .print() ;
				    WFGHypervolume<DoubleSolution> hype;
				    double hyp=0;
				    WFGHypervolume<DoubleSolution> hype2;
				    double hyp2=0;
				    try {
				    	if(i%2 == 0) {
							hype = new WFGHypervolume<>("FUN.tsv");
							hyp = hype.computeHypervolume(algorithm.getResult(), referencePoint);
							
							k[i] = hyp / normalizacao;
							k2[i] = 0;
							//System.out.println(i + ": " + k[i]);
				    	}else {
							hype2 = new WFGHypervolume<>("FUN.tsv");
							hyp2 = hype2.computeHypervolume(algorithm.getResult(), referencePoint);
							
							k2[i] = hyp2 / normalizacao;
							k[i] = 0;
							//System.out.println(i + ": " + k2[i]);
				    	}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				  
					
				}
				double res1 = 0;
				double res2 = 0;
				for(int i = 0; i< iteracoes; i++) {
					res1 += k[i]/tam;
					res2 += k2[i]/tam;
				}
				System.out.println("Random: " + res1);
				System.out.println("ML: " + res2);
		}
	    
	    }
	}
}

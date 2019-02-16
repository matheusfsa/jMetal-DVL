package org.uma.jmetal.experiment;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
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
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

public class Experiment {
		private  static String urlTreino = "http://127.0.0.1:5003/treinamento";
	  public static void main(String[] args) throws JMetalException {
		  	DoubleProblem problem;
		  	
		    Algorithm<List<DoubleSolution>> algorithm;
		    CrossoverOperator<DoubleSolution> crossover;
		    MutationOperator<DoubleSolution> mutation;
		    //int[] objs = {3, 5, 10};
		    //int[] vars = {12,14,19};
		    int[] objs = {3};
		    int[] vars = {12};
		    for (int n : vars) {
				for (int m : objs) {
				ArrayList<Double> upper = new ArrayList<>();
				ArrayList<Double> lower = new ArrayList<>();
				problem = new DTLZ2(n,m);
				for(int i = 0; i < n; i++) {
					upper.add(problem.getUpperBound(i));
					lower.add(problem.getLowerBound(i));
				}
			    double REFPOINT = InitialPop.getRefPoint(problem.getName());
			    System.out.println(problem.getName());
			    System.out.println(REFPOINT);
			    double hyp_ref[] = new double[problem.getNumberOfObjectives()];
				double normalizacao = 1;
				if (REFPOINT == 1.6) {
					for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
	
						hyp_ref[i] = 2 * (i + 1) + 1;
	
						normalizacao = hyp_ref[i] * normalizacao;
					}
				} else {
					for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
						hyp_ref[i] = REFPOINT;
	
						normalizacao = hyp_ref[i] * normalizacao;
					}
				}
				Point referencePoint = new ArrayPoint(hyp_ref);
			    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
			    double crossoverProbability = 0.9 ;
			    double crossoverDistributionIndex = 30.0 ;
			    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;
			    
			    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
			    double mutationDistributionIndex = 20.0 ;
			    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;
			    selection = new BinaryTournamentSelection<DoubleSolution>();
			    algorithm =new NSGAIIIBuilder<>(problem)
			            .setCrossoverOperator(crossover)
			            .setMutationOperator(mutation)
			            .setSelectionOperator(selection)
			            .setGeneratorOperator(new MLGenerator())
			            .setMaxIterations(100)
			            .setPopulation(null)
			            .build() ;
			    int iteracoes = 5;
			    double k[] = new double[iteracoes];
			    double k2[] = new double[iteracoes];
			  
			    for(int i = 0; i< iteracoes; i++) {
				    user userObject = new user(
						    "NSGA-III",
						    "sol_lhs",
						    new ArrayList(),
						    new ArrayList(),
						    algorithm.getPontos(),
						    75000,
						    problem.getNumberOfVariables(),
						    upper,
						    lower
						);
				    ArrayList new_sol = SMPSO.http(urlTreino, userObject);
				    ArrayList<DoubleSolution> pop = new ArrayList<>();
				    //bw.write(new_sol.size());
				    for(int j = 0; j< new_sol.size(); j++) {
				    	DoubleSolution ds = new DefaultDoubleSolution(problem);
				    	Double[] new_sol_d = (Double[]) ((List<Double>) new_sol.get(j)).toArray(new Double[new_sol.size()]);
				    	for (int l = 0; l < problem.getNumberOfVariables(); l++) {
							ds.setVariableValue(l, new_sol_d[l]);
							
						}
				    	problem.evaluate(ds);
						pop.add(ds);
					}
				    algorithm.add_pop(pop);
				    
				    pop = (ArrayList<DoubleSolution>) algorithm.gera_pop("experimento", lower, upper,100000);
				    ArrayList<DoubleSolution> pop2 = (ArrayList<DoubleSolution>) algorithm.getInitialPop();
				    new SolutionListOutput(pop)
			        .setSeparator("\t")
			        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
			        .print() ;
				    new SolutionListOutput(pop2)
			        .setSeparator("\t")
			        .setFunFileOutputContext(new DefaultFileOutputContext("FUN2.tsv"))
			        .print() ;
				    WFGHypervolume<DoubleSolution> hype;
				    double hyp=0;
				    WFGHypervolume<DoubleSolution> hype2;
				    double hyp2=0;
				    try {
						hype = new WFGHypervolume<>("FUN.tsv");
						hyp = hype.computeHypervolume(pop, referencePoint);
						System.out.println(pop.get(i));
						k[i] = hyp / normalizacao;
						System.out.println(k[i]);
						hype2 = new WFGHypervolume<>("FUN2.tsv");
						
						hyp2 = hype2.computeHypervolume(pop2, referencePoint);
						System.out.println(pop2.get(i));
						k2[i] = hyp2 / normalizacao;
						System.out.println(k2[i]);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
			    }
			    double aux = 0;
			    double aux2 = 0;
				for (int i = 0; i < iteracoes; i++) {
					aux += k[i];	
					aux2 += k2[i];
				}
				aux/= iteracoes;
				aux2/= iteracoes;
				System.out.print("\nNúmero de objetivos: " + m);
				System.out.print("\nNúmero de variáveis: " + n);
				System.out.println("Média: " + aux);
				System.out.println("Média2: " + aux2);
				}
		    }
			 
	  }
}
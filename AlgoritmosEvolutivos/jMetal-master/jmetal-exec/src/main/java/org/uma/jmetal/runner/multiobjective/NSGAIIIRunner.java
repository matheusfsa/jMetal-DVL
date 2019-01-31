package org.uma.jmetal.runner.multiobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.experiment.InitialPop;
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
import org.uma.jmetal.util.*;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Class to configure and run the NSGA-III algorithm
 */
public class NSGAIIIRunner extends AbstractAlgorithmRunner {
	/**
   * @param args Command line arguments.
   * @throws java.io.IOException
   * @throws SecurityException
   * @throws ClassNotFoundException
   * Usage: three options
   *        - org.uma.jmetal.runner.multiobjective.NSGAIIIRunner
   *        - org.uma.jmetal.runner.multiobjective.NSGAIIIRunner problemName
   *        - org.uma.jmetal.runner.multiobjective.NSGAIIIRunner problemName paretoFrontFile
   */
  public static Algorithm<List<DoubleSolution>> geraNSGA(DoubleProblem problem, int n_int, List<DoubleSolution> pop) {
	  	Algorithm<List<DoubleSolution>> algorithm;
	    CrossoverOperator<DoubleSolution> crossover;
	    MutationOperator<DoubleSolution> mutation;
	    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
	    double crossoverProbability = 0.9 ;
	    double crossoverDistributionIndex = 30.0 ;
	    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;

	    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
	    double mutationDistributionIndex = 20.0 ;
	    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;
	    
	    
	    selection = new BinaryTournamentSelection<DoubleSolution>();
	    
	   
	   
	    algorithm = new NSGAIIIBuilder<>(problem)
	    		.setCrossoverOperator(crossover)
	    		.setMutationOperator(mutation)
	    		.setSelectionOperator(selection)
	    		.setGeneratorOperator(new MLGenerator())
	    		.setMaxIterations(n_int)
	    		.setPopulation(pop)
	    		.build();
	    return algorithm;
  }
  @SuppressWarnings("resource")
public static void main(String[] args) throws JMetalException {
	    DTLZ1 problem = new DTLZ1();
	    Algorithm<List<DoubleSolution>> algorithm;
	    CrossoverOperator<DoubleSolution> crossover;
	    MutationOperator<DoubleSolution> mutation;
	    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;

    String problemName =problem.getName();// "org.uma.jmetal.problem.multiobjective.dtlz.DTLZ7" ;
    List<DoubleSolution> pop = null;
    int[] objs = {3};
    int[] vars = {12};
    //int[] objs = {3};
    //int[] vars = {12};
    int iteracoes = 1;
   
	
	double REFPOINT = InitialPop.getRefPoint(problemName);
	    
    

    //REFPOINT = 1.6;
	int n_int = 0;
	FileWriter fw;
	try {
		fw = new FileWriter("resultados.txt");
	
	BufferedWriter bw = new BufferedWriter(fw);
	
    //problem = ProblemUtils.loadProblem(problemName);
    for (int n : vars) {
		for (int m : objs) {
			//JMetalLogger.logger.info("Número de variáveis: " + n);
			//JMetalLogger.logger.info("Número de objetivos: " + m);
			if(m == 3)
				n_int = 1099;
			else if(m == 5)
				n_int = 476;
			else if(m == 10)
				n_int = 454;
			
			// int ka = n - 10;
			problem = new DTLZ1(n, m);
			//System.out.println(problem.getNumberOfVariables());
					//DTLZ7(n, m);
			double desvpad = 0;
			double desvpad_pontos = 0;
			double desvpad_lhs = 0;
			double aux = 0;
			double aux_pontos = 0;
			double aux_lhs = 0;
		    double k[] = new double[iteracoes];
		    double k_pontos[] = new double[iteracoes];
		    double k_lhs[] = new double[iteracoes];
			double max = Double.MIN_VALUE;
			double max_pontos = Double.MIN_VALUE;
			double max_lhs = Double.MIN_VALUE;
			double min = Double.MAX_VALUE;
			double min_pontos = Double.MAX_VALUE;
			double min_lhs = Double.MAX_VALUE;
			String nomearq;
			String nomearq_pontos;
			String nomearq_lhs;
			// INICIALIZA��O DAS VARI�VEIS DO HIPERPLANO
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
			for(int i = 0; i< iteracoes; i++) {
				//JMetalLogger.logger.info("Iteração: " + i);
				
			    double crossoverProbability = 0.9 ;
			    double crossoverDistributionIndex = 30.0 ;
			    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;

			    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
			    double mutationDistributionIndex = 20.0 ;
			    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;
			    
			    
			    selection = new BinaryTournamentSelection<DoubleSolution>();
			    
			   
			   
			    algorithm = new NSGAIIIBuilder<>(problem)
			    		.setCrossoverOperator(crossover)
			    		.setMutationOperator(mutation)
			    		.setSelectionOperator(selection)
			    		.setGeneratorOperator(new MLGenerator())
			    		.setMaxIterations(n_int)
			    		.setPopulation(pop)
			    		.build();
	
			    
			    if((i%2) == 0) {
			    	//pop = algorithm.getGenPop();
			    	pop = null;
			    }else {
			    	
			    	pop = null;
			    }
			    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute() ;
			    List<DoubleSolution> population = algorithm.getResult() ;
			    int n_sol = 10000;
				
				
			    algorithm.gera_pop("experimento",problem.getLower(),problem.getUpper(),n_sol);
			    nomearq = "/media/matheus/Matheus/RESULTADOS/" + m + "-OBJ/" + n + "-VAR/NSGA-III/FUN-" + m +"-"+ n +"-"+ i + ".tsv";
			    nomearq_pontos = "/media/matheus/Matheus/RESULTADOS/" + m + "-OBJ/" + n + "-VAR/Pontos/FUN-Pontos-" + m + "-"+ n +"-"+ i + ".tsv";
			    nomearq_lhs = "/media/matheus/Matheus/RESULTADOS/" + m + "-OBJ/" + n + "-VAR/LHS/FUN-LHS-" +m +"-"+ n +"-"+ i + ".tsv";
			    new SolutionListOutput(algorithm.getResult())
				.setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext("VAR-" + m +"-"+ n +"-"+ i + ".tsv"))
				.setFunFileOutputContext(new DefaultFileOutputContext(nomearq))
				.print() ;
			    new SolutionListOutput(algorithm.getGenPop())
			        .setSeparator("\t")
			        .setVarFileOutputContext(new DefaultFileOutputContext("VAR-Pontos-" + m + "-" + n + "-" + i + ".tsv"))
			        .setFunFileOutputContext(new DefaultFileOutputContext(nomearq_pontos))
			        .print() ;
		     	new SolutionListOutput(algorithm.getLHSPop())
			        .setSeparator("\t")
			        //.setVarFileOutputContext(new DefaultFileOutputContext("VAR-LHS-" +m +"-"+ n +"-"+ i + ".tsv"))
			        .setFunFileOutputContext(new DefaultFileOutputContext(nomearq_lhs))
			        .print() ;
			    
			    WFGHypervolume<DoubleSolution> hype;
			    double hyp=0;
			    WFGHypervolume<DoubleSolution> hype_pontos;
			    double hyp_pontos=0;
			    WFGHypervolume<DoubleSolution> hype_lhs;
			    double hyp_lhs=0;
			  
				try {
					hype = new WFGHypervolume<>(nomearq);
					hyp = hype.computeHypervolume(population, referencePoint);
					//System.out.println(hyp_pontos);
					//System.out.println(population);
					hype_pontos = new WFGHypervolume<>(nomearq_pontos);
					//System.out.println(algorithm.getGenPop().size());
					hyp_pontos = hype_pontos.computeHypervolume(algorithm.getGenPop(), referencePoint);
					//System.out.println(hyp_pontos);
					//System.out.println(algorithm.getGenPop());
					hype_lhs = new WFGHypervolume<>(nomearq_lhs);
					hyp_lhs = hype_lhs.computeHypervolume(algorithm.getLHSPop(), referencePoint);
					
					//System.out.println(hyp_lhs);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				k[i] = hyp / normalizacao;
				//System.out.println(k[i]);
				k_pontos[i] = hyp_pontos / normalizacao;
				//System.out.println(k_pontos[i]);
				k_lhs[i] = hyp_lhs / normalizacao;
				//System.out.println(k_lhs[i]);
				//System.out.println(algorithm.getLHSPop());
				//bw.write(k[i]);
				if (max < k[i]) {
					max = k[i];
				}
				if (min > k[i]) {
					min = k[i];
				}
				if (max_pontos < k_pontos[i]) {
					max_pontos = k_pontos[i];
				}
				if (min_pontos > k_pontos[i]) {
					min_pontos = k_pontos[i];
				}
				if (max_lhs < k_lhs[i]) {
					max_lhs = k_lhs[i];
				}
				if (min_lhs > k_lhs[i]) {
					min_lhs = k_lhs[i];
				}
			    
			    
			
			    
			}
			for (int i = 0; i < iteracoes; i++) {
				aux = aux + k[i];
				aux_pontos+= k_pontos[i];
				aux_lhs +=  k_lhs[i];
			}
			aux = aux / iteracoes;
			aux_pontos/= iteracoes;
			aux_lhs/= iteracoes;
			for (int i = 0; i < iteracoes; i++) {
				desvpad += Math.pow(aux - k[i], 2);
				desvpad_pontos += Math.pow(aux_pontos - k_pontos[i], 2);
				desvpad_lhs += Math.pow(aux_lhs - k_lhs[i], 2);
			//	gravarArq.print(k[i]+System.getProperty("line.separator"));
			}
			 
			//arq.close();
			desvpad = desvpad / (iteracoes - 1);
			desvpad = Math.sqrt(desvpad);
			desvpad_pontos = desvpad_pontos / (iteracoes - 1);
			desvpad_pontos = Math.sqrt(desvpad_pontos);
			desvpad_lhs = desvpad_lhs / (iteracoes - 1);
			desvpad_lhs = Math.sqrt(desvpad_lhs);
			bw.write("\nNúmero de objetivos: " + m);
			bw.write("\nNúmero de variáveis: " + problem.getNumberOfVariables());
			bw.write("NSGA-III\n");
			bw.write("Média: "+  aux);
			bw.write(" Max: " + max);
			bw.write(" Min " + min);
			bw.write(" Desvio Padrão: " + desvpad);
			bw.write(" \nPontos de Referência\n");
			bw.write("Média: " + aux_pontos);
			bw.write(" Max: " + max_pontos);
			bw.write(" Min " + min_pontos);
			bw.write(" Desvio Padrão: " + desvpad_pontos);
			bw.write("\nLHS\n");
			bw.write("Média: " + aux_lhs);
			bw.write(" Max: " + max_lhs);
			bw.write(" Min " + min_lhs);
			bw.write(" Desvio Padrão: " + desvpad_lhs);
			System.out.print("\nNúmero de objetivos: " + m);
			System.out.print("\nNúmero de variáveis: " + n);
			System.out.print("\nNSGA-III\n");
			System.out.print("Média: "+  aux);
			System.out.print(" Max: " + max);
			System.out.print(" Min " + min);
			System.out.print(" Desvio Padrão: " + desvpad);
			System.out.print(" \nPontos de Referência\n");
			System.out.print("Média: " + aux_pontos);
			System.out.print(" Max: " + max_pontos);
			System.out.print(" Min " + min_pontos);
			System.out.print(" Desvio Padrão: " + desvpad_pontos);
			System.out.print("\nLHS\n");
			System.out.print("Média: " + aux_lhs);
			System.out.print(" Max: " + max_lhs);
			System.out.print(" Min " + min_lhs);
			System.out.print(" Desvio Padrão: " + desvpad_lhs);
			//JMetalLogger.logger.info("Resultados escritos no arquivo .txt");
			
		}
	}
	 	} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
 
  
}

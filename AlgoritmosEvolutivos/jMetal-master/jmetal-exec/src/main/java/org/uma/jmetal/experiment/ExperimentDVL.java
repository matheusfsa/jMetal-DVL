package org.uma.jmetal.experiment;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.multiobjective.dtlz.*;
import org.uma.jmetal.runner.multiobjective.NSGAIIIRunner;
import org.uma.jmetal.solution.DoubleSolution;

public class ExperimentDVL {
	
public static void main(String[] args) {
	int n = 12;//número de variáveis
	int m = 3;//número de objetivos
	int ini_len_set = 1000;//tamanho do conjunto de soluções inicial
	int max_it = 1;//número de iterações maxima do algoritmo
	DoubleProblem problem = new DTLZ2(n, m);//problema
	Algorithm<List<DoubleSolution>> algorithm = NSGAIIIRunner.geraNSGA(problem, 10, null);//algoritmo auxiliar 
	IterativeML iml = new IterativeML(problem, algorithm);
	iml.setEstrategia("experimento2");
	ArrayList<DoubleSolution> pop = iml.pop_gen_lhs(ini_len_set, 0.001, max_it);
	int n_ava = (int) Math.round((iml.getIteracoes()*pop.size() + ini_len_set));
	System.out.println("Tamanho da população:" + pop.size());
	System.out.println("Número de iterações:" + iml.getIteracoes());
	System.out.println("Número de avaliações: " + n_ava);
	System.out.println("Valor de hypervolume da população resultante:" + HyperVolume.hv(problem, pop));
	
}
}

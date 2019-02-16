package org.uma.jmetal.algorithm.multiobjective.nsgaiii;

import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.util.EnvironmentalSelection;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.util.ReferencePoint;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSO;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by ajnebro on 30/10/14.
 * Modified by Juanjo on 13/11/14
 *
 * This implementation is based on the code of Tsung-Che Chiang
 * http://web.ntnu.edu.tw/~tcchiang/publications/nsga3cpp/nsga3cpp.htm
 */
@SuppressWarnings("serial")
public class NSGAIII<S extends Solution<?>> extends AbstractGeneticAlgorithm<S, List<S>> {
  protected int iterations ;

protected int iterations_s ;
  protected int maxIterations ;
  protected ArrayList<double[]> solucoes;
  protected ArrayList<double[]> objetivos;
  protected SolutionListEvaluator<S> evaluator ;
  private String urlTreino = "http://127.0.0.1:5003/treinamento";
  private String urlClassifica = "http://127.0.0.1:5003/classifica";
  private String urlSalva = "http://127.0.0.1:5003/save";
  private String urlInicializa = "http://127.0.0.1:5003/inicializa";
  protected Vector<Integer> numberOfDivisions  ;
  protected List<ReferencePoint<S>> referencePoints = new Vector<>() ;
  protected ArrayList<double[]> pontos = new ArrayList<>();
  @Override
  public ArrayList<double[]> getPontos() {
	return pontos;
  }

List<S> gen_pop = new ArrayList<>();
  List<S> lhs_pop = new ArrayList<>();
  /** Constructor */
  public NSGAIII(NSGAIIIBuilder<S> builder) { // can be created from the NSGAIIIBuilder within the same package
    super(builder.getProblem()) ;
    solucoes =new ArrayList<>();
    objetivos = new ArrayList<>();
    maxIterations = builder.getMaxIterations() ;
    
    crossoverOperator =  builder.getCrossoverOperator() ;
    mutationOperator  =  builder.getMutationOperator() ;
    selectionOperator =  builder.getSelectionOperator() ;
    generatorOperator = builder.getGeneratorOperator();
    evaluator = builder.getEvaluator() ;
    
    population = builder.getPopulation();

    /// NSGAIII
    if(builder.getProblem().getNumberOfObjectives() == 3){
    	numberOfDivisions = new Vector<>(1) ;
    	numberOfDivisions.add(12) ; // Default value for 3D problems
    }
    
    if(builder.getProblem().getNumberOfObjectives() == 5){
    	numberOfDivisions = new Vector<>(1) ;
    	numberOfDivisions.add(6) ; // Default value for 3D problems
    }
    
    if(builder.getProblem().getNumberOfObjectives() == 8){
    	numberOfDivisions = new Vector<>(2) ;
    	numberOfDivisions.add(3) ; // Default value for 3D problems
    	numberOfDivisions.add(2) ; // Default value for 3D problems
    }
    
    if(builder.getProblem().getNumberOfObjectives() == 10){
    	numberOfDivisions = new Vector<>(2) ;
    	numberOfDivisions.add(3) ; // Default value for 3D problems
    	numberOfDivisions.add(2) ; // Default value for 3D problems
    }
    
    if(builder.getProblem().getNumberOfObjectives() == 15){
    	numberOfDivisions = new Vector<>(2) ;
    	numberOfDivisions.add(2) ; // Default value for 3D problems
    	numberOfDivisions.add(1) ; // Default value for 3D problems
    }

    (new ReferencePoint<S>()).generateReferencePoints(referencePoints,getProblem().getNumberOfObjectives() , numberOfDivisions);

    int populationSize = referencePoints.size();
    while (populationSize%4>0) {
      populationSize++;
    }
   
    setMaxPopulationSize(populationSize);
    if(population!=null) {
    	evaluatePopulation(population);
    }
    //System.out.println(maxIterations);
    //JMetalLogger.logger.info("rpssize: " + referencePoints.size());
  }

  @Override
  protected void initProgress() {
    iterations = 1 ;
  }
  
  @Override
  protected void updateProgress() {
    iterations++ ;
  }
  @Override
  public List<S> gera_pop(String exp,List<Double> lower, List<Double> upper, int train_size) {
	  List<S> new_pop = new ArrayList<>();
	  gen_pop = new ArrayList<>();
	  List<S> pop = createInitialPopulation();
	  pontos = new ArrayList<>();
	    int n= solucoes.size();
	    if(n > train_size && train_size != -1) {
			solucoes =new ArrayList<double[]>(solucoes.subList(n-train_size, n));
			objetivos = new ArrayList<double[]>(objetivos.subList(n-train_size, n));
		}
		for (ReferencePoint<S> r : this.referencePoints) {
			double[] ponto = new double[getProblem().getNumberOfObjectives()];
			for(int i = 0; i < getProblem().getNumberOfObjectives(); i++) {
				ponto[i] = r.position.get(i);
			}
			pontos.add(ponto);
		}
		//System.out.println(solucoes.size());
	  @SuppressWarnings("unchecked")
	user userObject = new user(
			    getName(),
			    exp,
			    solucoes,
			    objetivos,
			    pontos,
			    0,
			    0,
			    upper,
			    lower
			);
	ArrayList new_sol = SMPSO.http(urlTreino, userObject);
	for(int i = 0; i< new_sol.size(); i++) {
		ArrayList<Double> sol = (ArrayList<Double>) new_sol.get(i);
		Double[] new_sol_d = new Double[sol.size()];
		for(int j = 0; j < sol.size(); j++)
			new_sol_d[j] = sol.get(j);
	
		generatorOperator.setNew_sol(new_sol_d);
		S s = generatorOperator.execute(pop.get(0));
		
		//System.out.println(Arrays.toString(d.evaluate1(new_sol_d)));
		if(i<maxPopulationSize-1) {
			gen_pop.add(s);
		}
		
	}
	
	gen_pop = evaluatePopulation(gen_pop);
	//System.out.println("Antes:" + gen_pop.size());
	//gen_pop = getNonDominatedSolutions(gen_pop);
	//System.out.println("Depois:" +gen_pop.size());
	return gen_pop;
  }
 
  @Override
  protected boolean isStoppingConditionReached() {
	if(iterations >= maxIterations) {
		this.add_pop(population);
		
	} 

    return iterations >= maxIterations;
  }
  public void gera_pops() {
	  
  }
  public List<S> rank_pop(List<S> pop) {
	  List<S> res = new ArrayList<>();
	  Ranking<S> ranking = computeRanking(pop);
	  List<List<S>> fronts = new ArrayList<>();
	    int rankingIndex = 0;
	    int candidateSolutions = 0;
	    while (candidateSolutions < getMaxPopulationSize()) {
	      fronts.add(ranking.getSubfront(rankingIndex));
	      candidateSolutions += ranking.getSubfront(rankingIndex).size();
	      if ((res.size() + ranking.getSubfront(rankingIndex).size()) <= getMaxPopulationSize())
	        addRankedSolutionsToPopulation(ranking, rankingIndex, res);
	      rankingIndex++;
	    }
	   return res;	  
  }
  
  @Override
	public void setPopulation(List<S> population) {
		// TODO Auto-generated method stub
		super.setPopulation(population);
	}
  @Override
  protected List<S> evaluatePopulation(List<S> population) {
    population = evaluator.evaluate(population, getProblem()) ;
    //Aqui eu envio todos as soluções para o modelo treinar
    this.add_pop(population);
    return population ;
  }
  @Override
  public void add_pop(List<S> population) {
	iterations_s++;
	int n_obj = population.get(0).getNumberOfObjectives();
    int n_var = population.get(0).getNumberOfVariables();
    for(int i = 0; i < population.size(); i++) {
    	double[] solucao = new double[n_var];
    	double[] objetivo = new double[n_obj];
    	
    	for(int j = 0; j < n_var; j++) {
    		solucao[j] = (double) population.get(i).getVariableValue(j);
    	}
    	for(int j = 0; j < n_obj; j++) {
    		objetivo[j] = (double) population.get(i).getObjective(j);
    	}
    	if(solucoes.contains(solucao)) {
    		solucoes.remove(solucao);
    		objetivos.remove(objetivo);
    	}	
    	solucoes.add(solucao);
    	objetivos.add(objetivo);
    }
    	
  }
  @Override
  public ArrayList<double[]> getSolucoes() {
	return solucoes;
}
@Override
public ArrayList<double[]> getObjetivos() {
	return objetivos;
}

@Override
  protected List<S> selection(List<S> population) {
    List<S> matingPopulation = new ArrayList<>(population.size()) ;
    for (int i = 0; i < getMaxPopulationSize(); i++) {
      S solution = selectionOperator.execute(population);
      matingPopulation.add(solution) ;
    }

    return matingPopulation;
  }

  @Override
  protected List<S> reproduction(List<S> population) {
	
    List<S> offspringPopulation = new ArrayList<>(getMaxPopulationSize());
    for (int i = 0; i < getMaxPopulationSize(); i+=2) {
      List<S> parents = new ArrayList<>(2);
      parents.add(population.get(i));
      parents.add(population.get(Math.min(i + 1, getMaxPopulationSize()-1)));

      List<S> offspring = crossoverOperator.execute(parents);

      mutationOperator.execute(offspring.get(0));
      mutationOperator.execute(offspring.get(1));

      offspringPopulation.add(offspring.get(0));
      offspringPopulation.add(offspring.get(1));
    }
    return offspringPopulation ;
  }

  
  private List<ReferencePoint<S>> getReferencePointsCopy() {
	  List<ReferencePoint<S>> copy = new ArrayList<>();
	  for (ReferencePoint<S> r : this.referencePoints) {
		  copy.add(new ReferencePoint<>(r));
	  }
	  return copy;
  }
  @Override
  protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
   
	List<S> jointPopulation = new ArrayList<>();
    jointPopulation.addAll(population) ;
    jointPopulation.addAll(offspringPopulation) ;
   
    /**
    ArrayList new_sol = SMPSO.http(urlTreino, userObject);
    Double[] new_sol_d = (Double[]) new_sol.toArray(new Double[new_sol.size()]);
    generatorOperator.setNew_sol(new_sol_d);
    S s = generatorOperator.execute(jointPopulation.get(0));
    List<S> n_ind = new ArrayList<>();
    n_ind.add(s);
    jointPopulation.addAll(evaluatePopulation(n_ind));
    **/
    Ranking<S> ranking = computeRanking(jointPopulation);
    
    //List<Solution> pop = crowdingDistanceSelection(ranking);
    List<S> pop = new ArrayList<>();
    List<List<S>> fronts = new ArrayList<>();
    int rankingIndex = 0;
    int candidateSolutions = 0;
    while (candidateSolutions < getMaxPopulationSize()) {
      fronts.add(ranking.getSubfront(rankingIndex));
      candidateSolutions += ranking.getSubfront(rankingIndex).size();
      if ((pop.size() + ranking.getSubfront(rankingIndex).size()) <= getMaxPopulationSize())
        addRankedSolutionsToPopulation(ranking, rankingIndex, pop);
      rankingIndex++;
    }
    
    // A copy of the reference list should be used as parameter of the environmental selection
    EnvironmentalSelection<S> selection =
            new EnvironmentalSelection<>(fronts,getMaxPopulationSize(),getReferencePointsCopy(),
                    getProblem().getNumberOfObjectives());
    //System.out.println(pop.size());
    pop = selection.execute(pop);
     
    return pop;
  }

  @Override
  public List<S> getResult() {
    return getNonDominatedSolutions(getPopulation()) ;
  }
  @Override
  public List<S> getGenPop() {
	  return  getNonDominatedSolutions(gen_pop);
	  //return gen_pop;
  }
  @Override
  public List<S> getLHSPop() {
	  //return getNonDominatedSolutions(lhs_pop);
	  return lhs_pop;
  }
  @Override
  public List<S> getInitialPop() {
	  return evaluatePopulation(createInitialPopulation());
	  //return lhs_pop;
  }
  protected Ranking<S> computeRanking(List<S> solutionList) {
    Ranking<S> ranking = new DominanceRanking<>() ;
    ranking.computeRanking(solutionList) ;

    return ranking ;
  }

  protected void addRankedSolutionsToPopulation(Ranking<S> ranking, int rank, List<S> population) {
    List<S> front ;

    front = ranking.getSubfront(rank);

    for (int i = 0 ; i < front.size(); i++) {
      population.add(front.get(i));
    }
  }

  protected List<S> getNonDominatedSolutions(List<S> solutionList) {
    return SolutionListUtils.getNondominatedSolutions(solutionList) ;
  }
  @Override
  public void setSolucoes(ArrayList<double[]> solucoes) {
	this.solucoes = solucoes;
}
  @Override
public void setObjetivos(ArrayList<double[]> objetivos) {
	this.objetivos = objetivos;
}

  @Override public String getName() {
    return "NSGAIII" ;
  }

  @Override public String getDescription() {
    return "Nondominated Sorting Genetic Algorithm version III" ;
  }

}

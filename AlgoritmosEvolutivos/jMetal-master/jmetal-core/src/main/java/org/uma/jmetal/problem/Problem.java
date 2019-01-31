package org.uma.jmetal.problem;

import java.io.Serializable;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;

/**
 * Interface representing a multi-objective optimization problem
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 *
 * @param <S> Encoding
 */

public interface Problem<S> extends Serializable {
  /* Getters */
  int getNumberOfVariables() ;
  int getNumberOfObjectives() ;
  int getNumberOfConstraints() ;
  String getName() ;

  /* Methods */
  void evaluate(S solution) ;
  S createSolution() ;
  S createSolution(S solution);
}

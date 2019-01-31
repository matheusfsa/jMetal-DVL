package org.uma.jmetal.operator;

public interface GeneratorOperator<Source> extends Operator<Source, Source> {
	  int getNumberOfSolution();
	  void setNew_sol(Double[] new_sol);
}

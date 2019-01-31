package org.uma.jmetal.operator.generator;

import java.util.HashMap;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.GeneratorOperator;
import org.uma.jmetal.solution.DoubleSolution;

public class MLGenerator implements GeneratorOperator<DoubleSolution>{
	private Double[] new_sol;
	
	@Override
	public DoubleSolution execute(DoubleSolution source ) {
		DoubleSolution s = (DoubleSolution) source.copy();
		int n_var = s.getNumberOfVariables();
	    for(int i = 0; i < n_var; i ++) {
	    	s.setVariableValue(i,new_sol[i]);
	    }
		return s;
	}

	@Override
	public int getNumberOfSolution() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void setNew_sol(Double[] new_sol) {
		this.new_sol = new_sol;
	}

}

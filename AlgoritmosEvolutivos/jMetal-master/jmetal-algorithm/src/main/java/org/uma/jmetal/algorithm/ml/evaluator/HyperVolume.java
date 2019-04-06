package org.uma.jmetal.algorithm.ml.evaluator;

import java.util.ArrayList;

import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

public class HyperVolume extends Evaluator {

	public HyperVolume(DoubleProblem problem) {
		super(problem);
	}
	private double getRefPoint(String problemName) {
		double REFPOINT = 0;
		if (problemName.equals("CDTLZ2"))
			REFPOINT = 2;
		else if (problemName.equals("DTLZ1"))
			REFPOINT = 1.5;
			//REFPOINT = 600.0;
		else if (problemName.equals("DTLZ2"))
			REFPOINT = 2;
		else if (problemName.equals("DTLZ3"))
			REFPOINT = 11;
			//REFPOINT = 1500.0;
		else if (problemName.equals("DTLZ4"))
			REFPOINT = 2;
		else if (problemName.equals("DTLZ5"))
			REFPOINT = 2;
		else if (problemName.equals("DTLZ6"))
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

	@Override
	public double evaluate(ArrayList<DoubleSolution> pop) {
		double hyp_ref[] = new double[problem.getNumberOfObjectives()];
		double normalizacao = 1;
		double REFPOINT = getRefPoint(problem.getName());
		//REFPOINT = 600.0;
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
		
		String nomearq = "i.tsv";
		WFGHypervolume<DoubleSolution> hype;
	    double hyp=0;
		hype = new WFGHypervolume<>();
		hyp = hype.computeHypervolume(pop, referencePoint);
		
		
		return hyp / normalizacao;
	}

}

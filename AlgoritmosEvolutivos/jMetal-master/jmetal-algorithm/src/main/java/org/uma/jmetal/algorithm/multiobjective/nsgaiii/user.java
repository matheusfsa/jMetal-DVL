package org.uma.jmetal.algorithm.multiobjective.nsgaiii;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;

public class user <S extends Solution<?>>{
	private String algoritmo;
	private String processar;
	private ArrayList Solucoes;
    private ArrayList Objectivos;
    private ArrayList Pontos;
    private int n_sol;
    private int n;
    private List<Double> upper;
    private List<Double>  lower;
    public user() {}
    
    public user(String name,String processar,ArrayList Solucoes, ArrayList Objectivos, ArrayList Pontos,int n_sol, int n,List<Double>  upper,List<Double>  lower) {
        this.algoritmo = name;
        this.processar = processar;
        this.Solucoes = Solucoes;
        this.Objectivos = Objectivos;
        this.Pontos = Pontos;
        this.n_sol = n_sol;
        this.n = n;
        this.upper = upper;
        this.lower = lower;
    }
    
    public String getAlgoritmo() {
    	return algoritmo;
    }
    public String getProcessar() {
    	return processar;
    }
    
    public List<Double>  getUpper() {
		return upper;
	}

	public void setUpper(List<Double>  upper) {
		this.upper = upper;
	}

	public List<Double>  getLower() {
		return lower;
	}

	public void setLower(List<Double>  lower) {
		this.lower = lower;
	}

	public ArrayList getSolucoes() {
    	return Solucoes;
    }
    
    public ArrayList getObjetivos()
    {
    	return Objectivos;
    }
    
    
    public ArrayList getPontos() {
		return Pontos;
	}

	public void setPontos(ArrayList pontos) {
		Pontos = pontos;
	}

	public int getN_sol() {
		return n_sol;
	}

	public void setN_sol(int n_sol) {
		this.n_sol = n_sol;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public String toString() {
    	return "Name: "+this.algoritmo+", Processar: "+this.processar+", Solucoes: "+this.Solucoes+", Objetivos: "+this.Objectivos+"";
    }
    
}
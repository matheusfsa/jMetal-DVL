package org.uma.jmetal.algorithm;

import org.uma.jmetal.util.naming.DescribedEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface representing an algorithm
 * @author Antonio J. Nebro
 * @version 0.1
 * @param <Result> Result
 */
public interface Algorithm<Result> extends Runnable, Serializable, DescribedEntity {
  void run() ;
  Result getResult() ;
  Result getGenPop();
  Result getLHSPop();
  ArrayList<double[]> getPontos();
  void add_pop(Result population);
ArrayList<double[]> getSolucoes();
ArrayList<double[]> getObjetivos();
Result getInitialPop();
Result gera_pop(String exp, List<Double> lower, List<Double> upper, int train_size); 
void setObjetivos(ArrayList<double[]> objetivos);
void setSolucoes(ArrayList<double[]> solucoes);
}

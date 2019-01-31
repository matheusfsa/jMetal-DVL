#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Aug 14 21:24:26 2018

@author: joel
"""
from pyDOE import *
from flask import Flask, request, jsonify
"""
from sklearn.externals import joblib
from sklearn.svm import SVR
from sklearn.ensemble import RandomForestRegressor
from sklearn.tree import DecisionTreeRegressor
from sklearn.neural_network import MLPRegressor
from sklearn.metrics import mean_squared_error
#from sklearn.cross_validation import cross_val_score
from sklearn.model_selection import GridSearchCV
"""
from sklearn.svm import SVR
from time import time

import numpy as np
#from pyDOE import lhs

import json

app = Flask(__name__)

#variaveis globais


"""
######################## INICIALIZA OS CLASSIFICADORES ##################################################################
"""
#classifierSVM = SVR(kernel='rbf', C=1e3, gamma=0.1, tol=0.01)
#classifierMLP = MLPRegressor(hidden_layer_sizes=(10,),max_iter=1000)
# #                                      activation='relu',
# #                                      solver='adam',
#  #                                     learning_rate='adaptive',
#   #                                    max_iter=10000,
#    #                                   learning_rate_init=0.01,
#     #                                  alpha=0.01)
#classifierRF = RandomForestRegressor(n_estimators=1000, max_depth=None,min_samples_split=2, random_state=0,criterion='mse');
#classifierTree = DecisionTreeRegressor(max_depth=500,min_samples_split=2, random_state=0,criterion='mse')


"""
########################## VARIAVEIS GLOBAIS ############################################################################
"""
execult = [];
tamanho = 0;
txErro = [];
#classifier = classifierTree;


"""
######################################## FUNÇÕES PARA SALVAR OS VALORES ##################################################
"""

def Leitura(name):
	arquivo = open(name,'r')
	linhas = arquivo.readlines()
	retorno = []

	for linha in linhas:
		retorno.append(eval(linha.replace("[","").replace("]","").replce(" ",",")))
	arquivo.close()

	return retorno


def Save(lista, name):
    arquivo = open(name,'w')
    i = 0
    while i < len(lista):    
        arquivo.writelines((lista[i]))
        i += 1
    arquivo.close()

def EscreveArquivo(indice, lista, name):
    arquivo = open(name,'w')
    i = 0
    while i < len(lista):    
        arquivo.write(str(indice[i]) +" "+ str(lista[i])+'\n')
        i += 1
    arquivo.close()


def EscreveArquivoC(indice, compara ,lista, name):
    arquivo = open(name,'w')
    i = 0
    arquivo.write("Indice" +" "+ "Predição"+" "+"Rotulo"+" "+"Erro"+'\n')
    while i < len(lista):
        arquivo.write(str(indice[i]) +" "+str(compara[i])+" "+ str(lista[i])+'\n')
        i += 1
    arquivo.close()



"""
##################################################### TREINAMENTO ########################################################
"""
from sklearn.neural_network import MLPRegressor
from sklearn import linear_model
from sklearn.gaussian_process import GaussianProcessRegressor
from sklearn.gaussian_process.kernels import DotProduct, WhiteKernel
import pandas as pd

def train_model(X, y, i):
    #model = linear_model.SGDRegressor(max_iter=1000, tol=1e-3)
    #kernel = DotProduct() + WhiteKernel()
    #model = GaussianProcessRegressor(kernel=kernel,random_state = 0).fit(X, y)
    model = linear_model.RidgeCV(alphas=[0.01, 0.03, 0.06, 0.1, 0.3, 0.6, 1, 3, 6, 10, 30, 60])
    inicio = time()
    #print(X)
    model = model.fit(X, y)
    fim = time()
    print("terminou o treinamento de ", i, "em ", fim - inicio, "segundos")
    return model


@app.route("/treinamento", methods=['GET', 'POST'])
def treino():
    global classifier
    lista = []
    message = request.get_json(silent=True)
    processar = message["processar"]
    nSolution = np.asarray(message["solucoes"])
    nObj = np.asarray(message["objetivos"])
    pontos = np.asarray(message["pontos"])
    nSol = int(message["n_sol"])
    n = int(message["n"])
    solucoes = []
    lower = np.asarray(message["lower"])
    upper = np.asarray(message["upper"])
    print(lower)
    print(upper)
    print(processar)
    #np.savetxt('/home/matheus/X.txt',nObj)
    #np.savetxt('/home/matheus/Y.txt', nSolution)
    #np.save('/home/matheus/Pontos.txt', pontos)
    if processar == "experimento":
        n = len(nSolution[0])
        m = len(nObj[0])
        reg = [train_model(nObj, nSolution[:, i], i) for i in range(n)]
        for p in pontos:
            print('ponto: ',p)
            lista = []
            for i in range(n):
                lista.append(reg[i].predict(np.reshape(p, [1, m]))[0])
            print('estimativa:', lista)
            solucoes.append(trunca(lista, lower, upper))
        k = lhs(m, samples=len(pontos))

        for ks in k:
            lista = []
            for i in range(n):
                lista.append(reg[i].predict(np.reshape(ks, [1, m]))[0])
            solucoes.append(trunca(lista, lower, upper))

    elif processar == "lhs":
        n = len(nSolution[0])
        m = len(nObj[0])
        svr = [SVR(gamma='auto') for _ in range(n)]
        for i in range(n):
            svr[i].fit(nObj, nSolution[:, i])

        k = 0.1*lhs(m, samples=len(pontos))

        for ks in k:
            lista = []
            for i in range(n):
                lista.append(svr[i].predict(np.reshape(ks, [1, 2]))[0])

            solucoes.append(trunca(lista, lower, upper))

    elif "sol_lhs":
        k = lhs(n, samples=nSol)
        for ks in k:
            lista = []
            for kks in ks:
                lista.append(kks)
            solucoes.append(trunca(lista, lower, upper))
    else:
        ypredict = classifier.predict(nSolution)
        lista = ypredict.tolist()

    print(processar)

    print(len(solucoes))
    return json.dumps({"retorno": solucoes})


def trunca(s, lower, upper):
    for i in range(len(s)):
        if s[i] < lower[i]:
            s[i] = lower[i]
        elif s[i] > upper[i]:
            s[i] = upper[i]
    return s



"""
######################################################### SALVAR #########################################################
"""
"""
@app.route("/save", methods=['GET', 'POST'])
def save():
    global classifier
    global txErro
    
    message = request.get_json(silent=True)
    
    #salva o erro e o classificador
    joblib.dump(classifier, "NOME.pkl")
    #EscreveArquivo(execult, real,'Real.txt')
    return message

"""
#################################### CLASSIFICA AS SOLUCOES ###############################################################
"""

@app.route("/classifica", methods=['GET', 'POST'])
def classifica():
    global classifier
    global txErro
    
    message = request.get_json(silent=True)
    nSolution = np.asarray(message["solucoes"]) #passa o numero de exemplos na posicao 0
    nObj = np.asarray(message["objetivos"]) #passa o numero de variaveis na posicao 0
        
    y_predict = classifier.predict(nSolution)
    
    #txErro.append(mean_squared_error(np.asarray(nObj),np.asarray(y_predictSVM)))
    #txErro.append(1.0 - classifierSVM.score(np.asarray(nSolution), np.asarray(nObj)))
            

    
    return json.dumps({"retorno": y_predict.tolist()})

"""
############################## INICIALIZA AS VARIAVEIS COM A FUNCAO LHS ####################################################
"""
@app.route("/inicializa", methods=['GET', 'POST'])
def Inicializa():
    message = request.get_json(silent=True)
    
    nSolution = np.asarray(message["solucoes"]) #passa o numero de exemplos na posicao 0
    nObj = np.asarray(message["objetivos"]) #passa o numero de variaveis na posicao 0
    
    retorno = lhs(nObj[0], samples=nSolution[0])
    retorno = np.asarray(retorno)


    return json.dumps({"retorno": retorno.tolist()})
"""

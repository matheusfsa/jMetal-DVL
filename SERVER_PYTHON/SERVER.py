from pyDOE import *
from flask import Flask, request
from time import time
from sklearn import linear_model
import pandas as pd
from sklearn.multioutput import MultiOutputRegressor

import numpy as np


import json

app = Flask(__name__)


"""
##################################################### TREINAMENTO ########################################################
"""


def train_model(x, y, n):
    x = x.iloc[:n, :]
    y = y.iloc[:n, :]
    est = linear_model.RidgeCV(alphas=[0.05, 0.1, 0.3, 1, 3, 5, 10, 15, 30, 50, 75])
    model = MultiOutputRegressor(est)
    model = model.fit(x, y)
    return model


@app.route("/treinamento", methods=['GET', 'POST'])
def treino():
    message = request.get_json(silent=True)
    processar = message["processar"]
    n_solution = np.asarray(message["solucoes"])
    n_obj = np.asarray(message["objetivos"])
    pontos = np.asarray(message["pontos"])
    n_sol = int(message["n_sol"])
    n = int(message["n"])
    solucoes = []
    lower = np.asarray(message["lower"])
    upper = np.asarray(message["upper"])
    if processar == "add_sols":
        np.savetxt("estimativa-" + message["algoritmo"] + '.txt', n_obj)
    if processar == "experimento1":
        # np.savetxt('X-LHS.txt', n_obj)
        # np.savetxt('Y-LHS.txt', n_solution)
        inicio = time()
        solucoes = est1(n_obj, n_solution, pontos, lower, upper, 300)
        fim = time()
        print("terminou o treinamento em ", fim - inicio, "segundos")
    if processar == "experimento2":
        # np.savetxt('X-LHS.txt', n_obj)
        # np.savetxt('Y-LHS.txt', n_solution)
        inicio = time()
        solucoes = est2(n_obj, n_solution, pontos, lower, upper)
        fim = time()
        print("terminou o treinamento em ", fim - inicio, "segundos")
    elif "sol_lhs":
        k = lhs(n, samples=n_sol)
        for ks in k:
            lista = []
            for kks in ks:
                lista.append(kks)
            solucoes.append(trunca(lista, lower, upper))

    print(processar)

    print(len(solucoes))
    return json.dumps({"retorno": solucoes})


def est1(n_obj, n_solution, pontos, lower, upper, n):
    solucoes = []
    train_points = pd.DataFrame(n_obj)
    train_sols = pd.DataFrame(n_solution)
    train_sols.columns = ['Var_' + str(i) for i in range(train_sols.shape[1])]
    train_points.columns = ['Obj_' + str(i) for i in range(train_points.shape[1])]
    data = pd.concat([train_points, train_sols], axis=1)
    m = len(n_obj[0])
    for p in pontos:
        dist = (data[train_points.columns] - p).apply(np.linalg.norm, axis=1)
        data = data.assign(dist=dist.values)
        data = data.sort_values(by=['dist'])
        reg = train_model(data[train_points.columns], data[train_sols.columns], n)
        lista = reg.predict(np.reshape(p, [1, m]))[0].tolist()
        solucoes.append(trunca(lista, lower, upper))
    return solucoes


def est2(n_obj, n_solution, pontos, lower, upper):
    solucoes = []
    train_points = pd.DataFrame(n_obj)
    train_sols = pd.DataFrame(n_solution)
    train_sols.columns = ['Var_' + str(i) for i in range(train_sols.shape[1])]
    train_points.columns = ['Obj_' + str(i) for i in range(train_points.shape[1])]
    m = len(n_obj[0])
    reg = train_model(train_points, train_sols, train_points.shape[0])
    for p in pontos:
        lista = reg.predict(np.reshape(p, [1, m]))[0].tolist()
        solucoes.append(trunca(lista, lower, upper))
    print("Tamanho:", len(solucoes[0]))
    return solucoes
    # np.savetxt('/home/matheus/sols.txt', np.array(solucoes))


def trunca(s, lower, upper):
    for i in range(len(s)):
        if s[i] < lower[i]:
            s[i] = lower[i]
        elif s[i] > upper[i]:
            s[i] = upper[i]
    return s


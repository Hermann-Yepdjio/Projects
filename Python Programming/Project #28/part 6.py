# -*- coding: utf-8 -*-
"""
Created on Sun Nov 21 04:05:25 2021

@author: ajoke
"""


import numpy as np
import numpy.lib.recfunctions as rec
import pandas as pd
import matplotlib as mat
import matplotlib.pyplot as plt

universe = ['IBM', 'MSFT', 'GOOG', 'AAPL', 'AMZN', 'FB', 'NFLX', 'TSLA', 'ORCL', 'SAP']
startingDay = 2
day = startingDay
interval = 1
MTM_buy_low = None
MTM_buy_high = None
optimal_interval_buy_low = None
optimal_interval_buy_high = None
optimal_MTM_buy_low = None
optimal_MTM_buy_high = None
Market = pd.DataFrame( { universe[0]+" Close": pd.read_csv(universe[0]+".csv", usecols=[5], squeeze=(True)),
    universe[0]+" Adj Close": pd.read_csv(universe[0]+".csv", usecols=[6], squeeze=(True)),
    universe[1]+" Close": pd.read_csv(universe[1]+".csv", usecols=[5], squeeze=(True)),
    universe[1]+" Adj Close": pd.read_csv(universe[1]+".csv", usecols=[6], squeeze=(True)),
    universe[2]+" Close": pd.read_csv(universe[2]+".csv", usecols=[5], squeeze=(True)),
    universe[2]+" Adj Close": pd.read_csv(universe[2]+".csv", usecols=[6], squeeze=(True)),
    universe[3]+" Close": pd.read_csv(universe[3]+".csv", usecols=[5], squeeze=(True)),
    universe[3]+" Adj Close": pd.read_csv(universe[3]+".csv", usecols=[6], squeeze=(True)),
    universe[4]+" Close": pd.read_csv(universe[4]+".csv", usecols=[5], squeeze=(True)),
    universe[4]+" Adj Close": pd.read_csv(universe[4]+".csv", usecols=[6], squeeze=(True)),
    universe[5]+" Close": pd.read_csv(universe[5]+".csv", usecols=[5], squeeze=(True)),
    universe[5]+" Adj Close": pd.read_csv(universe[5]+".csv", usecols=[6], squeeze=(True)),
    universe[6]+" Close": pd.read_csv(universe[6]+".csv", usecols=[5], squeeze=(True)),
    universe[6]+" Adj Close": pd.read_csv(universe[6]+".csv", usecols=[6], squeeze=(True)),
    universe[7]+" Close": pd.read_csv(universe[7]+".csv", usecols=[5], squeeze=(True)),
    universe[7]+" Adj Close": pd.read_csv(universe[7]+".csv", usecols=[6], squeeze=(True)),
    universe[8]+" Close": pd.read_csv(universe[8]+".csv", usecols=[5], squeeze=(True)),
    universe[8]+" Adj Close": pd.read_csv(universe[8]+".csv", usecols=[6], squeeze=(True)),
    universe[9]+" Close": pd.read_csv(universe[9]+".csv", usecols=[5], squeeze=(True)),
    universe[9]+" Adj Close": pd.read_csv(universe[9]+".csv", usecols=[6], squeeze=(True))
    } )

def Price(stock, amount):
    return Market.loc[day][stock+" Adj Close"] * amount

def Contest():
    vals = np.array([])
    uni = np.array(universe)
    for v in universe:
        c = (Market.loc[day][v+" Adj Close"] - Market.loc[day-interval][v+" Adj Close"])/Market.loc[day-interval][v+" Adj Close"]
        vals = np.append(vals, c)
    return np.sort(rec.merge_arrays((vals,uni)))[::-1]

def calcMTM(a):
    return a.loc[0]["Cash"] + Price(universe[0],a.loc[0][universe[0]]) + Price(universe[1],a.loc[0][universe[1]]) + Price(universe[2],a.loc[0][universe[2]]) + Price(universe[3],a.loc[0][universe[3]]) + Price(universe[4],a.loc[0][universe[4]]) + Price(universe[5],a.loc[0][universe[5]]) + Price(universe[6],a.loc[0][universe[6]]) + Price(universe[7],a.loc[0][universe[7]]) + Price(universe[8],a.loc[0][universe[8]]) + Price(universe[9],a.loc[0][universe[9]])

def Simulate():
    global day
    global optimal_interval_buy_low
    global optimal_interval_buy_high
    global optimal_MTM_buy_low
    global optimal_MTM_buy_high
    
    day = startingDay
    MTM1 = np.array([5000000.00]) #Buys low sells high
    MTM2 = np.array([5000000.00]) #Buys high sells low
    account1 = pd.DataFrame({
        "Cash": [5000000.00],
        universe[0]: [0],
        universe[1]: [0],
        universe[2]: [0],
        universe[3]: [0],
        universe[4]: [0],
        universe[5]: [0],
        universe[6]: [0],
        universe[7]: [0],
        universe[8]: [0],
        universe[9]: [0]})
    account2 = pd.DataFrame({
        "Cash": [5000000.00],
        universe[0]: [0],
        universe[1]: [0],
        universe[2]: [0],
        universe[3]: [0],
        universe[4]: [0],
        universe[5]: [0],
        universe[6]: [0],
        universe[7]: [0],
        universe[8]: [0],
        universe[9]: [0]})
    
    for x in universe:
        account1.at[0,"Cash"] += Price(x,5)
        account2.at[0,"Cash"] += Price(x,5)
        account1.at[0,x] += 5
        account2.at[0,x] += 5
        
        while (day < 249):
            day += 1
            if (day % interval == startingDay % interval):
                p = Contest()
                doit = 0
                transact1 = 0
                transact2 = 0
                while(doit < 10):
                    if doit < 5:
                        transact1 -= Price(p[doit][1],5)
                        account1.at[0,p[doit][1]] += 5
                        transact2 += Price(p[doit][1],5)
                        account1.at[0,p[doit][1]] -= 5
                    else:
                        transact1 += Price(p[doit][1],5)
                        account1.at[0,p[doit][1]] -= 5
                        transact2 -= Price(p[doit][1],5)
                        account1.at[0,p[doit][1]] += 5
                    doit+=1
                account1.at[0,"Cash"] += transact1
            account2.at[0,"Cash"] += transact2
        MTM1 = np.append(MTM1, calcMTM(account1))
        MTM2 = np.append(MTM2, calcMTM(account2))
        
    x = np.arange(0, MTM1.size, 1)
    fig, ax = plt.subplots()
    ax.plot(x, MTM1,c = "blue")
    ax.plot(x, MTM2,c = "red")
    plt.show()
    
    if (optimal_MTM_buy_high == None or optimal_MTM_buy_high < MTM1[-1]):
        optimal_MTM_buy_high = MTM1[-1]
    if (optimal_MTM_buy_low == None or optimal_MTM_buy_low < MTM1[-1]):
        optimal_MTM_buy_low = MTM2[-1]
    
    if (optimal_interval_buy_high == None or optimal_interval_buy_high < MTM1[-1]):
        optimal_interval_buy_high = interval
    if (optimal_interval_buy_low == None or optimal_interval_buy_low < MTM1[-1]):
        optimal_interval_buy_low = interval
        
    print(str(MTM1[-1])+", "+str(MTM2[-1]))
    print(str(optimal_MTM_buy_low)+", "+str(optimal_MTM_buy_high))
    print(str(optimal_interval_buy_low)+", "+str(optimal_interval_buy_high))
    print("")


while(interval < 249):
    Simulate()

print("final")
print(str(optimal_MTM_buy_low)+", "+str(optimal_MTM_buy_high))
print(str(optimal_interval_buy_low)+", "+str(optimal_interval_buy_high))

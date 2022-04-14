from math import *
import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import fsolve


A, a, b, B, p0 = 1, -3, 5, 0, 4.5

def compute_p():
    func = lambda p: a*p + b - A*p - B - log(1 + p, e)
    return fsolve(func, 0)


def compute_prices(n):
    prices = [0] * n
    for i in range(0, n):
        if (i == 0):
            prices[i] = (A * p0 + B + log(1 + p0, e) - b)/a 
        else:
            prices[i] = (A * prices[i-1] + B + log(1 + prices[i-1], e) - b)/a 
    return prices


def compute_supplies(prices):
    n = len(prices)
    supplies = [0] * n
    for i in range(0, n):
        if (i == 0):
            supplies[i] = (A * p0 + B + log(1 + p0, e)) 
        else:
            supplies[i] = (A * prices[i-1] + B + log(1 + prices[i-1], e))
    return supplies


def application():
    print "A stable price such that the production of wheat from year to year is constant is: ", compute_p()
    n = int(raw_input("Enter an integer for N: "))
    prices = compute_prices(n)
   # print prices
    supplies = compute_supplies(prices)
    arr = []
    for i in range(0, len(supplies)):
        arr.append(i)
    plt.plot(prices, supplies)
    plt.xlabel('prices')
    plt.ylabel('supplies')
    plt.show()
    plt.plot(arr, prices)
    plt.xlabel('years')
    plt.ylabel('prices')
    plt.show()
    print 'From the 2nd plot, we can see that the price becomes constant (about 1.06831) as N tends to infinity'

           
application()

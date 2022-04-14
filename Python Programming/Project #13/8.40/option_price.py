import numpy as np
import matplotlib.pyplot as plt
import scipy.optimize as opt



r, sigma, S0, mean, std_dev, T, K, N= 0.0002, 0.015, 100, 0, 1, 100, 102, 1000
np.random.seed(0)


def S(T):
    path = []
    e = np.random.normal(mean, std_dev, T)
    for t in range (T):
        if (t==0):
            path.append((1+r)*S0 + sigma*S0*e[t])
        else:
            path.append((1+r)*path[t-1] + sigma*path[t-1]*e[t])
    return path


def S_average(path):
    return np.mean(path)


def p(n):
    prices = []
    for i in range(n):
        prices.append((1+r)**(-T) * max(S_average(S(T)) - K, 0))
    return np.mean(prices)


def func(N, c):
    return c/np.sqrt(N)


def plot_prices():
    Ns, prices = [], [] 
    for i in range (N):
         Ns.append(i+1) 
         prices.append(p(i+1))
    plt.plot(Ns, prices)
    plt.xlabel('N')
    plt.ylabel('prices')
    plt.title(' Prices as a function of N with N = 1000.')
    plt.show()
    plt.close()
    return prices, Ns


def plot_errors():
    prices, Ns = plot_prices()
    right_price = prices[len(prices)-1]
    errors = []
    for price in prices:
        errors.append((np.abs(price - right_price)*100)/right_price)
    optimized_params, pcov = opt.curve_fit(func, Ns, errors)
    plt.plot(Ns, errors, '.', label = 'Error in Price Estimation')
    plt.plot( Ns, func(Ns, *optimized_params), label = 'fitting curve')
    plt.xlabel('N')
    plt.ylabel('percentage in error')
    plt.title('Error in the price estimation as a function of N with N = 1000.')
    plt.legend()
    plt.show()
    plt.close()


plot_errors()
    
    

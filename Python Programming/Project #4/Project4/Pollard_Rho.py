import random
import numpy as np

def Pollard_Rho(n):
    i = 1
    x1 = random.randint(0, n - 1)
    y, k = x1, 2
    while(True):
        i = i + 1
        x2 = ((x1 ** 2) - 1) % n
        d = np.gcd(y - x2, n)
        if d != 1 and d != n:
            return d
        if i == k:
            y = x2
            k = 2*k
        x1 = x2

def pollard_rho(n):
    """
    Attempts to factor an integer
    :param n: int to factor
    :return: a factor of n, or False
    """
    x = 2
    y = x
    d = 1
    while d == 1:
        x = (x ** 2 + 1) % n
        y = (y ** 2 + 1) % n
        y = (y ** 2 + 1) % n
        d = np.gcd(abs(y - x), n)
    if d == n:
        return False
    else:
        return d



#print (Pollard_Rho(2752977223569345565189))


from math import *
from decimal import *

k_values = [2, 3, 4, 5, 6, 7, 8, 9, 10]  # list containing the different values for k


def pathlength(x, y): # compute the pathlength
    sum1 = 0
    for i in range(1, len(x)):
        sum1 = sum1 + Decimal(sqrt((x[i]-x[i-1])**2 + (y[i]-y[i-1])**2))
    return sum1


def application():  # run the different applications cases and compute the error each time
    for i in k_values:
        n = 2**i
        x = [0] * (n+1)  # create a list x of size n+1 filled with zeros
        y = [0] * (n+1)  # create a list y of size n+1 filled with zeros
        for j in range(0, n+1): # compute n+1 points
            x[j] = cos(2*pi*j/n)/2
            y[j] = sin(2*pi*j/n)/2
        print "k =  ", i
        print "   pi_value               pi_Approximation               error"
        print " ", '%.8f' % pi,
        print "               ", '%.8f' % pathlength(x, y),
        print "               ", '%.2e' % (Decimal(pi) - pathlength(x, y)), "\n"


application() # runs the function application

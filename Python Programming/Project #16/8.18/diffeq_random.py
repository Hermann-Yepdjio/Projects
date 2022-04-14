import random
from scitools.std import plot, compute_histogram, show, get_backend
a = 8121
c = 28411
m = 134456
seed = random.randint(1, 100000)


def N_random_numbers():
    X = []
    Y = []
    print "Seed x0 = ",seed
    N = input("\nHow many random numbers would you like to generate? : ")
    print "\n"
    for n in range (0, N):
        if (n == 0):
            X.append((seed*a + c) % m)
        else:
            X.append((X[n-1]*a + c) % m)
        Y.append(float(X[n])/m)
        print 'random_number y' + str(n) , " : ", Y[n]
    return Y


def application():
    samples = N_random_numbers()
    x, y= compute_histogram(samples, nbins=20)
    plot(x, y, '-')
    show()   
    g = get_backend()
    g.show()
    raw_input('press enter')

application()

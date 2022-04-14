import math
pi = math.pi
T = 2*pi
alpha_values = {0.01, 0.25, 0.49} # holds the different values for alpha
n_values = {1, 3, 5, 10, 30, 100} # holds the different values for n


def S(t, n, T): # function S
    S = 0
    for i in range (1, n+1):
        S = S + math.sin((2*(2*i-1)*pi*t)/T)/(2*i-1)
    return (4/pi)*S


def f(t, T):  # function f
    if (0 < t) and (t < T/2):
        return 1
    elif t == T/2:
        return 0
    elif (T/2 < t) and (t < T/2):
        return -1


def tab():  # perform operations and display tabular information
    for n in sorted(n_values):
        print 'n = ', n
        for alpha in sorted(alpha_values):
            t = T*alpha
            F = f(t, T)
            s = S(t, n, T)
            print "alpha = ", alpha, "      ",
            print 'f(t, T) = ', F, "  ",
            print 'S(t, n, T) = ', s, "  ",
            print 'Error = ', F-s
        print


tab()  # executes the tab function


# After running the different application cases, we notice that the errors are similar for alpha = 0.01 and alpha = 0.49
# we also notice that the error is less important when alpha = 0.25 (than when alpha = 0.01 or 0.49 for the same n value
# Finally when following the pattern, we can observe that the error get better and better as n increases


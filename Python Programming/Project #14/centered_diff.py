from math import *
from decimal import *


def diff(f, x, h=1E-5): # compute the approximation of the derivative
    return (Decimal(f(x+h)) - Decimal(f(x-h)))/Decimal(2*h)


def test_diff(): # test function
    x = 5
    # f = 2*x**2 + 3*x + 5
    exact = 23  # f' = 4x+3 and f'(5) = 4*5 + 3 = 23
    success = diff(lambda x: 2*x**2 + 3*x + 5, x) == exact
    msg = 'test_diff failed!'
    assert success, msg


def application(): # compute the different application cases and finds the erros
    print "values of x        functions              derivative          exact derivative            approx derivative               error"
    print
    print "   x = 0           f = e**x              f'(x) = e**x           f'(0) = 1                 f'(0) =",
    result = diff(lambda x: e**x, x=0, h=0.01)
    print '%.8f' % result,
    print "         er = ", '%.2E' % (1 - result)

    print "   x = 0        f = e**(-2x**2)     f'(x) = -4xe**(-2x**2)     f'(0) = 0                  f'(0) =",
    result = diff(lambda x: e**(-2*x**2), x=0, h=0.01)
    print '%.8f' % result,
    print "         er = ", '%.2E' % (0 - result)

    print "   x = 2pi         f = cos x             f'(x) = -sin x        f'(2pi) = 0                f'(0) =",
    result = diff(lambda x: cos(x), x=2*pi, h=0.01)
    print '%.8f' % result,
    print "         er = ", '%.2E' %(0 - result)

    print "   x = 1           f = ln x              f'(x) = 1/x           f'(1) = 1                  f'(0) =",
    result = diff(lambda x: log(x, e), x=1, h=0.01)
    print '%.8f' % result,
    print "         er = ", '%.2E' %(1 - result)


# test_diff()
application() # to execute the function application

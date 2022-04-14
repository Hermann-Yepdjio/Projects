from scitools.sound import *
from scitools.std import *

def solve(x, p):
    X = x
    for n in range (p+1, len(x)):
        X[n] = (1.0/2)*(X[n-p] + X[n-p-1])
    return X

def method1(arr):
    arr[0] = 1
    return arr

    
def method2(arr):
    for i in range (0, len(arr)):
        arr[i] = random.uniform(-1, 1)
    return arr


def application():
    r = 44100
    p = r/440
    x1 = zeros(3*r)
    x1 = method1(x1)
    x1 = solve(x1, p)
    x1 = x1 * max_amplitude
    x2 = zeros(2*r)
    x2 = method2(x2)
    p = r/392
    x2 = solve(x2, p)
    x2 = x2 * max_amplitude
    x = concatenate((x1, x2))
    tmp = []
    for num in x:
        tmp.append([[num]])
    x = concatenate(tmp)
    write(x, 'guitar.wav')
    #x = read('guitar.wav')
    #play('guitar.wav')

application()

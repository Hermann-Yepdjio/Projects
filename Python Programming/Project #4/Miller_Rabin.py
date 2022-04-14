import random

def modular_exponentiation(a, b, n):
    #"Calculate (x ** y) % z efficiently."
    c = 0
    d = 1
    b = "{0:b}".format(b) # convert b to binary (the result is a string)
    b_k = list(map (int, b)) # split b into a list of characters and convert each character to int
    for i in range (len(b_k)):
       c = 2 * c
       d = (d * d) % n
       if b_k[i] == 1:
           c = c + 1
           d = (d * a) % n
    return d
def Witness (a, n):
    t = ((n - 1) & -(n - 1)).bit_length() -1
    u = (n - 1) >> 1
    x0 = modular_exponentiation(a, u, n)
    for i in range (1, t + 1):
        x1 = (x0 ** 2) % n
        if x1 == 1 and x0 != 1 and x0 != n - 1:
            return True
        x0 = x1
    if x0 != 1:
        return True
    return False


def Miller_Rabin(n, s):
    for j in range(1, s):
        a = random.randint(1, n - 1)
        if Witness(a, n):
            return False
    return True




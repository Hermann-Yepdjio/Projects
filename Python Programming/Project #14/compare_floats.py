

a = 1/947.0 * 947
b = 1
tol = 1e-15  # tolerance (very small number)
if a != b:
    print 'wrong result!'

if abs(a - b) < tol:
    print 'good result!'

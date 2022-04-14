from mayavi import mlab
import numpy as np
h0 = 2277
R = 4	
x = y = np.linspace(-10., 10., 41)
#print x
xv, yv = np.meshgrid(x, y, indexing='ij', sparse=False)
hv = h0/(1 + (xv**2+yv**2)/(R**2))                                                                  

mlab.figure(size=(640, 800), bgcolor=(0.16, 0.28, 0.46))

mlab.surf(xv, yv, hv, warp_scale=0.01, representation= 'fancymesh')
mlab.show()

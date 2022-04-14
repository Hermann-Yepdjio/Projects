from osgeo import gdal
from mayavi import mlab

ds = gdal.Open('new30.tif')
data = ds.ReadAsArray()
print data
mlab.figure(size=(640, 800), bgcolor=(0.16, 0.28, 0.46))

mlab.surf(data, warp_scale=0.1)
mlab.show()

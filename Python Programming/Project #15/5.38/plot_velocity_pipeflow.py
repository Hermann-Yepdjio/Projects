from scitools.std import *
import os, glob

R= 1
Beta= 0.02
Uo= 0.02
n= 0.1


def v(r): # used for plotting v(r) when n = 0.1
    return ((Beta/(2*Uo))**(1/n))*(n/(n+1))*(R**(1+1/n)-r**(1+1/n))


def v1(r, n): # used for animation when n varies
    return ((Beta/(2*Uo))**(1/n))*(n/(n+1))*(R**(1+1/n)-r**(1+1/n))


def application(): # plot the v(r) function
    r = linspace(0, R, 50)
    y = v(r)
    plot(r, y, '*', xlabel='v', ylabel='f',
            title= 'Plot velocity pipeflow',
            axis = [ 0, 1.1, 0, 0.00015], savefig= 'plot.png', show = True)




def animation(): # Make an animation of how v(r)varies as n decreases
    r = linspace(0, R, 50)
    n_values = linspace(1, 0.01, 50)
    for filename in glob.glob('tmp*.png'):
        os.remove(filename) 
    counter = 0
    for n in n_values:
        y = v1(r, n)
        plot(r, y, '*', xlabel='r', ylabel='v',
                title= 'Plot velocity pipeflow',
                axis = [ 0, 1.1, 0, v1(0, n)], savefig= 'tmp%04d.png' % counter)
        counter += 1


    cmd = 'convert -delay 6/100 tmp*.png movie.gif'
    os.system(cmd)

application()
animation()

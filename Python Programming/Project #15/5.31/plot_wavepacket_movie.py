from scitools.std import *
import glob,os, time 

def f(x, t):
    return (e**(-(x-3*t)**2))*sin(3*pi*(x-t))

def application():  #make the animation
    x = linspace(-6.0, 6.0, 1001)
    t_values = linspace(-1.0, 1.0, 61)

    # max_f is 1 since the exponential part of f has a neg pow and sin_max is 1
    max_f = 10

    #show the movie on the screen and make hardcopies of frames simultaneously

    for filename in glob.glob('tmp*.png'):  #to remove old files before creating new ones
        os.remove(filename)

    counter =0
    for t in t_values:  #Plots the wave function for each t value
        y = f(x, t)
        plot(x, y, '-', axis= [x[0], x[-1], -10, max_f],
                xlabel='x', ylabel='f', legend='t=%4.2f'%t,
                savefig='tmp%04d.png' % counter, show = True)


        #time.sleep(0.2)  # pause to control the movies speed###
        counter += 1

    cmd = 'convert -delay 6/100 tmp*.png movie.gif' #create the .gif file
    os.system(cmd)


application()

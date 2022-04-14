# Heather McKinnon
# CS 528 - Project 4
# Description: Make a guitar-like sound

# Exercise A.16 Given start values, x(0), x(1),..., x(p), the following 
# difference equation is known to create a guitar-like sound:
#
#       EQUATION: x(n) = 1/2 * ( x(n-p) + x(n-p-1) ) where n = p + 1,...,N
#
# With a sampling rate r, the frequency of this cound is given by r / p. 
# Make a program with a function solve(x, p) which returns the solution 
# array x of the formula above. To initialize the array x[0:p+1] we look 
# at two methods, which can be implemented in two alternative functions:
#   
#       - x(0) = 1, x(1) = x(2) = ... = x(p) = 0
#       - x(0),..., x(p) are uniformly distibuted random numbers in [-1,1]

# Import max_amplitude, write, and play, from scitools.sound module.
import numpy as np
import wave

# definition for equation
def solve(x, p):
    for n in range(p + 1, len(x)):
        x[n] = 0.5 * ( x[n - p] + x[n - p - 1] )
    return x   

# taken from scitools.sound, modified 
def write_sound(data, filename, sample_rate=44100):
    """
    Writes the array data to the specified filename.
    The array data type can be arbitrary as it will be
    converted to numpy.int16 in this function.
    """
    ofile = wave.open(filename, 'w')
#    m, n =numpy.shape(data)
    ofile.setnchannels(1)
    ofile.setsampwidth(2)
    ofile.setframerate(sample_rate)
#    data = newdata.flatten()
    ofile.writeframesraw(data.astype(np.int16).tostring())
    ofile.close()

# Choose a sampling rate r and set p = r / 440 to create a 440 Hz tone(A).
sample_rate = 44100
r = sample_rate
max_amp = 2**(15-1) # taken from scitools.sound


# Create an array x(1) of zeroes with length 3r such that the tone will 
# last for 3 seconds. 
# Initililze x(1) according to method 1 above and solve the equation above.
# Multiply the x(1) array by max amplitude. 
A_freq = 440
p = r / A_freq

x1 = np.zeros(3*r)
x1[0] = 1
x1 = x1.astype(np.float)

x1 = solve(x1, p)
for i in range(0, len(x1)):
    x1[i] *= max_amp


# Repeat this process for an array x(2) of length 2r, but use method 2 for 
# the initial values and choose p such that the tone is 392 Hz (G). 
G_freq = 392
p = r / G_freq

x2 = np.random.uniform(-1.0, 1.0, 2*r)    
x2 = x2.astype(np.float)
   
x2 = solve(x2, p)
for i in range(0, len(x2)):
    x2[i] *= max_amp

# Concatenate x(1) and x(2), call write and then play to play the sound. 
# This will sound like a guitar playing A for 3 seconds then G for 2 seconds.

guitar_sound = np.append(x1,x2)
guitar_sound = guitar_sound.astype(np.int16)
write_sound(guitar_sound, "guitar.wav")
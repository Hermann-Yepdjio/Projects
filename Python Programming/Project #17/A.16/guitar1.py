import scitools.sound
import numpy as np
from scitools.std import *

def note(frequency, length, amplitude=1, sample_rate=44100):
	time_points = np.linspace(0, length, length*sample_rate)
	data = np.sin(2*np.pi*frequency*time_points)
	data = amplitude*data
	#data = data.astype(np.int16)
	max_amplitude = 2**15 - 1
	data = max_amplitude*data
	tmp = []
	for num in data:
		tmp.append([[num]])
	data = concatenate(tmp)
	print data
	scitools.sound.write(data, 'Atone.wav')
	#scitools.sound.play('Atone.wav')
	return data

note(220, 6, 1, 44100)

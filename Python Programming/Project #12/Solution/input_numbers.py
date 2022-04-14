# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""

import numpy as np
import matplotlib.pyplot as plt

x = []
y = []

with open ('input.txt') as file:
    for line in file:
        tokens = line.split()
        x.append(int(tokens[0]))
        y.append(int(tokens[1]))
    
p = np.poly1d(np.polyfit(x, y, 3))

t = np.linspace(0, 60, 200)
plt.plot(x, y, 'o', t, p(t), '-')
plt.show()

import numpy as np
from math import *


def break_list(lst, num_chunks):
    chunk_size = ceil(len(lst)/num_chunks)
    for i in range(0, len(lst), chunk_size):
        yield lst[i : i + chunk_size]


def top_n(lst, n):
    if(n > len(lst)):
        print("error! n is bigger the size of the list.")
        exit()
    lst = np.array(lst)
    lst[::-1].sort()
    for i in range(0, n):
        yield lst[i]

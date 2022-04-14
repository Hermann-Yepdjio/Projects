import numpy as np
import time

#normalize a single element
def norm_elt(elt, min_value, max_value):
    if min_value == max_value:
        return elt
    return (elt - min_value)/(max_value - min_value)

#normalize numpy matrix column wise
def norm_matrix(np_matrix):
    start_time = time.time()
    print ("\n\n------------------------------------------------start normaliziing  data--------------------------------------------\n\n")
    tmp_matrix = np.transpose(np_matrix).astype(np_matrix.dtype)
    for i in range(len(tmp_matrix)):
        min_value = min(tmp_matrix[i])
        max_value = max(tmp_matrix[i])
        tmp_matrix[i] = [norm_elt(elt, min_value, max_value) for elt in  tmp_matrix[i]]

    runtime = time.time() - start_time
    print("\n--------------------------------End Normalizing data. Running time: ", runtime, " seconds---------------------------------\n")
    return tmp_matrix.transpose()


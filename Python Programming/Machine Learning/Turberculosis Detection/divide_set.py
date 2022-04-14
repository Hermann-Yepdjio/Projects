import numpy as np

#divide a set in subsets of sizes <= batch_size
def divide_set(original_set_1, original_set_2, batch_size):
    subsets_1 = []
    subsets_2 = []
    tmp_1 = []
    tmp_2 = []
    count = 0
    for i in range(len(original_set_1)):
        tmp_1.append(original_set_1[i])
        tmp_2.append(original_set_2[i])
        count += 1
        if count == batch_size or i == len(original_set_1) - 1:
            count = 0
            subsets_1.append(tmp_1)
            subsets_2.append(tmp_2)
            tmp_1 = []
            tmp_2 = []
    return np.array(subsets_1), np.array(subsets_2)



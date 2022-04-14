import numpy as np


"""
    @mat: the matrix to be sliced into submatrices
    @nr: the number of blocks we want row wise
    @nc: the number of columns we want columnn wise
"""
def slice_matrix(mat, nr, nc):
    shape = mat.shape
    r = shape[0] // nr
    c = shape[1] // nc
    sub_mats = []
    for i in range(0, shape[0], r):
        for j in range(0, shape[1], c):
            if i + 2 * r <= shape[0] and j + 2 * c <= shape[1]:
                sub_mats.append(mat[i : i + r, j : j + c].copy())
            elif i + 2 * r > shape[0] and j + 2 * c <= shape[1]:
                sub_mats.append(mat[i : shape[0], j : j + c].copy())
            elif i + 2 * r <= shape[0] and j + 2 * c > shape[1]:
                sub_mats.append(mat[i : i + r, j : shape[1]].copy())
                break
            else:
                sub_mats.append(mat[i : shape[0], j : shape[1]].copy())
                return sub_mats





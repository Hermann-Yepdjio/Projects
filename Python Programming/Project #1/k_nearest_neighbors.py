from scipy import spatial as sp
from scipy.optimize import curve_fit
import numpy as np
import matplotlib.pyplot as plt
import time

low_b= -1000000; high_b = 1000000

def create_KDTree():
    t_spent = []
    num_pts = np.linspace(10, 100000, 300)
    for num in num_pts:
        x, y = np.random.randint(low_b, high_b,int(num)), np.random.randint(low_b, high_b, int(num))
        t = time.time()
        tree = sp.KDTree(zip(x, y))
        t_spent.append(time.time() - t)

    def func(x, y):
        return x*np.log(x)/y

    popt, pcov = curve_fit(func, num_pts, t_spent)

    plt.plot(num_pts, t_spent, '.')
    plt.plot(num_pts, func(num_pts, *popt), '-')
    plt.xlabel('number of points')
    plt.ylabel('time spent to search')
    plt.show()

def find_nearest_neighbor():
    t_spent = []
    num_pts = np.linspace(10, 10000, 200)
    for num in num_pts:
        x, y = np.random.randint(low_b, high_b,int(num)), np.random.randint(low_b, high_b, int(num))
        tree = sp.KDTree(zip(x, y))
        t = time.time()
        pts = np.random.randint(low_b, high_b, (1, 2))
        tree.query(pts)
        t_spent.append(time.time() - t)

    def func(x, y):
        return np.log(x)/y

    popt, pcov = curve_fit(func, num_pts, t_spent)

    plt.plot(num_pts, t_spent, '.')
    plt.plot(num_pts, func(num_pts, *popt), '-')
    plt.xlabel('number of points')
    plt.ylabel('time spent to search')
    plt.show()

def find_k_nearest_neighbors(n):
    t_spent = []
    num_pts = np.linspace(10, 10000, 200)
    for num in num_pts:
        x, y = np.random.randint(low_b, high_b,int(num)), np.random.randint(low_b, high_b, int(num))
        tree = sp.KDTree(zip(x, y))
        t = time.time()
        pts = np.random.randint(low_b, high_b, (1, 2))
        tree.query(pts, n)
        t_spent.append(time.time() - t)

    def func(x, y):
           return np.log(x)/y

    popt, pcov = curve_fit(func, num_pts, t_spent)

    plt.plot(num_pts, t_spent, '.')
    plt.plot(num_pts, func(num_pts, *popt), '-')
    plt.xlabel('number of points')
    plt.ylabel('time spent to search')
    plt.show()

def nearest_neighbors_visualization():
    x_neighbors_1, x_neighbors_2, x_neighbors_3, y_neighbors_1, y_neighbors_2, y_neighbors_3, x_pts, y_pts = [], [], [], [], [], [], [], []

    x, y = np.random.randint(low_b, high_b, 100), np.random.randint(low_b, high_b, 100)
    tree = sp.KDTree(zip(x, y))
    pts = np.random.randint(low_b, high_b, (10, 2))
    for pt in pts:
        x0, y0 = tree.query(pt, 3)
        x_neighbors_1.append(x[y0[0]])
        x_neighbors_2.append(x[y0[1]])
        x_neighbors_3.append(x[y0[2]])
        y_neighbors_1.append(y[y0[0]])
        y_neighbors_2.append(y[y0[1]])
        y_neighbors_3.append(y[y0[2]])
        x_pts.append(pt[0])
        y_pts.append(pt[1])

    plt.plot(x, y, '.', markersize = 8 )
    plt.plot(x_pts, y_pts, 'r.', markersize = 12 )
    plt.xlabel('x values')
    plt.ylabel('y values')

    for i in range (0, len(pts)):
        plt.plot([pts[i][0], x_neighbors_1[i]], [pts[i][1], y_neighbors_1[i]], 'r-')
        plt.plot([pts[i][0], x_neighbors_2[i]], [pts[i][1], y_neighbors_2[i]], 'g-')
        plt.plot([pts[i][0], x_neighbors_3[i]], [pts[i][1], y_neighbors_3[i]], 'k-')

    plt.show()

create_KDTree()
find_nearest_neighbor()
find_k_nearest_neighbors(4)
nearest_neighbors_visualization()

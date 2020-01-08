# To add a new cell, type '# %%'
# To add a new markdown cell, type '# %% [markdown]'
from IPython import get_ipython
import csv
import numpy as np
xy = []
with open('../data/building_vector.csv','r') as csvfile:
    rows=csv.reader(csvfile)
    for row in rows:
        xy.append(row)

pts = np.array(xy)


print(pts.shape)
x = pts[1:10000, 0]
y = pts[1:10000, 1]



import matplotlib.pyplot as plt
plt.figure()
plt.ion()
plt.scatter(x, y, s=0.2, c='r')

plt.show()






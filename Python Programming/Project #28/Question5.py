import networkx as nx
import numpy as np
import scipy.sparse as sp
import matplotlib.pyplot as plt
import json

g = nx.Graph()
g.add_node(1)
g.add_nodes_from([3, 4, 5])

g.add_edge(1, 2)
g.add_edges_from([(3, 4), (5, 6)])

g.add_weighted_edges_from([(1, 3, 1.5), (3, 5, 2.5)])

g.add_weighted_edges_from([(6, 7, 1.5)])

with open("tokyo-metro.json") as f: 
    data = json.load(f)

g = nx.Graph()
for line in data.values(): 
    g.add_weighted_edges_from(line["travel_times"])
    g.add_edges_from(line["transfers"])

for n1, n2 in g.edges():
    g[n1][n2]["transfer"] = "weight" not in g[n1][n2]
    
on_foot = [e for e in g.edges() if g.get_edge_data(*e)["transfer"]]
on_train = [e for e in g.edges () if not g.get_edge_data(*e)["transfer"]]
colors = [data[n[0].upper()]["color"] for n in g.nodes()]


#fig, ax = plt.subplots(1, 1, figsize=(14, 10))
#pos = nx.drawing.nx_agraph.graphviz_layout(g, prog="neato")
#nx.draw(g, pos, ax=ax, node_size=200, node_color=colors)
#nx.draw_networkx_labels(g, pos=pos, ax=ax, font_size=6)
#nx.draw_networkx_edges(g, pos=pos, ax=ax, edgelist=on_train, width=2)
#nx.draw_networkx_edges(g, pos=pos, ax=ax, edgelist=on_foot, edge_color="blue")

g.degree()

d_max = max(d for (n, d) in g.degree())

p = nx.shortest_path(g, "Y24", "C19")

np.sum([g[p[n]][p[n+1]]["weight"] for n in range(len(p)-1) if "weight" in g[p[n]][p[n+1]]])

h = g.copy()
for n1, n2 in h.edges():
	if h[n1][n2]["transfer"]:
		h[n1][n2]["weight"] = 5

p = nx.shortest_path(h, "Y24", "C19")
np.sum([h[p[n]][p[n+1]]["weight"] for n in range(len(p)-1)])

p = nx.shortest_path(h, "Z1", "H16")
np.sum([h[p[n]][p[n+1]]["weight"] for n in range(len(p)-1)])

A = nx.to_scipy_sparse_matrix(g)
perm = sp.csgraph.reverse_cuthill_mckee(A)
fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(8, 4))
ax1.spy(A, markersize=2)

M, N = A.shape
Pr = sp.coo_matrix((np.ones(M), (perm, np.arange(N)))).tocsr()
Pc = sp.coo_matrix((np.ones(M), (np.arange(M), perm))).tocsr()
ax2.spy(Pr.T*A*Pc.T, markersize=2)

plt.show()

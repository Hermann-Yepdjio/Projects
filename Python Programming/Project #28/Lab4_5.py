# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""
import pandas as ps
import networkx as nx
import matplotlib.pyplot as plt

g = nx.Graph()
g.add_node(1)
g.nodes()
g.add_nodes_from([3, 4, 5])
g.nodes()

g.add_edge(1, 2)
g.edges()
g.add_edges_from([(3, 4), (5, 6)])
g.edges()

g.add_weighted_edges_from([(1, 3, 1.5), (3, 5, 2.5)])
g.edges(data=True)

g.add_weighted_edges_from([(6, 7, 1.5)])
g.nodes()
g.edges()

import json
with open("tokyo-metro.json") as f: 
    data = json.load(f)

data.keys()
data["C"]

g = nx.Graph()
for line in data.values(): 
    g.add_weighted_edges_from(line["travel_times"])
    g.add_edges_from(line["transfers"])

for n1, n2 in g.edges():
    g[n1][n2]["transfer"] = "weight" not in g[n1][n2]
    
on_foot = [e for e in g.edges() if g.get_edge_data(*e)["transfer"]]
on_train = [e for e in g.edges () if not g.get_edge_data(*e)["transfer"]]
colors = [data[n[0].upper()]["color"] for n in g.nodes()]

fig, ax = plt.subplots(1, 1, figsize=(14, 10))
pos = nx.drawing.nx_agraph.graphviz_layout(g, prog="neato")
nx.draw(g, pos, ax=ax, node_size=200, node_color=colors)
nx.draw_networkx_labels(g, pos=pos, ax=ax, font_size=6)
nx.draw_networkx_edges(g, pos=pos, ax=ax, edgelist=on_train, width=2)
nx.draw_networkx_edges(g, pos=pos, ax=ax, edgelist=on_foot, edge_color="blue")



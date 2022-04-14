# -*- coding: utf-8 -*-
"""
Created on Tue Mar  5 12:56:04 2019

@author: huanglinc
"""
import pickle

#----------load the IA--------------
train_w2v = pickle.load(open('train_w2v.sav', 'rb'))

print(train_w2v.wv.most_similar("man"))
print(train_w2v.wv.most_similar("usa"))

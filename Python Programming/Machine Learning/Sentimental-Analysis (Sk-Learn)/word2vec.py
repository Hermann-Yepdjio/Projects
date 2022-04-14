# -*- coding: utf-8 -*-
"""
Created on Tue Mar  5 12:23:03 2019

@author: huanglinc
"""

from gensim.models import Word2Vec
import time
import re

#---------- functions to calculate time passed----------
_start_time = time.time()

def tic():
    global _start_time 
    _start_time = time.time()

def tac():
    t_sec = round(time.time() - _start_time)
    (t_min, t_sec) = divmod(t_sec,60)
    (t_hour,t_min) = divmod(t_min,60) 
    print('Time passed: {}hour:{}min:{}sec'.format(t_hour,t_min,t_sec))
    
#------------ import the data------------
reviews_train = []
file = open('data/review25000.txt', 'r', encoding="utf8")
for line in file:
    
    reviews_train.append(line.strip())
    
    
print(reviews_train[5])

#-----------clean the data-------------
REPLACE_NO_SPACE = re.compile("(\.)|(\;)|(\:)|(\!)|(\')|(\?)|(\,)|(\")|(\()|(\))|(\[)|(\])|(\d+)")
REPLACE_WITH_SPACE = re.compile("(<br\s*/><br\s*/>)|(\-)|(\/)")
NO_SPACE = ""
SPACE = " "

def preprocess_reviews(reviews):
    
    reviews = [REPLACE_NO_SPACE.sub(NO_SPACE, line.lower()) for line in reviews]
    reviews = [REPLACE_WITH_SPACE.sub(SPACE, line) for line in reviews]
    
    return reviews

reviews_train_clean = preprocess_reviews(reviews_train)


print(reviews_train_clean[5])


#--------- word2vec------------------
#pip install --upgrade gensim


# store as list of lists of words
sentences = []
for sent_str in reviews_train_clean:
    tokens = sent_str.split()
    sentences.append(tokens)

tic()
train_w2v = Word2Vec(sentences=sentences, size=500, window=5, min_count=5, workers=4, sg=0)
tac()

#By default (sg=0), CBOW is used. Otherwise (sg=1), skip-gram is employed
train_w2v.wv.most_similar("man")
train_w2v.wv.most_similar("usa")

##---------save the model to disk---------
import pickle

pickle.dump(train_w2v, open('train_w2v.sav', 'wb'))



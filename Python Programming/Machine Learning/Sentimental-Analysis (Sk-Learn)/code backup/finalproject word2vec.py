# -*- coding: utf-8 -*-
"""
Created on Fri Mar  1 11:33:39 2019

@author: chao_
"""

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics import accuracy_score
import numpy as np
import pandas as pd
import os
import re

#------------ import the data------------
reviews_train = []
file = open('tar_data/movie_data/full_train.txt', 'r', encoding="utf8")
for line in file:
    
    reviews_train.append(line.strip())
    
reviews_test = []
file = open('tar_data/movie_data/full_test.txt', 'r', encoding="utf8")
for line in file:
    
    reviews_test.append(line.strip())
    
print(reviews_train[5])

#-----------clean the data-------------
import re

REPLACE_NO_SPACE = re.compile("(\.)|(\;)|(\:)|(\!)|(\')|(\?)|(\,)|(\")|(\()|(\))|(\[)|(\])|(\d+)")
REPLACE_WITH_SPACE = re.compile("(<br\s*/><br\s*/>)|(\-)|(\/)")
NO_SPACE = ""
SPACE = " "

def preprocess_reviews(reviews):
    
    reviews = [REPLACE_NO_SPACE.sub(NO_SPACE, line.lower()) for line in reviews]
    reviews = [REPLACE_WITH_SPACE.sub(SPACE, line) for line in reviews]
    
    return reviews

reviews_train_clean = preprocess_reviews(reviews_train)
reviews_test_clean = preprocess_reviews(reviews_test)

print(reviews_train_clean[5])

#----------vectorize the data--------
from sklearn.feature_extraction.text import CountVectorizer

cv = CountVectorizer(binary=True)
cv.fit(reviews_train_clean)
X = cv.transform(reviews_train_clean) #Compressed Sparse Row format
X_test = cv.transform(reviews_test_clean)

#--------- word2vec------------------
#pip install --upgrade gensim
from gensim.models import Word2Vec
from gensim.models import Doc2Vec

# store as list of lists of words
sentences = []
for sent_str in reviews_train_clean:
    tokens = sent_str.split()
    sentences.append(tokens)
    
train_w2v = Word2Vec(sentences=sentences, size=500, window=5, min_count=5, workers=4, sg=0)

#By default (sg=0), CBOW is used. Otherwise (sg=1), skip-gram is employed
train_w2v.wv.most_similar("man")


# average--- no good
def sent_vectorizer(sent, model):
    sent_vec =[]
    numw = 0
    for w in sent:
        try:
            if numw == 0:
                sent_vec = model[w]
            else:
                sent_vec = np.add(sent_vec, model[w])
            numw+=1
        except:
            pass
    
    return np.asarray(sent_vec) / numw
 
 
V=[]
for sentence in sentences:
    V.append(sent_vectorizer(sentence, train_w2v))  

from sklearn.linear_model import LogisticRegression

from sklearn.model_selection import train_test_split

target = [1 if i < 12500 else 0 for i in range(25000)]

X_train, X_val, y_train, y_val = train_test_split(
    V, target, train_size = 0.75
)


for c in [0.01, 0.05, 0.25, 0.5, 1]:
    
    lr = LogisticRegression(C=c)
    lr.fit(X_train, y_train)
    print ("Accuracy for C=%s: %s" 
           % (c, accuracy_score(y_val, lr.predict(X_val))))
    
#--------- doc2vec------------------
#pip install --upgrade gensim

from gensim.models.doc2vec import Doc2Vec, TaggedDocument

# store as list of lists of words
sentences = []
for sent_str in reviews_train_clean:
    tokens = sent_str.split()
    sentences.append(tokens)
 
documents = [TaggedDocument(doc, [i]) for i, doc in enumerate(sentences)]
model = Doc2Vec(documents, vector_size=100, window=10, min_count=3, workers=8)
#train_w2v = Word2Vec(sentences=sentences, size=500, window=5, min_count=5, workers=4, sg=0)

X=[]
for d in sentences:
     
    X.append(model.infer_vector(d))

    
from sklearn.linear_model import LogisticRegression

from sklearn.model_selection import train_test_split

target = [1 if i < 12500 else 0 for i in range(25000)]

X_train, X_val, y_train, y_val = train_test_split(
    X, target, train_size = 0.75
)


for c in [0.01, 0.05, 0.25, 0.5, 1]:
    
    lr = LogisticRegression(C=c)
    lr.fit(X_train, y_train)
    print ("Accuracy for C=%s: %s" 
           % (c, accuracy_score(y_val, lr.predict(X_val))))

#X_test                             
sentences_test = []
for sent_str in reviews_train_clean:
    tokens = sent_str.split()
    sentences_test.append(tokens)
    
X_test_d2v=[]
for d in sentences_test:
     
    X_test_d2v.append(model.infer_vector(d))
    
    
final_model = LogisticRegression(C=0.05)
final_model.fit(X, target)
print ("Final Accuracy: %s" 
       % accuracy_score(target, final_model.predict(X_test_d2v)))        
    
#----------------prepare target list and train-------
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split

target = [1 if i < 12500 else 0 for i in range(25000)]

X_train, X_val, y_train, y_val = train_test_split(
    X, target, train_size = 0.75
)

for c in [0.01, 0.05, 0.25, 0.5, 1]:
    
    lr = LogisticRegression(C=c)
    lr.fit(X_train, y_train)
    print ("Accuracy for C=%s: %s" 
           % (c, accuracy_score(y_val, lr.predict(X_val))))
    
#--------------test----------------------
final_model = LogisticRegression(C=0.05)
final_model.fit(X, target)
print ("Final Accuracy: %s" 
       % accuracy_score(target, final_model.predict(X_test)))



#-------------analysis------------

feature_to_coef = {
    word: coef for word, coef in zip(
        cv.get_feature_names(), final_model.coef_[0]
    )
}
for best_positive in sorted(
    feature_to_coef.items(), 
    key=lambda x: x[1], 
    reverse=True)[:5]:
    print (best_positive)

    
for best_negative in sorted(
    feature_to_coef.items(), 
    key=lambda x: x[1])[:5]:
    print (best_negative)


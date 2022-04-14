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

##########################################################
#----------remove stopwords-----------
from nltk.corpus import stopwords

english_stop_words = stopwords.words('english')
def remove_stop_words(corpus):
    removed_stop_words = []
    for review in corpus:
        removed_stop_words.append(
            ' '.join([word for word in review.split() 
                      if word not in english_stop_words])
        )
    return removed_stop_words

no_stop_words_train = remove_stop_words(reviews_train_clean)
no_stop_words_test = remove_stop_words(reviews_test_clean)

#----------vectorize the data no stop word--------
from sklearn.feature_extraction.text import CountVectorizer

cv = CountVectorizer(binary=True, max_features=1500)
cv.fit(no_stop_words_train)
X = cv.transform(no_stop_words_train) #Compressed Sparse Row format
X_test = cv.transform(no_stop_words_test)

#------ TF-IDF vectorizer -------

from sklearn.feature_extraction.text import TfidfVectorizer

tfidf_vectorizer = TfidfVectorizer()
tfidf_vectorizer.fit(no_stop_words_train)
X = tfidf_vectorizer.transform(no_stop_words_train)
#X_test = tfidf_vectorizer.transform(no_stop_words_test)

#############################################################

#--------- doc2vec------------------
#pip install --upgrade gensim

from gensim.models.doc2vec import Doc2Vec, TaggedDocument

# store as list of lists of words
sentences = []
for sent_str in reviews_train_clean:
    tokens = sent_str.split()
    sentences.append(tokens)
 
documents = [TaggedDocument(doc, [i]) for i, doc in enumerate(sentences)]
#model = Doc2Vec(documents, vector_size=300, window=10, min_count=3, workers=8)
#configuration from https://github.com/RaRe-Technologies/gensim/blob/develop/docs/notebooks/doc2vec-IMDB.ipynb
model = Doc2Vec(documents, dm=0, vector_size=100, negative=5, hs=0, min_count=2, sample=0, 
            epochs=20, workers=8)
#train_w2v = Word2Vec(sentences=sentences, size=500, window=5, min_count=5, workers=4, sg=0)

X=[]
for d in sentences:
     
    X.append(model.infer_vector(d))

    



#--------- doc2vec no stop words------------------
#pip install --upgrade gensim

from gensim.models.doc2vec import Doc2Vec, TaggedDocument

# store as list of lists of words
sentences = []
for sent_str in no_stop_words_train:
    tokens = sent_str.split()
    sentences.append(tokens)
 
documents = [TaggedDocument(doc, [i]) for i, doc in enumerate(sentences)]
model = Doc2Vec(documents, vector_size=300, window=10, min_count=3, workers=8)
#train_w2v = Word2Vec(sentences=sentences, size=500, window=5, min_count=5, workers=4, sg=0)

X=[]
for d in sentences:
     
    X.append(model.infer_vector(d))

#-------split the data------------
from sklearn.model_selection import train_test_split

target = [1 if i < 12500 else 0 for i in range(25000)]

X_train, X_val, y_train, y_val = train_test_split(
    X, target, train_size = 0.75
)

#----logistic regresion-----
# Accuracy for C=0.01: 0.86224 --- bag of word 1500
# Accuracy for C=1: 0.88448 --- TF-IDF
# Accuracy for C=1: 0.8088 doc2vec
# Accuracy for C=0.25: 0.84608 doc2vec new config 100
from sklearn.linear_model import LogisticRegression
for c in [0.01, 0.05, 0.25, 0.5, 1]:
    
    lr = LogisticRegression(C=c)
    lr.fit(X_train, y_train)
    print ("Accuracy for C=%s: %s" 
           % (c, accuracy_score(y_val, lr.predict(X_val))))
    
#---Random Forest---
#Accuracy for n_estimators=1000: 0.86592   very slow  --- bag of word
#Accuracy for n_estimators=1000: 0.83648  --- bag of word 1500
#Accuracy for n_estimators=1000: 0.86224   ---TF-IDF
# Accuracy for n_estimators=1000: 0.80112   doc2vec
# Accuracy for n_estimators=1000: 0.80752 doc2vec new config 100
# Accuracy for n_estimators=100: 0.79408 doc2vec new config 100    ----Accuracy for n_estimators=100: 0.7976 rando_state = none
# continue of last line:  Accuracy for n_estimators=500: 0.81184 Accuracy for n_estimators=1000: 0.80784
from sklearn.ensemble import RandomForestClassifier

classifier = RandomForestClassifier(n_estimators=500, random_state=555)  
classifier.fit(X_train, y_train)  
print ("Accuracy for n_estimators=%s: %s" 
           % (100, accuracy_score(y_val, classifier.predict(X_val))))

#---MLP---
#Accuracy for alpha=0.7: 0.86528 --- bag of word 1500
#Accuracy for alpha=0.7: 0.87808  --- TF-IDF
#Accuracy for alpha=0.7: 0.80736   doc2vec
#Accuracy for alpha=0.7: 0.85328 doc2vec new config 100

from sklearn.neural_network import MLPClassifier

classifier = MLPClassifier(alpha = 0.7, max_iter=100) 
classifier.fit(X_train, y_train)
print ("Accuracy for alpha=%s: %s" 
           % (0.7, accuracy_score(y_val, classifier.predict(X_val))))

#---SVM---
#Accuracy for C=0.01: 0.88528   --- bag of word
# Accuracy for C=0.01: 0.86064  --- bag of word 1500
# Accuracy for C=0.25: 0.89376   --- TF-IDF
# Accuracy for C=1: 0.81168  doc2vec
# Accuracy for C=0.5: 0.84608 doc2vec new config 100

from sklearn.svm import LinearSVC

for c in  [0.01, 0.05, 0.25, 0.5, 1]:
    
    svm = LinearSVC(C=c)
    svm.fit(X_train, y_train)
    print ("Accuracy for C=%s: %s" 
           % (c, accuracy_score(y_val, svm.predict(X_val))))

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


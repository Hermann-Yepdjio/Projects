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


#----------------prepare target list and train-------
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split
from sklearn.neural_network import MLPClassifier

target = [1 if i < 12500 else 0 for i in range(25000)]

X_train, X_val, y_train, y_val = train_test_split(
    X, target, train_size = 0.75
)


    
classifier = MLPClassifier(alpha = 0.7, max_iter=400) 
classifier.fit(X_train, y_train)

print ("Accuracy for alpha=%s: %s" 
           % (0.7, accuracy_score(y_val, classifier.predict(X_val))))
    
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


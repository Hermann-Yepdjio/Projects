# -*- coding: utf-8 -*-
"""
Created on Sun Mar  3 00:03:11 2019

@author: chao_
"""

import re
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.model_selection import train_test_split
from sklearn.svm import LinearSVC
from sklearn.metrics import accuracy_score, confusion_matrix
from sklearn.linear_model import LogisticRegression
from sklearn.neural_network import MLPClassifier
from sklearn.ensemble import RandomForestClassifier
import time
from sklearn.feature_extraction.text import TfidfVectorizer


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
reviews = []
file = open('data/review25000.txt', 'r', encoding="utf8")
for line in file:
    
    reviews.append(line.strip())
    
print(reviews[0])

#-----------clean the data-------------


REPLACE_NO_SPACE = re.compile("(\.)|(\;)|(\:)|(\!)|(\')|(\?)|(\,)|(\")|(\()|(\))|(\[)|(\])|(\d+)")
REPLACE_WITH_SPACE = re.compile("(<br\s*/><br\s*/>)|(\-)|(\/)")
NO_SPACE = ""
SPACE = " "

def preprocess_reviews(reviews):
    
    reviews = [REPLACE_NO_SPACE.sub(NO_SPACE, line.lower()) for line in reviews]
    reviews = [REPLACE_WITH_SPACE.sub(SPACE, line) for line in reviews]
    
    return reviews

reviews_clean = preprocess_reviews(reviews)

print(reviews_clean[0])

#----------remove few stop words and vectorize bag of word binary with ngram----
tic()
stop_words = ['in', 'of', 'at', 'a', 'the']
# 3 ngram increase the memory a lot, MLP use a lot of memory
#ngram_vectorizer = CountVectorizer(binary=True, ngram_range=(1,2),stop_words=stop_words, max_features=150000)
#Reviews Matrix Shape (25000, 1708493) ----with 2 ngram = too big 
    
tfidf_vectorizer = TfidfVectorizer(ngram_range=(1,2),stop_words=stop_words, max_features=150000)
tfidf_vectorizer.fit(reviews_clean)
X = tfidf_vectorizer.transform(reviews_clean)

tac()
print('Reviews Matrix Shape %s' % str(X.shape))
print ("----------")
#---------split the reviews 75% for training, 25% for testing----
n_reviews = len(reviews)
target = [1 if i < n_reviews/2 else 0 for i in range(n_reviews)] # first 50% = good review

X_train, X_test, y_train, y_test = train_test_split(
    X, target, test_size = 0.25
)


#---------train and test with logistic regresion -----
tic()
lr = LogisticRegression()
lr.fit(X_train, y_train)
y_predicted = lr.predict(X_test)
accuracy = accuracy_score(y_test, y_predicted)
cm = confusion_matrix(y_test, y_predicted)

print ("Accuracy for Logistic Regression: %s" %(accuracy))
print(cm)
tac()
print ("----------")

#---------train and test with SVM linear SVC-----
tic()
svm = LinearSVC()
svm.fit(X_train, y_train)
y_predicted = svm.predict(X_test)
accuracy = accuracy_score(y_test, y_predicted)
cm = confusion_matrix(y_test, y_predicted)

print ("Accuracy for SVM: %s" %(accuracy))
print(cm)
tac()
print ("----------")

#---------train and test with MLP-----
tic()
mlp = MLPClassifier(hidden_layer_sizes=(30, )) #default 100 hidden neurons
mlp.fit(X_train, y_train)
y_predicted = mlp.predict(X_test)
accuracy = accuracy_score(y_test, y_predicted)
cm = confusion_matrix(y_test, y_predicted)

print ("Accuracy for MLP: %s" %(accuracy))
print(cm)
tac()
print ("----------")

#---------train and test with random forest-----
tic()
randomforest = RandomForestClassifier(n_estimators=300, random_state=555)  
randomforest.fit(X_train, y_train)
y_predicted = randomforest.predict(X_test)
accuracy = accuracy_score(y_test, y_predicted)
cm = confusion_matrix(y_test, y_predicted)

print ("Accuracy for Random Forest: %s" %(accuracy))
print(cm)
tac()
print ("----------")



#import pickle
## save the model to disk
#filename = 'mlp_tfidp.sav'
#pickle.dump(mlp, open(filename, 'wb'))
# 
## some time later...
# 
## load the model from disk
#loaded_model = pickle.load(open(filename, 'rb'))
#y_predicted = loaded_model.predict(X_train)
#accuracy = accuracy_score(y_train, y_predicted)
#cm = confusion_matrix(y_train, y_predicted)
#print ("Accuracy for MLP train: %s" %(accuracy))
#print(cm)
#
#
#y_predicted = loaded_model.predict(X_test)
#accuracy = accuracy_score(y_test, y_predicted)
#cm = confusion_matrix(y_test, y_predicted)
#print ("Accuracy for MLP test: %s" %(accuracy))
#print(cm)



#Reviews Matrix Shape (25000, 150000)
#----------
#C:\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\linear_model\logistic.py:432: FutureWarning: Default solver will be changed to 'lbfgs' in 0.22. Specify a solver to silence this warning.
#  FutureWarning)
#Accuracy for Logistic Regression: 0.89248
#[[2746  351]
# [ 321 2832]]
#Time passed: 0hour:0min:1sec
#----------
#Accuracy for SVM: 0.90528
#[[2789  308]
# [ 284 2869]]
#Time passed: 0hour:0min:1sec
#----------
#Accuracy for MLP: 0.91296
#[[2814  283]
# [ 261 2892]]
#Time passed: 0hour:25min:48sec
#----------
#Accuracy for Random Forest: 0.85904
#[[2685  412]
# [ 469 2684]]
#Time passed: 0hour:3min:22sec
#----------



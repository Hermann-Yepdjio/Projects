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
from gensim.models.doc2vec import Doc2Vec, TaggedDocument
from gensim.models import Word2Vec

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
#tic()
#stop_words = ['in', 'of', 'at', 'a', 'the']
## 3 ngram increase the memory a lot, MLP use a lot of memory
#ngram_vectorizer = CountVectorizer(binary=True, ngram_range=(1,2),
#                                   stop_words=stop_words, max_features=150000)
##Reviews Matrix Shape (25000, 1708493) ----with 2 ngram = too big 
#ngram_vectorizer.fit(reviews_clean)
#X = ngram_vectorizer.transform(reviews_clean)
#
#tac()
#print('Reviews Matrix Shape %s' % str(X.shape))
#print ("----------")






#--------- doc2vec------------------
#pip install --upgrade gensim


tic()

sentences = []
for sent_str in reviews_clean:
    
    tokens = sent_str.split()
    sentences.append(tokens)
 
documents = [TaggedDocument(doc, [i]) for i, doc in enumerate(sentences)]

#configuration from https://github.com/RaRe-Technologies/gensim/blob/develop/docs/notebooks/doc2vec-IMDB.ipynb
model = Doc2Vec(documents, dm=0, vector_size=1000, negative=5, hs=0, min_count=2, sample=0, 
            epochs=20, workers=8)

X=[]
for d in sentences:
    
    X.append(model.infer_vector(d))

tac()
print('Reviews Matrix Shape %s' % len(X))
print ("----------")

#--------- word2vec interesting-----------------
#tic()
#train_w2v = Word2Vec(sentences=sentences, size=500, window=5, min_count=5, workers=4, sg=0)
##By default (sg=0), CBOW is used. Otherwise (sg=1), skip-gram is employed
#
#print(train_w2v.wv.most_similar("man"))
#print('----------')
#print(train_w2v.wv.most_similar('usa'))
#print('----------')
#print(train_w2v.wv.most_similar('excellent'))
#
#tac()
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


#---------------------------doc2vec vector_size=100----------------
#C:\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\linear_model\logistic.py:432: FutureWarning: Default solver will be changed to 'lbfgs' in 0.22. Specify a solver to silence this warning.
#  FutureWarning)
#Accuracy for Logistic Regression: 0.84512
#[[2675  486]
# [ 482 2607]]
#Time passed: 0hour:0min:1sec
#----------
#C:\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:922: ConvergenceWarning: Liblinear failed to converge, increase the number of iterations.
#  "the number of iterations.", ConvergenceWarning)
#Accuracy for SVM: 0.84592
#[[2683  478]
# [ 485 2604]]
#Time passed: 0hour:0min:8sec
#----------
#C:\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\neural_network\multilayer_perceptron.py:562: ConvergenceWarning: Stochastic Optimizer: Maximum iterations (200) reached and the optimization hasn't converged yet.
#  % self.max_iter, ConvergenceWarning)
#Accuracy for MLP: 0.82992
#[[2604  557]
# [ 506 2583]]
#Time passed: 0hour:0min:17sec
#----------
#Accuracy for Random Forest: 0.816
#[[2552  609]
# [ 541 2548]]
#Time passed: 0hour:0min:55sec
#----------

#-----------------------------doc2vec vector_size=1000-------------
#Reviews Matrix Shape 25000
#----------
#C:\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\linear_model\logistic.py:432: FutureWarning: Default solver will be changed to 'lbfgs' in 0.22. Specify a solver to silence this warning.
#  FutureWarning)
#Accuracy for Logistic Regression: 0.85296
#[[2681  449]
# [ 470 2650]]
#Time passed: 0hour:0min:10sec
#----------
#C:\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:922: ConvergenceWarning: Liblinear failed to converge, increase the number of iterations.
#  "the number of iterations.", ConvergenceWarning)
#Accuracy for SVM: 0.85728
#[[2690  440]
# [ 452 2668]]
#Time passed: 0hour:0min:45sec
#----------
#Accuracy for MLP: 0.83472
#[[2672  458]
# [ 575 2545]]
#Time passed: 0hour:3min:57sec
#----------
#Accuracy for Random Forest: 0.81024
#[[2520  610]
# [ 576 2544]]
#Time passed: 0hour:11min:3sec
#----------
# -*- coding: utf-8 -*-
"""
Created on Sun Mar  3 00:03:11 2019

@author: chao_
"""

import re
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.model_selection import train_test_split
from sklearn.svm import LinearSVC, SVC
from sklearn.metrics import accuracy_score, confusion_matrix
from sklearn.linear_model import LogisticRegression
from sklearn.neural_network import MLPClassifier
from sklearn.ensemble import RandomForestClassifier
import time
from gensim.models.doc2vec import Doc2Vec, TaggedDocument
from gensim.models import Word2Vec
import matplotlib.pyplot as plt
import numpy as np
#---------- functions to calculate time passed----------
_start_time = time.time()

def tic():
    global _start_time 
    _start_time = time.time()

def tac():
    t_sec_total = time.time() - _start_time
    (t_min, t_sec) = divmod(t_sec_total,60)
    (t_hour,t_min) = divmod(t_min,60) 
    print('Time passed: {}hour:{}min:{}sec'.format(t_hour,t_min,t_sec))
    return(t_sec_total)
    
    
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

print('####### Logistic Regression #######') 
solvers = ['liblinear', 'sag', 'saga', 'newton-cg', 'lbfgs']
accuracy_lr = [] 
runtime_lr = []     

for i in solvers:
   
    tic()
    lr = LogisticRegression(solver=i)
    lr.fit(X_train, y_train)
    y_predicted = lr.predict(X_test)
    accuracy = accuracy_score(y_test, y_predicted)
    accuracy_lr.append(accuracy)
    cm = confusion_matrix(y_test, y_predicted)
    
    print ("Accuracy for Logistic Regression %s: %s" %(i, accuracy))
    print(cm)
    runtime = tac()
    runtime_lr.append(runtime)
    print ("----------")

plt.figure()
plt.plot(solvers, accuracy_lr, linestyle='--', marker='o', color='b')
plt.grid()
plt.xlabel("Logistic regression solvers")
plt.ylabel("Accuracy")
plt.title("DOC2VEC LR Accuracy")
plt.savefig('plot_doc2vec/logistic_regression_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(solvers, runtime_lr, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("Logistic regression solvers")
plt.ylabel("run time (seg)")
plt.title("DOC2VEC LR Run time")
plt.savefig('plot_doc2vec/logistic_regression_runtime.png', bbox_inches='tight')

#---------train and test with SVM - SVC-----

print('####### SVM #######') 
#kernels = [ 'linearSVC', 'poly', 'rbf', 'sigmoid']
#SVC is very bad with large dataset, but linearSVC is pretty good, so we only use linearSVC
kernels = ['linearSVC']
accuracy_svm = [] 
runtime_svm = []    

for i in kernels:
    
    tic()
    if (i == 'linearSVC'):
        svm = LinearSVC()
    else:
        #svm = SVC(kernel = i)
        svm = SVC(gamma=0.01, C=100., probability=True, class_weight='balanced', kernel=i)
    svm.fit(X_train, y_train)
    y_predicted = svm.predict(X_test)
    accuracy = accuracy_score(y_test, y_predicted)
    accuracy_svm.append(accuracy)
    cm = confusion_matrix(y_test, y_predicted)
    
    print ("Accuracy for SVM %s: %s" %(i, accuracy))
    print(cm)
    runtime = tac()
    runtime_svm.append(runtime)
    print ("----------")

plt.figure()
plt.plot(kernels, accuracy_svm, linestyle='--', marker='o', color='b')
plt.grid()
plt.xlabel("SVM kernels")
plt.ylabel("Accuracy")
plt.title("DOC2VEC SVM Accuracy")
plt.savefig('plot_doc2vec/svm_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(kernels, runtime_svm, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("SVM kernels")
plt.ylabel("run time (seg)")
plt.title("DOC2VEC SVM Run time")
plt.savefig('plot_doc2vec/svm_runtime.png', bbox_inches='tight')

#---------train and test with MLP-----
print('####### MLP #######') 
hidden_neurons = [10, 30, 50, 100]
accuracy_mlp = [] 
runtime_mlp = []

for i in hidden_neurons:
    
    tic()
    mlp = MLPClassifier(hidden_layer_sizes=(i, )) #default 100 hidden neurons
    mlp.fit(X_train, y_train)
    y_predicted = mlp.predict(X_test)
    accuracy = accuracy_score(y_test, y_predicted)
    accuracy_mlp.append(accuracy)
    cm = confusion_matrix(y_test, y_predicted)
    
    print ("Accuracy for MLP hidden neurons %s: %s" %(i, accuracy))
    print(cm)
    runtime = tac()
    runtime_mlp.append(runtime)
    print ("----------")

plt.figure()
plt.plot(hidden_neurons, accuracy_mlp, linestyle='--', marker='o', color='b')
plt.grid()
plt.xlabel("MLP hidden neurons")
plt.ylabel("Accuracy")
plt.title("DOC2VEC MLP Accuracy")
plt.savefig('plot_doc2vec/mlp_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(hidden_neurons, runtime_mlp, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("MLP hidden neurons")
plt.ylabel("run time (seg)")
plt.title("DOC2VEC MLP Run time")
plt.savefig('plot_doc2vec/mlp_runtime.png', bbox_inches='tight')

#---------train and test with random forest-----
print('####### Random Forest #######') 
n_estimators = [10, 30, 70, 100, 200, 300]
accuracy_rf = [] 
runtime_rf = []

for i in n_estimators:
    tic()
    randomforest = RandomForestClassifier(n_estimators=i, random_state=555)  
    randomforest.fit(X_train, y_train)
    y_predicted = randomforest.predict(X_test)
    accuracy = accuracy_score(y_test, y_predicted)
    accuracy_rf.append(accuracy)
    cm = confusion_matrix(y_test, y_predicted)
    
    print ("Accuracy for Random Forest, n estimators %s: %s" %(i, accuracy))
    print(cm)
    runtime = tac()
    runtime_rf.append(runtime)
    print ("----------")

plt.figure()
plt.plot(n_estimators, accuracy_rf, linestyle='--', marker='o', color='b')
plt.grid()
plt.xlabel("Random Forest Number of estimators")
plt.ylabel("Accuracy")
plt.title("DOC2VEC RF Accuracy")
plt.savefig('plot_doc2vec/rf_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(n_estimators, runtime_rf, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("Random Forest Number of estimators")
plt.ylabel("run time (seg)")
plt.title("DOC2VEC RF Run time")
plt.savefig('plot_doc2vec/rf_runtime.png', bbox_inches='tight')



################# find the best configuration of each algorithm##################### 
algorithms = ['LR', 'SVM', 'MLP', 'RF']
best_accuracy = []
best_run_time = []
alg_name = []
for i in algorithms:
    if i == 'LR':
        max_value = max(accuracy_lr)
        max_pos = accuracy_lr.index(max_value)
        best_accuracy.append(max_value)
        best_run_time.append(runtime_lr[max_pos])
        alg_name.append(i + ' ' + solvers[max_pos])
    if i == 'SVM':
        max_value = max(accuracy_svm)
        max_pos = accuracy_svm.index(max_value)
        best_accuracy.append(max_value)
        best_run_time.append(runtime_svm[max_pos])
        alg_name.append(i + ' ' + kernels[max_pos])
    if i == 'MLP':
        max_value = max(accuracy_mlp)
        max_pos = accuracy_mlp.index(max_value)
        best_accuracy.append(max_value)
        best_run_time.append(runtime_mlp[max_pos])
        alg_name.append(i + ' ' + str(hidden_neurons[max_pos]))
    if i == 'RF':
        max_value = max(accuracy_rf)
        max_pos = accuracy_rf.index(max_value)
        best_accuracy.append(max_value)
        best_run_time.append(runtime_rf[max_pos])
        alg_name.append(i + ' ' + str(n_estimators[max_pos]))

plt.figure()
plt.plot(alg_name, best_accuracy, linestyle='--', marker='o', color='b')
plt.grid()
plt.xlabel("Best algorithm configuration")
plt.ylabel("Accuracy")
plt.title("DOC2VEC Best Algorithm Configuration Accuracy")
plt.savefig('plot_doc2vec/best_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(alg_name, best_run_time, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("Best algorithm configuration")
plt.ylabel("run time (seg)")
plt.title("DOC2VEC Best Algorithm Configuration Run time")
plt.savefig('plot_doc2vec/best_alg_runtime.png', bbox_inches='tight')


#-----vector_size=1000-------------------
#Time passed: 0.0hour:9.0min:26.276705503463745sec
#Reviews Matrix Shape 25000
#----------
######## Logistic Regression #######
#Accuracy for Logistic Regression liblinear: 0.85968
#[[2690  455]
# [ 422 2683]]
#Time passed: 0.0hour:0.0min:7.546234130859375sec
#----------
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\linear_model\sag.py:334: ConvergenceWarning: The max_iter was reached which means the coef_ did not converge
#  "the coef_ did not converge", ConvergenceWarning)
#Accuracy for Logistic Regression sag: 0.85872
#[[2686  459]
# [ 424 2681]]
#Time passed: 0.0hour:0.0min:16.22825574874878sec
#----------
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\linear_model\sag.py:334: ConvergenceWarning: The max_iter was reached which means the coef_ did not converge
#  "the coef_ did not converge", ConvergenceWarning)
#Accuracy for Logistic Regression saga: 0.85872
#[[2686  459]
# [ 424 2681]]
#Time passed: 0.0hour:0.0min:25.2565176486969sec
#----------
#Accuracy for Logistic Regression newton-cg: 0.85872
#[[2686  459]
# [ 424 2681]]
#Time passed: 0.0hour:0.0min:5.716395378112793sec
#----------
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\linear_model\logistic.py:757: ConvergenceWarning: lbfgs failed to converge. Increase the number of iterations.
#  "of iterations.", ConvergenceWarning)
#Accuracy for Logistic Regression lbfgs: 0.8592
#[[2687  458]
# [ 422 2683]]
#Time passed: 0.0hour:0.0min:2.0744385719299316sec
#----------
######## SVM #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:922: ConvergenceWarning: Liblinear failed to converge, increase the number of iterations.
#  "the number of iterations.", ConvergenceWarning)
#Accuracy for SVM linearSVC: 0.85856
#[[2684  461]
# [ 423 2682]]
#Time passed: 0.0hour:0.0min:28.703908681869507sec
#----------
######## MLP #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\neural_network\multilayer_perceptron.py:562: ConvergenceWarning: Stochastic Optimizer: Maximum iterations (200) reached and the optimization hasn't converged yet.
#  % self.max_iter, ConvergenceWarning)
#Accuracy for MLP hidden neurons 10: 0.81776
#[[2573  572]
# [ 567 2538]]
#Time passed: 0.0hour:0.0min:57.02952313423157sec
#----------
#Accuracy for MLP hidden neurons 30: 0.83808
#[[2625  520]
# [ 492 2613]]
#Time passed: 0.0hour:0.0min:39.87165451049805sec
#----------
#Accuracy for MLP hidden neurons 50: 0.84976
#[[2662  483]
# [ 456 2649]]
#Time passed: 0.0hour:0.0min:36.823540449142456sec
#----------
#Accuracy for MLP hidden neurons 100: 0.85776
#[[2673  472]
# [ 417 2688]]
#Time passed: 0.0hour:0.0min:36.47429847717285sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 10: 0.6616
#[[2399  746]
# [1369 1736]]
#Time passed: 0.0hour:0.0min:4.635227203369141sec
#----------
#Accuracy for Random Forest, n estimators 30: 0.73072
#[[2433  712]
# [ 971 2134]]
#Time passed: 0.0hour:0.0min:13.59542965888977sec
#----------
#Accuracy for Random Forest, n estimators 70: 0.76848
#[[2466  679]
# [ 768 2337]]
#Time passed: 0.0hour:0.0min:31.472816705703735sec
#----------
#Accuracy for Random Forest, n estimators 100: 0.7816
#[[2478  667]
# [ 698 2407]]
#Time passed: 0.0hour:0.0min:44.89513874053955sec
#----------
#Accuracy for Random Forest, n estimators 200: 0.80064
#[[2501  644]
# [ 602 2503]]
#Time passed: 0.0hour:1.0min:30.825007915496826sec
#----------
#Accuracy for Random Forest, n estimators 300: 0.81296
#[[2545  600]
# [ 569 2536]]
#Time passed: 0.0hour:2.0min:16.589736938476562sec
#----------


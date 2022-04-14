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
import matplotlib.pyplot as plt
from sklearn.feature_extraction.text import TfidfVectorizer
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

print('####### Logistic Regresion #######') 
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
plt.title("TF-IDF Logistic Regression Accuracy")
plt.savefig('plot_tfidf/logistic_regression_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(solvers, runtime_lr, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("Logistic regression solvers")
plt.ylabel("run time (seg)")
plt.title("TF-IDF Logistic Regression Run time")
plt.savefig('plot_tfidf/logistic_regression_runtime.png', bbox_inches='tight')

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
        svm = SVC(kernel = i)
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
plt.title("TF-IDF SVM Accuracy")
plt.savefig('plot_tfidf/svm_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(kernels, runtime_svm, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("SVM kernels")
plt.ylabel("run time (seg)")
plt.title("TF-IDF SVM Run time")
plt.savefig('plot_tfidf/svm_runtime.png', bbox_inches='tight')

#---------train and test with MLP-----
print('####### MLP #######') 
hidden_neurons = [10, 30, 50, 100]
accuracy_mlp = [] 
runtime_mlp = []

for i in hidden_neurons:
    
    tic()
    mlp = MLPClassifier(hidden_layer_sizes=(i, ), early_stopping=True) #default 100 hidden neurons
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
plt.title("TF-IDF MLP Accuracy")
plt.savefig('plot_tfidf/mlp_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(hidden_neurons, runtime_mlp, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("MLP hidden neurons")
plt.ylabel("run time (seg)")
plt.title("TF-IDF MLP Run time")
plt.savefig('plot_tfidf/mlp_runtime.png', bbox_inches='tight')

#---------train and test with random forest-----
print('####### Random Forest #######') 
n_estimators = [10, 30, 70, 100, 200, 300, 1000]
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
plt.xlabel("Number of estimators")
plt.ylabel("Accuracy")
plt.title("TF-IDF Random Forest Accuracy")
plt.savefig('plot_tfidf/rf_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(n_estimators, runtime_rf, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("Number of estimators")
plt.ylabel("run time (seg)")
plt.title("TF-IDF Random Forest Run time")
plt.savefig('plot_tfidf/rf_runtime.png', bbox_inches='tight')



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
plt.title("TF-IDF Best Algorithm Configuration Accuracy")
plt.savefig('plot_tfidf/best_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(alg_name, best_run_time, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("Best algorithm configuration")
plt.ylabel("run time (seg)")
plt.title("DTF-IDF Best Algorithm Configuration Run time")
plt.savefig('plot_tfidf/best_alg_runtime.png', bbox_inches='tight')

#--------------max_features=150000---------------------------
#Time passed: 0.0hour:0.0min:25.176562786102295sec
#Reviews Matrix Shape (25000, 150000)
#----------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.89184
#[[2754  356]
# [ 320 2820]]
#Time passed: 0.0hour:0.0min:1.0797488689422607sec
#----------
#Accuracy for Logistic Regression sag: 0.89184
#[[2754  356]
# [ 320 2820]]
#Time passed: 0.0hour:0.0min:1.347956895828247sec
#----------
#Accuracy for Logistic Regression saga: 0.89184
#[[2754  356]
# [ 320 2820]]
#Time passed: 0.0hour:0.0min:1.7561957836151123sec
#----------
#Accuracy for Logistic Regression newton-cg: 0.89184
#[[2754  356]
# [ 320 2820]]
#Time passed: 0.0hour:0.0min:4.32199764251709sec
#----------
#Accuracy for Logistic Regression lbfgs: 0.89184
#[[2754  356]
# [ 320 2820]]
#Time passed: 0.0hour:0.0min:3.7405946254730225sec
#----------
######## SVM #######
#Accuracy for SVM linearSVC: 0.90688
#[[2818  292]
# [ 290 2850]]
#Time passed: 0.0hour:0.0min:0.7094793319702148sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 10: 0.91072
#[[2843  267]
# [ 291 2849]]
#Time passed: 0.0hour:2.0min:32.980093002319336sec
#----------
#Accuracy for MLP hidden neurons 30: 0.91344
#[[2825  285]
# [ 256 2884]]
#Time passed: 0.0hour:6.0min:40.0584762096405sec
#----------
#Accuracy for MLP hidden neurons 50: 0.91168
#[[2833  277]
# [ 275 2865]]
#Time passed: 0.0hour:10.0min:50.59632396697998sec
#----------
#Accuracy for MLP hidden neurons 100: 0.91008
#[[2852  258]
# [ 304 2836]]
#Time passed: 0.0hour:15.0min:34.89562630653381sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 10: 0.75696
#[[2608  502]
# [1017 2123]]
#Time passed: 0.0hour:0.0min:5.341684103012085sec
#----------
#Accuracy for Random Forest, n estimators 30: 0.81504
#[[2625  485]
# [ 671 2469]]
#Time passed: 0.0hour:0.0min:15.463613033294678sec
#----------
#Accuracy for Random Forest, n estimators 70: 0.84304
#[[2665  445]
# [ 536 2604]]
#Time passed: 0.0hour:0.0min:35.642505407333374sec
#----------
#Accuracy for Random Forest, n estimators 100: 0.84848
#[[2666  444]
# [ 503 2637]]
#Time passed: 0.0hour:0.0min:50.5357141494751sec
#----------
#Accuracy for Random Forest, n estimators 200: 0.85408
#[[2665  445]
# [ 467 2673]]
#Time passed: 0.0hour:1.0min:41.000643491744995sec
#----------
#Accuracy for Random Forest, n estimators 300: 0.85648
#[[2663  447]
# [ 450 2690]]
#Time passed: 0.0hour:2.0min:32.89604187011719sec
#----------
#Accuracy for Random Forest, n estimators 1000: 0.86048
#[[2672  438]
# [ 434 2706]]
#Time passed: 0.0hour:8.0min:34.136587381362915sec
#----------
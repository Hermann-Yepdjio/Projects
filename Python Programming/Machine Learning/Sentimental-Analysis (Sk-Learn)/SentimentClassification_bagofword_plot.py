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
ngram_vectorizer = CountVectorizer(binary=True, ngram_range=(1,2),
                                   stop_words=stop_words, max_features=150000)
#Reviews Matrix Shape (25000, 1708493) ----with 2 ngram = too big 
ngram_vectorizer.fit(reviews_clean)
X = ngram_vectorizer.transform(reviews_clean)

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
plt.title("BagOfWord LR Accuracy")
plt.savefig('plot_bagofword/logistic_regression_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(solvers, runtime_lr, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("Logistic regression solvers")
plt.ylabel("run time (seg)")
plt.title("BagOfWord LR Run time")
plt.savefig('plot_bagofword/logistic_regression_runtime.png', bbox_inches='tight')

#---------train and test with SVM - SVC-----

print('####### SVM #######') 
kernels = [ 'linearSVC', 'poly', 'rbf', 'sigmoid']
#kernels = ['linearSVC']
accuracy_svm = [] 
runtime_svm = []    

for i in kernels:
    
    tic()
    if (i == 'linearSVC'):
        svm = LinearSVC(max_iter = 2000)
    else:
        svm = SVC(kernel = i)
        #svm = SVC(gamma=0.01, C=100., probability=True, class_weight='balanced', kernel=i)
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
plt.title("BagOfWord SVM Accuracy")
plt.savefig('plot_bagofword/svm_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(kernels, runtime_svm, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("SVM kernels")
plt.ylabel("run time (seg)")
plt.title("BagOfWord SVM Run time")
plt.savefig('plot_bagofword/svm_runtime.png', bbox_inches='tight')

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
plt.title("BagOfWord MLP Accuracy")
plt.savefig('plot_bagofword/mlp_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(hidden_neurons, runtime_mlp, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("MLP hidden neurons")
plt.ylabel("run time (seg)")
plt.title("BagOfWord MLP Run time")
plt.savefig('plot_bagofword/mlp_runtime.png', bbox_inches='tight')

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
plt.title("BagOfWord RF Accuracy")
plt.savefig('plot_bagofword/rf_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(n_estimators, runtime_rf, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("Random Forest Number of estimators")
plt.ylabel("run time (seg)")
plt.title("BagOfWord RF Run time")
plt.savefig('plot_bagofword/rf_runtime.png', bbox_inches='tight')



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
plt.title("Bag of Word Best Algorithm Configuration Accuracy")
plt.savefig('plot_bagofword/best_accuracy.png', bbox_inches='tight')

plt.figure()
plt.plot(alg_name, best_run_time, linestyle='--', marker='o', color='r')
plt.grid()
plt.xlabel("Best algorithm configuration")
plt.ylabel("run time (seg)")
plt.title("Bag of Word Best Algorithm Configuration Run time")
plt.savefig('plot_bagofword/best_alg_runtime.png', bbox_inches='tight')


#----------------

#Time passed: 0.0hour:0.0min:25.433627367019653sec
#Reviews Matrix Shape (25000, 150000)
#----------
######## Logistic Regression #######
#Accuracy for Logistic Regression liblinear: 0.89216
#[[2718  367]
# [ 307 2858]]
#Time passed: 0.0hour:0.0min:4.241924524307251sec
#----------
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\linear_model\sag.py:334: ConvergenceWarning: The max_iter was reached which means the coef_ did not converge
#  "the coef_ did not converge", ConvergenceWarning)
#Accuracy for Logistic Regression sag: 0.89312
#[[2721  364]
# [ 304 2861]]
#Time passed: 0.0hour:0.0min:7.24403715133667sec
#----------
#Accuracy for Logistic Regression saga: 0.8944
#[[2729  356]
# [ 304 2861]]
#Time passed: 0.0hour:0.0min:9.71173644065857sec
#----------
#Accuracy for Logistic Regression newton-cg: 0.89184
#[[2717  368]
# [ 308 2857]]
#Time passed: 0.0hour:0.0min:8.808095932006836sec
#----------
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\linear_model\logistic.py:757: ConvergenceWarning: lbfgs failed to converge. Increase the number of iterations.
#  "of iterations.", ConvergenceWarning)
#Accuracy for Logistic Regression lbfgs: 0.89184
#[[2717  368]
# [ 308 2857]]
#Time passed: 0.0hour:0.0min:5.3367016315460205sec
#----------
######## SVM #######
#Accuracy for SVM linearSVC: 0.88352
#[[2694  391]
# [ 337 2828]]
#Time passed: 0.0hour:0.0min:10.819516658782959sec
#----------
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:196: FutureWarning: The default value of gamma will change from 'auto' to 'scale' in version 0.22 to account better for unscaled features. Set gamma explicitly to 'auto' or 'scale' to avoid this warning.
#  "avoid this warning.", FutureWarning)
#Accuracy for SVM poly: 0.4936
#[[3085    0]
# [3165    0]]
#Time passed: 0.0hour:13.0min:56.243077993392944sec
#----------
#Accuracy for SVM rbf: 0.4936
#[[3085    0]
# [3165    0]]
#Time passed: 0.0hour:13.0min:56.82241249084473sec
#----------
#Accuracy for SVM sigmoid: 0.4936
#[[3085    0]
# [3165    0]]
#Time passed: 0.0hour:13.0min:13.656466484069824sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 10: 0.90688
#[[2765  320]
# [ 262 2903]]
#Time passed: 0.0hour:3.0min:50.4915292263031sec
#----------
#Accuracy for MLP hidden neurons 30: 0.90816
#[[2763  322]
# [ 252 2913]]
#Time passed: 0.0hour:8.0min:35.47192716598511sec
#----------
#Accuracy for MLP hidden neurons 50: 0.90624
#[[2761  324]
# [ 262 2903]]
#Time passed: 0.0hour:13.0min:23.848541259765625sec
#----------
#Accuracy for MLP hidden neurons 100: 0.904
#[[2757  328]
# [ 272 2893]]
#Time passed: 0.0hour:25.0min:12.753307104110718sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 10: 0.7688
#[[2569  516]
# [ 929 2236]]
#Time passed: 0.0hour:0.0min:5.870083570480347sec
#----------
#Accuracy for Random Forest, n estimators 30: 0.82464
#[[2589  496]
# [ 600 2565]]
#Time passed: 0.0hour:0.0min:17.115859031677246sec
#----------
#Accuracy for Random Forest, n estimators 70: 0.84528
#[[2624  461]
# [ 506 2659]]
#Time passed: 0.0hour:0.0min:39.14116311073303sec
#----------
#Accuracy for Random Forest, n estimators 100: 0.85728
#[[2644  441]
# [ 451 2714]]
#Time passed: 0.0hour:0.0min:55.942787408828735sec
#----------
#Accuracy for Random Forest, n estimators 200: 0.86016
#[[2629  456]
# [ 418 2747]]
#Time passed: 0.0hour:1.0min:50.35554027557373sec
#----------
#Accuracy for Random Forest, n estimators 300: 0.864
#[[2641  444]
# [ 406 2759]]
#Time passed: 0.0hour:2.0min:48.58206343650818sec
#----------



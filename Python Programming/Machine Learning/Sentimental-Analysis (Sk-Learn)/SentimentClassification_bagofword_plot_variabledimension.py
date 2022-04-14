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

stop_words = ['in', 'of', 'at', 'a', 'the']
# 3 ngram increase the memory a lot, MLP use a lot of memory
#ngram_vectorizer = CountVectorizer(binary=True, ngram_range=(1,2),stop_words=stop_words, max_features=150000)
#Reviews Matrix Shape (25000, 1708493) ----with 2 ngram = too big 


################# Testing with different dimension ############################
accuracy_lr_list = []
runtime_lr_list = []
accuracy_svm_list = []
runtime_svm_list = []
accuracy_mlp_list = []
runtime_mlp_list = []
accuracy_rf_list = []
runtime_rf_list = []

solvers = []
kernels = []
hidden_neurons = []
n_estimators = []

feature_dimension = [1500, 15000, 150000, 300000, 1000000]
#feature_dimension = [1500, 3000]

for dimension in feature_dimension:
    tic()    
    #tfidf_vectorizer = TfidfVectorizer(ngram_range=(1,2),stop_words=stop_words, max_features=dimension)
    #tfidf_vectorizer.fit(reviews_clean)
    #X = tfidf_vectorizer.transform(reviews_clean)
    
    # 2 ngram increase the memory a lot, MLP use a lot of memory
    ngram_vectorizer = CountVectorizer(binary=True, ngram_range=(1,2),
                                   stop_words=stop_words, max_features=dimension)
    #Reviews Matrix Shape (25000, 1708493) ----with 2 ngram = too big 
    ngram_vectorizer.fit(reviews_clean)
    X = ngram_vectorizer.transform(reviews_clean)

    tac()
    print ("-----------------------------------")
    print('Reviews Matrix Shape %s' % str(X.shape))
    print ("-----------------------------------")
    
    #---------split the reviews 75% for training, 25% for testing----
    n_reviews = len(reviews)
    target = [1 if i < n_reviews/2 else 0 for i in range(n_reviews)] # first 50% = good review
    
    X_train, X_test, y_train, y_test = train_test_split(
        X, target, test_size = 0.25
    )
    
    
    #---------train and test with logistic regresion -----
    
    print('####### Logistic Regresion #######') 
    #solvers = ['liblinear', 'sag', 'saga', 'newton-cg', 'lbfgs']
    solvers = ['saga'] #best
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
    
    
    #---------train and test with MLP-----
    print('####### MLP #######') 
    #hidden_neurons = [10, 30, 50, 100]
    hidden_neurons = [30] #best
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
    
    
    
    #---------train and test with random forest-----
    print('####### Random Forest #######') 
    #n_estimators = [10, 30, 70, 100, 200, 300, 1000]
    n_estimators = [300] #best 1000 is to slow 11 min for 1500 dimension
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

    #------- save result-----
    accuracy_lr_list.append(accuracy_lr[0])
    runtime_lr_list.append(runtime_lr[0])

    accuracy_svm_list.append(accuracy_svm[0])
    runtime_svm_list.append(runtime_svm[0])
 
    accuracy_mlp_list.append(accuracy_mlp[0])
    runtime_mlp_list.append(runtime_mlp[0])
    
    accuracy_rf_list.append(accuracy_rf[0])
    runtime_rf_list.append(runtime_rf[0])
    
algorithms = ['LR', 'SVM', 'MLP', 'RF']
alg_name = []
alg_name.append(algorithms[0] + ' ' + solvers[0])
alg_name.append(algorithms[1]  + ' ' + kernels[0])
alg_name.append(algorithms[2]  + ' ' + str(hidden_neurons[0]))
alg_name.append(algorithms[3]  + ' ' + str(n_estimators[0]))

################# plot the result ##################### 


plt.figure()
plt.plot(feature_dimension, accuracy_lr_list, linestyle='--', marker='o', label = alg_name[0])
plt.plot(feature_dimension, accuracy_svm_list, linestyle='--', marker='o', label = alg_name[1])
plt.plot(feature_dimension, accuracy_mlp_list, linestyle='--', marker='o', label = alg_name[2])
plt.plot(feature_dimension, accuracy_rf_list, linestyle='--', marker='o', label = alg_name[3])
plt.legend()
plt.grid()
plt.xlabel("Bag of Word maximum number of feature")
plt.ylabel("Accuracy")
plt.title("Bag of Word Accuracy")
plt.savefig('plot_variable_dimension/bagofword_accurary_dimension.png', bbox_inches='tight')

plt.figure()
plt.plot(feature_dimension, runtime_lr_list, linestyle='--', marker='o', label = alg_name[0])
plt.plot(feature_dimension, runtime_svm_list, linestyle='--', marker='o', label = alg_name[1])
plt.plot(feature_dimension, runtime_mlp_list, linestyle='--', marker='o', label = alg_name[2])
plt.plot(feature_dimension, runtime_rf_list, linestyle='--', marker='o', label = alg_name[3])
plt.legend()
plt.grid()
plt.xlabel("Bag of Word maximum number of feature")
plt.ylabel("run time (seg)")
plt.title("Bag of Word Run time")
plt.savefig('plot_variable_dimension/bagofword_accurary_runtime.png', bbox_inches='tight')


#---------- result form desktop pc --------------

#Time passed: 0.0hour:0.0min:25.67180609703064sec
#-----------------------------------
#Reviews Matrix Shape (25000, 1500)
#-----------------------------------
######## Logistic Regresion #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\linear_model\sag.py:334: ConvergenceWarning: The max_iter was reached which means the coef_ did not converge
#  "the coef_ did not converge", ConvergenceWarning)
#Accuracy for Logistic Regression saga: 0.84896
#[[2596  491]
# [ 453 2710]]
#Time passed: 0.0hour:0.0min:3.691997528076172sec
#----------
######## SVM #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:922: ConvergenceWarning: Liblinear failed to converge, increase the number of iterations.
#  "the number of iterations.", ConvergenceWarning)
#Accuracy for SVM linearSVC: 0.84768
#[[2593  494]
# [ 458 2705]]
#Time passed: 0.0hour:0.0min:5.763014554977417sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.8544
#[[2578  509]
# [ 401 2762]]
#Time passed: 0.0hour:0.0min:3.9717366695404053sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.82848
#[[2546  541]
# [ 531 2632]]
#Time passed: 0.0hour:2.0min:14.752463340759277sec
#----------
#Time passed: 0.0hour:0.0min:25.164565324783325sec
#-----------------------------------
#Reviews Matrix Shape (25000, 15000)
#-----------------------------------
######## Logistic Regresion #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\linear_model\sag.py:334: ConvergenceWarning: The max_iter was reached which means the coef_ did not converge
#  "the coef_ did not converge", ConvergenceWarning)
#Accuracy for Logistic Regression saga: 0.88016
#[[2772  376]
# [ 373 2729]]
#Time passed: 0.0hour:0.0min:6.040201663970947sec
#----------
######## SVM #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:922: ConvergenceWarning: Liblinear failed to converge, increase the number of iterations.
#  "the number of iterations.", ConvergenceWarning)
#Accuracy for SVM linearSVC: 0.85952
#[[2712  436]
# [ 442 2660]]
#Time passed: 0.0hour:0.0min:4.658236026763916sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.88736
#[[2720  428]
# [ 276 2826]]
#Time passed: 0.0hour:0.0min:32.574575662612915sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.85408
#[[2713  435]
# [ 477 2625]]
#Time passed: 0.0hour:2.0min:2.858213186264038sec
#----------
#Time passed: 0.0hour:0.0min:25.577824592590332sec
#-----------------------------------
#Reviews Matrix Shape (25000, 150000)
#-----------------------------------
######## Logistic Regresion #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\linear_model\sag.py:334: ConvergenceWarning: The max_iter was reached which means the coef_ did not converge
#  "the coef_ did not converge", ConvergenceWarning)
#Accuracy for Logistic Regression saga: 0.89312
#[[2744  363]
# [ 305 2838]]
#Time passed: 0.0hour:0.0min:9.673704862594604sec
#----------
######## SVM #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:922: ConvergenceWarning: Liblinear failed to converge, increase the number of iterations.
#  "the number of iterations.", ConvergenceWarning)
#Accuracy for SVM linearSVC: 0.88288
#[[2730  377]
# [ 355 2788]]
#Time passed: 0.0hour:0.0min:9.589651346206665sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.90272
#[[2783  324]
# [ 284 2859]]
#Time passed: 0.0hour:4.0min:34.962698459625244sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.85856
#[[2646  461]
# [ 423 2720]]
#Time passed: 0.0hour:2.0min:50.89354872703552sec
#----------
#Time passed: 0.0hour:0.0min:26.036039352416992sec
#-----------------------------------
#Reviews Matrix Shape (25000, 300000)
#-----------------------------------
######## Logistic Regresion #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\linear_model\sag.py:334: ConvergenceWarning: The max_iter was reached which means the coef_ did not converge
#  "the coef_ did not converge", ConvergenceWarning)
#Accuracy for Logistic Regression saga: 0.89488
#[[2781  334]
# [ 323 2812]]
#Time passed: 0.0hour:0.0min:14.189854621887207sec
#----------
######## SVM #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:922: ConvergenceWarning: Liblinear failed to converge, increase the number of iterations.
#  "the number of iterations.", ConvergenceWarning)
#Accuracy for SVM linearSVC: 0.88688
#[[2757  358]
# [ 349 2786]]
#Time passed: 0.0hour:0.0min:11.81718373298645sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.91168
#[[2811  304]
# [ 248 2887]]
#Time passed: 0.0hour:10.0min:16.853843212127686sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.87088
#[[2708  407]
# [ 400 2735]]
#Time passed: 0.0hour:4.0min:36.13282537460327sec
#----------
#Time passed: 0.0hour:0.0min:26.755391836166382sec
#-----------------------------------
#Reviews Matrix Shape (25000, 1000000)
#-----------------------------------
######## Logistic Regresion #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\linear_model\sag.py:334: ConvergenceWarning: The max_iter was reached which means the coef_ did not converge
#  "the coef_ did not converge", ConvergenceWarning)
#Accuracy for Logistic Regression saga: 0.896
#[[2821  330]
# [ 320 2779]]
#Time passed: 0.0hour:0.0min:23.233948469161987sec
#----------
######## SVM #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:922: ConvergenceWarning: Liblinear failed to converge, increase the number of iterations.
#  "the number of iterations.", ConvergenceWarning)
#Accuracy for SVM linearSVC: 0.88912
#[[2797  354]
# [ 339 2760]]
#Time passed: 0.0hour:0.0min:19.042085647583008sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.90896
#[[2809  342]
# [ 227 2872]]
#Time passed: 0.0hour:29.0min:3.896779775619507sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.85584
#[[2655  496]
# [ 405 2694]]
#Time passed: 0.0hour:13.0min:7.020978689193726sec
#----------



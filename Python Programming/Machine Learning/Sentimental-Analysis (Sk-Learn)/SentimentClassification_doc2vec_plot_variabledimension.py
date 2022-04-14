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
from gensim.models.doc2vec import Doc2Vec, TaggedDocument

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

feature_dimension = [100, 300, 500, 1000, 5000]
#feature_dimension = [10000, 50000, 100000]
#feature_dimension = [1500, 3000]

for dimension in feature_dimension:
   
    #--------- doc2vec------------------
    #pip install --upgrade gensim
    tic()
    
    sentences = []
    for sent_str in reviews_clean:
        
        tokens = sent_str.split()
        sentences.append(tokens)
     
    documents = [TaggedDocument(doc, [i]) for i, doc in enumerate(sentences)]
    
    #configuration from https://github.com/RaRe-Technologies/gensim/blob/develop/docs/notebooks/doc2vec-IMDB.ipynb
    model = Doc2Vec(documents, dm=0, vector_size=dimension, negative=5, hs=0, min_count=2, sample=0, 
                epochs=20, workers=8)
    
    X=[]
    for d in sentences:
        
        X.append(model.infer_vector(d))
    
    tac()
    
    print ("-----------------------------------")
    print ('Reviews Matrix Shape %s , %s' % (len(X),len(X[0])))
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
    solvers = ['liblinear'] #best
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
    hidden_neurons = [100] #best
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
plt.xlabel("Doc2Vec maximum number of feature")
plt.ylabel("Accuracy")
plt.title("Doc2Vec Accuracy")
plt.savefig('plot_variable_dimension/doc2vec_accurary_dimension.png', bbox_inches='tight')

plt.figure()
plt.plot(feature_dimension, runtime_lr_list, linestyle='--', marker='o', label = alg_name[0])
plt.plot(feature_dimension, runtime_svm_list, linestyle='--', marker='o', label = alg_name[1])
plt.plot(feature_dimension, runtime_mlp_list, linestyle='--', marker='o', label = alg_name[2])
plt.plot(feature_dimension, runtime_rf_list, linestyle='--', marker='o', label = alg_name[3])
plt.legend()
plt.grid()
plt.xlabel("Doc2Vec maximum number of feature")
plt.ylabel("run time (seg)")
plt.title("Doc2Vec Run time")
plt.savefig('plot_variable_dimension/doc2vec_accurary_runtime.png', bbox_inches='tight')


#----testing dimension 10000 to slow----
#Time passed: 5.0hour:40.0min:31.969525575637817sec
#-----------------------------------
#Reviews Matrix Shape 25000 , 10000
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.85632
#[[2748  449]
# [ 449 2604]]
#Time passed: 0.0hour:1.0min:26.248677015304565sec
#----------
######## SVM #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:922: ConvergenceWarning: Liblinear failed to converge, increase the number of iterations.
#  "the number of iterations.", ConvergenceWarning)
#Accuracy for SVM linearSVC: 0.85952
#[[2758  439]
# [ 439 2614]]
#Time passed: 0.0hour:4.0min:22.129828929901123sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 100: 0.85536
#[[2833  364]
# [ 540 2513]]
#Time passed: 0.0hour:2.0min:19.18157410621643sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.81744
#[[2567  630]
# [ 511 2542]]
#Time passed: 0.0hour:8.0min:28.61711359024048sec
----------




#---------- result form desktop pc --------------
#Intel Core i5-4570 @ 3.2 GHz 3.2 GHz

#Time passed: 0.0hour:3.0min:21.871511459350586sec
#-----------------------------------
#Reviews Matrix Shape 25000 , 100
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.84064
#[[2601  513]
# [ 483 2653]]
#Time passed: 0.0hour:0.0min:0.46792149543762207sec
#----------
######## SVM #######
#Accuracy for SVM linearSVC: 0.84016
#[[2602  512]
# [ 487 2649]]
#Time passed: 0.0hour:0.0min:6.385388374328613sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 100: 0.84816
#[[2622  492]
# [ 457 2679]]
#Time passed: 0.0hour:0.0min:2.760897159576416sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.81072
#[[2487  627]
# [ 556 2580]]
#Time passed: 0.0hour:0.0min:47.16270351409912sec
#----------
#Time passed: 0.0hour:4.0min:55.69433641433716sec
#-----------------------------------
#Reviews Matrix Shape 25000 , 300
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.84592
#[[2638  478]
# [ 485 2649]]
#Time passed: 0.0hour:0.0min:1.148796558380127sec
#----------
######## SVM #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:922: ConvergenceWarning: Liblinear failed to converge, increase the number of iterations.
#  "the number of iterations.", ConvergenceWarning)
#Accuracy for SVM linearSVC: 0.8456
#[[2647  469]
# [ 496 2638]]
#Time passed: 0.0hour:0.0min:14.265894651412964sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 100: 0.85632
#[[2659  457]
# [ 441 2693]]
#Time passed: 0.0hour:0.0min:3.8376615047454834sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.79248
#[[2448  668]
# [ 629 2505]]
#Time passed: 0.0hour:1.0min:13.822219610214233sec
#----------
#Time passed: 0.0hour:6.0min:18.924452543258667sec
#-----------------------------------
#Reviews Matrix Shape 25000 , 500
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.85248
#[[2658  478]
# [ 444 2670]]
#Time passed: 0.0hour:0.0min:2.429685354232788sec
#----------
######## SVM #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:922: ConvergenceWarning: Liblinear failed to converge, increase the number of iterations.
#  "the number of iterations.", ConvergenceWarning)
#Accuracy for SVM linearSVC: 0.85072
#[[2650  486]
# [ 447 2667]]
#Time passed: 0.0hour:0.0min:18.348726511001587sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 100: 0.85728
#[[2692  444]
# [ 448 2666]]
#Time passed: 0.0hour:0.0min:5.553852319717407sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.80352
#[[2482  654]
# [ 574 2540]]
#Time passed: 0.0hour:1.0min:36.439889669418335sec
#----------
#Time passed: 0.0hour:10.0min:24.97799849510193sec
#-----------------------------------
#Reviews Matrix Shape 25000 , 1000
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.8536
#[[2695  452]
# [ 463 2640]]
#Time passed: 0.0hour:0.0min:7.5952465534210205sec
#----------
######## SVM #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:922: ConvergenceWarning: Liblinear failed to converge, increase the number of iterations.
#  "the number of iterations.", ConvergenceWarning)
#Accuracy for SVM linearSVC: 0.85152
#[[2692  455]
# [ 473 2630]]
#Time passed: 0.0hour:0.0min:28.6898992061615sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 100: 0.85472
#[[2704  443]
# [ 465 2638]]
#Time passed: 0.0hour:0.0min:10.806495428085327sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.79856
#[[2506  641]
# [ 618 2485]]
#Time passed: 0.0hour:2.0min:20.306309700012207sec
#----------
#Time passed: 0.0hour:51.0min:56.51732611656189sec
#-----------------------------------
#Reviews Matrix Shape 25000 , 5000
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.85776
#[[2737  451]
# [ 438 2624]]
#Time passed: 0.0hour:0.0min:50.77891969680786sec
#----------
######## SVM #######
#C:\Users\huanglinc\Desktop\WPy-3670\python-3.6.7.amd64\lib\site-packages\sklearn\svm\base.py:922: ConvergenceWarning: Liblinear failed to converge, increase the number of iterations.
#  "the number of iterations.", ConvergenceWarning)
#Accuracy for SVM linearSVC: 0.8584
#[[2733  455]
# [ 430 2632]]
#Time passed: 0.0hour:2.0min:14.409788131713867sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 100: 0.86288
#[[2686  502]
# [ 355 2707]]
#Time passed: 0.0hour:1.0min:13.846528053283691sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.80704
#[[2568  620]
# [ 586 2476]]
#Time passed: 0.0hour:5.0min:46.8294095993042sec
#----------

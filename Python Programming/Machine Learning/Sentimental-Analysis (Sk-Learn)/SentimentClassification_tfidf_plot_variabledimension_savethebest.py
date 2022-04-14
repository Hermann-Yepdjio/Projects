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

#feature_dimension = [15000, 150000, 300000, 1000000]
feature_dimension = [150000]

for dimension in feature_dimension:
    tic()    
    tfidf_vectorizer = TfidfVectorizer(ngram_range=(1,2),stop_words=stop_words, max_features=dimension)
    tfidf_vectorizer.fit(reviews_clean)
    X = tfidf_vectorizer.transform(reviews_clean)
    
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
    



##---------save the model to disk---------
import pickle


pickle.dump(mlp, open('mlp30_tfidp.sav', 'wb'))
pickle.dump(tfidf_vectorizer, open('tfidf150000_vectorizer.sav', 'wb'))


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


############ Test the another 50000 dataset ###########
#------------ import the data------------
reviews_test = []
file = open('code backup/tar_data/movie_data/full_test.txt', 'r', encoding="utf8")
for line in file:
    
    reviews_test.append(line.strip())
    
print(reviews_test[0])

#-----------clean the data-------------


REPLACE_NO_SPACE = re.compile("(\.)|(\;)|(\:)|(\!)|(\')|(\?)|(\,)|(\")|(\()|(\))|(\[)|(\])|(\d+)")
REPLACE_WITH_SPACE = re.compile("(<br\s*/><br\s*/>)|(\-)|(\/)")
NO_SPACE = ""
SPACE = " "

def preprocess_reviews(reviews):
    
    reviews = [REPLACE_NO_SPACE.sub(NO_SPACE, line.lower()) for line in reviews]
    reviews = [REPLACE_WITH_SPACE.sub(SPACE, line) for line in reviews]
    
    return reviews

reviews_test_clean = preprocess_reviews(reviews_test)

print(reviews_test_clean[0])



X = tfidf_vectorizer.transform(reviews_test_clean) #we need to save this in disk
    
n_reviews = len(reviews_test_clean)

target = [1 if i < n_reviews/2 else 0 for i in range(n_reviews)] # first 50% = good review
    
X_test, X_null, y_test, y_null = train_test_split(
        X, target, test_size = 0
    )


y_predicted = mlp.predict(X_test) #we need to save this in disk
accuracy = accuracy_score(y_test, y_predicted)
accuracy_mlp.append(accuracy)
cm = confusion_matrix(y_test, y_predicted)

print ("----------")
print ("Accuracy for MLP: %s" %(accuracy))
print(cm)
print ("----------")


####### end testing ######


###### demo #####
input_sentences = []
input_sentences.append('the machine learning class is excellent, but it is difficult to understand')
input_sentences.append('the database class is boring, but sometimes challenging')
print(input_sentences)
reviews_test_clean = preprocess_reviews(input_sentences)
print(reviews_test_clean)
X = tfidf_vectorizer.transform(reviews_test_clean) #we need to save this in disk
y_predicted = mlp.predict(X)  
print(y_predicted)


#-----------result from testing---------------
#-----------------------------------
#Reviews Matrix Shape (25000, 150000)
#-----------------------------------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.91424
#[[2793  322]
# [ 214 2921]]
#Time passed: 0.0hour:9.0min:24.95566725730896sec
#----------
#I went and saw this movie last night after being coaxed to by a few friends of mine. I'll admit that I was reluctant to see it because from what I knew of Ashton Kutcher he was only able to do comedy. I was wrong. Kutcher played the character of Jake Fischer very well, and Kevin Costner played Ben Randall with such professionalism. The sign of a good movie is that it can toy with our emotions. This one did exactly that. The entire theater (which was sold out) was overcome by laughter during the first half of the movie, and were moved to tears during the second half. While exiting the theater I not only saw many women in tears, but many full grown men as well, trying desperately not to let anyone see them crying. This movie was great, and I suggest that you go see it before you judge.
#i went and saw this movie last night after being coaxed to by a few friends of mine ill admit that i was reluctant to see it because from what i knew of ashton kutcher he was only able to do comedy i was wrong kutcher played the character of jake fischer very well and kevin costner played ben randall with such professionalism the sign of a good movie is that it can toy with our emotions this one did exactly that the entire theater which was sold out was overcome by laughter during the first half of the movie and were moved to tears during the second half while exiting the theater i not only saw many women in tears but many full grown men as well trying desperately not to let anyone see them crying this movie was great and i suggest that you go see it before you judge
#----------
#Accuracy for MLP: 0.90228
#[[11174  1326]
# [ 1117 11383]]
#----------
#['the machine learning class is excellent, but it is difficult to understand', 'the database class is boring, but sometimes challenging']
#['the machine learning class is excellent but it is difficult to understand', 'the database class is boring but sometimes challenging']
#[1 0]





#---------- result form surface --------------
# Intel core i7 - 4650U 1.7GHz 2.3 GHz

#Time passed: 0.0hour:0.0min:41.96143865585327sec
#-----------------------------------
#Reviews Matrix Shape (25000, 1500)
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.86864
#[[2681  441]
# [ 380 2748]]
#Time passed: 0.0hour:0.0min:0.6676619052886963sec
#----------
######## SVM #######
#Accuracy for SVM linearSVC: 0.86224
#[[2667  455]
# [ 406 2722]]
#Time passed: 0.0hour:0.0min:0.5702304840087891sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.86368
#[[2671  451]
# [ 401 2727]]
#Time passed: 0.0hour:0.0min:8.62391209602356sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.83536
#[[2621  501]
# [ 528 2600]]
#Time passed: 0.0hour:5.0min:0.6444046497344971sec
#----------
#Time passed: 0.0hour:0.0min:44.75692009925842sec
#-----------------------------------
#Reviews Matrix Shape (25000, 15000)
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.89008
#[[2751  358]
# [ 329 2812]]
#Time passed: 0.0hour:0.0min:0.8630235195159912sec
#----------
######## SVM #######
#Accuracy for SVM linearSVC: 0.888
#[[2757  352]
# [ 348 2793]]
#Time passed: 0.0hour:0.0min:0.5946111679077148sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.8976
#[[2776  333]
# [ 307 2834]]
#Time passed: 0.0hour:1.0min:31.752577304840088sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.85344
#[[2691  418]
# [ 498 2643]]
#Time passed: 0.0hour:3.0min:25.36721444129944sec
#----------
#Time passed: 0.0hour:0.0min:48.53683161735535sec
#-----------------------------------
#Reviews Matrix Shape (25000, 150000)
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.89312
#[[2732  370]
# [ 298 2850]]
#Time passed: 0.0hour:0.0min:2.276533842086792sec
#----------
######## SVM #######
#Accuracy for SVM linearSVC: 0.90672
#[[2788  314]
# [ 269 2879]]
#Time passed: 0.0hour:0.0min:1.4120831489562988sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.91136
#[[2801  301]
# [ 253 2895]]
#Time passed: 0.0hour:12.0min:29.762352466583252sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.85744
#[[2656  446]
# [ 445 2703]]
#Time passed: 0.0hour:4.0min:42.895899057388306sec
#----------
#Time passed: 0.0hour:0.0min:44.26662802696228sec
#-----------------------------------
#Reviews Matrix Shape (25000, 300000)
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.88672
#[[2755  372]
# [ 336 2787]]
#Time passed: 0.0hour:0.0min:2.5185563564300537sec
#----------
######## SVM #######
#Accuracy for SVM linearSVC: 0.90192
#[[2812  315]
# [ 298 2825]]
#Time passed: 0.0hour:0.0min:1.299271583557129sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.90832
#[[2838  289]
# [ 284 2839]]
#Time passed: 0.0hour:30.0min:58.669902324676514sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.85152
#[[2672  455]
# [ 473 2650]]
#Time passed: 0.0hour:5.0min:23.096815824508667sec
#----------
#Time passed: 0.0hour:0.0min:38.64385199546814sec
#-----------------------------------
#Reviews Matrix Shape (25000, 1000000)
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.8864
#[[2693  384]
# [ 326 2847]]
#Time passed: 0.0hour:0.0min:3.445026159286499sec
#----------
######## SVM #######
#Accuracy for SVM linearSVC: 0.90624
#[[2764  313]
# [ 273 2900]]
#Time passed: 0.0hour:0.0min:1.7759811878204346sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.9136
#[[2794  283]
# [ 257 2916]]
#Time passed: 2.0hour:31.0min:1.878174066543579sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.8568
#[[2623  454]
# [ 441 2732]]
#Time passed: 0.0hour:45.0min:8.867155075073242sec
#----------




#---------- result from desktop pc -----------
#Time passed: 0.0hour:0.0min:23.472262620925903sec
#-----------------------------------
#Reviews Matrix Shape (25000, 1500)
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.85792
#[[2666  493]
# [ 395 2696]]
#Time passed: 0.0hour:0.0min:0.46231985092163086sec
#----------
######## SVM #######
#Accuracy for SVM linearSVC: 0.8536
#[[2677  482]
# [ 433 2658]]
#Time passed: 0.0hour:0.0min:0.36025023460388184sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.85808
#[[2663  496]
# [ 391 2700]]
#Time passed: 0.0hour:0.0min:4.5401482582092285sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.83216
#[[2622  537]
# [ 512 2579]]
#Time passed: 0.0hour:2.0min:10.29737377166748sec
#----------
#Time passed: 0.0hour:0.0min:24.21181011199951sec
#-----------------------------------
#Reviews Matrix Shape (25000, 15000)
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.89184
#[[2760  381]
# [ 295 2814]]
#Time passed: 0.0hour:0.0min:0.5723798274993896sec
#----------
######## SVM #######
#Accuracy for SVM linearSVC: 0.89136
#[[2761  380]
# [ 299 2810]]
#Time passed: 0.0hour:0.0min:0.38628530502319336sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.90272
#[[2785  356]
# [ 252 2857]]
#Time passed: 0.0hour:0.0min:41.675888776779175sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.85952
#[[2683  458]
# [ 420 2689]]
#Time passed: 0.0hour:1.0min:53.58179569244385sec
#----------
#Time passed: 0.0hour:0.0min:25.480655908584595sec
#-----------------------------------
#Reviews Matrix Shape (25000, 150000)
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.89232
#[[2736  387]
# [ 286 2841]]
#Time passed: 0.0hour:0.0min:1.0617191791534424sec
#----------
######## SVM #######
#Accuracy for SVM linearSVC: 0.9096
#[[2809  314]
# [ 251 2876]]
#Time passed: 0.0hour:0.0min:0.7095091342926025sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.91552
#[[2844  279]
# [ 249 2878]]
#Time passed: 0.0hour:7.0min:47.57047510147095sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.85824
#[[2662  461]
# [ 425 2702]]
#Time passed: 0.0hour:2.0min:31.62605333328247sec
#----------
#Time passed: 0.0hour:0.0min:26.4993793964386sec
#-----------------------------------
#Reviews Matrix Shape (25000, 300000)
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.8888
#[[2743  384]
# [ 311 2812]]
#Time passed: 0.0hour:0.0min:1.3178958892822266sec
#----------
######## SVM #######
#Accuracy for SVM linearSVC: 0.90688
#[[2817  310]
# [ 272 2851]]
#Time passed: 0.0hour:0.0min:0.8475878238677979sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.9152
#[[2838  289]
# [ 241 2882]]
#Time passed: 0.0hour:18.0min:0.4954204559326172sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.8632
#[[2695  432]
# [ 423 2700]]
#Time passed: 0.0hour:4.0min:2.5792689323425293sec
#----------
#Time passed: 0.0hour:0.0min:28.115410566329956sec
#-----------------------------------
#Reviews Matrix Shape (25000, 1000000)
#-----------------------------------
######## Logistic Regresion #######
#Accuracy for Logistic Regression liblinear: 0.8856
#[[2760  379]
# [ 336 2775]]
#Time passed: 0.0hour:0.0min:2.2595348358154297sec
#----------
######## SVM #######
#Accuracy for SVM linearSVC: 0.90256
#[[2823  316]
# [ 293 2818]]
#Time passed: 0.0hour:0.0min:1.3269116878509521sec
#----------
######## MLP #######
#Accuracy for MLP hidden neurons 30: 0.91024
#[[2894  245]
# [ 316 2795]]
#Time passed: 0.0hour:37.0min:48.24950313568115sec
#----------
######## Random Forest #######
#Accuracy for Random Forest, n estimators 300: 0.85936
#[[2701  438]
# [ 441 2670]]
#Time passed: 0.0hour:11.0min:27.28869652748108sec
#----------


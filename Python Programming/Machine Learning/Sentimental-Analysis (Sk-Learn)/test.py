# -*- coding: utf-8 -*-
"""
Created on Mon Mar  4 22:04:36 2019

@author: chao_
"""


from sklearn.model_selection import train_test_split
import re
import pickle
from sklearn.metrics import accuracy_score, confusion_matrix
#----------load the IA--------------
mlp = pickle.load(open('mlp30_tfidp.sav', 'rb'))
tfidf_vectorizer = pickle.load(open('tfidf150000_vectorizer.sav', 'rb')) 


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
        X, target, test_size = 0)


y_predicted = mlp.predict(X_test) #we need to save this in disk
accuracy = accuracy_score(y_test, y_predicted)
cm = confusion_matrix(y_test, y_predicted)

print ("----------")
print ("Accuracy for MLP: %s" %(accuracy))
print(cm)
print ("----------")


####### end testing ######
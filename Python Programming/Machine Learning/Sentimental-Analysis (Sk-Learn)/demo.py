# -*- coding: utf-8 -*-
"""
Created on Mon Mar  4 21:37:59 2019

@author: chao_
"""

import re

# load the model from disk
import pickle



#----------load the IA--------------
mlp = pickle.load(open('mlp30_tfidp.sav', 'rb'))
tfidf_vectorizer = pickle.load(open('tfidf150000_vectorizer.sav', 'rb')) 


#-----------clean the data-------------
REPLACE_NO_SPACE = re.compile("(\.)|(\;)|(\:)|(\!)|(\')|(\?)|(\,)|(\")|(\()|(\))|(\[)|(\])|(\d+)")
REPLACE_WITH_SPACE = re.compile("(<br\s*/><br\s*/>)|(\-)|(\/)")
NO_SPACE = ""
SPACE = " "

def preprocess_reviews(reviews):
    
    reviews = [REPLACE_NO_SPACE.sub(NO_SPACE, line.lower()) for line in reviews]
    reviews = [REPLACE_WITH_SPACE.sub(SPACE, line) for line in reviews]
    
    return reviews



###### demo #####
input_sentences = []
input_sentences.append('the machine learning class is excellent, but it is difficult to understand')
input_sentences.append('the database class is boring, but sometimes challenging')
print(input_sentences)
reviews_test_clean = preprocess_reviews(input_sentences)
X = tfidf_vectorizer.transform(reviews_test_clean) #we need to save this in disk
y_predicted = mlp.predict(X)  
reviews_result = [ 'Good Review' if i ==1 else 'Bad Review' for i in y_predicted]
print(reviews_result)
print("\n")
print("###### Review IA ########")

while True:
    review = input("Review sentence [write :q to quit]: ")
    if (review == ':q'):
        break
    review_list = []
    review_list.append(review) 
    review_list_clean = preprocess_reviews(review_list)
    X = tfidf_vectorizer.transform(review_list_clean) #we need to save this in disk
    y_predicted = mlp.predict(X)  
    reviews_result = [ 'Good Review' if i ==1 else 'Bad Review' for i in y_predicted]
    print(reviews_result)



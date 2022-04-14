import numpy as np
import Resample as rsmpl
from normalize import norm_matrix as norm
from sklearn.metrics import accuracy_score, confusion_matrix
from keras.models import Sequential
from keras.layers import Dense
import numpy
import tensorflow as tf

from divide_set import divide_set as div_set
from sklearn.linear_model import Perceptron
import time
import pickle
import os
import errno
import gc #import the garbage collector 

#check if a directory exists, if not create one
def make_sure_path_exists(path):
    try:
        os.makedirs(path)
    except OSError as exception:
        if exception.errno != errno.EEXIST:
            raise
    return path

# load the pre-processed data saved in memory
def load_data(path):
    start_time = time.time()
    print("\n\n---------------------------------start loading data---------------------------------\n\n")
    X_train = np.load(path + "Training_Set.npy") # load pre-processed train data set
    y_train = np.load(path + "Training_target_Set.npy") # load targets for train data set
    X_test = np.load(path + "Validation_Set.npy") # load pre-processed train data set
    y_test = np.load(path + "Validation_Target_Set.npy") # load targets for train data set

    #shape of train and test objects
    print("Shape of train set: ", X_train.shape)
    print("Shape of test set: ", X_test.shape)

    # shape of new y objects
    print("Shape of train targets: ", y_train.shape)
    print("Shape of test targets: ", y_test.shape)
    runtime = time.time() - start_time
    print("\n--------------End loading data.  Running time: ", runtime, " seconds---------------------\n")
    return X_train, y_train, X_test, y_test

#test with mlp using 30 hidden neurons
def MLP (X_train, y_train, X_test, y_test, path, file_name = "mlp.pkl", hidden_layers = (30, )):
    start_time = time.time()
    print("\n\n--------------------------------start MLP classification---------------------------------------\n\n")
    # create model
    model = Sequential()
    model.add(Dense(12, input_dim=len(X_train[0]), activation='relu'))
    model.add(Dense(8, activation='relu'))
    model.add(Dense(1, activation='sigmoid'))
    # Compile model
    model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
    # Fit the model
    model.fit(X_train, y_train, epochs=1, batch_size=100)
    # evaluate the model
    #scores = model.evaluate(X_test, y_test)
    y_predicted = model.predict(X_test)
    y_predicted = (y_predicted > 0.5)
    #print("\n%s: %.2f%%" % (model.metrics_names[1], scores[1]*100))
    accuracy = accuracy_score(y_test, y_predicted)
    cm = confusion_matrix(y_test, y_predicted)

    print("Accuracy for MLP with network configuration %s: %s" % (hidden_layers, accuracy))
    print( "\n Confusion Matrix: \n", cm)
    runtime = time.time() - start_time
    print("\n-----------------End MLP Classification. Running time: ", runtime, " seconds-------------------------\n")

    #save model on disk
    #mlp_models_path = make_sure_path_exists(path + "MLP_Models/")
    #with open(mlp_models_path + file_name, "wb") as f:
    #    pickle.dump(mlp, f)

def API():
    numpy.random.seed(7)
    tf.random.set_random_seed(1)
    path = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CheXpert-v1.0-small/"
    path_2 = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Master-Thesis/Assignments/HW#2/"
    X_train, y_train, X_test, y_test = load_data(path)
    #X_train = norm(X_train, norm = 'l2', axis = 0, copy= True, return_norm = False) # normalize X_train using l2(least squares)  # using sklearn normalizer
    #X_test = norm(X_test, norm = 'l2', axis = 0, copy = True, return_norm = False) # normalize X_test using l2 (least squares) # using sklearn normalizer
    #X_train = norm(X_train) # using my own normalizer
    #X_test = norm(X_test) # using my own normalizer
    #X_test = pca.fit(X_test).transform(X_test)
    #X_train, y_train = rsmpl.under_sampling(X_train, y_train)
    X_train, y_train = rsmpl.over_sampling(X_train, y_train)
    #X = np.concatenate((X_train, X_test))
    #y = np.concatenate((y_train, y_test))
    #pca = PCA(n_components = 1000)
    #X = pca.fit(X).transform(X)
    #print("here")
    #X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.33, random_state=42)
    #X_train, y_train = rsmpl.over_sampling(X_train, y_train)

    #KNN(X_train, y_train, X_test, y_test, path_2, "knn_3_us_norm.pkl", 3)
    MLP(X_train, y_train, X_test, y_test, path_2, "mlp_(5,)_us_norm.pkl",  (6, 6,))
    #RF(X_train, y_train, X_test, y_test, path_2, "rf_300_us_norm.pkl", 300)  
    #LP(X_train, y_train, X_test, y_test, path_2, "lp_100000_os.pkl", 100000)

def main():
    API()
    gc.collect()

if __name__ == "__main__":
    main()

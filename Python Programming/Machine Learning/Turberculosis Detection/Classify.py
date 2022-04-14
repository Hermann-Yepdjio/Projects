import numpy as np
from matplotlib import pyplot as plt
import Resample as rsmpl
import tensorflow as tf
from keras.models import Sequential
from keras.layers import Dense, Conv1D, Conv2D, Flatten, MaxPooling1D, Dropout
#from normalize import norm_matrix as norm
from sklearn.metrics import roc_auc_score
from sklearn.svm import SVC
from sklearn.neural_network import MLPClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, confusion_matrix
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
#from sklearn.preprocessing import normalize as norm
from sklearn.decomposition import PCA
from divide_set import divide_set as div_set
from sklearn.linear_model import Perceptron
import time
import pickle
import os
from pathlib import Path
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
def load_data(file_path):
    start_time = time.time()
    print("\n\n---------------------------------start loading data---------------------------------\n\n")

    t_s = Path(file_path + "Train/LoG_Training_Set.npy")
    t_t_s = Path(file_path + "Train/LoG_Training_target_Set.npy")
    v_s = Path(file_path + "Validate/LoG_Validation_Set.npy")
    v_t_s = Path(file_path + "Validate/LoG_Validation_Target_Set.npy")

#    t_s = Path(file_path + "Train/PHOG_Training_Set.npy")
#    t_t_s = Path(file_path + "Train/PHOG_Training_target_Set.npy")
#    v_s = Path(file_path + "Validate/PHOG_Validation_Set.npy")
#    v_t_s = Path(file_path + "Validate/PHOG_Validation_Target_Set.npy")

#    t_s = Path(file_path + "Train/Training_Set.npy")
#    t_t_s = Path(file_path + "Train/Training_target_Set.npy")
#    v_s = Path(file_path + "Validate/Validation_Set.npy")
#    v_t_s = Path(file_path + "Validate/Validation_Target_Set.npy")
    
#    t_s = Path(file_path + "Train/LBP_Training_Set.npy")
#    t_t_s = Path(file_path + "Train/LBP_Training_target_Set.npy")
#    v_s = Path(file_path + "Validate/LBP_Validation_Set.npy")
#    v_t_s = Path(file_path + "Validate/LBP_Validation_Target_Set.npy")

    #load X_train data
    with t_s.open('rb') as f:
        fsz = os.fstat(f.fileno()).st_size
        X_train = np.load(f)
        while f.tell() < fsz:
            X_train = np.vstack((X_train, np.load(f)))

    #load y_train data
    with t_t_s.open('rb') as f:
        fsz = os.fstat(f.fileno()).st_size
        y_train = np.load(f)
        while f.tell() < fsz:
            y_train = np.vstack((y_train, np.load(f)))

    #load X_test data
    with v_s.open('rb') as f:
        fsz = os.fstat(f.fileno()).st_size
        X_test = np.load(f)
        while f.tell() < fsz:
            X_test = np.vstack((X_test, np.load(f)))

    #load y_test_data
    with v_t_s.open('rb') as f:
        fsz = os.fstat(f.fileno()).st_size
        y_test = np.load(f)
        while f.tell() < fsz:
            y_test = np.vstack((y_test, np.load(f)))

    y_train = y_train.reshape((y_train.size, ))
    y_test = y_test.reshape((y_test.size, ))

#    X_train = np.load(path + "Train/Training_Set.npy") # load pre-processed train data set
#    y_train = np.load(path + "Train/Training_target_Set.npy") # load targets for train data set
#    X_test = np.load(path + "Validate/Validation_Set.npy") # load pre-processed train data set
#    y_test = np.load(path + "Validate/Validation_Target_Set.npy") # load targets for train data set

    #shape of train and test objects
    print("Shape of train set: ", X_train.shape)
    print("Shape of test set: ", X_test.shape)

    # shape of new y objects
    print("Shape of train targets: ", y_train.shape)
    print("Shape of test targets: ", y_test.shape)
    runtime = time.time() - start_time
    print("\n--------------End loading data.  Running time: ", runtime, " seconds---------------------\n")
    #return X_train, y_train, X_test, y_test
    return np.concatenate((X_train, X_test)), np.concatenate((y_train, y_test))
    

# test with KNN with 2 neighbors
def KNN(X_train, y_train, X_test, y_test, path, file_name = "knn.pkl", num_neighbors = 3):
    start_time = time.time()
    print("\n\n--------------------------------start KNN classification---------------------------------------\n\n")
    knn = KNeighborsClassifier(n_neighbors= num_neighbors, algorithm='kd_tree')
    knn.fit(X_train,y_train)
    y_predicted=knn.predict(X_test)
    accuracy = accuracy_score(y_test, y_predicted)
    cm = confusion_matrix(y_test, y_predicted)
    auc = roc_auc_score(y_test, y_predicted)

    print("AUC: ", auc)
    print("Accuracy for KNN with ", num_neighbors, ": ", accuracy)
    print("\n Confusion Matrix: \n", cm)
    runtime = time.time() - start_time
    print("\n----------------End KNN classification. Running time: ", runtime, " seconds------------------------\n")

    #save model on disk
    #knn_models_path = make_sure_path_exists(path + "KNN_Models/")
    #with open(knn_models_path + file_name, "wb") as f:
    #    pickle.dump(knn, f)

    return (accuracy, auc)


#test with mlp using 30 hidden neurons
def MLP (X_train, y_train, X_test, y_test, path, file_name = "mlp.pkl", hidden_layers = (30, )):
    start_time = time.time()
    print("\n\n--------------------------------start MLP classification---------------------------------------\n\n")
    mlp = MLPClassifier(hidden_layer_sizes = hidden_layers, early_stopping=True)  # default 30 hidden neurons
    mlp.fit(X_train, y_train)
    y_predicted = mlp.predict(X_test)
    accuracy = accuracy_score(y_test, y_predicted)
    cm = confusion_matrix(y_test, y_predicted)
    auc = roc_auc_score(y_test, y_predicted)

    print("AUC: ", auc)
    print("Accuracy for MLP with network configuration %s: %s" % (hidden_layers, accuracy))
    print("\n Confusion Matrix: \n", cm)
    runtime = time.time() - start_time
    print("\n-----------------End MLP Classification. Running time: ", runtime, " seconds-------------------------\n")

    #save model on disk
    #mlp_models_path = make_sure_path_exists(path + "MLP_Models/")
    #with open(mlp_models_path + file_name, "wb") as f:
    #    pickle.dump(mlp, f)
    return (accuracy, auc)

#test with mlp using 30 hidden neurons
def MLP_Keras (X_train, y_train, X_test, y_test, path, file_name = "mlp_keras.pkl", hidden_layers = (12, 8, 1,), num_epochs=1, batch_size=100):
    start_time = time.time()
    print("\n\n--------------------------------start MLP classification---------------------------------------\n\n")
    # create model
    model = Sequential()
    model.add(Dense(hidden_layers[0], input_dim=len(X_train[0]), activation='relu'))
    for i in range (0, len(hidden_layers) - 2):
    	model.add(Dense(hidden_layers[i + 1], activation='relu'))
    	print("here")

    model.add(Dense(hidden_layers[len(hidden_layers) - 1], activation='sigmoid'))
    # Compile model
    model.compile(loss='binary_crossentropy', optimizer ='adam', metrics=['accuracy'])
    # Fit the model
    history = model.fit(X_train, y_train,validation_split=0.33, epochs=num_epochs, batch_size=batch_size)
    # evaluate the model
    #scores = model.evaluate(X_test, y_test)
    y_predicted = model.predict(X_test)
    y_predicted = (y_predicted > 0.5)
    #print("\n%s: %.2f%%" % (model.metrics_names[1], scores[1]*100))
    accuracy = accuracy_score(y_test, y_predicted)
    cm = confusion_matrix(y_test, y_predicted)
    auc = roc_auc_score(y_test, y_predicted)

    print("AUC: ", auc)

#    train_acc = history.history['acc']
#    test_acc = history.history['val_acc']
#    epochs = range (1, len(train_acc) + 1)
#    plt.figure()
#    plt.plot(epochs, train_acc, label='train accuracy')
#    plt.plot(epochs, test_acc, label='test accuracy')
#    plt.title('train test accuracy')
#    plt.legend()
#    plt.grid()
#    plt.show()

    print("Accuracy for MLP with network configuration %s: %s" % (hidden_layers, accuracy))
    print ("\n Confusion Matrix: \n", cm)
    runtime = time.time() - start_time
    print("\n-----------------End MLP Classification. Running time: ", runtime, " seconds-------------------------\n")

    #save model on disk
    #mlp_models_path = make_sure_path_exists(path + "MLP_Models/")
    #with open(mlp_models_path + file_name, "wb") as f:
    #    pickle.dump(mlp, f)
    return (accuracy, auc)

#test with cnn using 30
def CNN_Keras (X_train, y_train, X_test, y_test, path, file_name = "cnn_keras.pkl", num_epochs=1, batch_size=100):
    start_time = time.time()
    print("\n\n--------------------------------start CNN classification---------------------------------------\n\n")
    # create model
    hidden_layers = 3
    num_outputs = 1
    model = Sequential()

#    #add layers
#    model.add(Dense(hidden_layers[0], input_dim=len(X_train[0]), activation='relu'))
#    for i in range (0, len(hidden_layers) - 2):
#        model.add(Dense(hidden_layers[i + 1], activation='relu'))
#        print("here")a

    #add model layers
    X_train = X_train.reshape(X_train.shape[0],X_train.shape[1], 1) 
    X_test = X_test.reshape(X_test.shape[0],X_test.shape[1], 1)
    print(X_train.shape)
    model.add(Conv1D(filters=64, kernel_size=3, activation='relu', input_shape=X_train[0].shape))#(X_train[0].shape[0], X_train[0].shape[1])))
    model.add(Conv1D(filters=64, kernel_size=3, activation='relu'))
    model.add(Dropout(0.5))
    model.add(MaxPooling1D(pool_size=2 ))
    model.add(Flatten())
    model.add(Dense(100, activation='relu'))
    model.add(Dense(num_outputs, activation='softmax'))

    # Compile model
    model.compile(loss='binary_crossentropy', optimizer ='adam', metrics=['accuracy'])
    # Fit the model
    history = model.fit(X_train, y_train,validation_split=0.33, epochs=num_epochs, batch_size=batch_size)
    # evaluate the model
    #scores = model.evaluate(X_test, y_test)
    y_predicted = model.predict(X_test)
    y_predicted = (y_predicted > 0.5)
    #print("\n%s: %.2f%%" % (model.metrics_names[1], scores[1]*100))
    accuracy = accuracy_score(y_test, y_predicted)
    cm = confusion_matrix(y_test, y_predicted)
    auc = roc_auc_score(y_test, y_predicted)

    print("AUC: ", auc)

#    train_acc = history.history['acc']
#    test_acc = history.history['val_acc']
#    epochs = range (1, len(train_acc) + 1)
#    plt.figure()
#    plt.plot(epochs, train_acc, label='train accuracy')
#    plt.plot(epochs, test_acc, label='test accuracy')
#    plt.title('train test accuracy')
#    plt.legend()
#    plt.grid()
#    plt.show()

    print("Accuracy for MLP with network configuration %s: %s" % (hidden_layers, accuracy))
    print ("\n Confusion Matrix: \n", cm)
    runtime = time.time() - start_time
    print("\n-----------------End MLP Classification. Running time: ", runtime, " seconds-------------------------\n")

    #save model on disk
    #mlp_models_path = make_sure_path_exists(path + "MLP_Models/")
    #with open(mlp_models_path + file_name, "wb") as f:
    #    pickle.dump(mlp, f)

    return (accuracy, auc)

#test with support vector machine
def SVM (X_train, y_train, X_test, y_test, path, file_name = "svm.pkl"):
    start_time = time.time()
    print("\n\n--------------------------------start SVM classification---------------------------------------\n\n")
    svm = SVC(kernel = 'linear', gamma = "auto")
    svm.fit(X_train, y_train)
    y_predicted = svm.predict(X_test)
    accuracy = accuracy_score(y_test, y_predicted)
    cm = confusion_matrix(y_test, y_predicted)
    auc = roc_auc_score(y_test, y_predicted)

    print("AUC: ", auc)
    print("Accuracy for Support Vector Machine: %s" % ( accuracy))
    print("\n Confusion Matrix: \n", cm)
    runtime = time.time() - start_time
    print("\n--------------------End SVM classification. Running time: ", runtime, " seconds---------------------\n")

    #save model on disk
    #rf_models_path = make_sure_path_exists(path + "RF_Models/")
    #with open(rf_models_path + file_name, "wb") as f:
    #    pickle.dump(randomforest, f)

    return (accuracy, auc)

#test with random forest
def RF (X_train, y_train, X_test, y_test, path, file_name = "rf.pkl", num_estimators = 300):
    start_time = time.time()
    print("\n\n--------------------------------start RF classification---------------------------------------\n\n")
    randomforest = RandomForestClassifier(n_estimators=num_estimators, random_state=555)
    randomforest.fit(X_train, y_train)
    start_time_2 = time.time()
    y_predicted = randomforest.predict(X_test)
    runtime_2 = time.time() - start_time_2
    print("\n---------End RF Model Test. Running time: ", runtime_2, " seconds. Number of samples used for testing: ", len(y_predicted),"------------------\n")
    accuracy = accuracy_score(y_test, y_predicted)
    cm = confusion_matrix(y_test, y_predicted)
    auc = roc_auc_score(y_test, y_predicted)

    print("AUC: ", auc)
    print("Accuracy for Random Forest, n estimators %s: %s" % (num_estimators, accuracy))
    print("\n Confusion Matrix: \n", cm)
    runtime = time.time() - start_time
    print("\n--------------------End RF classification. Running time: ", runtime, " seconds---------------------\n")

    #save model on disk
    #rf_models_path = make_sure_path_exists(path + "RF_Models/")
    #with open(rf_models_path + file_name, "wb") as f:
    #    pickle.dump(randomforest, f)

    return (accuracy, auc)

#text with linear perceptron
def LP(X_train, y_train, X_test, y_test, path, file_name = "perceptron.pkl", batch_size = 100000):
    start_time = time.time()
    print("\n\n--------------------------------start linear Perceptron classification---------------------------------------\n\n")
    X_train_set, y_train_set = div_set(X_train, y_train, batch_size) #divides X_train and Y_train into subsets of size = batch_size for partial fitting (because they whole set can't fit into the memory
    perceptron = Perceptron(max_iter=1, tol= 0.0001)
    #partially fit the model
    for i in range(len(X_train_set)):
        print("\nThe model is being  partially fitted with batch #%s %s %s%s" % (i+1, " out of ", len(X_train_set), "...\n"))
        if i == 0:
            perceptron.partial_fit(X_train_set[i], y_train_set[i], classes = np.unique(y_train))
        else:
            perceptron.partial_fit(X_train_set[i], y_train_set[i])
    y_predicted = perceptron.predict(X_test)
    accuracy = accuracy_score(y_test, y_predicted)
    cm = confusion_matrix(y_test, y_predicted)
    auc = roc_auc_score(y_test, y_predicted)

    print("AUC: ", auc)
    print("Accuracy for linear perceptron with batch_size,  %s: %s" % (batch_size, accuracy))
    print("\n Confusion Matrix: \n", cm)
    runtime = time.time() - start_time
    print("\n--------------------End linear Perceptron classification. Running time: ", runtime, " seconds---------------------\n")

    ##save model on disk
    #lp_models_path = make_sure_path_exists(path + "LP_Models/")
    #with open(lp_models_path + file_name, "wb") as f:
    #    pickle.dump(perceptron, f)

    return (accuracy, auc)


def pca(X, n_components):
    print("--------------------------------------start PCA: ", n_components, " features--------------------------------------------\n\n")
    start_time = time.time()
    pca = PCA(n_components = n_components)
    X = pca.fit(X).transform(X)
    runtime = time.time() - start_time
    print("Dataset's  new shape: ", X.shape)
    print("\n-----------------------------------End PCA. Running time: ", runtime, " seconds-------------------------------------\n")
    return X



def API(n_components):
    path = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/Clean_Segmented/"
    path_2 = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Master-Thesis/Assignments/HW#7/CHINA_dataset/"
    #X_train, y_train, X_test, y_test = load_data(path)
    X, y = load_data(path)
    #X_train = norm(X_train, norm = 'l2', axis = 0, copy= True, return_norm = False) # normalize X_train using l2(least squares)  # using sklearn normalizer
    #X_test = norm(X_test, norm = 'l2', axis = 0, copy = True, return_norm = False) # normalize X_test using l2 (least squares) # using sklearn normalizer
    #X_train = norm(X_train) # using my own normalizer
    #X_test = norm(X_test) # using my own normalizer
    #X_test = pca.fit(X_test).transform(X_test)
    #X, y = rsmpl.under_sampling(X, y)
    #X, y = rsmpl.over_sampling(X, y)
    #X = np.concatenate((X_train, X_test))
    #y = np.concatenate((y_train, y_test))
    #X = pca(X, n_components)
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.33, random_state=42)
    X_train, y_train = rsmpl.over_sampling(X_train, y_train)

    KNN_res = KNN(X_train, y_train, X_test, y_test, path_2, "knn_3_us_norm.pkl", 3)
    MLP_res = MLP(X_train, y_train, X_test, y_test, path_2, "mlp_(5,)_us_norm.pkl",  (750, 400, 400, 220, ))
    MLP_Keras_res = MLP_Keras(X_train, y_train, X_test, y_test, path_2, "mlp_(5,)_us_norm.pkl",  (750, 400, 400, 10,  1), 180, 1000)
#    CNN_res = CNN_Keras(X_train, y_train, X_test, y_test, path_2, "cnn_(5,)_us_norm.pkl", 200, 1000)
    SVM_res = SVM(X_train, y_train, X_test, y_test, path_2, "svm_300_us_norm.pkl")
    RF_res = RF(X_train, y_train, X_test, y_test, path_2, "rf_300_us_norm.pkl", 900)  
    LP_res = LP(X_train, y_train, X_test, y_test, path_2, "lp_100000_os.pkl", 1000)

    return (KNN_res, MLP_res, MLP_Keras_res, SVM_res, RF_res, LP_res)

def main():
    for i in range(100, 101, 100):
        API(i)
    gc.collect()

if __name__ == "__main__":
    main()



import numpy as np
import time

#reduces the larger data set to the size of the smaller data set
def under_sampling(X_train, y_train):
    start_time = time.time()
    print("\n\n---------------------------------start undersampling training set--------------------------------------\n\n")
    pos_set_indexes = [] #set of HOG features for images with anomalies(indexes where they are located in X_train)
    neg_set_indexes = [] #set of HOG features for images with no anomalies(indexes where they are located in X_train)
    new_X_train = [] # new train set after undersampling
    new_y_train = [] # new target values after undersampling

    #populate pos_set_indexes and neg_set_indexes
    for i in range(len(y_train)):
        if y_train[i] == 1:
            pos_set_indexes.append(i)
        else:
            neg_set_indexes.append(i)

    #checks which set needs to be undersampled and performs the undersampling
    if (len(pos_set_indexes) > len(neg_set_indexes)):
        print ("The positive set is  being undersampled...\n")
        for i in range(len(neg_set_indexes)):
            new_X_train.append(X_train[pos_set_indexes[i]])
            new_y_train.append(1)
            new_X_train.append(X_train[neg_set_indexes[i]])
            new_y_train.append(0)
        
        #print shapes of new training and target sets
        new_X_train = np.array(new_X_train)
        new_y_train = np.array(new_y_train)
        print("Shape of new train set: ", new_X_train.shape)
        print("Shape of new train targets: ", new_y_train.shape)

        runtime = time.time() - start_time
        print("\n--------------End undersampling training set.  Running time: ", runtime, " seconds---------------------\n")
        return new_X_train, new_y_train

    else:
        print ("The negative set is being undersampled...\n")
        for i in range(len(pos_set_indexes)):
            new_X_train.append(X_train[pos_set_indexes[i]])
            new_y_train.append(1)
            new_X_train.append(X_train[neg_set_indexes[i]])
            new_y_train.append(0)

        #print shapes of new training and target sets
        new_X_train = np.array(new_X_train)
        new_y_train = np.array(new_y_train)
        print("Shape of new train set: ", new_X_train.shape)
        print("Shape of new train targets: ", new_y_train.shape)

        runtime = time.time() - start_time
        print("\n--------------End undersampling training set. Running time: ", runtime, " seconds---------------------\n")
        return new_X_train, new_y_train 

#increses the smaller data set to the size of the larger data set
def over_sampling(X_train, y_train):
    start_time = time.time()
    print("\n\n--------------------------------start oversampling training set---------------------------------------\n\n")
    pos_set_indexes = [] #set of HOG features for images with anomalies(indexes where they are located in X_train)
    neg_set_indexes = [] #set of HOG features for images with no anomalies(indexes where they are located in X_train)
    new_X_train = [] # new train set after undersampling
    new_y_train = [] # new target values after undersampling
    counter  = 0
    
    #populate pos_set_indexes and neg_set_indexes
    for i in range(len(y_train)): 
        if y_train[i] == 1:
            pos_set_indexes.append(i)
        else:
            neg_set_indexes.append(i)
    
    #check which set need to be oversampled and performs the oversampling
    if (len(pos_set_indexes) > len(neg_set_indexes)):
        print ("The negative set is  being oversampled...\n")
        for i in range(len(pos_set_indexes)):
            new_X_train.append(X_train[pos_set_indexes[i]])
            new_y_train.append(1)
            new_X_train.append(X_train[neg_set_indexes[counter]])
            new_y_train.append(0)
            counter += 1
            if counter >= len(neg_set_indexes):
                counter = 0

        #print shapes of new training and target sets
        new_X_train = np.array(new_X_train)
        new_y_train = np.array(new_y_train)
        print("Shape of new train set: ", new_X_train.shape)
        print("Shape of new train targets: ", new_y_train.shape)

        runtime = time.time() - start_time
        print("\n--------------End oversampling trainig set. Running time: ", runtime, " seconds---------------------\n")
        return new_X_train, new_y_train 

    else:
        print ("The positive set is being oversampled...\n")
        for i in range(len(neg_set_indexes)):
            new_X_train.append(X_train[pos_set_indexes[counter]])
            new_y_train.append(1)
            new_X_train.append(X_train[neg_set_indexes[i]])
            new_y_train.append(0)
            counter += 1
            if counter >= len(pos_set_indexes):
                counter = 0

        #print shapes of new training and target sets
        new_X_train = np.array(new_X_train)
        new_y_train = np.array(new_y_train)
        print("Shape of new train set: ", new_X_train.shape)
        print("Shape of new train targets: ", new_y_train.shape)

        runtime = time.time() - start_time
        print("\n-------------- End oversampling training set. Running time: ", runtime, " seconds---------------------\n")
        return new_X_train, new_y_train 


from keras.datasets import mnist
import numpy as np
import time
import random
from sklearn.neighbors import KNeighborsClassifier
from sklearn import metrics
from scipy.spatial import distance
from sklearn.metrics import roc_auc_score
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, confusion_matrix
from sklearn.svm import SVC
from sklearn.neural_network import MLPClassifier
#******************************************************#
# Read the MNIST data into the memory
#******************************************************#
def ReadMNIST():
    print("Start reading the data ...")
    # Get the time
    StartTime = time.time()
    (x_train, y_train), (x_test, y_test) = mnist.load_data()
    # Convert the values
    x_train = x_train.astype('float32') / 255.
    x_test = x_test.astype('float32') / 255.
    x_train = x_train.reshape((len(x_train), np.prod(x_train.shape[1:])))
    x_test = x_test.reshape((len(x_test), np.prod(x_test.shape[1:])))
    # Print the size of the data
    print(x_train.shape,y_train.shape)
    print(x_test.shape, y_test.shape)
    #Get the time
    EndTime = time.time()
    print("Elapsed time to read data into memory: ", EndTime-StartTime)
    #return x_train[:200,:], y_train[:200], x_test, y_test;
    return x_train, y_train, x_test, y_test;
#******************************************************#
# K-nearest neighbour
#******************************************************#
def KNNClassification(TrainData, TrainLabel, TestData, TestLabel, Metric="euclidean",K=5):

    # print("************************************************")
    print("Start K-nearest neighbor ...")
    # Get the time
    StartTime = time.time()

    # Print stats about the data
    print("Number of training samples: ", TrainData.shape[0])
    print("Number of test samples: ", TestData.shape[0])
    print("Dimension of the data: ", TrainData.shape[1])
    print("Metric in use: ", Metric)
    print("Number of neighbours: ", K)

    knn = KNeighborsClassifier(n_neighbors=K, algorithm='auto', metric=Metric)

    # Train the model using the training sets
    knn.fit(TrainData, TrainLabel)

    # Predict the response for test dataset
    PredictedLabel = knn.predict(TestData)

    Accuracy = metrics.accuracy_score(TestLabel, PredictedLabel)
    print("Accuracy:", Accuracy)

    EndTime = time.time()
    ElapsedTime = int(EndTime - StartTime)
    print("Elapsed time to do the classification: ", ElapsedTime )
    print("************************************************")
    return Accuracy, ElapsedTime;

#test with mlp using 30 hidden neurons
def MLP (X_train, y_train, X_test, y_test,  hidden_layers = (750, 400, 400 )):
    start_time = time.time()
    print("\n\n--------------------------------start MLP classification---------------------------------------\n\n")
    mlp = MLPClassifier(hidden_layer_sizes = hidden_layers, early_stopping=True)  # default 30 hidden neurons
    mlp.fit(X_train, y_train)
    y_predicted = mlp.predict(X_test)
    accuracy = accuracy_score(y_test, y_predicted)
#    cm = confusion_matrix(y_test, y_predicted)
#    auc = roc_auc_score(y_test, y_predicted)
#
#    print("AUC: ", auc)
    print("Accuracy for MLP with network configuration %s: %s" % (hidden_layers, accuracy))
#   print("\n Confusion Matrix: \n", cm)
    runtime = time.time() - start_time
    print("\n-----------------End MLP Classification. Running time: ", runtime, " seconds-------------------------\n")

    return accuracy, runtime;

#test with support vector machine
def SVM (X_train, y_train, X_test, y_test):
    start_time = time.time()
    print("\n\n--------------------------------start SVM classification---------------------------------------\n\n")
#    svm = SVC(kernel = 'linear', gamma = "auto")
    svm = SVC(kernel = 'rbf', gamma = "auto")
    svm.fit(X_train, y_train)
    y_predicted = svm.predict(X_test)
    accuracy = accuracy_score(y_test, y_predicted)
#    cm = confusion_matrix(y_test, y_predicted)
#    auc = roc_auc_score(y_test, y_predicted)

#    print("AUC: ", auc)
    print("Accuracy for Support Vector Machine: %s" % ( accuracy))
#    print("\n Confusion Matrix: \n", cm)
    runtime = time.time() - start_time
    print("\n--------------------End SVM classification. Running time: ", runtime, " seconds---------------------\n")

    return accuracy, runtime;

#test with random forest
def RF (X_train, y_train, X_test, y_test,file_name = "rf.pkl", num_estimators = 300):
    start_time = time.time()
    print("\n\n--------------------------------start RF classification---------------------------------------\n\n")
    randomforest = RandomForestClassifier(n_estimators=num_estimators, random_state=555)
    randomforest.fit(X_train, y_train)
    y_predicted = randomforest.predict(X_test)
    accuracy = accuracy_score(y_test, y_predicted)
#    cm = confusion_matrix(y_test, y_predicted)
#    auc = roc_auc_score(y_test, y_predicted)

#    print("AUC: ", auc)
    print("Accuracy for Random Forest, n estimators %s: %s" % (num_estimators, accuracy))
#    print("\n Confusion Matrix: \n", cm)
    runtime = time.time() - start_time
    print("\n--------------------End RF classification. Running time: ", runtime, " seconds---------------------\n")

    return accuracy, runtime;


#******************************************************#
# K-nearest neighbour for different neighbours
#******************************************************#
def KNNEvaluation(TrainData, TrainLabel, TestData, TestLabel, Metrics, Neigbours):
    Accuracy = []
    Metric = []
    NoOfNeighbors = []
    ElapsedTime = []

    for i in Neigbours:
        for j in Metrics:
            (Acc, Time) = KNNClassification(TrainData, TrainLabel, TestData, TestLabel, j, i)
            Accuracy.append(Acc)
            Metric.append(j)
            NoOfNeighbors.append(i)
            ElapsedTime.append(Time)
    return accuracy,Metric,NoOfNeighbors,ElapsedTime;

#******************************************************#
# Print the results of the KNN_Evaluation
#******************************************************#
def PrintKNNEvaluationResults(Accuracy,Metric,NoOfNeighbors,ElapsedTime,Percentage,File):

    print("Results for percentage: ", Percentage)

    Max = 0.0
    Index = 0;
    k = 0;
    for i in range(len(NoOfNeighbors)):
        print("%2.2f[M=%s][T=%d][K=%d]\t" %(Accuracy[k]*100,Metric[k],ElapsedTime[k],NoOfNeighbors[k]))
        if (Max < Accuracy[k]):
            Max = Accuracy[k]
            Index = k;

        k += 1

    print("************************************************")
    print("Best result:%2.2f [M=%s][K=%d][P=%2.2f]" %(Accuracy[Index]*100,Metric[Index],NoOfNeighbors[Index],Percentage))
    File.write("Best result:%2.2f [M=%s][K=%d][P=%2.2f]\n" %(Accuracy[Index]*100,Metric[Index],NoOfNeighbors[Index],Percentage))
    File.flush()
    print("************************************************")
    return 0;


#******************************************************#
# Random data selection using percentage considering that
# the original data is equally distributed over the
# different classes
#******************************************************#
def MNISTRandomDataSelection(Data, Labels, Percent):

    Items = np.arange(Data.shape[0])
    List = Items.tolist();
    Cols = Data.shape[1]
    #print("Cols:", Cols )
    NoOfNewItems = int(Data.shape[0]*Percent/100)
    print("Size of data:", NoOfNewItems)
    RandomItems = random.sample(List, NoOfNewItems)
    #print(RandomItems)



    RandomData = np.zeros((NoOfNewItems, Cols), dtype = float)
    RandomLabels =np.zeros((NoOfNewItems), dtype = float)

    for i in range(0,NoOfNewItems):
        for j in range(0,Data.shape[1]):
            RandomData[i,j] = Data[RandomItems[i],j]
        RandomLabels[i] = Labels[RandomItems[i]]

    return RandomData, RandomLabels;


#******************************************************#
# Random data selection using percentage considering that
# the original data is equally distributed over the
# different classes
#******************************************************#
def GenerateEmbeddedData(ReferenceData, ReferenceLabels, Data, Labels):
    # print("************************************************")
    StartTime = time.time()
    print("Start generating the embedded data ...")
    NoOfNewItems = int(Data.shape[0])
    print("No of new items:", NoOfNewItems)
    Cols = int(ReferenceData.shape[0])

    EmbeddedData = np.zeros((NoOfNewItems, Cols), dtype = float)
    EmbeddedLabels =np.zeros((NoOfNewItems), dtype = float)

    for i in range(0,NoOfNewItems):
        for j in range(0,Cols):
            EmbeddedData[i,j] = distance.euclidean(Data[i], ReferenceData[j])
        EmbeddedLabels[i] = Labels[i]

    EndTime = time.time()
    ElapsedTime = int(EndTime - StartTime)
    print("Elapsed time to do the embedding: ", ElapsedTime)
    return EmbeddedData, EmbeddedLabels;

#******************************************************#
# Split data into classes
#******************************************************#
def SplitData(Images, Labels):
    # print("************************************************")
    StartTime = time.time()
    print("Start Splitting data...")
    SplittedData = [[],[],[],[],[],[],[],[],[],[]]
    #split dataset into classes
    for i in range(0, len(Images)):
        SplittedData[Labels[i]].append(Images[i])

    EndTime = time.time()
    ElapsedTime = int(EndTime - StartTime)
    print("Elapsed time to do the splitting: ", ElapsedTime)
    return SplittedData;


#******************************************************#
# Compute the mean of a set of images 
#******************************************************#
def ComputeMeanImages(Data):
    # print("************************************************")
    StartTime = time.time()
    print("Start Computing Mean images...")
    
    MeanImages = []
    #compute means
    for Class_i in Data:
        MeanClass_i = list(map(lambda column: sum(column) / len(column), zip(*Class_i))) #compute mean of each column in class_i and save results in a new list
        MeanImages.append(MeanClass_i)

#    print (len(MeanImages[0]))

    EndTime = time.time()
    ElapsedTime = int(EndTime - StartTime)
    print("Elapsed time to compute the mean images: ", ElapsedTime)
    return MeanImages

#******************************************************#
# Generate Reference Images
#******************************************************#
def ClosestToMeanDataSelection(Data, MeanImages, Percent):
    # print("************************************************")
    StartTime = time.time()
    print("Start Selecting Reference Images...")

    #10 lists corresponding to 10 classes. Each list will hold distances from each image to its corresponding class mean image
    Distances = [[],[],[],[],[],[],[],[],[],[]]  # I used lists rather than numpy matrix because I assume that classes have different numbers of samples

    #Data contain 10 lists each containing images for each class
    for i in range(0, len(Data)):
        for IMG in Data[i]:
            Distances[i].append(distance.euclidean(IMG, MeanImages[i]))

    #Sort the distances by sorting the array indexes (i.e The results are arrays of indexes rather than the values themselves)
    SortedDistances = []
    for ClassDistances in Distances:
        SortedDistances.append(sorted(range(len(ClassDistances)), key=lambda k: ClassDistances[k], reverse=True))

    #Build list of reference images
    ReferenceImages = []
    ReferenceImagesLabels = []

    for i in range(0, len(SortedDistances)):
        NoOfReferences = int(len(SortedDistances[i])*Percent/100)
        start_index = int((len(SortedDistances[i]) - NoOfReferences)/2)
        end_index = int((len(SortedDistances[i]) + NoOfReferences)/2)
#        for index in SortedDistances[i][0:NoOfReferences]: #Select Percent% closest images to the mean image of each class and append to ReferenceImages
        for index in SortedDistances[i][start_index:end_index]: #Select Percent% images in the middle
            ReferenceImages.append(Data[i][index])
            ReferenceImagesLabels.append(i)
            
    EndTime = time.time()
    ElapsedTime = int(EndTime - StartTime)
    print("Elapsed time to select reference images: ", ElapsedTime)
    return np.array(ReferenceImages), np.array(ReferenceImagesLabels)

#******************************************************#
# Main function
#******************************************************#


#Read data into memory
#(TrainData, TrainLabel, TestData, TestLabel) = ReadMNIST()

#Imgs = list(TrainData) + list(TestData)
#Labels = list(TrainLabel) + list(TestLabel)
#SplittedData = SplitData(Imgs, Labels)
#MeanImages = ComputeMeanImages(SplittedData)
#ClosestToMeanDataSelection(SplittedData, MeanImages, 2)


#
# #KNN
# # Distance metric (euclidean, manhattan, chebyshev, minkowski, wminkowski, seuclidean, mahalanobis
#
# # Neighbours
#Neighbors = [1, 3, 5]
## # Metrics
#DistanceMetrics = ["euclidean", "manhattan", "minkowski"];

#File = open("MLP_Embedded_0.5_100_0.5_02142021.txt","w+")

#for Percentage in np.arange(0.5,5,0.5):
#    (NewData,NewLabels) = ClosestToMeanDataSelection(SplittedData, MeanImages, Percentage) #MNISTRandomDataSelection(TrainData, TrainLabel,Percentage)


#    (EmbeddedTrainData, EmbeddedTrainLabels) = GenerateEmbeddedData(NewData,NewLabels,TrainData,TrainLabel)

#    (EmbeddedTestData, EmbeddedTestLabels) = GenerateEmbeddedData(NewData, NewLabels, TestData, TestLabel)

#    (Accuracy,Metric,NoOfNeighbors,ElapsedTime) = KNNEvaluation(EmbeddedTrainData, EmbeddedTrainLabels, EmbeddedTestData, EmbeddedTestLabels, DistanceMetrics, Neighbors)
# # Print the results
#    (Acc, Time) = SVM(EmbeddedTrainData, EmbeddedTrainLabels, EmbeddedTestData, EmbeddedTestLabels)
#    PrintKNNEvaluationResults(Accuracy,Metric,NoOfNeighbors,ElapsedTime,Percentage,File)

#File.close()

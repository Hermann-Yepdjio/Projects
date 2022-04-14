import torchvision as tv
import torch
import math
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import numpy as np
from sklearn.cluster import KMeans
from sklearn.metrics import *

#Load the data (MNIST) and split into training and testing sets
def load_data(b_size):
    print("------------------------------------------------------------------Start Loading Data-------------------------------------------------------------------------------")
    # datasets
    trainset = tv.datasets.MNIST('./data', download=False, train=True, transform=tv.transforms.Compose([tv.transforms.ToTensor()]))
    testset = tv.datasets.MNIST('./data', download=False, train=False, transform=tv.transforms.Compose([tv.transforms.ToTensor()]))

    
#    # dataloaders
#    trainloader = torch.utils.data.DataLoader(trainset, batch_size=b_size,
#     shuffle=True, num_workers=2)
#    testloader = torch.utils.data.DataLoader(testset, batch_size=b_size,
#     shuffle=False, num_workers=2)
#    # constant for classes
#    classes = ('T-shirt/top', 'Trouser', 'Pullover', 'Dress', 'Coat', 'Sandal', 'Shirt', 'Sneaker', 'Bag', 'Ankle Boot')
    print("------------------------------------------------------------------End Loading Data---------------------------------------------------------------------------------\n\n")
    return (trainset, testset)

# create a subset with num_per_cat items from each category in input_set
def extract_subset(input_set, num_per_cat, num_cat):
    subset, u_subset, labels = [], [], [] #the subsets to be returned (labelled set, unlabelled set, labels)
    
    counts = [0] * num_cat  #to keep track of how many item from each of the num_cat categories have been added to the subset
    for i, data in enumerate(input_set):
        if counts[data[1]] < num_per_cat:
            subset.append(data)
            u_subset.append(data[0].numpy())
            labels.append(data[1])
            counts[data[1]] += 1
        if all(count == num_per_cat for count in counts): #check if the subset contains num_per_cat elements for each category
            break
    return subset, u_subset, labels

#cluster data in input_set and return labels
def cluster(input_set, num_clusters):
    print("------------------------------------------------------------------Start Clustering---------------------------------------------------------------------------------")
    kmeans = KMeans(n_clusters=num_clusters, random_state=0).fit(input_set)
    print("-------------------------------------------------------------------End Clustering----------------------------------------------------------------------------------")
    #print(kmeans.labels_)

    return kmeans.labels_

#find the correct class that corresponds to each cluster
def find_corresponding_labels(labels, pred_clusters):
    tmp_c_m = confusion_matrix(labels, pred_clusters) #temporary because clustering assign random numbers to each cluster
    acc = accuracy_score(labels, pred_clusters)
    print("Initial confusion matrix: \n", tmp_c_m, "\n\n Number of classified images:", sum(map(sum, tmp_c_m)), "\n\n Classification Accuracy: ", acc, "%\n\n")

    p_c_classes = np.argmax(tmp_c_m, axis = 1) #Find which class is the most represented in each cluster( p_c_classes = Potential_correct_classes)
    print("\n\nMost represented classes per cluster:\n", p_c_classes) # shows that class 8 is not assigned while class 9 is assigned to cluster 5 and 8
    condition = True

    while condition:
        unused_label = -1
        same_values_at = []

        for i in range(0, 10):
            tmp = np.where(p_c_classes == i)[0]
            if len(tmp) == 0:
                unused_label = i
            elif len(tmp) > 1:
                same_values_at = tmp
                if(unused_label!=-1):
                    break

        if len(same_values_at) > 1:
            if (tmp_c_m[same_values_at[0]][unused_label] < tmp_c_m[same_values_at[1]][unused_label]):
                p_c_classes[same_values_at[1]] = unused_label
            else:
                p_c_classes[same_values_at[0]] = unused_label
        else:
            condition = False

    #p_c_classes[5] = 8 # assign class 8 to cluster 5 because in tmp_c_m, class 8 is more represented in cluster 5 than it is in cluster 8
    print("\n\npotential correct label for each cluster:\n", p_c_classes) # print the potential correct label for each cluster

    #replace cluster number with appropriate class label
    true_pred_labels = []
    for num in pred_clusters:
        true_pred_labels.append(list(p_c_classes).index(num))
    #print(true_pred_labels)

    return true_pred_labels

def main():
    trainset, testset = load_data(4)
    subset, u_subset, labels = extract_subset(testset, 100, 10)
    u_subset, labels= np.asarray(u_subset), np.asarray(labels)
    img_size = u_subset[0].size
    u_subset = u_subset.reshape(len(u_subset), (img_size) )
    pred_clusters = cluster(u_subset, 10)

    true_pred_labels = find_corresponding_labels(labels, pred_clusters) #find the correct class that corresponds to each cluster
    c_m = confusion_matrix(labels, true_pred_labels) #temporary because clustering assign random numbers to each cluster
    acc = accuracy_score(labels, true_pred_labels)
    print("\n\nFinal confusion Matrix: \n", c_m, "\n\n Number of classified images:", sum(map(sum, c_m)), "\n\n Classification Accuracy: ", acc, "%\n\n")
    

if __name__ == "__main__":
    main()

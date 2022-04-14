import torchvision as tv
import torch
import math
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import numpy as np
from sklearn.cluster import KMeans
from sklearn.metrics import *
import matplotlib.pyplot as plt
from sklearn.decomposition import PCA

class Autoencoder(nn.Module):
    def __init__(self):
        super(Autoencoder, self).__init__()
        self.encoder = nn.Sequential( # like the Composition layer you built
            nn.Conv2d(1, 16, 3, stride=2, padding=1),
            nn.ReLU(),
            nn.Conv2d(16, 32, 3, stride=2, padding=1),
            nn.ReLU(),
            nn.Conv2d(32, 64, 7)
        )
        self.decoder = nn.Sequential(
            nn.ConvTranspose2d(64, 32, 7),
            nn.ReLU(),
            nn.ConvTranspose2d(32, 16, 3, stride=2, padding=1, output_padding=1),
            nn.ReLU(),
            nn.ConvTranspose2d(16, 1, 3, stride=2, padding=1, output_padding=1),
            nn.Sigmoid()
        )

    def forward(self, x):
        x = self.encoder(x)
        x = self.decoder(x)
        return x

def train(model, mnist_data, num_epochs=5,  batch_size=64, learning_rate=1e-3):
    torch.manual_seed(42)
    criterion = nn.MSELoss() # mean square error loss
    optimizer = torch.optim.Adam(model.parameters(),
                                 lr=learning_rate, 
                                 weight_decay=1e-5) # <--
    train_loader = torch.utils.data.DataLoader(mnist_data, 
                                               batch_size=batch_size, 
                                               shuffle=True)
    outputs = []
    for epoch in range(num_epochs):
        for data in train_loader:
            img, _ = data
            recon = model(img)
            loss = criterion(recon, img)
            loss.backward()
            optimizer.step()
            optimizer.zero_grad()

        print('Epoch:{}, Loss:{:.4f}'.format(epoch+1, float(loss)))
        outputs.append((epoch, img, recon),)
    return outputs

def plot_images(max_epochs, outputs):
    for k in range(0, max_epochs, 5):
        plt.figure(figsize=(9, 2))
        imgs = outputs[k][1].detach().numpy()
        recon = outputs[k][2].detach().numpy()
        for i, item in enumerate(imgs):
            if i >= 9: break
            plt.subplot(2, 9, i+1)
            plt.imshow(item[0])

        for i, item in enumerate(recon):
            if i >= 9: break
            plt.subplot(2, 9, 9+i+1)
            plt.imshow(item[0])

    plt.show()


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

#Reduce the dimensional of the input images to just the most important dimensions (principal component analysis
def P_C_A(subset, num_components):
    pca = PCA(n_components=num_components)
    X = pca.fit(subset).transform(subset)
    return X

def main():
#    torch.manual_seed(0)
    trainset, testset = load_data(4)
    subset, u_subset, labels = extract_subset(trainset, 1000, 10)
    model = Autoencoder()
    max_epochs = 20
    outputs = train(model, subset, num_epochs=max_epochs)
    #plot_images(max_epochs, outputs)
    u_subset = model.encoder(torch.tensor(u_subset)).detach()
    print(len(u_subset))

#    print(u_subset[0:10])

    u_subset, labels= np.asarray(u_subset), np.asarray(labels)


    img_size = u_subset[0].size
    u_subset = u_subset.reshape(len(u_subset), (img_size) )
    print(u_subset.shape)

#    print(u_subset[0:10])
    u_subset = P_C_A(u_subset, 32)

    print("Shape of resulting subset after PCA:", u_subset.shape)
#    print(u_subset[0:10])

    pred_clusters = cluster(u_subset, 10)
    
    true_pred_labels = find_corresponding_labels(labels, pred_clusters) #find the correct class that corresponds to each cluster
    c_m = confusion_matrix(labels, true_pred_labels) 
    acc = accuracy_score(labels, true_pred_labels)
    print("\n\nFinal confusion Matrix: \n", c_m, "\n\n Number of classified images:", sum(map(sum, c_m)), "\n\n Classification Accuracy: ", acc, "%\n\n")
    

if __name__ == "__main__":
    main()

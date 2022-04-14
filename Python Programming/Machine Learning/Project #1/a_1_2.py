import torchvision as tv
import torch
import math
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import numpy as np

class Net(nn.Module):

    def __init__(self):
        super(Net, self).__init__()
        self.fc1 = nn.Linear(28*28, 1024)
        self.fc2 = nn.Linear(1024, 1024)
        self.fc3 = nn.Linear(1024, 10)

    def forward(self, x):
        #model = nn.Sequential(self.fc1, nn.ReLU(), self.fc2)
        #flatten the image input
        x = x.view(-1, 28*28)
        x = self.fc1(x)
        x = F.relu(x) #F.sigmoid(x)
        x = self.fc2(x)
        x = F.relu(x) #F.sigmoid(x)
        x = self.fc3(x)
        return x #model(x)

#Load the data (FashionMNIST) and split into training and testing sets
def load_data(b_size):
    print("---------------------------------------------------------------------Start Loading Data--------------------------------------------------------------------------")
    # datasets
    trainset = tv.datasets.FashionMNIST('./data', download=False, train=True, transform=tv.transforms.Compose([tv.transforms.ToTensor()]))
    testset = tv.datasets.FashionMNIST('./data', download=False, train=False, transform=tv.transforms.Compose([tv.transforms.ToTensor()]))

    #pollute the trainset
    trainset = pollute(trainset, 10, 0.01)

    # dataloaders
    trainloader = torch.utils.data.DataLoader(trainset, batch_size=b_size,
     shuffle=True, num_workers=2)
    testloader = torch.utils.data.DataLoader(testset, batch_size=b_size,
     shuffle=False, num_workers=2)
    # constant for classes
    classes = ('T-shirt/top', 'Trouser', 'Pullover', 'Dress', 'Coat', 'Sandal', 'Shirt', 'Sneaker', 'Bag', 'Ankle Boot')
    print("------------------------------------------------------------------End Loading Data-------------------------------------------------------------------------------")
    return (trainloader, testloader)

#train the model
def train(model, trainloader, criterion, optimizer, n_epochs):

    print ('-----------------------------------------------------------------Started Training-------------------------------------------------------------------------------')
    for epoch in range(n_epochs):  # loop over the dataset multiple times

        running_loss = 0.0
        for i, data in enumerate(trainloader, 0):
            # get the inputs; data is a list of [inputs, labels]
            inputs, labels = data

            # zero the parameter gradients
            optimizer.zero_grad()

            # forward + backward + optimize
            outputs = model(inputs)
            loss = criterion(outputs, labels)
            loss.backward()
            optimizer.step()

            # print statistics
            running_loss += loss.item()
            if i % 2000 == 1000:    # print every 2000 mini-batches
                print('[%d, %5d] loss: %.3f' % (epoch + 1, i + 1, running_loss / 2000))
                running_loss = 0.0

    print('------------------------------------------------------------------Finished Training-----------------------------------------------------------------------------')

#test the model on unseen data
def test(model, testloader):
    correct = 0
    total = 0
    print ('------------------------------------------------------------------Started Testing------------------------------------------------------------------------------')
    with torch.no_grad():
        for data in testloader:
            images, labels = data
            outputs = model(images)
            _, predicted = torch.max(outputs.data, 1)
            total += labels.size(0)
            correct += (predicted == labels).sum().item()

    print ('---------------------------------------------------------------------Finished Testing--------------------------------------------------------------------------')

    print('Accuracy of the network on the', len(testloader), ' test images: %d %%' % (100 * correct / total))

#perform pollution on the training set
def pollute(trainset, num_cat, percentage):
    counts = [0] * num_cat #to hold counters for the 10 labels or categories

    #find the number of images per category
    for  data in trainset:
            counts[data[1]]+= 1 #access the label in data and increament the corresponding counter
#    print(counts)

    tmp_trainset = [list(data) for data in trainset] #convert to list of lists from list of tuples so that we can change the values (tuples are immutable) 

    print("------------------------------------------------------Pollution of the training set starting.....................................................................")
    #perform pollution for each category
    for i, count in enumerate(counts):
        num_imgs_to_pollute = count * percentage
        #add subsets of a category to the other categories
        for j in range (num_cat):
            if j != i:
                tmp_count = 0
                for k, data in enumerate(tmp_trainset):
                    if data[1] == i:
#                        print ("changed from ", tmp_trainset[k][1], " to ") 
                        tmp_trainset[k][1] = j
#                        print (tmp_trainset[k][1], ".\n")
                        tmp_count+=1
                        if (tmp_count == num_imgs_to_pollute):
                            break

    trainset =[tuple(data) for data in tmp_trainset] #convert back to list of tuples
    print("------------------------------------------------------Pollution of the training set ending.....................................................................")
    return trainset

def main():
#    trainloader, testloader = load_data(30)
    num_epochs = 10

    criterion = nn.CrossEntropyLoss() #loss function
    b_sizes = [1] #[1, 10, 1000]
    l_rates = [0.01] #[1.0, 0.1, 0.01, 0.001]

    for b_size in b_sizes: #try different mini-batch sizes
        trainloader, testloader = load_data(b_size)
        for l_rate in l_rates: #try different learning rates
            model = Net()
            optimizer = optim.SGD(model.parameters(), lr=l_rate, momentum=0) #optimizer stochastic gradient descent with learning rate = 0.001 and momemtum = 0 
            
            print("Mini-batch size: ", b_size,".     Learning rate: ", l_rate,".\n")
            train(model, trainloader, criterion, optimizer, num_epochs)
            test(model, testloader)

    

if __name__ == "__main__":
    main()

import torchvision as tv
import torch
import math
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import matplotlib.pyplot as plt
import numpy as np
import torchvision.transforms as transforms
from time import time

class Net(nn.Module):

    def __init__(self):
        super(Net, self).__init__()
        self.conv1 = nn.Conv2d(3, 9, 3, stride=1, padding=1)
        self.pool = nn.AvgPool2d(2, stride=1)
        self.conv2 = nn.Conv2d(9, 15, 3, stride=1, padding=1)
        self.conv3 = nn.Conv2d(15, 21, 3, stride=1, padding=1)
        self.conv4 = nn.Conv2d(21, 27, 3, stride=1, padding=1)
        self.conv5 = nn.Conv2d(27, 33, 3, stride=1, padding=1)
        self.fc1   = nn.Linear(33 * 27 * 27, 120)
        self.fc2   = nn.Linear(120, 84)
        self.fc3   = nn.Linear(84, 10)

    def forward(self, x):
#        x = self.pool(F.sigmoid(self.conv1(x))) #Apply pooling after conv
#        x = self.pool(F.sigmoid(self.conv2(x))) #apply pooling after conv
#        x = x.view(-1, 16 * 5 * 5)  #flatten the image input (output of the 2nd pooling later)
#        x = F.sigmoid(self.fc1(x))
#        x = F.sigmoid(self.fc2(x))

#        x = self.pool(F.tanh(self.conv1(x))) #Apply pooling after conv
#        x = self.pool(F.tanh(self.conv2(x))) #apply pooling after conv
#        x = x.view(-1, 16 * 5 * 5)  #flatten the image input (output of the 2nd pooling later)
#        x = F.tanh(self.fc1(x))
#        x = F.tanh(self.fc2(x))

        x = self.pool(F.relu(self.conv1(x))) #Apply pooling after conv
        x = self.pool(F.relu(self.conv2(x))) #apply pooling after conv
        x = self.pool(F.relu(self.conv3(x))) #apply pooling after conv
        x = self.pool(F.relu(self.conv4(x))) #apply pooling after conv
        x = self.pool(F.relu(self.conv5(x))) #apply pooling after conv
        x = x.view(-1, 33 * 27 * 27)  #flatten the image input (output of the 2nd pooling later)
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))

        x = self.fc3(x)
        return x 

#Load the data (CIFAR-10) and split into training and testing sets
def load_data(b_size):
    print("---------------------------------------Start Loading Data-------------------------------------")
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5, 0.5, 0.5), (0.5, 0.5, 0.5))])

    # datasets
    trainset = tv.datasets.CIFAR10(root='./data', train=True, download=False, transform=transform)
    testset = tv.datasets.CIFAR10(root='./data', train=False, download=False, transform=transform)
    # dataloaders
    trainloader = torch.utils.data.DataLoader(trainset, batch_size=b_size, shuffle=True, num_workers=2)
    testloader = torch.utils.data.DataLoader(testset, batch_size=b_size, shuffle=False, num_workers=2)
    # constant for classes
    classes = ('plane', 'car', 'bird', 'cat', 'deer', 'dog', 'frog', 'horse', 'ship', 'truck')
    print("---------------------------------------End Loading Data-------------------------------------")
    return (trainset, trainloader, testloader)

#train the model
def train_val(model, train_loader, val_loader, criterion,  optimizer, n_epochs):

    print ('-------------------------------------------------------Started Training-validation---------------------------------------------------------------')
    train_losses, val_losses = [], []
    for epoch in range(n_epochs):  # loop over the dataset multiple times

        train_running_loss, val_running_loss = 0.0, 0.0
        
        model.train()
        # While iterating over the dataset do training
        for i, data in enumerate(train_loader, 0):
            # get the inputs; data is a list of [inputs, labels]
            inputs, labels = data

            # zero the parameter gradients
            optimizer.zero_grad()

            # forward + backward + optimize
            outputs = model(inputs)
            loss = criterion(outputs, labels)
            loss.backward()
            optimizer.step()

            train_running_loss += loss.item() * len(inputs) 

#            # print statistics
#            running_loss += loss.item()
#            if i % 2000 == 1999:    # print every 2000 mini-batches
#                print('[%d, %5d] loss: %.3f' % (epoch + 1, i + 1, running_loss / 2000))
#                running_loss = 0.0
            
        epoch_loss = train_running_loss / len(train_loader.dataset)
        train_losses.append(epoch_loss)
        
        with torch.no_grad():
            model.eval()
            # While iterating over the dataset do validation
            for i, data in enumerate(val_loader):
                #data, targets = data.cuda(), targets.cuda() # comment for cpu version
                inputs, labels = data

                outputs = model(inputs) # forward pass
                loss = criterion(outputs, labels)

                val_running_loss += loss.item() * len(inputs)

            epoch_loss = val_running_loss / len(val_loader.dataset)
            val_losses.append(epoch_loss)

        print('epoch: ' + str(epoch + 1) + ' Training loss: ' + str(train_losses[epoch]))

        print('epoch: ' + str(epoch + 1) + ' Validation loss: ' + str(val_losses[epoch]))


    print('------------------------------------------------------------Finished Training-validation---------------------------------------------------------')
    return train_losses, val_losses

#test the model on unseen data
def test(model, testloader):
    correct = 0
    total = 0
    print ('-------------------------------------------------------Started Testing---------------------------------------------------------------')
    with torch.no_grad():
        for data in testloader:
            images, labels = data
            outputs = model(images)
            _, predicted = torch.max(outputs.data, 1)
            total += labels.size(0)
            correct += (predicted == labels).sum().item()

    print ('-------------------------------------------------------Finished Testing---------------------------------------------------------------')

    print('Accuracy of the network on the', len(testloader), ' test images: %d %%' % (100 * correct / total))

# Visualize feature maps
activation = {}
def get_activation(name):
    def hook(model, input, output):
        activation[name] = output.detach()
    return hook

def display_feature_maps(model, image, f_name):
    model.conv2.register_forward_hook(get_activation('conv2'))
    image.unsqueeze_(0)
    output = model(image)

    act = activation['conv2'].squeeze()
    fig, axarr = plt.subplots(act.size(0))
    for idx in range(act.size(0)):
        axarr[idx].imshow(act[idx])
    #plt.show()
    plt.savefig(f_name)
    plt.clf()

def plot(f_name, x, y, z):
    plt.plot(x, y, label = "Train")
    plt.plot(x, z, label = "Validation")
    plt.xlabel("Epoch Number")
    plt.ylabel("Loss")
    plt.title("Epoch Vs Loss")
    plt.legend()

    #plt.show()
    plt.savefig(f_name)
    
    plt.clf()

def main(): 
#    load_data(4)
    b_size = 100;
    num_epochs = 50;
    epochs = np.arange(1, num_epochs + 1)
    
    loss_function_label = ["cross-entropy", "MSE"]
    criterions = [nn.CrossEntropyLoss()]#, nn.MSELoss()] #loss function
    #b_sizes = [4] #[1, 10, 1000]
    l_rates = [0.1] #[0.1, 0.01, 0.001]

    for criterion in criterions: #try different loss functions
        trainset, trainloader, testloader = load_data(b_size)
       
        for l_rate in l_rates: #try different learning rates
            model = Net()
            optimizer = optim.SGD(model.parameters(), lr=l_rate, momentum=0) #optimizer stochastic gradient descent with learning rate = 0.001 and momemtum = 0 
            
            print("Mini-batch size: ", b_size,".     Learning rate: ", l_rate,".\n")
            start_time = time()
            train_losses, val_losses = train_val(model, trainloader, testloader, criterion, optimizer, num_epochs)
            seconds_elapsed_1 = time() - start_time
            
            start_time = time()
            test(model, testloader)
            seconds_elapsed_2 = time() - start_time

            print("\n\nTime elapsed: Train  %d secs, Test %d secs.\n\n" % (seconds_elapsed_1, seconds_elapsed_2))

#            for i in range(10):
#                image, _ = trainset[i]
#                file_name = "./Images/Feature_Maps/image_" + str(i) + ".png"
#                display_feature_maps(model, image, file_name)
            
            file_name = "./Images/Epoch_VS_Loss/ReLU_5_conv_" + loss_function_label[criterions.index(criterion)] + "_" + str(l_rate) + ".png"
            plot(file_name, epochs, np.array(train_losses), np.array(val_losses))
    

if __name__ == "__main__":
    main()

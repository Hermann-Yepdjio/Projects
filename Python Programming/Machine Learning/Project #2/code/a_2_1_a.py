import torchvision as tv
import torch
import math
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import matplotlib.pyplot as plt
import numpy as np
import torchvision.transforms as transforms

class Net(nn.Module):

    def __init__(self):
        super(Net, self).__init__()
        self.conv1 = nn.Conv2d(3, 6, 5, stride=1, padding=0)
        self.pool = nn.MaxPool2d(2, 2)
        self.conv2 = nn.Conv2d(6, 16, 5, stride=1, padding=0)
        self.fc1   = nn.Linear(16*5*5, 120)
        self.fc2   = nn.Linear(120, 84)
        self.fc3   = nn.Linear(84, 10)

    def forward(self, x):
#        x = self.pool(F.sigmoid(self.conv1(x))) #Apply pooling after conv
#        x = self.pool(F.sigmoid(self.conv2(x))) #apply pooling after conv
#        x = x.view(-1, 16 * 5 * 5)  #flatten the image input (output of the 2nd pooling later)
#        x = F.sigmoid(self.fc1(x))
#        x = F.sigmoid(self.fc2(x))

        x = self.pool(F.tanh(self.conv1(x))) #Apply pooling after conv
        x = self.pool(F.tanh(self.conv2(x))) #apply pooling after conv
        x = x.view(-1, 16 * 5 * 5)  #flatten the image input (output of the 2nd pooling later)
        x = F.tanh(self.fc1(x))
        x = F.tanh(self.fc2(x))
#
#        x = self.pool(F.relu(self.conv1(x))) #Apply pooling after conv
#        x = self.pool(F.relu(self.conv2(x))) #apply pooling after conv
#        x = x.view(-1, 16 * 5 * 5)  #flatten the image input (output of the 2nd pooling later)
#        x = F.relu(self.fc1(x))
#        x = F.relu(self.fc2(x))

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
    return (trainloader, testloader)

#train the model
def train_val(model, train_loader, val_loader, criterion, flag,  optimizer, n_epochs):

    print ('-------------------------------------------------------Started Training-validation---------------------------------------------------------------')
    train_losses, val_losses = [], []
    for epoch in range(n_epochs):  # loop over the dataset multiple times

        train_running_loss, val_running_loss = 0.0, 0.0

        # While iterating over the dataset do training
        for i, data in enumerate(train_loader, 0):
            # get the inputs; data is a list of [inputs, labels]
            inputs, labels = data

            # zero the parameter gradients
            optimizer.zero_grad()

            # forward + backward + optimize
            outputs = model(inputs)
            loss = 0
            if (flag == 1): #convert criterion parameters to float because it expects floats and not ints 
                loss = criterion(outputs.float(), labels.float())
            else:
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
            # While iterating over the dataset do validation
            for i, data in enumerate(val_loader):
                #data, targets = data.cuda(), targets.cuda() # comment for cpu version
                inputs, labels = data

                outputs = model(inputs) # forward pass
                loss = 0
                if (flag == 1): #convert criterion parameters to float because it expects floats and not ints
                    loss = criterion(outputs.float(), labels.float())
                else:
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
    b_size = 10;
    num_epochs = 20;
    epochs = np.arange(1, num_epochs + 1)
    
    loss_function_label = ["cross-entropy", "MSE"]
    criterions = [nn.CrossEntropyLoss(), nn.MSELoss()] #loss function
    #b_sizes = [4] #[1, 10, 1000]
    l_rates = [0.1, 0.01, 0.001]

    for criterion in criterions: #try different loss functions
        trainloader, testloader = load_data(b_size)
       
        flag = criterions.index(criterion)        
        for l_rate in l_rates: #try different learning rates
            model = Net()
            optimizer = optim.SGD(model.parameters(), lr=l_rate, momentum=0) #optimizer stochastic gradient descent with learning rate = 0.001 and momemtum = 0 
            
            print("Mini-batch size: ", b_size,".     Learning rate: ", l_rate,".\n")
            train_losses, val_losses = train_val(model, trainloader, testloader, criterion, flag, optimizer, num_epochs)
            test(model, testloader)
            
            file_name = "./Images/Tanh_" + loss_function_label[criterions.index(criterion)] + "_" + str(l_rate) + ".png"
            plot(file_name, epochs, np.array(train_losses), np.array(val_losses))
    

if __name__ == "__main__":
    main()

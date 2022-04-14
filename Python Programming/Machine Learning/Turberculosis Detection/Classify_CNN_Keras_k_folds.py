# importing libraries
from PIL import Image
from matplotlib import pyplot as plt
from sklearn.model_selection import train_test_split, StratifiedKFold
from keras.preprocessing.image import ImageDataGenerator 
from keras.models import Sequential 
from keras.layers import Conv2D, MaxPooling2D 
from keras.layers import Activation, Dropout, Flatten, Dense 
from sklearn.metrics import accuracy_score, confusion_matrix
from keras import backend as K 
import scipy
import numpy as np
import os 
import time
import tensorflow as tf

start_time = time.time()
img_width, img_height = 600, 600
num_folds = 3
  
train_data_dir = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/MC_imgs/MC_seg_imgs/Train"
validation_data_dir = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/MC_imgs/MC_seg_imgs/Validate"
nb_train_samples = len(os.listdir(train_data_dir + "/abnormal")) + len(os.listdir(train_data_dir + "/normal"))
nb_validation_samples = len(os.listdir(validation_data_dir + "/abnormal")) + len(os.listdir(validation_data_dir + "/normal"))

#optain names of all the images
abnormal_1 = os.listdir(train_data_dir + "/abnormal") 
abnormal_2 = os.listdir(validation_data_dir + "/abnormal")
normal_1 = os.listdir(train_data_dir + "/normal" )
normal_2 = os.listdir(validation_data_dir + "/normal")

print(nb_train_samples, nb_validation_samples)
epochs = 20
batch_size = 15
  
if K.image_data_format() == 'channels_first': 
    input_shape = (1, img_width, img_height)
else: 
    input_shape = (img_width, img_height, 1) 
  
model = Sequential() 
model.add(Conv2D(32, (2, 2), input_shape = input_shape)) 
model.add(Activation('relu')) 
model.add(MaxPooling2D(pool_size =(2, 2))) 
  
model.add(Conv2D(32, (2, 2))) 
model.add(Activation('relu')) 
model.add(MaxPooling2D(pool_size =(2, 2))) 
  
model.add(Conv2D(64, (2, 2))) 
model.add(Activation('relu')) 
model.add(MaxPooling2D(pool_size =(2, 2))) 

 
  
model.add(Flatten()) 
model.add(Dense(64)) 
model.add(Activation('relu')) 
model.add(Dropout(0.5)) 
model.add(Dense(1)) 
model.add(Activation('sigmoid')) 
  
model.compile(loss ='binary_crossentropy', 
                     optimizer ='adam', 
                   metrics =['accuracy'])
#model.compile(loss ='binary_crossentropy', 
#                     optimizer ='rmsprop', 
#                   metrics =['accuracy']) 
  
train_datagen = ImageDataGenerator(
                rescale = 1. / 255, 
                 shear_range = 0.2, 
                  zoom_range = 0.2, 
            horizontal_flip = True,
		validation_split = 0.2) #this is where the split happens (20% for validation) 

test_datagen = ImageDataGenerator(rescale = 1. / 255)

X, Y = [], []
for img in abnormal_1:
    X.append(np.array(Image.open(train_data_dir + "/abnormal/"+ img)))
    Y.append(0)

for img in abnormal_2:
    X.append(np.array(Image.open(validation_data_dir + "/abnormal/"+ img)))
    Y.append(0)

for img in normal_1:
    X.append(np.array(Image.open(train_data_dir + "/normal/"+ img)))
    Y.append(1)

for img in normal_2:
    X.append(np.array(Image.open(validation_data_dir + "/normal/"+ img)))
    Y.append(1)

X = np.array(X)
Y = np.array(Y)


folds = list(StratifiedKFold(n_splits=num_folds, shuffle=True, random_state=1).split(X, Y))


for j, (train_idx, val_idx) in enumerate(folds):
    
    print ('\nFold ',j)
    X_train_cv = X[train_idx]
    y_train_cv = Y[train_idx]
    X_valid_cv = X[val_idx]
    y_valid_cv= Y[val_idx]

    #the next 3 lines are just to shuffle X_train_cv and y_train_cv otherwise train_datagen.flow will throw an error because the training or validation subset does not contain at least one sample from each class
    X_train, X_val, y_train, y_val = train_test_split(X_train_cv, y_train_cv, test_size=0.2, stratify=y_train_cv)
    X_train_cv = np.concatenate((X_train, X_val))
    y_train_cv = np.concatenate((y_train, y_val))

    X_train_cv = np.array([np.resize(img, (img_width, img_height)) for img in X_train_cv]) #resize all the images so that they all have the same shape
    X_valid_cv = np.array([np.resize(img, (img_width, img_height)) for img in X_valid_cv]) #resize all the images so that they all have the same shape

    X_train_cv = np.array([img.reshape(input_shape) for img in X_train_cv])#reshape the images to input_shape instead of 2d nparray (required by the flow function below)
    X_valid_cv = np.array([img.reshape(input_shape) for img in X_valid_cv])#reshape the images to input_shape instead of 2d nparray (required by the flow function below)

    train_generator = train_datagen.flow(X_train_cv, y_train_cv, batch_size = batch_size, subset='training') 
    validation_generator_1 = train_datagen.flow(X_train_cv, y_train_cv, batch_size = batch_size, subset='validation')
    validation_generator_2 = test_datagen.flow(X_valid_cv, y_valid_cv, batch_size = batch_size)
    

#test_datagen = ImageDataGenerator(rescale = 1. / 255) 
#  
#train_generator = train_datagen.flow_from_directory(train_data_dir, 
#                              target_size =(img_width, img_height), 
#                     batch_size = batch_size, class_mode ='binary',  color_mode='grayscale', subset='training') # set as training data
#
##for validation in each epoch(one portion of the trainning set is reserved for validation)
#validation_generator_1 = train_datagen.flow_from_directory(train_data_dir, 
#                              target_size =(img_width, img_height), 
#                     batch_size = batch_size, class_mode ='binary',  color_mode='grayscale', subset='validation') # set as validation data 
#
##Real validation set reserved to test the model after the last epoch	  
#validation_generator_2 = test_datagen.flow_from_directory( 
#                                    validation_data_dir, 
#                   target_size =(img_width, img_height), 
#          batch_size = batch_size, class_mode ='binary',  color_mode='grayscale') 
#
#train the model on the batches generated by  datagen,flow  
    history = model.fit_generator(train_generator, 
        steps_per_epoch = nb_train_samples // batch_size, 
        epochs = epochs, validation_data = validation_generator_1, 
        validation_steps = nb_validation_samples // batch_size) 

    y_predicted = model.predict_generator(generator= validation_generator_2, steps = len(validation_generator_2))
    print(validation_generator_2)
    y_test = validation_generator_2.y #gives true labels

    y_predicted = (y_predicted > 0.5) #gives predicted labels

    accuracy = accuracy_score(y_test, y_predicted)
    cm = confusion_matrix(y_test, y_predicted)

    print("Accuracy for CNN with network configuration: %s", accuracy)
    print("\n Confusion Matrix: \n", cm)
    runtime = time.time() - start_time
    print("\n-----------------End MLP Classification. Running time: ", runtime, " seconds-------------------------\n")
  
#model.save_weights('model_saved.h5')

#train_acc = history.history['acc']
#test_acc = history.history['val_acc']
#epochs = range (1, len(train_acc) + 1)
#plt.figure()
#plt.plot(epochs, train_acc, label='train accuracy')
#plt.plot(epochs, test_acc, label='test accuracy')
#plt.title('train test accuracy')
#plt.legend()
#plt.grid()
#plt.show()


#plt.savefig('accuracy_project3.png')




# importing libraries
from PIL import Image
import numpy as np
from matplotlib import pyplot as plt
from sklearn.model_selection import train_test_split, StratifiedKFold
from keras.preprocessing.image import ImageDataGenerator 
from keras.models import Sequential 
from keras.layers import Conv2D, MaxPooling2D 
from keras.layers import Activation, Dropout, Flatten, Dense 
from keras import backend as K 
  
  
img_width, img_height = 224, 224
  
train_data_dir = 'v_data/train'
validation_data_dir = 'v_data/test'
nb_train_samples = 400 
nb_validation_samples = 100
epochs = 150
batch_size = 30
num_folds = 10
  
if K.image_data_format() == 'channels_first': 
    input_shape = (3, img_width, img_height) 
else: 
    input_shape = (img_width, img_height, 3) 
  
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
                     optimizer ='rmsprop', 
                   metrics =['accuracy']) 
  
train_datagen = ImageDataGenerator( 
                rescale = 1. / 255, 
                 shear_range = 0.2, 
                  zoom_range = 0.2, 
            horizontal_flip = True) 
  
# test_datagen = ImageDataGenerator(rescale = 1. / 255) 
  

X = []
Y = []
for i in range(1, 201):
    X.append(np.array(Image.open('v_data/train/cars/' + str(i) + '.jpg')))
    Y.append(1)
    X.append(np.array(Image.open('v_data/train/planes/' + str(i) + '.jpg')))
    Y.append(0)
for i in range(1, 51):
    X.append(np.array(Image.open('v_data/test/cars/' + str(i) + '.jpg')))
    Y.append(1)
    X.append(np.array(Image.open('v_data/test/planes/' + str(i) + '.jpg')))
    Y.append(0)

X = np.array(X)
Y = np.array(Y)

folds = list(StratifiedKFold(n_splits=num_folds, shuffle=True, random_state=1).split(X, Y))


for j, (train_idx, val_idx) in enumerate(folds):
    
    print '\nFold ',j
    X_train_cv = X[train_idx]
    y_train_cv = Y[train_idx]
    X_valid_cv = X[val_idx]
    y_valid_cv= Y[val_idx]

    train_generator = train_datagen.flow(X_train_cv, y_train_cv, batch_size = batch_size) 


    #train the model on the batches generated by  datagen,flow  
    history = model.fit_generator(train_generator, 
        steps_per_epoch = len(X_train_cv) // batch_size, 
        epochs = epochs, validation_data = (X_valid_cv, y_valid_cv), 
        validation_steps = len(X_valid_cv) // batch_size) 
    
    #model.save_weights('model_saved.h5')

    train_acc = history.history['acc']
    test_acc = history.history['val_acc']
    tmp_epochs = range (1, len(train_acc) + 1)
    plt.figure()
    plt.plot(tmp_epochs, train_acc, label='train accuracy')
    plt.plot(tmp_epochs, test_acc, label='test accuracy')
    plt.title('train test accuracy')
    plt.legend()
    plt.grid()
    #plt.show()
    plt.savefig('accuracy_fold' + str(j)+ '.png')

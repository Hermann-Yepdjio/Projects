from SVM_Experiment1 import *
import pandas as pd
import numpy as np
import librosa
import librosa.display
import matplotlib.pyplot as plt
import matplotlib
import pywt
import pywt.data


# Notification
import time

# File IO
import h5py
import os

# Deep learning
# Keras-related imports
from keras.models import Sequential
from keras.layers import Dense, Dropout, Activation, Flatten, LSTM
from keras.layers import Convolution1D, MaxPooling2D, Convolution2D
from keras import backend as K
# K.set_image_dim_ordering('th')
from keras.callbacks import ModelCheckpoint
from keras.callbacks import RemoteMonitor
from keras.callbacks import EarlyStopping
from keras.models import load_model
from keras.layers import Conv2D, MaxPooling2D


# Result processing
from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix
from sklearn.utils.multiclass import unique_labels
from sklearn.metrics import f1_score

## Parameterise feature transform here. The default is to use log-mel features, but the user may implement any choice
## or use the raw time-series as preferred
#
#feature_type = 'dwt'  # Select from {'log-mel', 'mfcc', 'dwt'}
#
#
## Select the root of the processed 1 second audio chunks, where votes have been aggregated already
## This path is relative, and should by default point to the correct location
#
#wav_root = '/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #2 (Mosquitos Audio Samples)/Zooniverse_audio_1sec/audio_1sec/'
#
#
#df = pd.read_csv('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #2 (Mosquitos Audio Samples)/Zooniverse_audio_1sec/audio_1sec.csv',  header=None, names=["path", "yes", "no", "not_sure","subject_set"])
#df['path'] = df['path'].astype(str) + '.wav'
#
## Create the y vector by treating 'not_sure' as 0.5, 'yes' as 1.0, 'no' as 0.0 and average
#df['res'] = (df['yes'].astype(int)*1 + df['not_sure'].astype(int)*0.5) / (df['yes'] + df['no'] + df['not_sure']) >= 0.5
#total_audio_n = len(df.index.values.tolist())
#
#y = np.zeros((total_audio_n, 2))
#
#y[:,1] = np.array(np.array(df['res']).astype(int)).astype(int)
#y[:,0] = 1-y[:,1].astype(int)
#
#
#wav_path = wav_root + df["path"]
#
## Initialise empty X matrix
#wav, fs = librosa.load(wav_path.iloc[0],sr=None)  # Load one spectrogram file to calculate dimensions of entire set
#
#cA, cD = pywt.dwt(wav, 'db3')
#
#print (len(cA), len(cD))
#
#approx_data = np.zeros((total_audio_n, len(cA)))
#detailed_data = np.zeros((total_audio_n, len(cD)))
#
#start_time = time.time()
#for i in range(0, total_audio_n):
#    # Create X matrix
#    audio_file, fs = librosa.load(wav_path.loc[i],sr=None)
#    cA, cD = pywt.dwt(audio_file, 'db3')
#    approx_data[i] = cA
#    detailed_data[i] = cD
#
#    if i%1000 == 0:
#        time_used = time.time()-start_time
#        time_total = time_used * total_audio_n / (i+1)
#
#        print('Iteration', str(i), "%i"%(time_used), "%i"%(time_total))
#
#
## Save files
#file_names = ['data_dwt_approx_not_sure_single_into_0_5.h5', 'data_dwt_detailed_not_sure_single_into_0_5.h5', 'label_dwt_not_sure_single_into_0_5.h5']
#
#for file_name in file_names:
#    if not os.path.isfile('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #2 (Mosquitos Audio Samples)/Zooniverse_audio_1sec/Extracted Features/Wavelet/db3/proc_data/' + file_name):
#        hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #2 (Mosquitos Audio Samples)/Zooniverse_audio_1sec/Extracted Features/Wavelet/db3/proc_data/data_' + feature_type + '_approx_not_sure_single_into_0_5.h5', 'w')
#        hf.create_dataset('../proc_data/data_' + feature_type + '_approx_majority_labels_not_sure_single_into_0_5',
#                          data=approx_data)
#        hf.close()
#
#        hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #2 (Mosquitos Audio Samples)/Zooniverse_audio_1sec/Extracted Features/Wavelet/db3/proc_data/data_' + feature_type + '_detailed_not_sure_single_into_0_5.h5', 'w')
#        hf.create_dataset('../proc_data/data_' + feature_type + '_detailed_majority_labels_not_sure_single_into_0_5',
#                          data=approx_data)
#        hf.close()
#        
#        hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #2 (Mosquitos Audio Samples)/Zooniverse_audio_1sec/Extracted Features/Wavelet/db3/proc_data/label_' + feature_type + '_not_sure_single_into_0_5.h5', 'w')
#        hf.create_dataset('../proc_data/label_' + feature_type + '_majority_labels_not_sure_single_into_0_5', data=y)
#        hf.close()

# Load files
feature_type = 'dwt'
save_name = 'not_sure_single_into_0_5'

hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #2 (Mosquitos Audio Samples)/Zooniverse_audio_1sec/Extracted Features/Wavelet/db3/proc_data/data_' + feature_type + '_detailed_' + save_name + '.h5', 'r')
spec_matrix_read = np.array(hf.get('../proc_data/data_' + feature_type + '_detailed' + '_majority_labels_' + save_name))
hf.close()

hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #2 (Mosquitos Audio Samples)/Zooniverse_audio_1sec/Extracted Features/Wavelet/db3/proc_data/label_' + feature_type + '_' + save_name + '.h5', 'r')
y = np.array(hf.get('../proc_data/label_' + feature_type + '_majority_labels_' + save_name))
hf.close()

# Convert to dB

spec_matrix_db = spec_matrix_read #np.zeros_like(spec_matrix_read)

for i, spec in enumerate(spec_matrix_read):
    spec_matrix_db[i] = librosa.power_to_db(spec,ref=np.max)

pred_list = []
y_test_list = []

list = []
print(np.amin(spec_matrix_db), np.amax(spec_matrix_db))
print(spec_matrix_read.shape)
for item in spec_matrix_db:
    list.append(np.histogram(item, 900, (0, np.amax(spec_matrix_db)))[0])
#    list.append(np.histogram(item, 900, (np.amin(spec_matrix_db) + 15, np.amax(spec_matrix_db) - 15))[0])

spec_matrix_db= np.array(list)

#print(spec_matrix_db.shape)

for random_state in [10,20,30,40,50,60,70,80,90,100]:

    X_train, X_test, y_train, y_test = train_test_split(spec_matrix_db, np.array(y), test_size=0.33,
                                                        random_state=random_state)
    # Normalise by statistics of training data
#    X_train= (X_train - np.amin(X_train))/(np.amax(X_train) - np.amin(X_train)) 
#    X_train= (X_train - np.mean(X_train))/np.std(X_train)
##    X_test= (X_test - np.amin(X_test))/(np.amax(X_test) - np.amin(X_test)) 
#    X_test= (X_test - np.mean(X_train))/np.std(X_train)
    print(X_train.shape)
#    X_train = X_train.reshape(X_train.shape[0], 1, 30, 30)#X_train.shape[1], X_train.shape[2])
#    X_test= X_test.reshape(X_test.shape[0], 1, 30, 30)#X_test.shape[1], X_test.shape[2])
#    (Acc, Time) = KNNClassification(X_train, y_train[:, 0], X_test, y_test[:, 0])
    (Acc, Time) = SVM(X_train, y_train[:, 0], X_test, y_test[:, 0])

    ################################ CONVOLUTIONAL NEURAL NETWORK ################################
    ## NN parameters
#    class_weight = {0: 1.,
#                    1: 10.,
#                    }
#    input_shape = (1, X_train.shape[2], X_train.shape[-1])
#
#    model = Sequential()
#    n_dense = 128
#    nb_classes = 2
#    # number of convolutional filters
#    nb_conv_filters = 32
#    # num_hidden = 236
#    nb_conv_filters_2 = 64
#    convout1 = Activation('relu')
#    convout2 = Activation('relu')
#
#    model.add(Conv2D(nb_conv_filters, kernel_size = (3,3),
#         activation = 'relu', padding = 'valid', strides = 1,
#         input_shape = input_shape, data_format='channels_first'))
#
#    model.add(MaxPooling2D(pool_size=(2, 2)))
#    model.add(Conv2D(nb_conv_filters_2, kernel_size = (3,3),
#         activation = 'relu', padding = 'valid'))
#    model.add(MaxPooling2D(pool_size=(2, 2)))
#
#    # model.add(Conv2D(nb_conv_filters_2, kernel_size = (5,5),
#    #      activation = 'relu', padding = 'valid'))
#    # model.add(MaxPooling2D(pool_size=(2, 2)))
#
#    model.add(Dropout(0.2))
#    model.add(Flatten())
#    # Shared between MLP and CNN:
#    model.add(Dense(n_dense, activation='relu'))
#    model.add(Dense(nb_classes, activation='softmax'))
#    model.compile(loss='categorical_crossentropy',
#                    optimizer='adadelta',
#                    metrics=['accuracy'])
#
#    model.fit(x=X_train, y=y_train, batch_size=None, epochs=3, verbose=1, callbacks=None, validation_split=0.0,
#              validation_data=None,
#              shuffle=True, class_weight=class_weight, sample_weight=None, initial_epoch=0,
#              steps_per_epoch=None, validation_steps=None)
#
#
#    loss, acc = model.evaluate(x=X_test, y=y_test, batch_size=None, verbose=0, sample_weight=None, steps=None)
#    pred = model.predict(X_test)
#    plt.hist(pred[:,1]) # Optional: visualise histogram of labels
##     plt.show()
#    print(random_state, loss, acc)
#
#    pred_list.append(pred)  # Collect y_test to report classification performance
#    y_test_list.append(y_test)  # Collect y_test to report classification performance

#print(wav.shape, fs)
#print(len(cA))
#print(np.array(y))

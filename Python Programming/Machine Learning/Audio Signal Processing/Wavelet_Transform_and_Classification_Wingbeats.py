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
import keras
from keras.models import Sequential
from keras.layers import Dense, Dropout, Activation, Flatten, LSTM
from keras.layers import Convolution1D, MaxPooling2D, Convolution2D
from keras import backend as K
# K.set_image_dim_ordering('th')
from keras.callbacks import ModelCheckpoint
from keras.optimizers import SGD
from keras.callbacks import RemoteMonitor
from keras.callbacks import EarlyStopping
from keras.models import load_model
from keras.layers import Conv2D, Conv1D, MaxPooling1D, GlobalAveragePooling1D
from keras.layers.normalization import BatchNormalization
from keras.callbacks import ModelCheckpoint, EarlyStopping, ReduceLROnPlateau, CSVLogger


# Result processing
from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix
from sklearn.utils.multiclass import unique_labels
from sklearn.metrics import f1_score
import skimage.io
import pylab
from skimage.feature import local_binary_pattern
import cv2
from scipy import ndimage

# For plotting headlessly
from matplotlib.backends.backend_agg import FigureCanvasAgg as FigureCanvas

np. set_printoptions(threshold=np. inf)

#return indexes for num_from_each_cat TN, FN, FP, TP classified data so we can see what they look like (1 means noise, 0 means mosquito
def get_classified_samples (num_from_each_cat, y_test, y_predicted):
    tn_idxes = [idx for idx, elt in enumerate(y_test) if elt == 0 and y_predicted[idx] == 0]
    fn_idxes = [idx for idx, elt in enumerate(y_test) if elt == 1 and y_predicted[idx] == 0]
    fp_idxes = [idx for idx, elt in enumerate(y_test) if elt == 0 and y_predicted[idx] == 1]
    tp_idxes = [idx for idx, elt in enumerate(y_test) if elt == 1 and y_predicted[idx] == 1]

    return tn_idxes[0:num_from_each_cat], fn_idxes[0:num_from_each_cat], fp_idxes[0:num_from_each_cat], tp_idxes[0:num_from_each_cat]

#return some TN, FN, FP, TP  feature maps
def get_feature_maps(tn_idxes, fn_idxes, fp_idxes, tp_idxes, X_test, model, new_output_layer_idx):
    intermediate_layer_model = keras.Model(inputs=model.input, outputs=model.layers[new_output_layer_idx].output)
    print("Model Layers:", model.layers)
    tn_list, fn_list, fp_list, tp_list = [], [], [], []
    print("\n\nlen tn_idx:", len(tn_idxes))
    for i in range (len(tn_idxes)):
        tn_list.append(intermediate_layer_model.predict(X_test[tn_idxes[i]].reshape(1,1,X_test.shape[-1])))
        fn_list.append(intermediate_layer_model.predict(X_test[fn_idxes[i]].reshape(1,1,X_test.shape[-1])))
        fp_list.append(intermediate_layer_model.predict(X_test[fp_idxes[i]].reshape(1,1,X_test.shape[-1])))
        tp_list.append(intermediate_layer_model.predict(X_test[tp_idxes[i]].reshape(1,1,X_test.shape[-1])))

    return np.array(tn_list), np.array(fn_list), np.array(fp_list), np.array(tp_list)


#another way of computing feature map using a Keras function, in [x, 0], 0 means output in test mode, 1 means in train mode
#    get_3rd_layer_output = K.function([model.layers[0].input, K.learning_phase()],
#                                  [model.layers[3].output])
#
#    # output in test mode = 0
#    layer_output = get_3rd_layer_output([x, 0])[0]
#
#    # output in train mode = 1
#    layer_output = get_3rd_layer_output([x, 1])[0]


def get_data(target_names):

        # Read about 20K of recs from every species
        # Note: All wav files must be the same sampling frequency and number of datapoints!

        X = []                    # holds all data raw
        Z = []                    # holds all frequency domain data
        y = []                    # holds all class labels

        filenames = []            # holds all the file names
        target_count = []         # holds the counts in a class

        feature_type = 'dwt'  # Select from {'log-mel', 'mfcc', 'dwt'}

        start_time = time.time()
        for i, target in enumerate(target_names):
            target_count.append(0)  # initialize target count
            path='/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Wingbeats/' + target + '/'    # assemble path string

            for [root, dirs, files] in os.walk(path, topdown=False):
                for filename in files:
                    name,ext = os.path.splitext(filename)
                    if ext=='.wav':
                        name=os.path.join(root, filename)
                        data, fs = librosa.load(name, sr=None) #data, fs = sf.read(name)
#                        stft = librosa.stft(data)
                        mel_spec = librosa.feature.melspectrogram(y=data, sr=fs)
#                        chroma_cqt = librosa.feature.chroma_cqt(y=data, sr=fs, fmin=30)
#                        D = librosa.stft(y=data)
#                        S_db = librosa.amplitude_to_db(np.abs(D), ref=np.max)
                        Z.append(mel_spec)
#                        cA, cD = pywt.dwt(data, 'db1')
#                        X.append(data)
#                        Z.append(cA)
                        y.append(i)
                        filenames.append(name)
                        target_count[i]+=1
                        if target_count[i]>20000:
                                break
                        if target_count[i]%1000 == 0:
                            time_used = time.time()-start_time

                            print('Iteration', str(i), "%i"%(time_used))
                            
            print (target,'#recs = ', target_count[i])

#        X = np.vstack(X)
        Z = np.array(Z)
        y = np.hstack(y)

#        X = X.astype("float32")
        Z = Z.astype("float32")
        print ("")
        print ("Total dataset size:")
#        print ('# of classes: %d' % len(np.unique(y)))
        print ('total dataset size: %d' % Z.shape[0])
        print ('Sampling frequency = %d Hz' % fs)
        print ("n_samples: %d" % Z.shape[1])
        print ("duration (sec): %f" % (Z.shape[1]/fs))
        print ("\nshape of dataset", Z.shape) 

        
        return X, Z, y

## Save files
def save_files(raw_data, freq_data, labels):
        
#        hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/raw/raw_data.h5', 'w')
#        hf.create_dataset('raw_data', data=raw_data)
#        hf.close()

#        hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/stft/stft_data.h5', 'w')
#        hf.create_dataset('stft_data', data=freq_data)
#        hf.close()
#
#        hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/stft/labels.h5', 'w')
#        hf.create_dataset('labels', data=labels)
#        hf.close()

        hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/mel_spec/mel_log_data.h5', 'w')
        hf.create_dataset('mel_log_data', data=freq_data)
        hf.close()

        
        

# Load files
def load_files():
#    hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/raw/raw_data.h5', 'r')
#    spec_matrix_read = np.array(hf.get('raw_data'))
#    hf.close()
#
#    hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/raw/labels.h5', 'r')
#    y = np.array(hf.get('labels'))
#    hf.close()

#    hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/db1/db1_data.h5', 'r')
#    spec_matrix_read = np.array(hf.get('db1_data_cD'))
#    hf.close()

#    hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/db1/db1_data_cA.h5', 'r')
#
#    spec_matrix_read = np.concatenate((spec_matrix_read, np.array(hf.get('db1_data_cA'))), axis = 1)
#    #spec_matrix_read_2 = np.array(hf.get('db1_data_cA'))
#    hf.close()

#    hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/db1/labels.h5', 'r')
#    y = np.array(hf.get('labels'))
#    hf.close()


#    hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/mel_spec/mel_log_data.h5', 'r')
#    spec_matrix_read = np.array(hf.get('mel_log_data'))
#    hf.close()

    hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/mel_spec/mel_lbp_data.h5', 'r')
    spec_matrix_read = np.array(hf.get('mel_lbp_data')) #np.concatenate((spec_matrix_read, np.array(hf.get('mel_lbp_data'))), axis=1)
    hf.close()

    hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/mel_spec/labels.h5', 'r')
    y = np.array(hf.get('labels'))
    hf.close()

#    hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/chroma/chroma_data.h5', 'r')
#    spec_matrix_read = np.array(hf.get('chroma_data'))
#    hf.close()
#
#    hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/chroma/labels.h5', 'r')
#    y = np.array(hf.get('labels'))
#    hf.close()

#    hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/stft/stft_data.h5', 'r')
#    spec_matrix_read = np.array(hf.get('stft_data'))
#    hf.close()
#
#    hf = h5py.File('/media/hermann/Tonpi/tonpi/Collegecourses/PSU/Graduate-School/Thesis/Dataset/Dataset #3 (Mosquito Wingbeats Recording (Kaggle))/archive/Extracted_Features/stft/labels.h5', 'r')
#    y = np.array(hf.get('labels'))
#    hf.close()
    


    #spec_matrix_read = db1_data
    #y = labels

    # Convert to dB

    spec_matrix_db = spec_matrix_read #np.zeros_like(spec_matrix_read)

#    for i, spec in enumerate(spec_matrix_read):
#        spec_matrix_db[i] = librosa.power_to_db(spec,ref=np.max)

#    for i, spec in enumerate(spec_matrix_read):
#        spec_matrix_db[i] = librosa.amplitude_to_db(np.abs(spec), ref=np.max)

#    pred_list = []
#    y_test_list = []

    print("Spec_matrix_db min and max:", np.amin(spec_matrix_db), np.amax(spec_matrix_db))
    print("Spec_matrix_db shape:", spec_matrix_db.shape)
    return spec_matrix_db, y


def scale_minmax(X, min=0.0, max=1.0):
    X_std = (X - X.min()) / (X.max() - X.min())
    X_scaled = X_std * (max - min) + min
    return X_scaled

def spectrogram_image(mels, out):
    # use log-melspectrogram
#    mels = librosa.feature.melspectrogram(y=y, sr=sr, n_mels=n_mels,
#                                            n_fft=hop_length*2, hop_length=hop_length)
#    mels = numpy.log(mels + 1e-9) # add small number to avoid log(0)

    # min-max scale to fit inside 8-bit range
    img = scale_minmax(mels, 0, 255).astype(np.uint8)
    img = np.flip(img, axis=0) # put low frequencies at the bottom in image
    img = 255-img # invert. make black==more energy

    # save as PNG
    skimage.io.imsave(out, img)

#plot some samples
def plot_time_series(spec_matrix_db, y):
    fig, ax = plt.subplots(nrows=6, sharex=True, sharey=True) #nrows = 6 because we want 6 plots
    for i in range(0, 6):
        index = np.nonzero(y == i)[0][0] #return index of first occurence of number i in y
        librosa.display.waveplot(spec_matrix_db[index], sr=8000, ax=ax[i]) #8000 comes from printing sr (sample rate) in the get_data method
        ax[i].set(title='class ' + str(i))
        ax[i].label_outer()
    plt.show()

def plot_mel_spec(spec_matrix_db):
    output = spec_matrix_db
    for i in range(0, 1):#spec_matrix_db.shape[0]):
        fig = plt.Figure()
        canvas = FigureCanvas(fig)
        ax = fig.add_subplot(111)
        img = librosa.display.specshow(spec_matrix_db[i], ax=ax)
#        output[i] = img.get_array().reshape(img._meshHeight, img._meshWidth)
#        output[i] = scale_minmax(output[i], 0, 255)
#        plt.specgram(spec_matrix_db[i], Fs=8000)
        plt.show()
        fig.savefig('spec.png', bbox_inches='tight', pad_inches=0)
    return output

def MEL_LBP(spec_matrix_db):
    mel_lbp_hists = []
    for i in range(0, spec_matrix_db.shape[0]):
        fig = plt.Figure()
        canvas = FigureCanvas(fig)
        ax = fig.add_subplot(111)
        img = librosa.display.specshow(spec_matrix_db[i], ax=ax)
        fig.savefig('spec_2.png', bbox_inches='tight', pad_inches=0)
#        lbp_hist = LBP('spec.png', 512)
        lbp_hist = LOG('spec_2.png', 512)
#        print(lbp_hist)
        mel_lbp_hists.append(lbp_hist)
    return np.array(mel_lbp_hists)

def LBP(path, n_bins):
    radius = 3
    n_points = 8 * radius
    METHOD = 'uniform'
    eps = 1e-7
    img_bgr = cv2.imread(path, 1)
    img_gray = cv2.cvtColor(img_bgr, cv2.COLOR_BGR2GRAY)
    img_lbp = local_binary_pattern(img_gray, n_points, radius, METHOD)
    (hist, _) = np.histogram(img_lbp.ravel(), bins=n_bins)# range=(0, numPoints + 2))
    # normalize the histogram
    hist = hist.astype("float")
    hist /= (hist.sum() + eps)

    return hist

def LOG(path, n_bins):
    eps = 1e-7
    img_bgr = cv2.imread(path, 1)
    img_gray = cv2.cvtColor(img_bgr, cv2.COLOR_BGR2GRAY)
    img_log = ndimage.gaussian_laplace(img_gray, sigma=3)
    (hist, _) = np.histogram(img_log.ravel(), bins=n_bins)# range=(0, numPoints + 2))
    # normalize the histogram
    hist = hist.astype("float")
    hist /= (hist.sum() + eps)

    return hist


def train_classify(spec_matrix_db, y):
    for random_state in [2021]: #[10,20,30,40,50,60,70,80,90,100]:

        X_train, X_test, y_train, y_test = train_test_split(spec_matrix_db, np.array(y), test_size=0.20,
                                                            random_state=random_state)
#        # Normalise by statistics of training data
        X_train= (X_train - np.amin(X_train))/(np.amax(X_train) - np.amin(X_train)) 
#        X_train= (X_train - np.mean(X_train))/np.std(X_train)
        X_test= (X_test - np.amin(X_test))/(np.amax(X_test) - np.amin(X_test)) 
#        X_test= (X_test - np.mean(X_train))/np.std(X_train)
        print("X_train Sha[e:", X_train.shape)
#        X_train = X_train.reshape(X_train.shape[0], 1, X_train.shape[1])#X_train.shape[1], X_train.shape[2])
#        X_test= X_test.reshape(X_test.shape[0], 1, X_test.shape[1])#X_test.shape[1], X_test.shape[2])

        # Convert label to onehot
        y_train = keras.utils.to_categorical(y_train, num_classes=6)
        y_test = keras.utils.to_categorical(y_test, num_classes=6)

        #KNN and SVM
#        X_train = X_train.reshape(X_train.shape[0], X_train.shape[1] * X_train.shape[2]) # 1, X_train.shape[1], X_train.shape[2])
#        X_test= X_test.reshape(X_test.shape[0], X_test.shape[1] * X_test.shape[2]) #1, X_test.shape[1], X_test.shape[2])
        
#        #CNN 1D Mel_Spec
#        X_train = X_train.reshape(X_train.shape[0], X_train.shape[1] * X_train.shape[2], 1) # 1, X_train.shape[1], X_train.shape[2])
#        X_test= X_test.reshape(X_test.shape[0], X_test.shape[1] * X_test.shape[2], 1) #1, X_test.shape[1], X_test.shape[2])

        #CNN 1D Wavelet
        X_train = X_train.reshape(X_train.shape[0], X_train.shape[1], 1) 
        X_test= X_test.reshape(X_test.shape[0], X_test.shape[1], 1)

        #CNN 2D
#        X_train = X_train.reshape(X_train.shape[0], X_train.shape[1], X_train.shape[2])
#        X_test= X_test.reshape(X_test.shape[0], X_test.shape[1], X_test.shape[2])

#        (Acc, Time) = KNNClassification(X_train, y_train, X_test, y_test)
#        (Acc, Time) = SVM(X_train, y_train, X_test, y_test)
#        (Acc, Time) = RF(X_train, y_train, X_test, y_test)
#        (Acc, Time) = MLP(X_train, y_train, X_test, y_test)

#        ################################ CONVOLUTIONAL NEURAL NETWORK ################################
#        ## NN parameters
#        class_weight = {0: 1.,
#                        1: 1.,
#                        2: 1.,
#                        3: 1.,
#                        4: 1.,
#                        5: 1.,
#                        }
##        input_shape = (X_train.shape[1], X_train.shape[0],  X_train.shape[-1]) #1D
#        input_shape = (1, X_train.shape[2], X_train.shape[-1]) #2D
        input_shape = (X_train.shape[1], X_train.shape[-1]) #1D Kaggle Mel
        # Build the Neural Network
        model = Sequential()

        model.add(Conv1D(16, 3, activation='relu', input_shape=input_shape))
        model.add(Conv1D(16, 3, activation='relu'))
        model.add(BatchNormalization())

        model.add(Conv1D(32, 3, activation='relu'))
        model.add(Conv1D(32, 3, activation='relu'))
        model.add(BatchNormalization())

        model.add(MaxPooling1D(2))
        model.add(Conv1D(64, 3, activation='relu'))
        model.add(Conv1D(64, 3, activation='relu'))
        model.add(BatchNormalization())

        model.add(MaxPooling1D(2))
        model.add(Conv1D(128, 3, activation='relu'))
        model.add(Conv1D(128, 3, activation='relu'))
        model.add(BatchNormalization())

        model.add(MaxPooling1D(2))
        model.add(Conv1D(256, 3, activation='relu'))
        model.add(Conv1D(256, 3, activation='relu'))
        model.add(BatchNormalization())
        model.add(GlobalAveragePooling1D())

        model.add(Dropout(0.5))
        model.add(Dense(6, activation='softmax'))

        model.compile(loss='categorical_crossentropy',
                      optimizer='adam',
                      metrics=['accuracy'])

        # Plot model
        # from keras.utils import plot_model
        # plot_model(model, to_file='model.png')


        model_name = 'deep_1'
        top_weights_path = 'model_' + str(model_name) + '.h5'

        callbacks_list = [ModelCheckpoint(top_weights_path, monitor = 'val_acc', verbose = 1, save_best_only = True, save_weights_only = True), 
            EarlyStopping(monitor = 'val_acc', patience = 6, verbose = 0),
            ReduceLROnPlateau(monitor = 'val_acc', factor = 0.1, patience = 3, verbose = 1),
            CSVLogger('model_' + str(model_name) + '.log')]

        model.fit(X_train, y_train, batch_size=128, epochs=50, validation_data = [X_test, y_test], callbacks = callbacks_list)


        model.load_weights(top_weights_path)
        loss, acc = model.evaluate(X_test, y_test, batch_size=16)
        pred = model.predict(X_test)

        #print('loss', loss)
        print('Test accuracy:', acc)

        pred = (pred > 0.5)
        c_f = confusion_matrix(y_test.argmax(axis=1), pred.argmax(axis=1))

        print(c_f)



#
#        model = Sequential()
#        n_dense = 128
#        nb_classes = 6
#        # number of convolutional filters
#        nb_conv_filters = 32
#        # num_hidden = 236
#        nb_conv_filters_2 = 64
#        convout1 = Activation('relu')
#        convout2 = Activation('relu')
#
#        model.add(Conv2D(nb_conv_filters, kernel_size = (3,3),
#             activation = 'relu', padding = 'valid', strides = 1,
#             input_shape = input_shape, data_format='channels_first'))
#        model.add(MaxPooling2D(pool_size= (2, 2)))
#
##        model.add(Conv1D(nb_conv_filters, kernel_size = (5),
##             activation = 'relu', padding = 'valid', strides =1, data_format='channels_first'))
##        model.add(MaxPooling1D(pool_size=2, data_format='channels_first'))
##
##
##        model.add(Dropout(0.6))
##        model.add(Conv1D(nb_conv_filters_2, kernel_size = (2),
##             activation = 'relu', padding = 'valid', strides =1, data_format='channels_first'))
##        model.add(MaxPooling1D(pool_size=2, data_format='channels_first'))
#        
#        model.add(Dropout(0.2))
#        model.add(Conv2D(nb_conv_filters_2, kernel_size = (5,5),
#              activation = 'relu', padding = 'valid'))
#        model.add(MaxPooling2D(pool_size=(2, 2)))
#
#        model.add(Dropout(0.4))
#        model.add(Flatten())
#        # Shared between MLP and CNN:
#        model.add(Dense(n_dense, activation='relu'))
#        model.add(Dense(nb_classes, activation='softmax'))
#        model.compile(loss='categorical_crossentropy',
#                        optimizer='adam',
#                        metrics=['accuracy'])
#
#        model.fit(x=X_train, y=y_train, batch_size=None, epochs=20, verbose=1, callbacks=None, validation_split=0.1,
#                  shuffle=True, sample_weight=None, initial_epoch=0,
#                  steps_per_epoch=None)
#
#
#        loss, acc = model.evaluate(x=X_test, y=y_test, batch_size=None, verbose=0, sample_weight=None, steps=None)
#        pred = model.predict(X_test)
#        plt.hist(pred[:,1]) # Optional: visualise histogram of labels
#    #     plt.show()
#        print(random_state, loss, acc)
#
#    #    pred_list.append(pred)  # Collect y_test to report classification performance
#    #    y_test_list.append(y_test)  # Collect y_test to report classification performance
#        
#        pred = (pred > 0.5)
#        c_f = confusion_matrix(y_test.argmax(axis=1), pred.argmax(axis=1))
#
#        print(c_f)



    #    tn_idxes, fn_idxes, fp_idxes, tp_idxes = get_classified_samples (5, y_test, np.rint(pred))
    #    tn_list, fn_list, fp_list, tp_list = get_feature_maps(tn_idxes, fn_idxes, fp_idxes, tp_idxes, X_test, model, 3) #index 3 = second maxpooling layer
    #    print(c_f, "\n\n TN samples: ", tn_list, "\n\n FN samples: ", fn_list, "\n\n FP samples: ", fp_list, "\n\n TP samples: ", tp_list)
    #    print(c_f, "\n\n TN samples: ", tn_list.shape, "\n\n FN samples: ", fn_list.shape, "\n\n FP samples: ", fp_list.shape, "\n\n TP samples: ", tp_list.shape)


    #print(wav.shape, fs)
    #print(len(cA))
    #print(np.array(y))

def main():
#    target_names = ['Ae. aegypti', 'Ae. albopictus', 'An. gambiae', 'An. arabiensis', 'C. pipiens', 'C. quinquefasciatus']
#    raw_data, freq_data, labels = get_data(target_names)
#    save_files (raw_data, freq_data, labels)
    spec_matrix_db, y = load_files()
#    plot_time_series(spec_matrix_db, y)
#    mel_lbp_hists = MEL_LBP(spec_matrix_db)
#    save_files ([], mel_lbp_hists, y)
#    spectrogram_image(spec_matrix_db[5], "out.png")
    train_classify(spec_matrix_db, y)
##
if __name__ == '__main__':
    main()

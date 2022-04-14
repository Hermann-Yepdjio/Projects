import pandas as pd
from pathlib import Path
import cv2
from skimage import feature
import numpy as np
import time
import os

path = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/Clean_Segmented/Validate/"
path_2 = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/Clean_Segmented/Train/"

def extract_lbp_features(train_path, valid_path):

    numPoints = 24
    radius = 8
    eps = 1e-7
    #parameters (for hog.compute) that define how many features will be extracted from the input image
#    winSize = (600, 600)
#    blockSize = (16,16)
#    blockStride = (8,8)
#    cellSize = (8,8)
#    nbins = 9
#    derivAperture = 1
#    winSigma = 4.
#    histogramNormType = 0
#    L2HysThreshold = 2.0000000000000001e-01
#    gammaCorrection = 0
#    nlevels = 64
#
#    hog = cv2.HOGDescriptor(winSize,blockSize,blockStride,cellSize,nbins,derivAperture,winSigma,
#                            histogramNormType,L2HysThreshold,gammaCorrection,nlevels)
#    winStride = (8,8)
#    padding = (8,8)
#    locations = ((10,20),)
    valid_set = []
    valid_target_set = []
    t1 = time.time()


    count = 0 #just to know when to append data to the file on disk and free up memory
    v_s = Path(valid_path + "LBP_Validation_Set.npy")
    v_t_s = Path(valid_path + "LBP_Validation_Target_Set.npy")
    t_s = Path(train_path + "LBP_Training_Set.npy")
    t_t_s = Path(train_path + "LBP_Training_target_Set.npy")
    batch_size = 10 #to know how much must be saved in memory before being saved on disk to free up memory

    #delete the .npy files on disk if they exist
    if os.path.exists(v_s):
        os.remove(v_s)
    if os.path.exists(v_t_s):
        os.remove(v_t_s)
    if os.path.exists(t_s):
        os.remove(t_s)
    if os.path.exists(t_t_s):
        os.remove(t_t_s)

    for tmp_path in os.listdir(valid_path + "abnormal"):
        count += 1
        print(valid_path + "abnormal/" + tmp_path)
        image = cv2.imread(valid_path + "abnormal/" + tmp_path, 0)
        lbp = feature.local_binary_pattern(image, numPoints, radius, method="uniform")
        (hist, _) = np.histogram(lbp.ravel(), bins=np.arange(0, numPoints + 3), range=(0, numPoints + 2))
 
	# normalize the histogram
        hist = hist.astype("float")
        hist /= (hist.sum() + eps)

#        hist = np.concatenate(hog.compute(image, winStride, padding, locations))
        valid_set.append(hist)
        valid_target_set.append(1)
        if count == batch_size:
            print("Saving to disk and freeing up memory...")
            with v_s.open('ab') as f:
                for item in valid_set:
                    np.save(f, np.array(item))

            with v_t_s.open('ab') as f:
                for item in valid_target_set:
                    np.save(f, np.array(item))

            valid_set, valid_target_set, count = [], [], 0

    for tmp_path in os.listdir(valid_path + "normal"):
        count += 1
        print(valid_path + "normal/" + tmp_path)
        image = cv2.imread(valid_path + "normal/" + tmp_path, 0)
        lbp = feature.local_binary_pattern(image, numPoints, radius, method="uniform")
        (hist, _) = np.histogram(lbp.ravel(), bins=np.arange(0, numPoints + 3), range=(0, numPoints + 2))

        # normalize the histogram
        hist = hist.astype("float")
        hist /= (hist.sum() + eps)
#        hist = np.concatenate(hog.compute(image, winStride, padding, locations))
        valid_set.append(hist)
        valid_target_set.append(0)
        if count == batch_size:
            print("Saving to disk and freeing up memory...")
            with v_s.open('ab') as f:
                for item in valid_set:
                    np.save(f, np.array(item))

            with v_t_s.open('ab') as f:
                for item in valid_target_set:
                    np.save(f, np.array(item))

            valid_set, valid_target_set, count = [], [], 0

    #saving what was not saved yet (because count didnt reach batch_size)
    with v_s.open('ab') as f:
        for item in valid_set:
            np.save(f, np.array(item))

    with v_t_s.open('ab') as f:
        for item in valid_target_set:
            np.save(f, np.array(item))
    valid_set, valid_target_set, count = [], [], 0  #free up memory and reinitialize the counter

    train_set = []
    train_target_set = []
    for tmp_path in os.listdir(train_path + "abnormal"):
        count += 1
        print(train_path + "abnormal/" + tmp_path)
        image = cv2.imread(train_path + "abnormal/" + tmp_path, 0)
        lbp = feature.local_binary_pattern(image, numPoints, radius, method="uniform")
        (hist, _) = np.histogram(lbp.ravel(), bins=np.arange(0, numPoints + 3), range=(0, numPoints + 2))

        # normalize the histogram
        hist = hist.astype("float")
        hist /= (hist.sum() + eps)
#        hist = np.concatenate(hog.compute(image, winStride, padding, locations))
        train_set.append(hist)
        train_target_set.append(1)
        if count == batch_size:
            print("Saving to disk and freeing up memory...")
            with t_s.open('ab') as f:
                for item in train_set:
                    np.save(f, np.array(item))

            with t_t_s.open('ab') as f:
                for item in train_target_set:
                    np.save(f, np.array(item))

            train_set, train_target_set, count = [], [], 0
        
     
    for tmp_path in os.listdir(train_path + "normal"):
        count += 1
        print(train_path + "normal/" + tmp_path)
        image = cv2.imread(train_path + "normal/" + tmp_path, 0)
        lbp = feature.local_binary_pattern(image, numPoints, radius, method="uniform")
        (hist, _) = np.histogram(lbp.ravel(), bins=np.arange(0, numPoints + 3), range=(0, numPoints + 2))

        # normalize the histogram
        hist = hist.astype("float")
        hist /= (hist.sum() + eps)
        train_set.append(hist)
        train_target_set.append(0)
        if count == batch_size:
            print("Saving to disk and freeing up memory...")
            with t_s.open('ab') as f:
                for item in train_set:
                    np.save(f, np.array(item))

            with t_t_s.open('ab') as f:
                for item in train_target_set:
                    np.save(f, np.array(item))

            train_set, train_target_set, count = [], [], 0


    #saving what was not saved yet (because count didnt reach batch_size)
    with t_s.open('ab') as f:
        for item in train_set:
            np.save(f, np.array(item))

    with t_t_s.open('ab') as f:
        for item in train_target_set:
            np.save(f, np.array(item))
    train_set, train_target_set, count = [], [], 0  #free up memory and reinitialize the counter


    t2 = time.time() - t1

    print("\n\n Time elapsed to extract features from image files: ", t2, "seconds")


def main():
    extract_lbp_features(path_2, path)
#    for p in (os.listdir(path + "Negative")):
#        print(p)


if __name__ == "__main__":
    main()


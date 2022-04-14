import pandas as pd
import cv2
import numpy as np
import time
import os

path = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/CHINA_dataset/Validate/"
path_2 = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/CHINA_dataset/Train/"

def extract_HOG_features(train_path, valid_path):

    #parameters (for hog.compute) that define how many features will be extracted from the input image
    winSize = (600, 600)
    blockSize = (16,16)
    blockStride = (8,8)
    cellSize = (8,8)
    nbins = 9
    derivAperture = 1
    winSigma = 4.
    histogramNormType = 0
    L2HysThreshold = 2.0000000000000001e-01
    gammaCorrection = 0
    nlevels = 64

    hog = cv2.HOGDescriptor(winSize,blockSize,blockStride,cellSize,nbins,derivAperture,winSigma,
                            histogramNormType,L2HysThreshold,gammaCorrection,nlevels)
    winStride = (8,8)
    padding = (8,8)
    locations = ((10,20),)
    valid_set = []
    valid_target_set = []
    t1 = time.time()


    for tmp_path in os.listdir(valid_path + "abnormal"):
     print(valid_path + "abnormal/" + tmp_path)
     image = cv2.imread(valid_path + "abnormal/" + tmp_path, 0)
     hist = np.concatenate(hog.compute(image, winStride, padding, locations))
     valid_set.append(hist)
     valid_target_set.append(1)

    for tmp_path in os.listdir(valid_path + "normal"):
        print(valid_path + "normal/" + tmp_path)
        image = cv2.imread(valid_path + "normal/" + tmp_path, 0)
        hist = np.concatenate(hog.compute(image, winStride, padding, locations))
        valid_set.append(hist)
        valid_target_set.append(0)



    np.save(valid_path + "Validation_Set", np.array(valid_set))
    np.save(valid_path + "Validation_Target_Set", np.array(valid_target_set))


    train_set = []
    train_target_set = []
    count = 0
    for tmp_path in os.listdir(train_path + "abnormal"):
        print(train_path + "abnormal/" + tmp_path)
        image = cv2.imread(train_path + "abnormal/" + tmp_path, 0)
        hist = np.concatenate(hog.compute(image, winStride, padding, locations))
        train_set.append(hist)
        train_target_set.append(1)
        count += 1
     
    for tmp_path in os.listdir(train_path + "normal"):
        print(train_path + "normal/" + tmp_path)
        image = cv2.imread(train_path + "normal/" + tmp_path, 0)
        hist = np.concatenate(hog.compute(image, winStride, padding, locations))
        train_set.append(hist)
        train_target_set.append(0)

    np.save(train_path + "Training_Set", np.array(train_set))
    np.save(train_path + "Training_target_Set", np.array(train_target_set))

    t2 = time.time() - t1

    print("\n\n Time elapsed to extract features from image files: ", t2, "seconds")


def main():
    extract_HOG_features(path_2, path)
#    for p in (os.listdir(path + "Negative")):
#        print(p)


if __name__ == "__main__":
    main()


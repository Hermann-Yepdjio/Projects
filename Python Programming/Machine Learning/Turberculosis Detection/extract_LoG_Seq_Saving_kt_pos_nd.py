import Classify as clsf
import csv
import pandas as pd
from pathlib import Path
import cv2
from skimage import feature
import numpy as np
import time
import os
from Slice_Matrix import *

path = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/Clean_Segmented/Validate/"
path_2 = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/Clean_Segmented/Train/"

def extract_LoG_features(train_path, valid_path, num_r, num_c):
    
    nr = num_r #number of cuts row wise
    nc = num_c #number of cuts column wise
    start_range = 5
    end_range = 45
    num_bins = end_range - start_range   
    eps = 1e-7
    k = 3
    valid_set = []
    valid_target_set = []
    t1 = time.time()


    count = 0 #just to know when to append data to the file on disk and free up memory
    v_s = Path(valid_path + "LoG_Validation_Set.npy")
    v_t_s = Path(valid_path + "LoG_Validation_Target_Set.npy")
    t_s = Path(train_path + "LoG_Training_Set.npy")
#            hist.append(lst_1[2])
    t_t_s = Path(train_path + "LoG_Training_target_Set.npy")
    batch_size = 100 #to know how much must be saved in memory before being saved on disk to free up memory

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

        # Apply Gaussian Blur
        blur = cv2.GaussianBlur(image,(k,k),0)

        # Apply Laplacian operator in some higher datatype
        laplacian = cv2.Laplacian(blur,cv2.CV_64F)

        # But this tends to localize the edge towards the brighter side.
        laplacian1 = laplacian#/laplacian.max()
        hist = []
        chunks = slice_matrix(laplacian, nr, nc)
        for item in chunks:
            (lst_1, _) = np.histogram(item.ravel(), bins=num_bins, range = (start_range, end_range))
            for item_1 in lst_1:
                hist.append(item_1)
        
#        (hist, _) = np.histogram(laplacian1.ravel(), bins = num_bins, range = (start_range, end_range))#laplacian.max() + 1))
 
	# normalize the histogram
        hist = np.array(hist).astype("float")
        hist /= (hist.sum() + eps)
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

        # Apply Gaussian Blur
        blur = cv2.GaussianBlur(image,(k,k),0)
        
        # Apply Laplacian operator in some higher datatype
        laplacian = cv2.Laplacian(blur,cv2.CV_64F)
        
        # But this tends to localize the edge towards the brighter side.
        laplacian1 = laplacian#/laplacian.max()

        hist = []
        chunks = slice_matrix(laplacian, nr, nc)
        for item in chunks:
            (lst_1, _) = np.histogram(item.ravel(), bins=num_bins, range = (start_range, end_range))
            for item_1 in lst_1:
                hist.append(item_1)

#        (hist, _) = np.histogram(laplacian1.ravel(), bins = num_bins, range = (start_range, end_range))#laplacian.max() + 1))
#        (hist, _) = np.histogram(laplacian1.ravel(), bins=np.arange(1, num_bins + 1))

        # normalize the histogram
        hist = np.array(hist).astype("float")
        hist /= (hist.sum() + eps)
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

        # Apply Gaussian Blur
        blur = cv2.GaussianBlur(image,(k,k),0)
        
        # Apply Laplacian operator in some higher datatype
        laplacian = cv2.Laplacian(blur,cv2.CV_64F)
        
        # But this tends to localize the edge towards the brighter side.
        laplacian1 = laplacian#/laplacian.max()

        hist = []
        chunks = slice_matrix(laplacian, nr, nc)
        for item in chunks:
            (lst_1, _) = np.histogram(item.ravel(), bins=num_bins, range = (start_range, end_range))
            for item_1 in lst_1:
                hist.append(item_1)

#        (hist, _) = np.histogram(laplacian1.ravel(), bins = num_bins, range = (start_range, end_range))#laplacian.max() + 1))
#        (hist, _) = np.histogram(laplacian1.ravel(), bins=np.arange(1, num_bins + 1))

        # normalize the histogram
        hist = np.array(hist).astype("float")
        hist /= (hist.sum() + eps)
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

        # Apply Gaussian Blur
        blur = cv2.GaussianBlur(image,(k,k),0)
        
        # Apply Laplacian operator in some higher datatype
        laplacian = cv2.Laplacian(blur,cv2.CV_64F)
        
        # But this tends to localize the edge towards the brighter side.
        laplacian1 = laplacian#/laplacian.max()

        hist = []
        chunks = slice_matrix(laplacian, nr, nc)
        for item in chunks:
            (lst_1, _) = np.histogram(item.ravel(), bins=num_bins, range = (start_range, end_range))
            for item_1 in lst_1:
                hist.append(item_1)

#        (hist, _) = np.histogram(laplacian1.ravel(), bins = num_bins, range = (start_range, end_range ))#laplacian.max() + 1))
#        (hist, _) = np.histogram(laplacian1.ravel(), bins=np.arange(1, num_bins + 1))

        # normalize the histogram
        hist = np.array(hist).astype("float")
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
#    knn_res, mlp_res, mlp_keras_res, svm_res, rf_res, lp_res = [], [], [], [], [], []
    for num_r in range (1, 21):
        knn_sub_res, mlp_sub_res, mlp_keras_sub_res, svm_sub_res, rf_sub_res, lp_sub_res = [], [], [], [], [], []
        for num_c in range (1, 21):
            print( "\n\n-----------------------------------------------------Starting (NR, NC) = (", num_r, ",", num_c, ")------------------------------------------------------\n")
            extract_LoG_features(path_2, path, num_r, num_c)
            print( "\n\n----------------------------------------------------------------Starting Classification-----------------------------------------------------------------\n")
            res = clsf.API(0)
            res_rounded = []
            for item in res:
                res_rounded.append((round(item[0] * 100, 2), round(item[1], 3)))
            knn_sub_res.append(res_rounded[0])
            mlp_sub_res.append(res_rounded[1])
            mlp_keras_sub_res.append(res_rounded[2])
            svm_sub_res.append(res_rounded[3])
            rf_sub_res.append(res_rounded[4])
            lp_sub_res.append(res_rounded[5])
            print("\n\n\n------------------------", res_rounded, "------------------------\n")
            print( "\n\n---------------------------------------------------------------Ending Classification--------------------------------------------------------------------\n")
            print( "\n\n------------------------------------------------Ending (NR, NC) = (", num_r, ",", num_c, ")-------------------------------------------------------------\n")

#        knn_res.append(knn_sub_res)
#        mlp_res.append(mlp_sub_res)
#        mlp_keras_res.append(mlp_keras_sub_res)
#        svm_res.append(svm_sub_res)
#        rf_res.append(rf_sub_res)
#        lp_res.append(lp_sub_res)

        with open("Results/knn.csv","a+") as my_csv:
            csvWriter = csv.writer(my_csv, delimiter=',')
            csvWriter.writerow(knn_sub_res)

        with open("Results/mlp.csv","a+") as my_csv:
            csvWriter = csv.writer(my_csv, delimiter=',')
            csvWriter.writerow(mlp_sub_res)

        with open("Results/mlp_keras.csv","a+") as my_csv:
            csvWriter = csv.writer(my_csv, delimiter=',')
            csvWriter.writerow(mlp_keras_sub_res)
        
        with open("Results/svm.csv","a+") as my_csv:
            csvWriter = csv.writer(my_csv, delimiter=',')
            csvWriter.writerow(svm_sub_res)

        with open("Results/rf.csv","a+") as my_csv:
            csvWriter = csv.writer(my_csv, delimiter=',')
            csvWriter.writerow(rf_sub_res)

        with open("Results/lp.csv","a+") as my_csv:
            csvWriter = csv.writer(my_csv, delimiter=',')
            csvWriter.writerow(lp_sub_res)

#    print("\n\n\n", rf_res, "\n\n\n")
#    for p in (os.listdir(path + "Negative")):
#        print(p)


if __name__ == "__main__":
    main()


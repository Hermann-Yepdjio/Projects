import pandas as pd
from pathlib import Path
import cv2
import numpy as np
import time
import os
from phog import *

path = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/Clean_Segmented/Validate/"
path_2 = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/Clean_Segmented/Train/"

def extract_PHOG_features(train_path, valid_path):

    phog = PHogFeatures()

    valid_set = []
    valid_target_set = []
    t1 = time.time()


    count = 0 #just to know when to append data to the file on disk and free up memory
    v_s = Path(valid_path + "PHOG_Validation_Set.npy")
    v_t_s = Path(valid_path + "PHOG_Validation_Target_Set.npy")
    t_s = Path(train_path + "PHOG_Training_Set.npy")
    t_t_s = Path(train_path + "PHOG_Training_target_Set.npy")
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
        p_hist = phog.get_features(valid_path + "abnormal/" + tmp_path)
        print(valid_path + "abnormal/" + tmp_path, len(p_hist))
        valid_set.append(p_hist)
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
        p_hist = phog.get_features(valid_path + "normal/" + tmp_path)
        print(valid_path + "normal/" + tmp_path, len(p_hist))
        valid_set.append(p_hist)
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
        p_hist = phog.get_features(train_path + "abnormal/" + tmp_path)
        print(train_path + "abnormal/" + tmp_path, len(p_hist))
        train_set.append(p_hist)
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
        p_hist = phog.get_features(train_path + "normal/" + tmp_path)
        print(train_path + "normal/" + tmp_path, len(p_hist))
        train_set.append(p_hist)
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
    extract_PHOG_features(path_2, path)


if __name__ == "__main__":
    main()


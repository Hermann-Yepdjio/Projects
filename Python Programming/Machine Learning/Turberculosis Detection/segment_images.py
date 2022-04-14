from sklearn.cluster import KMeans
from PIL import Image
import numpy as np
import os

def segment(path_1, path_2, path_3):
    
    #segment Train/abnormal/
    for img_path in os.listdir(path_1 + "Train/abnormal/"):
        dot_index = len(img_path) - 4 #to know where to insert substring "_MASK" to obtain the mask corresponding to the image (-4 because it has to be inserted before the dot of .png)
        mask_path = img_path[:dot_index] + "_MASK" + img_path[dot_index:]
        print(path_1 + "Train/abnormal/" + img_path)
        image = Image.open(path_1 + "Train/abnormal/" + img_path)
        image_pix = np.array(image)
        mask = Image.open(path_2 + "Train/abnormal/" + mask_path)
        mask_pix = np.array(mask)
        size = image_pix.shape
#        print(img_path, mask_path)
        for i in range(0, size[0]):
            for j in range(0, size[1]):
                if mask_pix[i][j] == 0:
                    image_pix[i][j] = 0
        new_img = Image.fromarray(image_pix)
        new_img.save(path_3 + "Train/abnormal/" + img_path)
        
    #segment Train/normal/
    for img_path in os.listdir(path_1 + "Train/normal/"):
        dot_index = len(img_path) - 4 #to know where to insert substring "_MASK" to obtain the mask corresponding to the image (-4 because it has to be inserted before the dot of .png)
        mask_path = img_path[:dot_index] + "_MASK" + img_path[dot_index:]
        print(path_1 + "Train/normal/" + img_path)
        image = Image.open(path_1 + "Train/normal/" + img_path)
        image_pix = np.array(image)
        mask = Image.open(path_2 + "Train/normal/" + mask_path)
        mask_pix = np.array(mask)
        size = image_pix.shape
        for i in range(0, size[0]):
            for j in range(0, size[1]):
                if mask_pix[i][j] == 0:
                    image_pix[i][j] = 0
        new_img = Image.fromarray(image_pix)
        new_img.save(path_3 + "Train/normal/" + img_path)
    
    #segment Validata/abnormal/
    for img_path in os.listdir(path_1 + "Validate/abnormal/"):
        dot_index = len(img_path) - 4 #to know where to insert substring "_MASK" to obtain the mask corresponding to the image (-4 because it has to be inserted before the dot of .png)
        mask_path = img_path[:dot_index] + "_MASK" + img_path[dot_index:]
        print(path_1 + "Train/abnormal/" + img_path)
        image = Image.open(path_1 + "Validate/abnormal/" + img_path)
        image_pix = np.array(image)
        mask = Image.open(path_2 + "Validate/abnormal/" + mask_path)
        mask_pix = np.array(mask)
        size = image_pix.shape
        for i in range(0, size[0]):
            for j in range(0, size[1]):
                if mask_pix[i][j] == 0:
                    image_pix[i][j] = 0
        new_img = Image.fromarray(image_pix)
        new_img.save(path_3 + "Validate/abnormal/" + img_path)

    #segment Validate/normal
    for img_path in os.listdir(path_1 + "Validate/normal/"):
        dot_index = len(img_path) - 4 #to know where to insert substring "_MASK" to obtain the mask corresponding to the image (-4 because it has to be inserted before the dot of .png)
        mask_path = img_path[:dot_index] + "_MASK" + img_path[dot_index:]
        print(path_1 + "Train/abnormal/" + img_path)
        image = Image.open(path_1 + "Validate/normal/" + img_path)
        image_pix = np.array(image)
        mask = Image.open(path_2 + "Validate/normal/" + mask_path)
        mask_pix = np.array(mask)
        size = image_pix.shape
        for i in range(0, size[0]):
            for j in range(0, size[1]):
                if mask_pix[i][j] == 0:
                    image_pix[i][j] = 0
        new_img = Image.fromarray(image_pix)
        new_img.save(path_3 + "Validate/normal/" + img_path)

 



def main():
    path_1 = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/CHINA_dataset/"
    path_2 = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/Masks/"
    path_3 = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/Segmented/"
    segment(path_1, path_2, path_3)
if __name__ == "__main__":
    main()

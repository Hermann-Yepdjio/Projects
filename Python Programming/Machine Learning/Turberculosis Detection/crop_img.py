import numpy as np
from PIL import Image
import os

#input pix is a 2d array and size is a tuple for the number of rows and columns in pix
def crop_empty_h_lines(pix):
    tmp = []
    for lst in pix:
        if not all(val == 0 for val in lst):
            tmp.append(lst)
    return tmp

def crop_black_pixels(source, dest, folder):
    for tmp_path in os.listdir(source + folder):
        print(source + folder + tmp_path)
        img = Image.open(source + folder + tmp_path) # Can be many different formats.
        pix = np.array(img)
        new_pix = crop_empty_h_lines(pix)
        new_pix = np.transpose(new_pix)
        new_pix = crop_empty_h_lines(new_pix)
        new_img = Image.fromarray(np.transpose(new_pix))
        new_img.save(dest + folder + tmp_path)


def API():
    source = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/Segmented/"
    dest = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/Clean_Segmented/"
    crop_black_pixels(source, dest, "Train/abnormal/")
    crop_black_pixels(source, dest, "Train/normal/")
    crop_black_pixels(source, dest, "Validate/abnormal/")
    crop_black_pixels(source, dest, "Validate/normal/")


def main():
    API()

if __name__ == "__main__":
    main()

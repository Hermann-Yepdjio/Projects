import numpy as np
import cv2, os 
from PIL import Image
from math import floor
import multiprocessing as mp

source_folder = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/CHINA_dataset/Train/abnormal"
dest_folder = "/media/hermann/Tonpi/tonpi/Collegecourses/CWU/Graduate-School/Thesis/Data Sets/CHINA_dataset/My_segmented/Train/abnormal"
paths_to_images = os.listdir(source_folder)
paths_to_seg_images = os.listdir(dest_folder)

def find_extremes(hull):
    l, r, b, t = 1000, 0, 1000, 0 
    l_coord, r_coord, b_coord, t_coord = [], [], [], []
    for elt in hull:
        if elt[0][0] < l:
            l, l_coord = elt[0][0], elt[0]
        if elt[0][0] > r:
            r, r_coord = elt[0][0], elt[0]
        if elt[0][1] < b:
            b, b_coord = elt[0][1], elt[0]
        if elt[0][1] > t:
            t, t_coord= elt[0][1], elt[0]

    return r - l, t - b, l, r, b, t, l_coord, r_coord, b_coord, t_coord

def compute_area(good_contour_and_extremes):
    return cv2.contourArea(good_contour_and_extremes[0])
 
def extract_lung_area(orig_fname, val, h_proportion, size, pix, good_contours_and_extremes):
    color_contours = (0, 255, 0) # green - color for contours
    color = (255, 255, 255) # blue - color for convex hull
#    img = Image.open(orig_fname) # Can be many different formats.
#    pix = img.load() 
#    size = img.size
    src = cv2.imread(orig_fname, 1) # read input image

    gray = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY) # convert to grayscale
    blur = cv2.blur(gray, (20, 20)) # blur the image
    ret, thresh = cv2.threshold(gray, val, 255, cv2.THRESH_BINARY)

    # Finding contours for the thresholded image
    contours, hierarchy = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)

    #create an empty black image
    drawing = np.zeros((thresh.shape[0], thresh.shape[1], 3), np.uint8)
     
    # find the good countours and their extremes
    for i in range(len(contours)):
        tmp = []
        width, height, l, r, b, t, l_coord, r_coord, b_coord, t_coord = find_extremes(contours[i])
        if(height < (3 * size[1]) / 4 and height > size[1] / h_proportion  and width  < 2 * size[0]/ 3 and width > size[0]/6 and l > size[0] / 25 and r < size[0] - size[0] / 25 and b > size[1] / 25 and t < size[1] - size[1] / 25): # and (contours[i], [l, r, b, t]) not in good_contours_and_extremes:
            counter = 0
            contour_length = len(contours[i])
            for pixel in contours[i]: #just to make sure that blocks of white pixels are not considered
                if pix[int(pixel[0][0]), int(pixel[0][1])] > 220:
                    counter += 1
                if counter > contour_length / 2:
                    return 1

            tmp.append(l)
            tmp.append(r)
            tmp.append(b)
            tmp.append(t)
            good_contours_and_extremes.append((contours[i], tmp)) #hold the good contours and their respective extremes as a tuple for easy sorting later

def segment_image(ctour_1, ctour_2, ext_1, ext_2, save_fname, img, pix, size):
    counter = 0
    target_contours = [ctour_1, ctour_2]
    print("here")
    for contour in target_contours:
        c_y_coords, c_x_min_max_coords = [], [] #to hold all distinct y coordinates of the contour and their corresponding x_min and x_max (as a list of 2 elts) coordinates
        for pixel in contour:
            if pixel[0][1] not in c_y_coords:
                c_y_coords.append(int(pixel[0][1]))
                c_x_min_max_coords.append([pixel[0][0], pixel[0][0]]) # x_min and x_max
            else:
                tmp_index = c_y_coords.index(pixel[0][1])
                if c_x_min_max_coords[tmp_index][0] > pixel[0][0]:
                    c_x_min_max_coords[tmp_index][0] = pixel[0][0]
                elif c_x_min_max_coords[tmp_index][1] < pixel[0][0]:
                    c_x_min_max_coords[tmp_index][1] = pixel[0][0]
        for i in range(0, len(c_y_coords)):#y coordinate of pixel
            if(counter == 0):
                for j in range(ext_1[0], c_x_min_max_coords[i][0]):#x coordinate of pixel
                    pix[j, c_y_coords[i]] = 0
                for j in range(c_x_min_max_coords[i][1], ext_1[1]):#x coordinate of pixel
                    pix[j, c_y_coords[i]] = 0
            else:
                for j in range(ext_2[0], c_x_min_max_coords[i][0]):#x coordinate of pixel
                    pix[j, c_y_coords[i]] = 0
                for j in range(c_x_min_max_coords[i][1], ext_2[1]):#x coordinate of pixel
                    pix[j, c_y_coords[i]] = 0
        counter += 1

    for i in range (0, size[0]):
        for j in range(0, size[1]):
            if not(j > ext_1[2] and j < ext_1[3] and i > ext_1[0] and i < ext_1[1]) and not(j > ext_2[2] and j < ext_2[3] and i > ext_2[0] and i < ext_2[1]):
                pix[i, j] = 0

    img.save(save_fname)

def API(k):

    orig_fname = source_folder + "/" + paths_to_images[k]
    save_fname = dest_folder + "/" + paths_to_images[k]

    if paths_to_images[k] not in paths_to_seg_images:

        img = Image.open(orig_fname) # Can be many different formats.
        pix = img.load()
        size = img.size
        for h_proportion in range(2, 5): #h_proportion is to know how much we should divide the image's height to find the range for the lung height. Variable because in some images the lung height is more  than half the height of the image and in other images it is less than half
            good_contours_and_extremes = [] #hold the good contours and their respective extremes as a tuple for easy sorting later
            for val in range(1, 300 , 1):
                extract_lung_area( orig_fname, val, h_proportion, size, pix, good_contours_and_extremes)
            
            good_contours_and_extremes.sort(key = compute_area, reverse = True) #sort the list by countour area in descending order      
            
            for i in range (0, len(good_contours_and_extremes) - 1):
                for j in range (1, len(good_contours_and_extremes)):

                    diff1 = good_contours_and_extremes[j][1][0] - good_contours_and_extremes[i][1][1] # tmp[4] - tmp[1] 
                    diff2 = good_contours_and_extremes[i][1][0] - good_contours_and_extremes[j][1][1] #tmp[0] - tmp[5]
                    if ((diff1 > size[0] / 15 and diff1 < size[0] / 7) or (diff2 > size[0] / 15 and diff2 < size[0] / 7)):
                        ctour_1 = good_contours_and_extremes[i][0]
                        ctour_2 = good_contours_and_extremes[j][0]
                        ext_1 = good_contours_and_extremes[i][1]
                        ext_2 = good_contours_and_extremes[j][1]
                        segment_image(ctour_1, ctour_2, ext_1, ext_2, save_fname, img, pix, size)
                        print( str(k) + "  good")
                        return 0
        print( str(k) + "  not good")


def main():
    pool = mp.Pool(mp.cpu_count())
    print("number of cores being used :", mp.cpu_count())
    results = pool.map(API, [i for i in range(0, len(paths_to_images))], chunksize=1)
        

if __name__ == "__main__":
    main()  

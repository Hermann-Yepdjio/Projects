import numpy as np
import cv2 
from PIL import Image, ImageEnhance
from math import floor

#find left_most, right_most, bottom_most and top_most pixels of a shape(in an image)
def find_extremes(hull):
    l, r, b, t = 1000, 0, 1000, 0 
    for elt in hull:
        if elt[0][0] < l:
            l = elt[0][0]
        if elt[0][0] >  r:
            r = elt[0][0]
        if elt[0][1] < b:
            b = elt[0][1]
        if elt[0][1] > t:
            t = elt[0][1]

#    print( "\n", r - l, " ", t - b, " \n")

    return r - l, t - b, l, r, b, t

#find smallest and largest elements of a list
def find_s_l(arr):
    smallest, largest = 1000, 0
    for elt in arr:
        if elt < smallest:
            smallest = elt
        if elt > largest:
            largest = elt
    return smallest, largest

img = Image.open('12.jpg') # Can be many different formats.
pix = img.load() 
size = img.size
src = cv2.imread("12_3_clusters_pix_only.jpg", 1) # read input image

gray = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY) # convert to grayscale
blur = cv2.blur(gray, (3, 3)) # blur the image
ret, thresh = cv2.threshold(blur, 100, 255, cv2.THRESH_BINARY)

# Finding contours for the thresholded image
contours, hierarchy = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)

# create hull array for convex hull points
hull = []

print (src.shape)
#calculate points for each contour
for i in range(len(contours)):
    # creating convex hull object for each contour
    hull.append(cv2.convexHull(contours[i], False))

#create an empty black image
drawing = np.zeros((thresh.shape[0], thresh.shape[1], 3), np.uint8)


tmp = []
target_contours = []
 
# draw contours and hull points
for i in range(len(contours)):
    color_contours = (0, 255, 0) # green - color for contours
    color = (255, 255, 255) # blue - color for convex hull
    # draw ith contour
    width, height, l, r, b, t = find_extremes(contours[i])
    if(height < 240 and height > 130  and width  < 125 and width > 65):
        tmp.append(l)
        tmp.append(r)
        tmp.append(b)
        tmp.append(t)
        target_contours.append(contours[i])
       # find_extremes(hull[i])
        cv2.drawContours(drawing, contours, i, color_contours, 1, 8, hierarchy)
        # draw ith convex hull object
        cv2.drawContours(drawing, hull, i, color, 1, 8)

print(len(tmp));


#final_img = cv2.hconcat((np.array(img), drawing))
#cv2.imshow('Final', final_img)

cv2.imshow('drawing',drawing)

cv2.waitKey(0)
counter = 0



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
            for j in range(tmp[0], c_x_min_max_coords[i][0]):#x coordinate of pixel
                pix[j, c_y_coords[i]] = 0
            for j in range(c_x_min_max_coords[i][1], tmp[1]):#x coordinate of pixel
                pix[j, c_y_coords[i]] = 0
        else:
            for j in range(tmp[4], c_x_min_max_coords[i][0]):#x coordinate of pixel
                pix[j, c_y_coords[i]] = 0
            for j in range(c_x_min_max_coords[i][1], tmp[5]):#x coordinate of pixel
                pix[j, c_y_coords[i]] = 0
    counter += 1

for i in range (0, size[0]):#x coordinate of pixel
    for j in range(0, size[1]):#y coordinate of pixel
        if not(i > tmp[0] and i < tmp[1] and j > tmp[2] and j < tmp[3]) and not(i > tmp[4] and i < tmp[5] and j > tmp[6] and j < tmp[7] ):
            pix[i, j] = 0
img.show()
#img.save("12_seg.jpg")
cv2.destroyAllWindows()

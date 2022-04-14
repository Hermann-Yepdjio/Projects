import numpy as np
import cv2 
from PIL import Image
from math import floor

def find_extremes(hull):
    l, r, b, t = 1000, 0, 1000, 0 
    for elt in hull:
        if elt[0][0] < b:
            b = elt[0][0]
        if elt[0][0] >  t:
            t = elt[0][0]
        if elt[0][1] < l:
            l = elt[0][1]
        if elt[0][1] > r:
            r = elt[0][1]

    print( "\n", r - l, " ", t - b, " \n")

    return r - l, t - b, l, r, b, t

img = Image.open('12.jpg') # Can be many different formats.
pix = img.load() 
size = img.size
src = cv2.imread("12_3_clusters_pix_only.jpg", 1) # read input image

gray = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY) # convert to grayscale
blur = cv2.blur(gray, (3, 3)) # blur the image
ret, thresh = cv2.threshold(blur, 50, 255, cv2.THRESH_BINARY)

# Finding contours for the thresholded image
contours, hierarchy = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

# create hull array for convex hull points
hull = []

#print(src[1][1])
# calculate points for each contour
#for i in range(floor(src.shape[0]/10), src.shape[0]):
 #   for j in range(floor(src.shape[1]/2)):
  #      if(src[i][j] == [255, 255, 0]):
   #         hull.append([i, j])

print (src.shape)
#calculate points for each contour
for i in range(len(contours)):
    # creating convex hull object for each contour
    hull.append(cv2.convexHull(contours[i], False))

#create an empty black image
drawing = np.zeros((thresh.shape[0], thresh.shape[1], 3), np.uint8)

#print(contours[21])
#print(hull[21][0][0][0])

tmp = []
 
# draw contours and hull points
for i in range(len(contours)):
    color_contours = (0, 255, 0) # green - color for contours
    color = (255, 255, 255) # blue - color for convex hull
    # draw ith contour
    width, height, l, r, b, t = find_extremes(hull[i])
    if(width < 240 and width > 130  and height  < 125 and height > 75):
        tmp.append(l)
        tmp.append(r)
        tmp.append(b)
        tmp.append(t)
       # find_extremes(hull[i])
        cv2.drawContours(drawing, contours, i, color_contours, 1, 8, hierarchy)
        # draw ith convex hull object
        cv2.drawContours(drawing, hull, i, color, 1, 8)

print(len(tmp));
cv2.imshow('drawing',drawing)

cv2.waitKey(0)

for i in range (0, size[0]):
    for j in range(0, size[1]):
        if not(j > tmp[0] and j < tmp[1] and i > tmp[2] and i < tmp[3]) and not(j > tmp[4] and j < tmp[5] and i > tmp[6] and i < tmp[7] ):
            pix[i, j] = 0

#img.save("29_seg.jpg")
cv2.destroyAllWindows()

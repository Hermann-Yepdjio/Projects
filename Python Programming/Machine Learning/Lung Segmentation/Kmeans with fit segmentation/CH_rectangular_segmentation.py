import numpy as np
import cv2 
from PIL import Image
from math import floor

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
 
def extract_lung_area(orig_fname, clust_fname, cc_fname, save_fname, val):
    img = Image.open(orig_fname) # Can be many different formats.
    pix = img.load() 
    size = img.size
    src = cv2.imread(clust_fname, 1) # read input image

    gray = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY) # convert to grayscale
    blur = cv2.blur(gray, (1, 1)) # blur the image
    ret, thresh = cv2.threshold(blur, val, 255, cv2.THRESH_BINARY)

    # Finding contours for the thresholded image
    contours, hierarchy = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    # create hull array for convex hull points
    hull = []

    #print (src.shape)
    #calculate points for each contour
    for i in range(len(contours)):
        # creating convex hull object for each contour
        hull.append(cv2.convexHull(contours[i], False))
        #hull.append(contours[i])

    #create an empty black image
    drawing = np.zeros((thresh.shape[0], thresh.shape[1], 3), np.uint8)

    tmp = []
    tmp_coords = [] 
    final_hulls = [] 
     
    # draw contours and hull points
    for i in range(len(contours)):
        color_contours = (0, 255, 0) # green - color for contours
        color = (255, 255, 255) # blue - color for convex hull
        # draw ith contour
        width, height, l, r, b, t, l_coord, r_coord, b_coord, t_coord = find_extremes(hull[i])
        if(height < 240 and height > 130  and width  < 125 and width > 65):
            tmp.append(l)
            tmp.append(r)
            tmp.append(b)
            tmp.append(t)
#            tmp_coords.append(l_coord)
#            tmp_coords.append(r_coord)
#            tmp_coords.append(b_coord)
#            tmp_coords.append(t_coord)
            # draw ith contour 
            cv2.drawContours(drawing, contours, i, color_contours, 1, 8, hierarchy)
            # draw ith convex hull object
            cv2.drawContours(drawing, hull, i, color, 1, 8)
            final_hulls.append(hull[i])

    #print(len(tmp));
    #cv2.imshow('drawing',drawing)
    cv2.imwrite(cc_fname, drawing)


    #cv2.waitKey(0)
    
    if(len(tmp) == 8): 
        for i in range (0, size[0]):
            for j in range(0, size[1]):
                if not(j > tmp[2] and j < tmp[3] and i > tmp[0] and i < tmp[1]) and not(j > tmp[6] and j < tmp[7] and i > tmp[4] and i < tmp[5] ):
                    pix[i, j] = 0

#        for elt in final_hulls[0]:
#            if elt[0][0] < tmp_coords[2][0] or elt[0][0] < tmp_coords[3][0]:
#                for i in range (tmp[0], elt[0][0]):
#                    pix[i, int(elt[0][1])] = 0
#
#            elif elt[0][0] > tmp_coords[2][0] or elt[0][0] > tmp_coords[3][0]:
#                for i in range (elt[0][0], tmp[1]):
#                    #print(elt[0][1], i, tmp[1])
#                    pix[i, int(elt[0][1])] = 0
#
#        for elt in final_hulls[1]:
#            if elt[0][0] < tmp_coords[6][0] or elt[0][0] < tmp_coords[7][0]:
#                for i in range (tmp[4], elt[0][0]):
#                    pix[i, int(elt[0][1])] = 0
#
#            elif elt[0][0] > tmp_coords[6][0] or elt[0][0] > tmp_coords[7][0]:
#                for i in range (elt[0][0], tmp[5]):
#                    pix[i, int(elt[0][1])] = 0
#


        img.save(save_fname)
    #cv2.destroyAllWindows()
    return len(tmp), tmp

def API(i):
    for j in range(3, 6):
        for val in range(30, 181, 5):
            orig_fname = str(i) + '/' + str(i) + '.jpg'
            clust_fname = str(i) + '/' + str(i) + '_' + str(j) + '_clusters_pix_only.jpg'
            cc_fname = str(i) + '/' + str(i) + '_cc.jpg'
            save_fname = str(i) + '/' + str(i) + '_seg.jpg'
            ret, tmp = extract_lung_area( orig_fname, clust_fname, cc_fname, save_fname, val)
            if ret == 8:
                diff1 = tmp[4] - tmp[1] 
                diff2 = tmp[0] - tmp[5]
                if ((diff1 > 15 and diff1 < 65) or (diff2 > 15 and diff2 < 65)):
                    print( str(i) + "  good")
                    return 0


def main():
    ret = 0
    for i in range(1, 31):
        API(i)
        

if __name__ == "__main__":
    main()  

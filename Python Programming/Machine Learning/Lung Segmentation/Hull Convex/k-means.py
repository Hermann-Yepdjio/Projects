from sklearn.cluster import KMeans
from PIL import Image
import numpy as np
import cv2 
from math import floor

def normalize(file_name):
    img = Image.open(file_name +  ".jpg") # Can be many different formats.
    pix = img.load()
    np_pix = np.array(img)
    size = np_pix.shape
    print(np_pix)
    print(size)

    val_0 = np_pix[0][0]
    x_0 = 1 / size[0]
    y_0 = 1 / size[1]
    norm_np_pix = np.array([[val_0, x_0, y_0]])
    count = 0

    #for i in range(0, size[0]):
    #    for j in range(0, size[1]):
    #        if( count != 0):
    #            norm_np_pix = np.concatenate((norm_np_pix, [[np_pix[i][j], (i + 1)/size[0], (j + 1)/size[1]]]))
    #        else:
    #            count += 1
    return np.reshape(np_pix, (np_pix.size, 1)), size, np_pix #norm_np_pix, size, np_pix


def cluster_and_recolor(file_name, norm_np_pix, size, num_clusters, np_pix):
    clt = KMeans(n_clusters = num_clusters)
    clt.fit(norm_np_pix)

    #tmp_np_pix = np.reshape(np_pix, np_pix.size)
    tmp_np_pix = np.zeros((size[0], size[1], 3), dtype =np.uint8 )
    k, j = 0, 0
    hull = []
    for i in range(0, (clt.labels_).size):
        if( clt.labels_[i] == 0):
            tmp_np_pix[k][j] = [255, 0, 0] #red	
        elif clt.labels_[i] == 1:
            tmp_np_pix[k][j] = [255, 255, 0] #yellow
            hull.append([k, j])
        elif (clt.labels_[i] == 2):
            tmp_np_pix[k][j] = [0, 0, 255] #blue
        elif(clt.labels_[i] == 3):
            tmp_np_pix[k][j] = [0, 128, 0]
        else:
            tmp_np_pix[k][j] = [176, 196, 222]
        j += 1
        if j == size[1]:
            k += 1
            j = 0


    print(clt.labels_)
    #print(tmp_np_pix)
    #tmp_np_pix = np.reshape(tmp_np_pix, (size[0], size[1]))


    print(tmp_np_pix)  

    new_img = Image.fromarray(tmp_np_pix).convert('RGB')
    #new_img.save(file_name + '_' + str(num_clusters) + '_clusters_pix_only.jpg')

    gray = cv2.cvtColor(new_img, cv2.COLOR_BGR2GRAY) # convert to grayscale    
    blur = cv2.blur(gray, (3, 3)) # blur the image
    ret, thresh = cv2.threshold(blur, 50, 255, cv2.THRESH_BINARY)

    # Finding contours for the thresholded image
    contours, hierarchy = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    # create hull array for convex hull points
    hull = []

    #print(src[1][1])
    # calculate points for each contour
    #for i in range(floor(src.shape[0]/10), src.shape[0]):
    #    for j in range(floor(src.shape[1]/2)):
    #	if(src[i][j] == [255, 255, 0]):
    #	    hull.append([i, j])

    print (src.shape)
    # calculate points for each contour
    #for i in range(len(contours)):
        # creating convex hull object for each contour
    #    hull.append(cv2.convexHull(contours[i], False))

    # create an empty black image
    drawing = np.zeros((thresh.shape[0], thresh.shape[1], 3), np.uint8)
     
    # draw contours and hull points
    for i in range(len(contours)):
        color_contours = (0, 255, 0) # green - color for contours
        color = (255, 255, 255) # blue - color for convex hull
        # draw ith contour
        cv2.drawContours(drawing, contours, i, color_contours, 1, 8, hierarchy)
        # draw ith convex hull object
        cv2.drawContours(drawing, hull, i, color, 1, 8)
        
    cv2.imshow('drawing',drawing)

    cv2.waitKey(0)
    cv2.destroyAllWindows()


def main():
        norm_np_pix, size, np_pix= normalize("1")
        cluster_and_recolor("1", norm_np_pix, size, 3, np_pix)
        

if __name__ == "__main__":
    main()

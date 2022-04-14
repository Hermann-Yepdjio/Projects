from sklearn.cluster import KMeans
from PIL import Image
import numpy as np
#from minisom import MiniSum
from  mvpa2.suite import *

def normalize(file_name):
    img = Image.open(file_name + "/" + file_name +  ".jpg") # Can be many different formats.
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

    for i in range(0, size[0]):
        for j in range(0, size[1]):
            if( count != 0):
                norm_np_pix = np.concatenate((norm_np_pix, [[np_pix[i][j], (i + 1)/size[0], (j + 1)/size[1]]]))
            else:
                count += 1
    return norm_np_pix, size, np_pix

def cluster_and_recolor(file_name, norm_np_pix, size, num_clusters, np_pix):
    print(norm_np_pix)
    som = SimpleSOMMapper((num_clusters, 1), 5, learning_rate=0.05)
    som.train(norm_np_pix)
    mapped = som(norm_np_pix)

    #tmp_np_pix = np.reshape(np_pix, np_pix.size)
    tmp_np_pix = np.zeros((size[0], size[1], 3), dtype =np.uint8 )
    k, j = 0, 0
    print(len(mapped), "\n")
    for i in range(0, len(mapped)):
        if( mapped[i][0] == 0):
            tmp_np_pix[k][j] = [255, 0, 0]
        elif mapped[i][0] == 1:
            tmp_np_pix[k][j] = [255, 255, 0]
        elif mapped[i][0] == 2:
            tmp_np_pix[k][j] = [0, 0, 255]
        elif mapped[i][0] == 3:
            tmp_np_pix[k][j] = [0, 128, 0]
        else:
            tmp_np_pix[k][j] = [176, 196, 222]
        j += 1
        if j == size[1]:
            k += 1
            j = 0

    
    print(mapped)
    #print(tmp_np_pix)
    #tmp_np_pix = np.reshape(tmp_np_pix, (size[0], size[1]))



    print(tmp_np_pix)

    new_img = Image.fromarray(tmp_np_pix).convert('RGB')
    new_img.save(file_name + '/' + file_name + '_' + str(num_clusters) + '_clusters.jpg')

def main():
    for i in range(1, 17):
        norm_np_pix, size, np_pix= normalize(str(i))
        for j in range(3, 6):
            cluster_and_recolor(str(i), norm_np_pix, size, j, np_pix)
            print(str(i) + "_" + str(j) + "   done!")

if __name__ == "__main__":
    main()

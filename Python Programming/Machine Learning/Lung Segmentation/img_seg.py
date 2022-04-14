from PIL import Image

img = Image.open('4.jpg') # Can be many different formats.
pix = img.load()
size = img.size
print "\n The size of the image is : ", size, ".\n"  # Get the width and hight of the image for iterating over

#find the middle
for i in range(0, size[1]):
        print "[",
        for j in range(0, size[0]):
            print pix[j, i], " ",
        print "]\n"

middle = size[0] / 2
condition_1, condition_2 = False, False
for i in range(0, size[0]):
    tmp_1, tmp_2= 0, 0
    if pix[i, size[1] / 2 ] < 240:
        condition_1 = True
    if condition_1 and pix[i, size[1] / 2 ] > 240:
        condition_2 = True
        tmp_1 = i
    while(condition_1 and condition_2 and i < size[0] - 1):
        i += 1
        if pix[i, size[1] / 2] < 240:
            tmp_2 = i - 1
            middle = (tmp_1 + tmp_2) / 2
            break
    if condition_1 and condition_2:
        break

print "the middle of the image is at pixel: ", middle

#for i in range(0, size[1]):
#   for j in range(0, size[0]):
#        if pix[j, i] < 70 or pix[j, i] > 180:
#            pix[j, i] = 0
#        #else:
#           # break

#from middle
for i in range(0, size[1]):
    #from middle to right
    for j in range(middle, size[0]):
        if pix[j, i] > 140:
            pix[j, i] = 0
        else:
            break
    #from middle to left
    for k in range(middle - 1, 0, -1):
        if pix[k, i] > 140:
            pix[k, i] = 0
        else:
            break
#from left
for i in range(0, size[1]):
    for j in range(0, size[0]):
        if pix[j, i] < 150:
            pix[j, i] = 0
        else:
            break
#from right
for i in range(0, size[1]):
    for j in range(size[0] - 1, 0, -1):
        if pix[j, i] < 150:
            pix[j, i] = 0
        else:
            break
#from top
for i in range(0, size[0]):
    for j in range(0, size[1]):
        if pix[i, j] < 100:
            pix[i, j] = 0
        else:
            break
#from bottom
for i in range(0, size[0]):
    for j in range(size[1] - 1, 0, -1):
        if pix[i, j] < 90:
            pix[i, j] = 0
        else:
            break
         



img.save("4_seg.jpg")




#print pix[x,y]  # Get the RGBA Value of the a pixel of an image
#pix[x,y] = value  # Set the RGBA Value of the image (tuple)
#im.save('alive_parrot.png')  # Save the modified pixels as .png

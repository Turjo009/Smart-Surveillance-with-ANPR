import cv2
from matplotlib import pyplot as plt
import numpy as np
import imutils
import easyocr

# Read image and gray scale it:
img = cv2.imread("image2.jpg")

if img is None:
    print("Error: Image not found or could not be read.")
else:
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    # plt.imshow(gray, cmap='gray')
    # # Displays the image in grayscale
    # plt.show() 

    # applying filters and finding edges for localization:
    bi_filter = cv2.bilateralFilter(gray,11,17,17)
    edged = cv2.Canny(bi_filter, 30, 200)
    # plt.imshow(edged,cmap='gray')
    # plt.show()

    # Find contours:
    keypoints = cv2.findContours(edged.copy(), cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    contours = imutils.grab_contours(keypoints)
    contours = sorted(contours, key=cv2.contourArea, reverse=True)[:10]

    location = None
    for contour in contours:
        approx = cv2.approxPolyDP(contour, 10, True)
        if len(approx) == 4:
            location = approx
            break
    
    # print(location)

    # apply mask:
    mask = np.zeros(gray.shape, np.uint8)
    new_image = cv2.drawContours(mask, [location], 0, 255, -1)
    new_image = cv2.bitwise_and(img, img, mask= mask)

    # plt.imshow(new_image, cmap='gray')
    # plt.show()

    #crop:
    (x,y) = np.where(mask==255)
    (x1,y1) = (np.min(x), np.min(y))
    (x2,y2) = (np.max(x), np.max(y))
    cropped_image = gray[x1:x2+1, y1:y2+1]

    # plt.imshow(cropped_image, cmap='gray')
    # plt.show()

    # Detect Text using easyocr
    reader = easyocr.Reader(['en'])
    result = reader.readtext(cropped_image)
    # print(result[0][1])
    
    with open("plate_number.txt", "w") as file:
        file.write(str(result[0][1]))


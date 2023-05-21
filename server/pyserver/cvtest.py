import random

import keras.utils
from keras.datasets import mnist
from keras.utils import load_img,img_to_array
import cv2 as cv
import os
import numpy as np
import matplotlib.pyplot as plt
import tensorflow as tf
import glob
from PIL import Image

def load_preprosess_image(path):
    image = tf.io.read_file(path)
    image = tf.image.decode_png(image, channels=3)
    image = tf.cast(image, tf.float32)
    image = (image/127.5) - 1
    return image

def readPicture(batch_size):
    image_path = glob.glob(r"defect_image/*.png")
    img_ds = tf.data.Dataset.from_tensor_slices(image_path)
    AUTOTUNE = tf.data.experimental.AUTOTUNE
    img_ds = img_ds.map(load_preprosess_image, num_parallel_calls=AUTOTUNE)
    BATCH_SIZE = batch_size
    image_count = len(image_path)
    img_ds = img_ds.shuffle(image_count).batch(BATCH_SIZE)
    img_ds = img_ds.prefetch(AUTOTUNE)
    return img_ds

def readPic():
    image_path = glob.glob(r"/Users/mete0r/Downloads/anime/*.png")
    x_train = np.zeros((len(image_path), 64, 64, 3), dtype=np.float32)
    # batchs = []
    # for i in idx:
    #     batchs.append(image_path[i])
    ct = 0
    for batch in image_path:
        img = Image.open(batch)
        arr = np.asarray(img)  # 图片转array
        x_train[ct, :, :, :] = arr  # 赋值
        ct = ct + 1
    return x_train
if __name__ == "__main__":
    # image_path = glob.glob(r"/Users/mete0r/Downloads/guangdong1_round1_testB_20190919/*.jpg")
    # for i in range(len(image_path)):
    image = Image.open('./images/output.png')
    image = image.resize((2446, 1000))
    image.save('./images/output2.png')


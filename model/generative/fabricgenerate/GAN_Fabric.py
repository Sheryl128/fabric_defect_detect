from __future__ import print_function, division

import keras.losses
from keras.datasets import mnist
from keras import utils, callbacks
from keras.layers import Input, Dense, Reshape, Flatten,\
    Dropout, Convolution2D, MaxPooling2D, \
    AveragePooling2D, Convolution2DTranspose, Conv2DTranspose,GlobalAveragePooling2D
from keras.layers import BatchNormalization, Activation, ZeroPadding2D
from keras.layers.activation import LeakyReLU
from keras.layers.convolutional import UpSampling2D, Conv2D
from keras.models import Sequential, Model
from keras.optimizers import Adam,SGD
from PIL import Image
import tensorflow as tf
import matplotlib.pyplot as plt
import sys

import numpy as np

import cvtest



class DCGAN(Model):
    def __init__(self):
        super().__init__()
        self.img_rows = 128
        self.img_cols = 128
        self.channels = 3
        self.img_shape = (self.img_rows, self.img_cols, self.channels)
        self.latent_dim = 100

        self.optimizer = Adam(0.00001)

    def generator_model(self):
        model = Sequential()
        model.add(Dense(8 * 8 * 256, use_bias=False, input_shape=(100,)))
        model.add(BatchNormalization())
        model.add(LeakyReLU())

        model.add(Reshape((8, 8, 256)))  # 输出8*8*256
        # 反卷积
        model.add(Conv2DTranspose(128, (5, 5), strides=(1, 1), padding='same', use_bias='false'))
        model.add(BatchNormalization())
        model.add(LeakyReLU())  # 输出8*8*128

        model.add(Conv2DTranspose(64, (5, 5), strides=(2, 2), padding='same', use_bias='false'))
        model.add(BatchNormalization())
        model.add(LeakyReLU())  # 输出16*16*64

        model.add(Conv2DTranspose(32, (5, 5), strides=(2, 2), padding='same', use_bias='false'))
        model.add(BatchNormalization())
        model.add(LeakyReLU())  # 输出32*32*32

        model.add(Conv2DTranspose(16, (5, 5), strides=(2, 2), padding='same', use_bias='false'))
        model.add(BatchNormalization())
        model.add(LeakyReLU())  # 输出64*64*16

        model.add(Conv2DTranspose(3, (5, 5),
                                         strides=(2, 2),
                                         padding='same',
                                         use_bias=False,
                                         activation='tanh'))  # 输出128*128*3

        return model


    def discriminator_model(self):
        model = Sequential()
        model.add(Conv2D(16,
                         (5, 5),
                         strides=(2, 2),
                         padding='same',
                         input_shape=(128, 128, 3)))  # 输入为64*64*3
        model.add(LeakyReLU())
        model.add(Dropout(0.3))  # 64*64*16

        model.add(Conv2D(32,
                                (5, 5),
                                strides=(2, 2),
                                padding='same'))
        model.add(BatchNormalization())
        model.add(LeakyReLU())
        model.add(Dropout(0.3))  # 32*32*32

        model.add(Conv2D(64,
                         (5, 5),
                         strides=(2, 2),
                         padding='same'))
        model.add(BatchNormalization())
        model.add(LeakyReLU())
        model.add(Dropout(0.3))  # 16*16*64

        model.add(Conv2D(128,
                         (5, 5),
                         strides=(2, 2),
                         padding='same'))
        model.add(BatchNormalization())
        model.add(LeakyReLU())
        model.add(Dropout(0.3))  # 8*8*128

        model.add(Conv2D(256,
                                (5, 5),
                                strides=(2, 2),
                                padding='same'))
        model.add(BatchNormalization())
        model.add(LeakyReLU())  # 4*4*256

        model.add(GlobalAveragePooling2D())
        # Global Average Pooling(简称GAP，全局池化层)，被认为是可以替代全连接层的一种新技术

        model.add(Dense(1024))
        model.add(BatchNormalization())
        model.add(LeakyReLU())
        model.add(Dense(1, activation='sigmoid'))
        return model

    def d_on_g(self, d, g):
        model = Sequential()
        model.add(g)
        d.trainable = False
        model.add(d)
        d.trainable = True
        return model

    def train(self, batch_size, epochs):
        discriminator = self.discriminator_model()
        discriminator.compile(loss='binary_crossentropy', optimizer=self.optimizer, metrics=['accuracy'])
        generator = self.generator_model()
        generator.compile(loss='binary_crossentropy', optimizer=self.optimizer)
        d_on_g = self.d_on_g(discriminator, generator)
        d_on_g.compile(loss='binary_crossentropy', optimizer=self.optimizer)

        valid = np.ones((batch_size, 1))
        fake = np.zeros((batch_size, 1))
        for epoch in range(epochs):
            all_imgs = cvtest.readPicture(batch_size)
            ct = 1
            for batch in all_imgs:
                if batch.shape[0] != batch_size:
                    continue
                # ---------------------
                #  Train Discriminator
                # ---------------------

                # Select a random batch of images

                noise = np.random.normal(0, 1, (batch_size, self.latent_dim))

                gen_imgs = generator.predict(noise)

                d_loss_real = discriminator.train_on_batch(batch, valid)
                d_loss_fake = discriminator.train_on_batch(gen_imgs, fake)
                d_loss = 0.5 * np.add(d_loss_real, d_loss_fake)
                # ---------------------
                #  Train Generator
                # ---------------------

                # Train the generator (to have the discriminator label samples as valid)
                g_loss = d_on_g.train_on_batch(noise, valid)

                # Plot the progress
                print("epoch:%d batch: %d [D loss: %f accuracy: %.2f] [G loss: %f]" % (epoch, ct, d_loss[0], 100*d_loss[1], g_loss))
                ct += 1

            if epoch % 5 == 0:
                generator.save_weights('fg' + str(501+epoch) + '.h5')
                discriminator.save_weights('fd' + str(501+epoch) + '.h5')
                noise = np.random.normal(0, 1, (4, self.latent_dim))
                pre_image = generator.predict(noise)
                fig = plt.figure(figsize=(16, 3))  # figsize:指定figure的宽和高，单位为英寸
                for i in range(pre_image.shape[0]):  # pre_image的shape的第一个维度就是个数，这里是6
                    plt.subplot(1, 4, i + 1)  # 几行几列的 第i+1个图片（从1开始）
                    plt.imshow((pre_image[i, :, :, :] + 1) / 2)  # 加1除2: 将生成的-1～1的图片弄到0-1之间,
                    plt.axis('off')  # 不要坐标
                plt.savefig("images/%d.png" % epoch)


if __name__ == "__main__":
    gan = DCGAN()
    # gan.train(32, 11)
    gen = gan.generator_model()
    gen.compile(loss='binary_crossentropy', optimizer=gan.optimizer)
    gen.load_weights('fg751.h5')
    noise = np.random.normal(0, 1, (4, 100))
    pre_image = gen.predict(noise)
    # fig = plt.figure(figsize=(16, 3))  # figsize:指定figure的宽和高，单位为英寸
    for i in range(pre_image.shape[0]):  # pre_image的shape的第一个维度就是个数，这里是6
        images = tf.keras.preprocessing.image.array_to_img(pre_image[i, :, :, :])
        images.save('images/test'+str(i)+'.png')
        # plt.subplot(1, 4, i + 1)  # 几行几列的 第i+1个图片（从1开始）
        # plt.imshow((pre_image[i, :, :, :] + 1) / 2)  # 加1除2: 将生成的-1～1的图片弄到0-1之间,
        # plt.axis('off')  # 不要坐标
    # plt.savefig("images/tt.png")

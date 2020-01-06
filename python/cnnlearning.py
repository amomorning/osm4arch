import csv
import numpy as np
from time import time
from PIL import Image
import random

random.seed(233)
batch_size = 128

def load_data(pathname):
    filelist = []
    with open('./data/{}.csv'.format(pathname),'r') as csvfile:
        rows=csv.reader(csvfile)
        headers = next(rows)
        for row in rows:
            filelist.append(row[1])

    # print(filelist)
    x = np.array([np.array(Image.open('./data/{}/'.format(pathname) + fname + '.jpg')) for fname in filelist])
    x = (x.astype(np.float32)-127.5)/127.5
    return x


def split_data(X, Y):

    N = X.shape[0]
    idlist = range(N)
    M = N*5//100
    M += (N-M) % batch_size

    testId = random.sample(idlist, M)
    trainId = [idx for idx in idlist if not idx in testId]
    print(len(trainId))
    trainX = X[trainId, :]
    trainY = Y[trainId, :]
    testX = X[testId, :]
    testY = Y[testId, :]

    return (trainX, trainY), (testX, testY)

start = time()
X = load_data('X_270_270')
Y = load_data('Y_90_90')
print('{} seconds'.format(time() - start))

start = time()
(trainX, trainY), (testX, testY) = split_data(X, Y)
print('{} seconds'.format(time() - start))

from tensorflow.keras.layers import Conv2D,MaxPool2D,Dense,Flatten, Conv2DTranspose, BatchNormalization
from tensorflow.keras.models import Sequential 

cnn=Sequential()
cnn.add(Conv2D(6,kernel_size=3,strides=1,padding='same',input_shape=(135, 135, 3))) # (135, 135, 16)
cnn.add(Conv2D(16,kernel_size=5,strides=2,padding='valid')) # (133, 133, 16)
cnn.add(Conv2D(32,kernel_size=7,strides=3,padding='valid')) 
cnn.add(BatchNormalization())
cnn.add(Conv2DTranspose(16, kernel_size=5, strides=2, padding='valid', kernel_initializer='glorot_normal'))
cnn.add(BatchNormalization())
cnn.add(Conv2DTranspose(6, kernel_size=7, strides=2, padding='valid', kernel_initializer='glorot_normal'))
cnn.add(MaxPool2D(pool_size=(2,2)))
cnn.add(Conv2D(3, kernel_size=3, strides=1, padding='same'))

out = cnn.predict(testX[1:4, :])

print(out.shape)
import tensorflow as tf
np.sum(tf.keras.losses.mean_squared_error(out[0, :], testY[1, :]).numpy())

from tensorflow.keras.optimizers import Adagrad


ag = Adagrad(0.1)
cnn.compile(optimizer=ag, loss='mse')

history = cnn.fit(trainX, trainY, batch_size=256, epochs=50, validation_data=[testX, testY])

cnn.save('cnn50.h5')

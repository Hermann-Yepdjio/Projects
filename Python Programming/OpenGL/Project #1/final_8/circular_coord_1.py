# -*- coding: utf-8 -*-
"""
Created on Sat Mar  2 19:11:43 2019

@author: SongJ
"""

from canvas import Point2, Canvas
import OpenGL.GL as gl
import OpenGL.GLUT as glut
import OpenGL.GLU as glu
import numpy as np
import pandas as pd
import sys
import copy

class CircularCoord(object):
    def __init__(self, filename, cvs):
        assert isinstance(filename, str), "filename must be string type."
        self.filename = filename
        self.cvs = cvs
        self.center = Point2(x = 0.0, y = 0.0)
        self.startAngle = 90
        self.showDashLine = True
        self.highlight = 0
        self.r = 1
        self.screen_height = 480
        self.screen_width = 720
        (self.data, self.classColor,
         self.classIsVisible, self.dim, self.dataTot) = self.read_data()
        self.originData = copy.deepcopy(self.data)
        self.data_coord = dict()
        self.numInRect = dict()
        self.random_rects = dict()
        self.bestRects = dict()
        self.bestNum = dict()
        self.intersections = dict()
        self.intersectionsRand = dict()
        self.n_dict = dict()
        self.precisions = dict()
        self.recalls = dict()
        self.acc_by_class = dict()
        self.accuracy = 0
        self.precisionsRand = dict()
        self.recallsRand = dict()
        self.acc_by_classRand = dict()
        self.accuracyRand = 0
        self.reverseX = None
        np.random.seed(75982879)


    def scale_data(self, data, new_min, new_max):
        factor = (data - np.min(data, axis = 0)) / (np.max(data, axis = 0) - np.min(data, axis = 0))
        data = new_min + factor * (new_max - new_min)
        return data

    def read_data(self):
        data_df = pd.read_csv(self.filename)
        data = data_df.values
        numeric_data = data[:, 1:-1]
        numeric_data = self.scale_data(numeric_data, 0, 1)
        data[:, 0] = data[:, 0].astype(np.int)
        data[:, 1:-1] = numeric_data
        data_tot = numeric_data.shape[0]
        fname = self.filename.split("/")[-1]
        pd.DataFrame(data).to_csv('normalized_' + fname + '.csv',
                                  index = False)
        dataClassUique = np.unique(data[:, -1])
        dataDict = dict()
        classColor = dict()
        classIsVisible = dict()
        for c in dataClassUique:
            indices, = np.where(data[:, -1] == c)
            dataByClass = np.take(numeric_data, indices, axis = 0)
            dataDict[c] = dataByClass
            classColor[c] = np.append(np.random.random(3), 1.0)
            classIsVisible[c] = True
        return dataDict, classColor, classIsVisible, numeric_data.shape[1], data_tot




    def drawCoord(self):
        self.cvs.setColor(0.0, 0.0, 0.0)
        self.cvs.drawCircle(self.center, self.r, self.startAngle)
        angle = np.deg2rad(self.startAngle)
        angleInc = np.deg2rad(float(360) / float(self.dim))
        for i in range(self.dim):
            self.cvs.moveTo(self.center.x + 0.95 * self.r * np.cos(angle),
                            self.center.y + 0.95 * self.r * np.sin(angle))
            self.cvs.lineTo(self.center.x + 1.05 * self.r * np.cos(angle),
                            self.center.y + 1.05 * self.r * np.sin(angle))
            self.cvs.drawString2(self.center.x + (1 + 1/6) * self.r * np.cos(angle - 0.5 * angleInc),
                                 self.center.y + (1 + 1/6) * self.r * np.sin(angle - 0.5 * angleInc),
                                 'X' + str(i + 1))
            angle -= angleInc

    def drawRectangle(self, point1, point2, color):
        self.cvs.setColor(color[0], color[1], color[2])
        x1 = point1[0]
        x2 = point2[0]
        y1 = point1[1]
        y2 = point2[1]
        gl.glLineWidth(5)
        self.cvs.moveTo(x1, y1)
        self.cvs.lineTo(x1, y2)
        self.cvs.lineTo(x2, y2)
        self.cvs.lineTo(x2, y1)
        self.cvs.lineTo(x1, y1)
        gl.glLineWidth(1)

    def drawRectangles(self, rectangles):
        for rectangle in rectangles:
            if (rectangles[rectangle][1][1] != None):
                self.drawRectangle(rectangles[rectangle][0],
                                   rectangles[rectangle][1],
                                   self.classColor[rectangle])


    def drawData(self, x = None):
        self.reverseX = x
        self.data_coord = dict()
        data_sub = dict()
        for c in self.data.keys():
            if self.classIsVisible[c]:
                if x and x <= self.dim:
                    self.data[c][:, x - 1] = 1.0 - self.data[c][:, x - 1]
                data_sub[c] = self.data[c]

        for c in data_sub.keys():
            self.n_dict[c] = list()
            color = self.classColor[c]
            self.cvs.setColor(color[0], color[1], color[2], 1.0)
            self.data_coord[c] = list()
            for d in data_sub[c]:
                start = self.startAngle
                coord = list()
                for n, i in enumerate(d):
                    dataAngle = np.deg2rad(start - \
                                           float(360) / self.dim * i)
                    x = self.center.x + self.r * np.cos(dataAngle)
                    y = self.center.y + self.r * np.sin(dataAngle)
                    if n == 0:
                        self.cvs.moveTo(x, y)
                        coord.append((x, y))
                    else:
                        self.cvs.lineTo(x, y)
                        coord.append((x, y))
                    start -= float(360) / self.dim
                    if (n == self.dim - 1) and (self.showDashLine == True):
                        dataAngle = np.deg2rad(start - \
                                               float(360) / self.dim * i)
                        gl.glLineStipple(4, 0xAAAA)
                        gl.glEnable(gl.GL_LINE_STIPPLE)
                        self.cvs.lineTo(self.center.x + self.r * np.cos(dataAngle),
                                        self.center.y + self.r * np.sin(dataAngle))
                        gl.glDisable(gl.GL_LINE_STIPPLE)
                self.data_coord[c].append(coord)



    def drawLabels(self):
        self.cvs.setViewport(int(self.screen_width - (self.screen_width - self.screen_height) / 2 - 30),
                             self.screen_width, 0, self.screen_height)
        self.cvs.setWindow(0, self.screen_width, 0, self.screen_height)
        for n, k in enumerate(self.classColor.keys()):
            color = self.classColor[k]
            gl.glPointSize(10.0)
            if not self.classIsVisible[k]:
                self.cvs.setColor(color[0], color[1], color[2], 0.2)
            else:
                self.cvs.setColor(color[0], color[1], color[2], 1.0)
            gl.glBegin(gl.GL_POINTS)
            gl.glVertex2d(25, (self.screen_height - 45) - (25 * (n + 1)))
            gl.glEnd()
            gl.glFlush()
            if self.classIsVisible[k]:
                num = list(self.data_coord.keys()).index(k)
                if n != self.highlight:
                    self.cvs.setColor(0.0, 0.0, 0.0)
                else:
                    self.cvs.setColor(1.0, 0.0, 0.0)
                self.cvs.drawString2(95,
                                     (self.screen_height - 50) - (25 * (n + 1)),
                                     'c' + str(num + 1) + ':' + str(k))
            else:
                if n != self.highlight:
                    self.cvs.setColor(0.0, 0.0, 0.0, 0.2)
                else:
                    self.cvs.setColor(1.0, 0.0, 0.0, 0.2)
                self.cvs.drawString2(95,
                                     (self.screen_height - 50) - (25 * (n + 1)),
                                     'c' + str(n + 1) + ':' + str(k))
        self.cvs.setWindow(-1.5 * self.r, 1.5 * self.r, -1.5 * self.r, 1.5 * self.r)
        self.cvs.setViewport(int((self.screen_width - self.screen_height) / 2),
                    int(self.screen_width - (self.screen_width - self.screen_height) / 2),
                    0, int(self.screen_height))

    def genRandRects(self):
        self.random_rects = dict()
        for key in self.data_coord.keys():
            randNum = list()
            randData = np.random.choice(len(self.data_coord[key]),
                                        size = 4, replace = False)
            m = np.random.choice(self.dim, size = self.dim)
            for n in range(0, len(randData), 2):
                randNum.append(self.data_coord[key][randData[n]][m[n]][0])
                randNum.append(self.data_coord[key][randData[n]][m[n]][1])
            self.random_rects[key] = [(randNum[0], randNum[1]),
                                             (randNum[2], randNum[3])]

    def detectRects(self):
        max_acc = dict()
        self.bestNum = dict()
        self.bestRects = dict()
        for i in range(800):
            self.genRandRects()
            self.isInRects()
            for n, key in enumerate(self.numInRect.keys()):
                # num is a list of list storing the number of crossing rectangles
                for num in self.numInRect[key]:

                    tmp_acc = num[n] / np.sum(num)
                    if i == 0:
                        max_acc[key] = tmp_acc
                        self.bestNum[key] = num
                        self.bestRects[key] = self.random_rects[key]
                    else:
                        if max_acc[key] < tmp_acc:
                            max_acc[key] = tmp_acc
                            self.bestNum[key] = num
                            self.bestRects[key] = self.random_rects[key]
                        elif max_acc[key] == tmp_acc:
                            if num[n] > self.bestNum[key][n]:
                                self.bestRects[key] = self.random_rects[key]

    def isInRects(self):
        self.numInRect = dict()
        for k in self.data_coord.keys():
            self.numInRect[k] = list()
            for m, rect in enumerate(range(0, len(self.random_rects[k]), 2)):
                lb = self.random_rects[k][rect]
                rt = self.random_rects[k][rect + 1]
                if lb[1] == None or rt[1] == None:
                    break
                self.numInRect[k].append([0] * len(self.data_coord))
                # data is a list a tuple, and each tuple is the coord for each point
                for j, d in enumerate(self.data_coord.keys()):
                    for n, data in enumerate(self.data_coord[d]):
                        for i in range(len(data) - 1):
                            if self.isintersect_line_rect(data[i], data[i + 1], lb, rt):
                                self.numInRect[k][-1][j] += 1
                                break

    def drawConfMatrixRand(self):
        self.cvs.setViewport(0,
                             int((self.screen_width - self.screen_height) / 2 - 30),
                             0, self.screen_height)
        self.cvs.setWindow(0, self.screen_width, 0, self.screen_height)
        tmp_labels = list()
        for n, k in enumerate(self.bestNum.keys()):
            if self.classIsVisible[k]:
                tmp_labels.append('c' + str(n + 1))
        tmp_labels = str(tmp_labels)
        tmp_labels = tmp_labels.replace('[', '')
        tmp_labels = tmp_labels.replace(']', '')
        tmp_labels = tmp_labels.replace('\'', '')
        tmp_labels = tmp_labels.replace(',', '   ')
        tmp_precision = str(list(self.precisionsRand.values()))

        tmp_precision = tmp_precision.replace("[", "")
        tmp_precision = tmp_precision.replace("]", "")
        tmp_precision = tmp_precision.replace(",", "   ")
        tmp_recalls = str(list(self.recallsRand.values()))

        tmp_recalls = tmp_recalls.replace("[", "")
        tmp_recalls = tmp_recalls.replace("]", "")
        tmp_recalls = tmp_recalls.replace(",", "   ")
        tmp_acc_by_class = str(list(self.acc_by_classRand.values()))

        tmp_acc_by_class = tmp_acc_by_class.replace("[", "")
        tmp_acc_by_class = tmp_acc_by_class.replace("]", "")
        tmp_acc_by_class = tmp_acc_by_class.replace(",", "   ")
        tmp_accuracy = str(self.accuracyRand)
        tmp_accuracy = tmp_accuracy.replace("[", "")
        tmp_accuracy = tmp_accuracy.replace("]", "")
        tmp_accuracy = tmp_accuracy.replace(",", "   ")

        b = 0
        for n, k in enumerate(self.bestNum):
            color = self.classColor[k]
            gl.glPointSize(10.0)
            self.cvs.setColor(0.0, 0.0, 0.0)
            if not self.classIsVisible[k]:
                self.cvs.setColor(color[0], color[1], color[2], 0.2)
            else:
                self.cvs.setColor(color[0], color[1], color[2], 1.0)

            if self.classIsVisible[k]:
                self.cvs.setColor(0.0, 0.0, 0.0)
                if self.bestNum[k]:
                    tem_str = str(self.bestNum[k])
                    tem_str = tem_str.replace("[", "")
                    tem_str = tem_str.replace("]", "")
                    tem_str = tem_str.replace(",", "   ")
                    self.cvs.drawString2(10,
                                         (self.screen_height - 75) - (25 * (b + 1)),
                                         'c' +  str(n + 1))
                    self.cvs.drawString2(350,
                                         (self.screen_height - 75) - (25 * (b + 1)),
                                         tem_str)
                    self.cvs.drawString2(350,
                                             (self.screen_height - 75),
                                             str(tmp_labels))
                b += 1
        self.cvs.drawString2(350, (self.screen_height - 50), "Manual:")
        b += 1
        self.cvs.drawString2(10, (self.screen_height - 75) - (25 * (b + 1)),
                             "precision:")
        self.cvs.drawString2(350, (self.screen_height - 75) - (25 * (b + 1)),
                             str(tmp_precision))
        b += 1
        self.cvs.drawString2(10, (self.screen_height - 75) - (25 * (b + 1)),
                             "recall:")
        self.cvs.drawString2(350, (self.screen_height - 75) - (25 * (b + 1)),
                             str(tmp_recalls))
        b += 1
        self.cvs.drawString2(10, (self.screen_height - 75) - (25 * (b + 1)),
                             "accuracy:")
        self.cvs.drawString2(350, (self.screen_height - 75) - (25 * (b + 1)),
                             str(tmp_acc_by_class))
        b += 1
        self.cvs.drawString2(350, (self.screen_height - 75) - (25 * (b + 1)),
                             str(tmp_accuracy))
        self.cvs.setWindow(-1.5 * self.r, 1.5 * self.r, -1.5 * self.r, 1.5 * self.r)
        self.cvs.setViewport(int((self.screen_width - self.screen_height) / 2),
                    int(self.screen_width - (self.screen_width - self.screen_height) / 2),
                    0, int(self.screen_height))

    def drawConfMatrix(self):
        self.cvs.setViewport(0,
                             int((self.screen_width - self.screen_height) / 2 - 30),
                             0, self.screen_height)
        self.cvs.setWindow(0, self.screen_width, 0, self.screen_height)
        tmp_labels = list()
        for n, k in enumerate(self.intersections.keys()):
            if self.classIsVisible[k]:
                tmp_labels.append('c' + str(n + 1))
        tmp_labels = str(tmp_labels)
        tmp_labels = tmp_labels.replace('[', '')
        tmp_labels = tmp_labels.replace(']', '')
        tmp_labels = tmp_labels.replace('\'', '')
        tmp_labels = tmp_labels.replace(',', '   ')
        tmp_precision = str(list(self.precisions.values()))
        tmp_precision = tmp_precision.replace("[", "")
        tmp_precision = tmp_precision.replace("]", "")
        tmp_precision = tmp_precision.replace(",", "   ")
        tmp_recalls = str(list(self.recalls.values()))
        tmp_recalls = tmp_recalls.replace("[", "")
        tmp_recalls = tmp_recalls.replace("]", "")
        tmp_recalls = tmp_recalls.replace(",", "   ")
        tmp_acc_by_class = str(list(self.acc_by_class.values()))
        tmp_acc_by_class = tmp_acc_by_class.replace("[", "")
        tmp_acc_by_class = tmp_acc_by_class.replace("]", "")
        tmp_acc_by_class = tmp_acc_by_class.replace(",", "   ")
        tmp_accuracy = str(self.accuracy)
        tmp_accuracy = tmp_accuracy.replace("[", "")
        tmp_accuracy = tmp_accuracy.replace("]", "")
        tmp_accuracy = tmp_accuracy.replace(",", "   ")

        b = 0
        for n, k in enumerate(self.intersections):
            color = self.classColor[k]
            gl.glPointSize(10.0)
            self.cvs.setColor(0.0, 0.0, 0.0)
            if not self.classIsVisible[k]:
                self.cvs.setColor(color[0], color[1], color[2], 0.2)
            else:
                self.cvs.setColor(color[0], color[1], color[2], 1.0)

            if self.classIsVisible[k]:
                self.cvs.setColor(0.0, 0.0, 0.0)
                if self.intersections[k]:
                    tem_str = str(self.intersections[k])
                    tem_str = tem_str.replace("[", "")
                    tem_str = tem_str.replace("]", "")
                    tem_str = tem_str.replace(",", "   ")
                    self.cvs.drawString2(10,
                                         (self.screen_height - 500) - (25 * (b + 1)),
                                         'c' +  str(n + 1))
                    self.cvs.drawString2(350,
                                         (self.screen_height - 500) - (25 * (b + 1)),
                                         tem_str)
                    self.cvs.drawString2(350,
                                             (self.screen_height - 500),
                                             str(tmp_labels))
                b += 1
        self.cvs.drawString2(350, (self.screen_height - 475), "Manual:")
        b += 1
        self.cvs.drawString2(10, (self.screen_height - 500) - (25 * (b + 1)),
                             "precision:")
        self.cvs.drawString2(350, (self.screen_height - 500) - (25 * (b + 1)),
                             str(tmp_precision))
        b += 1
        self.cvs.drawString2(10, (self.screen_height - 500) - (25 * (b + 1)),
                             "recall:")
        self.cvs.drawString2(350, (self.screen_height - 500) - (25 * (b + 1)),
                             str(tmp_recalls))
        b += 1
        self.cvs.drawString2(10, (self.screen_height - 500) - (25 * (b + 1)),
                             "accuracy:")
        self.cvs.drawString2(350, (self.screen_height - 500) - (25 * (b + 1)),
                             str(tmp_acc_by_class))
        b += 1
        self.cvs.drawString2(350, (self.screen_height - 500) - (25 * (b + 1)),
                             str(tmp_accuracy))
        self.cvs.setWindow(-1.5 * self.r, 1.5 * self.r, -1.5 * self.r, 1.5 * self.r)
        self.cvs.setViewport(int((self.screen_width - self.screen_height) / 2),
                    int(self.screen_width - (self.screen_width - self.screen_height) / 2),
                    0, int(self.screen_height))


    def isintersect_line_rect(self, l1, l2, r1, r2):
        #if l1[1] > r1[1] and l1[1] > r2[1] and l2[1] > r1[1] and l2[1] > r2[1]:
        #    return False
        #if l1[1] < r1[1] and l1[1] < r2[1] and l2[1] < r1[1] and l2[1] < r2[1]:
        #    return False
        #if l1[0] < r1[0] and l1[0] < r2[0] and l2[0] < r1[0] and l2[0] < r2[0]:
        #    return False
        #if l1[0] < r1[0] and l1[0] < r2[0] and l2[0] < r1[0] and l2[0] < r2[0]:
        #    return False
        if r1[0] <= r2[0]:
            crossX1 = l1[0] + (r1[1] - l1[1]) * (l2[0] - l1[0]) / (l2[1] - l1[1])
            if r1[0] <= crossX1 and crossX1 <= r2[0]:
                return True
            crossX2 = l1[0] + (r2[1] - l1[1]) * (l2[0] - l1[0]) / (l2[1] - l1[1])
            if r1[0] <= crossX2 and crossX2 <= r2[0]:
                return True
        else:
            crossX1 = l1[0] + (r1[1] - l1[1]) * (l2[0] - l1[0]) / (l2[1] - l1[1])
            if r2[0] <= crossX1 and crossX1 <= r1[0]:
                return True
            crossX2 = l1[0] + (r2[1] - l1[1]) * (l2[0] - l1[0]) / (l2[1] - l1[1])
            if r2[0] <= crossX2 and crossX2 <= r1[0]:
                return True
        if r2[1] <= r1[1]:
            crossY1 = l1[1] + (r1[0] - l1[0]) * (l2[1] - l1[1]) / (l2[0] - l1[0])
            if r2[1] <= crossY1 and crossY1 <= r1[1]:
                return True
            crossY2 = l1[1] + (r2[0] - l1[0]) * (l2[1] - l1[1]) / (l2[0] - l1[0])
            if r2[1] <= crossY2 and crossY2 <= r1[1]:
                return True
        else:
            crossY1 = l1[1] + (r1[0] - l1[0]) * (l2[1] - l1[1]) / (l2[0] - l1[0])
            if r1[1] <= crossY1 and crossY1 <= r2[1]:
                return True
            crossY2 = l1[1] + (r2[0] - l1[0]) * (l2[1] - l1[1]) / (l2[0] - l1[0])
            if r1[1] <= crossY2 and crossY2 <= r2[1]:
                return True
        return False

    def isintersect(self, rectangles):
        self.intersections = dict()
        #data_coord_copy = copy.deepcopy(self.data_coord)
        for k in self.data_coord.keys():
            self.intersections[k] = list()
            for rect in range(0, len(rectangles[k]), 2):
                lb = rectangles[k][rect]
                rt = rectangles[k][rect + 1]
                if lb[1] == None or rt[1] == None:
                    break
                # data is a list a tuple, and each tuple is the coord for each point
                for d in self.data_coord.keys():
                    counter = 0
                    for n, data in enumerate(self.data_coord[d]):
                        if not n in self.n_dict[d]:
                            for i in range(len(data) - 1):
                                if self.isintersect_line_rect(data[i], data[i + 1], lb, rt):
                                    counter += 1
                                    self.n_dict[d].append(n)
                                    break
                    if self.classIsVisible[d]:
                        self.intersections[k].append(counter)


    def computeAccuracy(self):
        self.precisions = dict()
        self.recalls = dict()
        self.acc_by_class = dict()
        self.accuracy = 0.0
        intersections_arr = np.array([])
        for key in self.intersections.keys():
            if self.intersections[key]:
                if intersections_arr.size == 0:
                     intersections_arr = np.append(intersections_arr,
                                                   self.intersections[key])
                     intersections_arr = np.expand_dims(intersections_arr,
                                                        axis = 0)
                else:
                    intersections_arr = np.append(intersections_arr,
                              [self.intersections[key]],
                              axis = 0)
        acc_tot = 0
        total = 0
        if intersections_arr.shape[0] > 0 and intersections_arr.shape[0] != intersections_arr.size:
            if intersections_arr.shape[0] == intersections_arr.shape[1]:
                precisions_sum = np.sum(intersections_arr, axis = 0)
                recalls_sum = np.sum(intersections_arr, axis = 1)
                for n, k in enumerate(self.intersections.keys()):
                    self.precisions[k] = self.intersections[k][n] / precisions_sum[n,]
                    self.recalls[k] = self.intersections[k][n] / recalls_sum[n]
                    self.acc_by_class[k] = self.intersections[k][n] / self.data[k].shape[0]
                    acc_tot += self.intersections[k][n]
                    total += self.data[k].shape[0]
                if intersections_arr.any():
                    self.accuracy =  acc_tot / total


    def computeAccuracyRand(self):
        self.precisionsRand = dict()
        self.recallsRand = dict()
        self.acc_by_classRand = dict()
        self.accuracyRand = 0
        intersections_arr = np.array([])
        for key in self.bestNum.keys():
            if self.bestNum[key]:
                if intersections_arr.size == 0:
                     intersections_arr = np.append(intersections_arr,
                                                   self.bestNum[key])
                     intersections_arr = np.expand_dims(intersections_arr,
                                                        axis = 0)
                else:
                    intersections_arr = np.append(intersections_arr,
                              [self.bestNum[key]],
                              axis = 0)

        acc_tot = 0
        total = 0
        if intersections_arr.shape[0] > 0 and intersections_arr.shape[0] != intersections_arr.size:
            if intersections_arr.shape[0] == intersections_arr.shape[1]:
                precisions_sum = np.sum(intersections_arr, axis = 0)
                recalls_sum = np.sum(intersections_arr, axis = 1)
                for n, k in enumerate(self.bestNum.keys()):
                    self.precisionsRand[k] = self.bestNum[k][n] / precisions_sum[n,]
                    self.recallsRand[k] = self.bestNum[k][n] / recalls_sum[n]
                    self.acc_by_classRand[k] = self.bestNum[k][n] / self.data[k].shape[0]
                    acc_tot += self.bestNum[k][n]
                    total += self.data[k].shape[0]

                if intersections_arr.any():
                    self.accuracyRand =  acc_tot / total




def display():
    cvs.setWindow(-1.5 * 1, 1.5 * 1, -1.5 * 1, 1.5 * 1)
    cvs.setViewport(int((screen_width - screen_height) / 2),
                    int(screen_width - (screen_width - screen_height) / 2),
                    0, int(screen_height))
    cvs.clearScreen()

    clrcoord.drawCoord()
    clrcoord.drawData()
    clrcoord.drawLabels()

    clrcoord.detectRects()
    clrcoord.drawRectangles(clrcoord.bestRects)
    clrcoord.isintersect(d)
    clrcoord.drawConfMatrix()
    clrcoord.computeAccuracy()



    #cvs.drawCoord(-0.9, 0.9, -0.9, 0.9)
    #gl.glPointSize(3)
    #p = Point2(x = 0.3, y = 0.5)
    #p.draw()
    #cvs.ngon(3, 0.0, 0.0, 0.8, 90)
    #cvs.drawCircle(Point2(), 0.8, 90)
    #cvs.drawArc(Point2(), 0.6, 90, 120)
    #cvs.drawEllipse(0.0, 0.0, 0.8, 0.5, 90)
    #cvs.setColor(0.0, 0.0, 0.0)
    #cvs.drawString2(0.0, 0.0, "test0001")

    #cvs.clearScreen()
    glut.glutSwapBuffers()


def initialization():
    gl.glEnable(gl.GL_LINE_SMOOTH)
    gl.glHint(gl.GL_LINE_SMOOTH_HINT, gl.GL_NICEST)
    gl.glEnable(gl.GL_POINT_SMOOTH)
    gl.glHint(gl.GL_POINT_SMOOTH_HINT, gl.GL_NICEST)
    gl.glEnable(gl.GL_BLEND);
    gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA)


def reshape(width, height):
    global screen_width
    global screen_height
    screen_width = width
    screen_height = height

def keyboard(key, x, y):
    if key == b'r':
        for k in clrcoord.classColor.keys():
            clrcoord.classColor[k] = np.append(np.random.random(3),
                                       clrcoord.classColor[k][3])
    if key == b'\r':
        highlighted = list(clrcoord.classIsVisible.keys())[clrcoord.highlight]

        if clrcoord.classIsVisible[highlighted] == True:
            clrcoord.classIsVisible[highlighted] = False
        else:
            clrcoord.classIsVisible[highlighted] = True

    glut.glutPostRedisplay()

def mouse(button, state, x, y):
    pass

def movedMouse(mouseX, mouseY):
    pass

def specialKeys(key, mouseX, mouseY):
    if key == glut.GLUT_KEY_UP:
        if clrcoord.highlight == 0:
            clrcoord.highlight = len(clrcoord.classColor) - 1
        else:
            clrcoord.highlight -= 1
    if key == glut.GLUT_KEY_DOWN:
        if clrcoord.highlight == len(clrcoord.classColor) - 1:
            clrcoord.highlight = 0
        else:
            clrcoord.highlight += 1
    glut.glutPostRedisplay()


if __name__ == '__main__':
    screen_width = 720
    screen_height = 480
    cvs = Canvas(screen_width, screen_height, 'test0001')
    cvs.setBackgroundColor(0.95, 0.95, 0.95)
    cvs.setColor(0.8, 0.3, 0.2)
    clrcoord = CircularCoord('iris.csv', cvs)
    d = dict()
    d['Iris-setosa'] = [(None, None), (None, None)]
    d['Iris-versicolor'] = [(None, None), (None, None)]
    d['Iris-virginica'] = [(None, None), (None, None)]
    clrcoord.drawCoord()
    #clrcoord.drawRectangles(d)
    #
    #clrcoord.drawData()
    #clrcoord.drawLabels()

    #clrcoord.isintersect(d)
    #clrcoord.drawConfMatrix()
    #clrcoord.computeAccuracy()
    #clrcoord.detectRects()
    #clrcoord = CircularCoord('yeast.csv', cvs)
    #clrcoord = CircularCoord('forestfires.csv', cvs)
    #clrcoord = CircularCoord('breast-cancer-wisconsin-fixed.csv', cvs)
    #clrcoord = CircularCoord('wine_quality.csv', cvs)
    #clrcoord.drawLabels()
    #clrcoord = CircularCoord('iris.csv', cvs)
    #clrcoord.drawCoord()
    #cvs.ngon(5, 0.0, 0.0, 0.8, 90)
    #cvs.drawString2(0.5, 0.5, "test0001")
    glut.glutReshapeFunc(reshape)
    glut.glutDisplayFunc(display)
    glut.glutKeyboardFunc(keyboard)
    glut.glutSpecialFunc(specialKeys)
    initialization()
    glut.glutMainLoop()
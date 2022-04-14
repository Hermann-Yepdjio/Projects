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

class CircularCoord(object):
    def __init__(self, filename, cvs):
        assert isinstance(filename, str), "filename must be string type."
        # assert isinstance(cvs, Canvas), "cvs must be Canvas type."
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
         self.classIsVisible, self.dim) = self.read_data()

    def scale_data(self, data, new_min, new_max):
        factor = (data - np.min(data, axis = 0)) / (np.max(data, axis = 0) - np.min(data, axis = 0))
        #print(np.min(data, axis = 0))
        data = new_min + factor * (new_max - new_min)
        return data

    def read_data(self):
        data_df = pd.read_csv(self.filename)
        data = data_df.values
        numeric_data = data[:, 1:-1]
        numeric_data = self.scale_data(numeric_data, 0, 1)
        data[:, 0] = data[:, 0].astype(np.int)
        #data[:, -1] = data[:, -1].astype(np.int)
        data[:, 1:-1] = numeric_data
        #print(data)
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
        return dataDict, classColor, classIsVisible, numeric_data.shape[1]




    def drawCoord(self):#, point1, point2, color):
        self.cvs.setColor(0.0, 0.0, 0.0)
        self.cvs.drawCircle(self.center, self.r, self.startAngle)
        print(self.center)
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
        x1 = (point1[0] - self.screen_width/2) * 2.4 / (self.screen_width/2)
        x2 = (point2[0] - self.screen_width / 2) * 2.4 / (self.screen_width/2)
        y1 = -1 * (point1[1] - self.screen_height / 2) * 1.5 / (self.screen_height / 2)
        y2 = -1 * (point2[1] - self.screen_height / 2) * 1.5 / (self.screen_height / 2)
        gl.glLineWidth(5)
        self.cvs.moveTo(x1, y1)
        self.cvs.lineTo(x1, y2)
        self.cvs.lineTo(x2, y2)
        self.cvs.lineTo(x2, y1)
        self.cvs.lineTo(x1, y1)
        gl.glLineWidth(1)

    def drawRectangles(self, rectangles):
        print(rectangles)
        print(self.classColor)
        for rectangle in rectangles:
            if (rectangles[rectangle][1][1] != None):
                self.drawRectangle(rectangles[rectangle][0], rectangles[rectangle][1], self.classColor[rectangle])


    def drawData(self):
        for c in self.data.keys():
            color = self.classColor[c]
            if not self.classIsVisible[c]:
                self.cvs.setColor(color[0], color[1], color[2], 0.0)
            else:
                self.cvs.setColor(color[0], color[1], color[2], 1.0)
                for d in self.data[c]:
                    start = self.startAngle
                    for n, i in enumerate(d):
                        dataAngle = np.deg2rad(start - \
                                               float(360) / self.dim * i)
                        if n == 0:
                            self.cvs.moveTo(self.center.x + self.r * np.cos(dataAngle),
                                            self.center.y + self.r * np.sin(dataAngle))
                        else:
                            self.cvs.lineTo(self.center.x + self.r * np.cos(dataAngle),
                                            self.center.y + self.r * np.sin(dataAngle))
                        start -= float(360) / self.dim
                        if (n == self.dim - 1) and (self.showDashLine == True):
                            dataAngle = np.deg2rad(start - \
                                                   float(360) / self.dim * i)
                            gl.glLineStipple(4, 0xAAAA)
                            gl.glEnable(gl.GL_LINE_STIPPLE)
                            self.cvs.lineTo(self.center.x + self.r * np.cos(dataAngle),
                                            self.center.y + self.r * np.sin(dataAngle))
                            gl.glDisable(gl.GL_LINE_STIPPLE)

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
            gl.glVertex2d(55, (self.screen_height - 45) - (25 * (n + 1)))
            gl.glEnd()
            gl.glFlush()
            if self.classIsVisible[k]:
                if n != self.highlight:
                    self.cvs.setColor(0.0, 0.0, 0.0)
                else:
                    self.cvs.setColor(1.0, 0.0, 0.0)
                self.cvs.drawString2(95,
                                     (self.screen_height - 50) - (25 * (n + 1)),
                                     str(k))
            else:
                if n != self.highlight:
                    self.cvs.setColor(0.0, 0.0, 0.0, 0.2)
                else:
                    self.cvs.setColor(1.0, 0.0, 0.0, 0.2)
                self.cvs.drawString2(95,
                                     (self.screen_height - 50) - (25 * (n + 1)),
                                     str(k))



def display():
    cvs.setWindow(-1.5 * 1, 1.5 * 1, -1.5 * 1, 1.5 * 1)
    cvs.setViewport(int((screen_width - screen_height) / 2),
                    int(screen_width - (screen_width - screen_height) / 2),
                    0, int(screen_height))
    cvs.clearScreen()

    clrcoord.drawCoord()
    clrcoord.drawData()
    clrcoord.drawLabels()


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
        print(clrcoord.classIsVisible[highlighted])
        if clrcoord.classIsVisible[highlighted] == True:
            clrcoord.classIsVisible[highlighted] = False
        else:
            clrcoord.classIsVisible[highlighted] = True

        print(clrcoord.classIsVisible[highlighted])
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
    # initialization()
    glut.glutMainLoop()
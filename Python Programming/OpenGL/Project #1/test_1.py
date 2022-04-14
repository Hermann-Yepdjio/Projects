import sys
import pandas as pd
from PyQt5.QtWidgets import *
from PyQt5 import QtGui
from PyQt5.QtCore import Qt
from PyQt5.QtOpenGL import *
import OpenGL.GL as gl
import OpenGL.GLUT as glut
import OpenGL.GLU as glu
from OpenGL.GLU import *
import math

from canvas import Point2, Canvas
from circular_coord_1 import CircularCoord
import numpy as np


class OpenGLWidget(QGLWidget):
    def __init__(self, parent, fname):
        QGLWidget.__init__(self, parent)
        # Current 2d point
        self.CP = Point2(x=0.0, y=0.0)
        # Current direction
        self.CD = 0.0
        self.clrcoord = CircularCoord(fname, self)
        self.rectangles = {}
        for key in self.clrcoord.data.keys():
            self.rectangles[key] = [(None, None), (None, None)]
        self.rectangle_start = None
        self.rectangle_end = None

    def setWindow(self, left, right, bottom, top):
        gl.glMatrixMode(gl.GL_PROJECTION)
        gl.glLoadIdentity()
        glu.gluOrtho2D(left, right, bottom, top)

    def setViewport(self, left, right, bottom, top):
        gl.glViewport(left, bottom, right - left, top - bottom)

    def clearScreen(self):
        gl.glClear(gl.GL_COLOR_BUFFER_BIT)

    def setBackgroundColor(self, r, g, b):
        gl.glClearColor(r, g, b, 0.0)

    def setColor(self, r, g, b, alpha=1.0):
        gl.glColor4f(r, g, b, alpha)

    def moveTo(self, *args, **kwds):
        if len(args) == 2:
            self.CP.setCP(args[0], args[1])
        elif len(args) == 1 and isinstance(args[0], Point2):
            self.CP.setCP(args[0].x, args[0].y)

    def lineTo(self, *args, **kwds):
        if len(args) == 2:
            gl.glBegin(gl.GL_LINES)
            gl.glVertex2f(self.CP.x, self.CP.y)
            gl.glVertex2f(args[0], args[1])
            gl.glEnd()
            gl.glFlush()
            self.CP.setCP(args[0], args[1])
        if len(args) == 1 and isinstance(args[0], Point2):
            gl.glBegin(gl.GL_LINES)
            gl.glVertex2f(self.CP.x, self.CP.y)
            gl.glVertex2f(args[0].x, args[0].y)
            gl.glEnd()
            gl.glFlush()
            self.CP.setCP(self.CP.x, self.CP.y)

    def moveRel(self, dx, dy):
        self.CP.setCP(self.CP.x + dx, self.CP.y + dy)

    def lineRel(self, dx, dy):
        x = self.CP.x + dx
        y = self.CP.y + dy
        self.lineTo(x, y)

    def turnTo(self, angle):
        self.CD = angle

    def turn(self, angle):
        self.CD += angle

    def forward(self, dist, isVisible):
        x = self.CP.x + dist * np.cos(np.deg2rad(self.CD))
        y = self.CP.y + dist * np.sin(np.deg2rad(self.CD))
        if isVisible:
            self.lineTo(x, y)
        else:
            self.moveTo(x, y)

    def lerp(a, b, t):
        return a + (b - a) * t

    def ngon(self, n, centerX, centerY, radius, rotAngle):
        assert n >= 3, "ngon: The number of n must be equal greater than 3."
        # initial angle
        angle = np.deg2rad(rotAngle)
        # The increment of angle each iteration
        # angleInc =  2 * np.pi / n
        angleInc = np.deg2rad(float(360) / float(n))
        self.moveTo(radius * np.cos(angle) + centerX,
                    radius * np.sin(angle) + centerY)
        for k in range(1, n + 1):
            angle -= angleInc
            self.lineTo(radius * np.cos(angle) + centerX,
                        radius * np.sin(angle) + centerY)

    def drawCircle(self, center, radius, start):
        assert isinstance(center, Point2), "The type of center must be Point2."
        numVertexs = 360
        self.ngon(numVertexs, center.x, center.y, radius, start)

    def drawArc(self, center, radius, startAngle, sweep):
        assert isinstance(center, Point2), "The type of center must be Point2."
        n = 360
        angle = np.deg2rad(startAngle)
        angleInc = np.deg2rad(float(sweep) / float(n))
        cx = center.x
        cy = center.y
        self.moveTo(cx + radius * np.cos(angle), cy + radius * np.sin(angle))
        for i in range(1, n + 1):
            angle -= angleInc
            self.lineTo(cx + radius * np.cos(angle), cy + radius * np.sin(angle))

    def drawEllipse(self, centerX, centerY, W, H, rotAngle):
        numVertexs = 360
        angle = np.deg2rad(rotAngle)
        angleInc = 2 * np.pi / numVertexs
        self.moveTo(W * np.cos(angle) + centerX, H * np.sin(angle) + centerY)
        for i in range(1, numVertexs + 1):
            angle -= angleInc
            self.lineTo(W * np.cos(angle) + centerX, H * np.sin(angle) + centerY)

    def drawCoord(self, left, right, bottom, top):
        self.moveTo(left, 0.0)
        self.lineTo(right, 0.0)
        self.moveTo(0.0, bottom)
        self.lineTo(0.0, top)


    def drawString2(self, x, y, string):
        gl.glRasterPos2f(x, y)
        glut.glutBitmapString(glut.GLUT_BITMAP_8_BY_13,
                              bytes(string, 'ascii'))
        gl.glFlush()

    def paintGL(self):
        if self.clrcoord is not None:
            self.setWindow(-1.5 * 1, 1.5 * 1, -1.5 * 1, 1.5 * 1)
            self.setViewport(int((self.clrcoord.screen_width - self.clrcoord.screen_height) / 2),
                             int(self.clrcoord.screen_width - (
                                         self.clrcoord.screen_width - self.clrcoord.screen_height) / 2),
                             0, int(self.clrcoord.screen_height))
            self.clearScreen()

            self.clrcoord.drawRectangles(self.rectangles)
            self.clrcoord.drawCoord()
            self.clrcoord.drawData()
            self.clrcoord.drawLabels()


    def resizeGL(self, w, h):
        if self.clrcoord is not None:
            self.clrcoord.screen_height = h
            self.clrcoord.screen_width = w

    def initializeGL(self):
        glut.glutInit()
        glut.glutInitDisplayMode(glut.GLUT_DOUBLE | glut.GLUT_RGBA)

        gl.glMatrixMode(gl.GL_PROJECTION)
        gl.glLoadIdentity()
        glu.gluOrtho2D(-1, 1, -1, 1)

        gl.glClearColor(0.95, 0.95, 0.95, 0.0)
        gl.glClearDepth(1.0)

    def mousePressEvent(self, QMouseEvent):
        x = QMouseEvent.pos().x()
        y = QMouseEvent.pos().y()
        highlighted = list(self.clrcoord.classIsVisible.keys())[self.clrcoord.highlight]
        if self.rectangles[highlighted][0][0] is None:
            self.rectangles[highlighted][0] = (x, y)
            print("First point selected: " + str(self.rectangles[highlighted]) + " for " + highlighted)
        elif self.rectangles[highlighted][1][0] is None:
            self.rectangles[highlighted][1] = (x, y)
            print("Draw rectangle: " + str(self.rectangles[highlighted]) + " for " + highlighted)
        else:
            self.rectangles[highlighted] = [(None, None), (None, None)]
            print("Clear rectangle for " + highlighted)
        self.update()


class App(QMainWindow):
    def __init__(self):
        super().__init__()
        self.data = None

        self.setWindowTitle("Circular Coordinates")
        self.setGeometry(0, 0, 720, 480)
        self.initMenubar()
        self.widget = None
        self.show()

    def keyPressEvent(self, e):
        if self.widget is not None:
            if e.key() == Qt.Key_Up:
                if self.widget.clrcoord.highlight == 0:
                    self.widget.clrcoord.highlight = len(self.widget.clrcoord.classColor) - 1
                else:
                    self.widget.clrcoord.highlight -= 1
            elif e.key() == Qt.Key_Down:
                if self.widget.clrcoord.highlight == len(self.widget.clrcoord.classColor) - 1:
                    self.widget.clrcoord.highlight = 0
                else:
                    self.widget.clrcoord.highlight += 1
            elif e.key() == Qt.Key_R:
                for k in self.widget.clrcoord.classColor.keys():
                    self.widget.clrcoord.classColor[k] = np.append(np.random.random(3),
                                                                   self.widget.clrcoord.classColor[k][3])
            elif e.key() == Qt.Key_Return:
                highlighted = list(self.widget.clrcoord.classIsVisible.keys())[self.widget.clrcoord.highlight]
                if self.widget.clrcoord.classIsVisible[highlighted] == True:
                    self.widget.clrcoord.classIsVisible[highlighted] = False
                else:
                    self.widget.clrcoord.classIsVisible[highlighted] = True
            self.widget.update()


    def initMenubar(self):
        mainMenu = self.menuBar()
        fileMenu = mainMenu.addMenu('File')
        editMenu = mainMenu.addMenu('Edit')
        viewMenu = mainMenu.addMenu('View')
        toolsMenu = mainMenu.addMenu('Tools')

        openButton = QAction(QtGui.QIcon.fromTheme("document-open"), 'Open', self)
        openButton.setShortcut('Ctrl+O')
        openButton.setStatusTip('Open a file')
        openButton.triggered.connect(self.openFileDialog)
        fileMenu.addAction(openButton)

        exitButton = QAction(QtGui.QIcon.fromTheme("application-exit"), 'Exit', self)
        exitButton.setShortcut('Ctrl+Q')
        exitButton.setStatusTip('Exit application')
        exitButton.triggered.connect(self.close)
        fileMenu.addAction(exitButton)

    def openFileDialog(self):
        fname = QFileDialog.getOpenFileName(self, 'Open file', 'c:\\', "CSV files (*.csv)")[0]
        if len(fname) > 0:
            self.widget = OpenGLWidget(self, fname)
            self.setCentralWidget(self.widget)
            self.update()


if __name__ == '__main__':
    app = QApplication(sys.argv)
    ex = App()
    sys.exit(app.exec_())

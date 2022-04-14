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
        self.clrcoord.screen_width = screen_width
        self.clrcoord.screen_height = screen_height
        self.clrcoord.r = r
        self.rectangles = {}
        self.randam_rects = dict()
        for key in self.clrcoord.data.keys():
            self.rectangles[key] = [(None, None), (None, None)]
        self.isReverse = False
        self.x = None


    def setWindow(self, left, right, bottom, top):
        gl.glMatrixMode(gl.GL_PROJECTION)
        gl.glLoadIdentity()
        glu.gluOrtho2D(left, right, bottom, top)

    def setViewport(self, left, right, bottom, top):
        top = top - bottom
        right = right - left
        if left < 0:
            left = 0
        if right < 0:
            right = 0
        if top < 0:
            top = 0
        if bottom < 0:
            bottom = 0
        gl.glViewport(left, bottom, right, top)

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
            self.setWindow(-1.5 * self.clrcoord.r, 1.5 * self.clrcoord.r,
                           -1.5 * self.clrcoord.r, 1.5 * self.clrcoord.r)
            self.setViewport(int((self.clrcoord.screen_width - self.clrcoord.screen_height) / 2),
                             int(self.clrcoord.screen_width - (
                                         self.clrcoord.screen_width - self.clrcoord.screen_height) / 2),
                             0, int(self.clrcoord.screen_height))
            self.clearScreen()

            self.clrcoord.drawCoord()


            self.clrcoord.drawData(self.x)
            self.x = None

            self.clrcoord.drawLabels()


            self.clrcoord.isintersect(self.rectangles)
            self.clrcoord.drawRectangles(self.rectangles)
            self.clrcoord.computeAccuracy()
            self.clrcoord.drawConfMatrix()

            self.clrcoord.drawRectangles(self.clrcoord.bestRects)
            self.clrcoord.computeAccuracyRand()
            self.clrcoord.drawConfMatrixRand()


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
        gl.glEnable(gl.GL_LINE_SMOOTH)
        gl.glHint(gl.GL_LINE_SMOOTH_HINT, gl.GL_NICEST)
        gl.glEnable(gl.GL_POINT_SMOOTH)
        gl.glHint(gl.GL_POINT_SMOOTH_HINT, gl.GL_NICEST)
        gl.glEnable(gl.GL_BLEND);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA)

    def mousePressEvent(self, QMouseEvent):
        highlighted = list(self.clrcoord.classIsVisible.keys())[self.clrcoord.highlight]
        if QMouseEvent.button() == Qt.LeftButton:
            x = QMouseEvent.pos().x()
            y = self.clrcoord.screen_height - QMouseEvent.pos().y()
            x_real = (x - self.clrcoord.screen_width / 2) * ((3 * self.clrcoord.r) / self.clrcoord.screen_height)
            y_real = (y - self.clrcoord.screen_height / 2) * ((3 * self.clrcoord.r) / self.clrcoord.screen_height)
            if self.rectangles[highlighted][0][0] is None:
                self.rectangles[highlighted][0] = (x_real, y_real)
            elif self.rectangles[highlighted][1][0] is None:
                self.rectangles[highlighted][1] = (x_real, y_real)
        else:
            self.rectangles[highlighted] = [(None, None), (None, None)]
        self.update()

    def clearRectangles(self):
        self.rectangles = {}
        for key in self.clrcoord.data.keys():
            self.rectangles[key] = [(None, None), (None, None)]

    def setColors(self, color):
        highlighted = list(self.clrcoord.classIsVisible.keys())[self.clrcoord.highlight]
        self.clrcoord.classColor[highlighted] = np.array([color[0] / 255.0,
                                                         color[1] / 255.0,
                                                         color[2] / 255.0,
                                                         self.clrcoord.classColor[highlighted][3]])


    def setX(self, x):
        self.x = x


    def btnstate(self, b):
        if b.text() == "Button1":
            if b.isChecked() == True:
                print(b.text()+" is selected")
            else:
                print(b.text()+" is deselected")
        if b.text() == "Button2":
            if b.isChecked() == True:
                print(b.text()+" is selected")
            else:
                print(b.text()+" is deselected")
        self.update()


class App(QMainWindow):
    def __init__(self):
        super().__init__()
        self.data = None

        self.setWindowTitle("Circular Coordinates")
        self.setGeometry(0, 0, screen_width, screen_height)
        qtRectangle = self.frameGeometry()
        centerPoint = QDesktopWidget().availableGeometry().center()
        qtRectangle.moveCenter(centerPoint)
        self.move(qtRectangle.topLeft())
        self.widget = None
        self.initMenubar()
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
            elif e.key() == Qt.Key_D:
                self.widget.clrcoord.detectRects()
            elif e.key() == Qt.Key_X:
                if not self.widget.isReverse:
                    self.widget.isReverse = True
                else:
                    self.widget.isReverse = False
            elif e.key() == Qt.Key_1:
                if self.widget.isReverse == True:
                    self.widget.setX(1)
            elif e.key() == Qt.Key_2:
                if self.widget.isReverse == True:
                    self.widget.setX(2)
            elif e.key() == Qt.Key_3:
                if self.widget.isReverse == True:
                    self.widget.setX(3)
            elif e.key() == Qt.Key_4:
                if self.widget.isReverse == True:
                    self.widget.setX(4)
            elif e.key() == Qt.Key_5:
                if self.widget.isReverse == True:
                    self.widget.setX(5)
            elif e.key() == Qt.Key_6:
                if self.widget.isReverse == True:
                    self.widget.setX(6)
            elif e.key() == Qt.Key_7:
                if self.widget.isReverse == True:
                    self.widget.setX(7)
            elif e.key() == Qt.Key_8:
                if self.widget.isReverse == True:
                    self.widget.setX(8)
            elif e.key() == Qt.Key_9:
                if self.widget.isReverse == True:
                    self.widget.setX(9)
            self.widget.update()

    def clearRectangles(self):
        if self.widget is None:
            return
        self.widget.clearRectangles()
        self.widget.update()

    def setColors(self):
        if self.widget is None:
            return
        highlighted = list(self.widget.clrcoord.classIsVisible.keys())[self.widget.clrcoord.highlight]
        curColor = self.widget.clrcoord.classColor[highlighted]
        curColor = QtGui.QColor(int(curColor[0] * 255), int(curColor[0] * 255), int(curColor[0] * 255), 255)
        colorDialog = QColorDialog.getColor()

        if colorDialog.isValid():
            color = colorDialog.getRgb()
            self.widget.setColors(color)
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

        colorButton = QAction(QtGui.QIcon.fromTheme("application-exit"), 'Set class color', self)
        colorButton.setStatusTip('Set the color of the selected class')
        colorButton.triggered.connect(self.setColors)
        toolsMenu.addAction(colorButton)

        clearButton = QAction(QtGui.QIcon.fromTheme("application-exit"), 'Clear rectangles', self)
        clearButton.setShortcut('Ctrl+W')
        clearButton.setStatusTip('Remove all rectangles from the screen')
        clearButton.triggered.connect(self.clearRectangles)
        toolsMenu.addAction(clearButton)

        #viewStatAct = QAction("Select class", self, checkable=True)
        #viewStatAct.setStatusTip("Select class")
        #viewStatAct.setChecked(True)
        #viewStatAct.triggered.connect(lambda:self.toggleMenu(viewStatAct))
        #viewMenu.addAction(viewStatAct)


    def openFileDialog(self):
        fname = QFileDialog.getOpenFileName(self, 'Open file', 'c:\\', "CSV files (*.csv)")[0]
        if len(fname) > 0:
            self.widget = OpenGLWidget(self, fname)
            self.setCentralWidget(self.widget)
            self.update()

    def toggleMenu(self, viewact):
        if self.widget is None:
            return
        for n, k in enumerate(self.widget.clrcoord.classIsVisible.keys()):
            highlighted = list(self.widget.clrcoord.classIsVisible.keys())[n]
            if viewact.text() == "Select class":
                viewact.setText('c' + str(n) + ':' + k)
                if viewact.isChecked():
                    self.widget.clrcoord.classIsVisible[highlighted] = False
                else:
                    self.widget.clrcoord.classIsVisible[highlighted] = True
            else:
                viewact = QAction('c' + str(n) + ':' + k, self, checkable=True)
                if viewact.isChecked():
                    self.widget.clrcoord.classIsVisible[highlighted] = False
                else:
                    self.widget.clrcoord.classIsVisible[highlighted] = True
        #self.widget.toggleMenu()
        self.widget.update()

if __name__ == '__main__':
    r = 1
    screen_width = 1024
    screen_height = 720
    app = QApplication(sys.argv)
    ex = App()
    sys.exit(app.exec_())

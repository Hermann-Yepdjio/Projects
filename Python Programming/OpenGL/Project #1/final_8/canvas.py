# -*- coding: utf-8 -*-
"""
Created on Sat Mar  2 19:17:08 2019

@author: SongJ
"""

import OpenGL.GL as gl
import OpenGL.GLUT as glut
import OpenGL.GLU as glu
import numpy as np

class Point2(object):
    def __init__(self, x = 0.0, y = 0.0):
        self.x = x
        self.y = y
    def setCP(self, x, y):
        self.x = x
        self.y = y
    def __repr__(self):
        return "Point2: (%f, %f)" % (self.x, self.y)
    def draw(self):
        gl.glBegin(gl.GL_POINTS)
        gl.glVertex2f(self.x, self.y)
        gl.glEnd()
        gl.glFlush()

class Canvas(object):
    def __init__(self, screen_width, screen_height, windowTitle):
        glut.glutInit()
        glut.glutInitDisplayMode(glut.GLUT_DOUBLE | glut.GLUT_RGBA)
        glut.glutInitWindowSize(int(screen_width), int(screen_height))
        glut.glutInitWindowPosition(100, 150)
        glut.glutCreateWindow(bytes(windowTitle, 'ascii'))
        self.setWindow(0, screen_width, 0, screen_height)
        self.setViewport(0, int(screen_width), 0, int(screen_height))
        # Current 2d point
        self.CP = Point2(x = 0.0, y = 0.0)
        # Current direction
        self.CD = 0.0
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
    def setColor(self, r, g, b, alpha = 1.0):
        gl.glColor4f(r, g, b, alpha)
    def moveTo(self, *args, **kwds):
        if len(args) == 2:
            self.CP.setCP(args[0], args[1])
        elif len(args == 1) and isinstance(args[0], Point2):
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
            angle -=  angleInc
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
    def drawRect(self, lb, rt):
        assert isinstance(lb, tuple), "lb must be a tuple."
        assert isinstance(rt, tuple), "rt must be a tuple."
        gl.glBegin(gl.GL_LINE_LOOP)
        gl.glVertex2fv((lb[0], lb[1]))
        gl.glVertex2fv((rt[0], lb[1]))
        gl.glVertex2fv((rt[0], rt[1]))
        gl.glVertex2fv((lb[0], rt[1]))
        gl.glEnd()
        gl.glFlush()

def display():
    cvs.setWindow(-1, 1, -1, 1)
    cvs.setViewport(int((screen_width - screen_height) / 2),
                    int(screen_width - (screen_width - screen_height) / 2),
                    0, int(screen_height))
    cvs.clearScreen()
    cvs.setBackgroundColor(0.95, 0.95, 0.95)
    cvs.setColor(0.8, 0.3, 0.2)
    cvs.drawCoord(-0.9, 0.9, -0.9, 0.9)
    gl.glPointSize(3)
    p = Point2(x = 0.3, y = 0.5)
    p.draw()
    cvs.ngon(3, 0.0, 0.0, 0.8, 90)
    cvs.drawCircle(Point2(), 0.8, 90)
    #cvs.drawArc(Point2(), 0.6, 90, 120)
    #cvs.drawEllipse(0.0, 0.0, 0.8, 0.5, 90)
    #cvs.setColor(0.0, 0.0, 0.0)
    #cvs.drawString2(0.0, 0.0, "test0001")
    cvs.drawRect((0.1, 0.2), (0.9, 0.8))
    #cvs.clearScreen()
    glut.glutSwapBuffers()


def reshape(width, height):
    global screen_width
    global screen_height
    screen_width = width
    screen_height = height

def keyboard(key, x, y):
    pass

if __name__ == '__main__':
    screen_width = 720
    screen_height = 480
    cvs = Canvas(screen_width, screen_height, 'test0001')
    #cvs.ngon(5, 0.0, 0.0, 0.8, 90)
    #cvs.drawString2(0.5, 0.5, "test0001")
    glut.glutReshapeFunc(reshape)
    glut.glutDisplayFunc(display)
    glut.glutKeyboardFunc(keyboard)
    glut.glutMainLoop()
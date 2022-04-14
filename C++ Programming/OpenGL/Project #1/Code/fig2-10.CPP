#include <windows.h>
#include <gl/Gl.h>
#include <gl/Glu.h>
#include <iostream>
#include "glut.h"
#define TWOPI 2 * 3.14159265
//Point2 Arr[];
// this includes code from Figs. 3.25 3.26 3.27 3.28 
// and page 101
using namespace std;

//<<Support Classes for Canvas>>>
class Point2   //single point w/ floating point coordinates
{
public:
	Point2() { x = y = 0.0f; } //constructor 1
	Point2(float xx, float yy) { x = xx; y = yy; } //constructor 2
	void set(float xx, float yy) { x = xx; y = yy; }
	float getX() { return x; }
	float getY() { return y; }
	void draw(void)
	{
		glBegin(GL_POINTS); //draw this point
		glVertex2f((GLfloat)x, (GLfloat)y);
		glEnd();
	}
private:
	float x, y;
};

class IntRect  //aligned rectangle with integer coordinates, used for viewport
{
public:
	IntRect() { l = 0; r = 100; b = 0; t = 100; } //constructors
	IntRect(int left, int right, int bottom, int top)
	{
		l = left; r = right; b = bottom; t = top;
	}
	void set(int left, int right, int bottom, int top)
	{
		l = left; r = right; b = bottom; t = top;
	}
	void draw(void); //draw this rectangle using OpenGL
	int getL(void)
	{
		return l;
	}
	int getR(void)
	{
		return r;
	}
	int getT(void)
	{
		return t;
	}
	int getB(void)
	{
		return b;
	}

private:
	int l, r, b, t;
};

class RealRect   //simlar to IntRect but w/ floating points & used for world window
{
public:
	RealRect() { l = 0; r = 100; b = 0; t = 100; } //constructors
	RealRect(float left, float right, float bottom, float top)
	{
		l = left; r = right; b = bottom; t = top;
	}
	void set(float left, float right, float bottom, float top)
	{
		l = left; r = right; b = bottom; t = top;
	}
	float getL(void)
	{
		return l;
	}
	float getR(void)
	{
		return r;
	}
	float getT(void)
	{
		return t;
	}
	float getB(void)
	{
		return b;
	}
	void draw(void); //draw this rectangle using OpenGL
private:
	float l, r, b, t;
};


//<<End Support Classes>>>



class Canvas
{
public:
	Canvas(int width, int height, char* windowTitle); //constructor
	void setWindow(float l, float r, float b, float t);
	void setViewport(int l, int r, int b, int t);
	IntRect getViewport(void); //divulge the viewport data
	RealRect getWindow(void); // divulge the window data
	float getWindowAspectRatio(void);
	void clearScreen();
	void setBackgroundColor(float r, float g, float b);
	void setColor(float r, float g, float b);
	void lineTo(float x, float y);
	void lineTo(Point2 p);
	void moveTo(float x, float y);
	void moveTo(Point2 p);
	void moveRel(float dx, float dy);
	void drawTween(Point2 A[], Point2 B[], int n, float t);
	void drawPolygonTween(Point2 A[], Point2 B[], int n, float t);

private:
	Point2 CP;         //current position in the world
	IntRect viewport;  //the current window
	RealRect window;   //the current viewport

};

//<<moveTo>>
//changes current point
void Canvas::moveTo(float x, float y)
{
	CP.set(x, y);
}

//<<lineTo>>
//draws a line from current point to new point
void Canvas::lineTo(float x, float y)
{
	glBegin(GL_LINES);
	glVertex2f((GLfloat)CP.getX(), (GLfloat)CP.getY());
	glVertex2f((GLfloat)x, (GLfloat)y); //draw the line
	glEnd();
	CP.set(x, y); //update current point to new point
	glFlush();
}

//<<setWindow>>
void Canvas::setWindow(float l, float r, float b, float t)
{
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluOrtho2D((GLdouble)1, (GLdouble)r, (GLdouble)b, (GLdouble)t);
	window.set(l, r, b, t);
}

//<<setViewport>>
void Canvas::setViewport(int l, int r, int b, int t)
{
	glViewport(l, b, r - l, t - b);
	viewport.set(l, r, b, t);
}

IntRect Canvas::getViewport(void)
{
	return viewport;
}

RealRect Canvas::getWindow(void)
{
	return window;
}

void Canvas::clearScreen(void)
{
	glClear(GL_COLOR_BUFFER_BIT);
}

void Canvas::setBackgroundColor(float r, float g, float b)
{
	glClearColor(r, g, b, 0.0);   //4th variable level of transparency, may need to change 
}

void Canvas::setColor(float r, float g, float b)
{
	glColor3f(r, g, b);
}

void Canvas::lineTo(Point2 p)
{
	glBegin(GL_LINES);
	glVertex2f((GLfloat)CP.getX(), (GLfloat)CP.getY());
	glVertex2f((GLfloat)p.getX(), (GLfloat)p.getY());
	glEnd();
	CP.set(p.getX(), p.getY());
	glFlush();
}

Canvas::Canvas(int width, int height, char* windowTitle)
{
	char* argv[1];  //dummy argument list for glutinit()
	char dummyString[8];
	argv[0] = dummyString;  //hook up the pointer
	int argc = 1;

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
	glutInitWindowSize(width, height);
	glutInitWindowPosition(20, 20);
	glutCreateWindow(windowTitle);
	setWindow(0, (float)width, 0, (float)height); // default world window
	setViewport(0, width, 0, height); //default viewport
	CP.set(0.0f, 0.0f); //initialize the cp to (0,0)
}

void Canvas::moveTo(Point2 p)  //moves current point CP to point p object 
{
	float x1, y1;
	x1 = p.getX();
	y1 = p.getY();
	CP.set(x1, y1);
}

float Canvas::getWindowAspectRatio(void)  //calculates aspect ratio of world window
{
	float width, height, aspectRatio;
	width = window.getR() - window.getL();
	height = window.getT() - window.getB();
	aspectRatio = width / height;
	return aspectRatio;
}

void Canvas::moveRel(float dx, float dy)
{
	CP.set(CP.getX() + dx, CP.getY() + dy);
}

Canvas cvs(640, 480, "try out Canvas"); //global canvas object

void ngon(int n, float cx, float cy, float radius, float rotAngle)
{
	//assumes global Canvas object, cvs
	if (n<3) return; //bad number of sides
	double angle = rotAngle * 3.14159265 / 180; //initial angle
	double angleInc = 2 * 3.14159265 / n;  //angle increment
	cvs.moveTo(radius * cos(angle) + cx, radius * sin(angle) + cy);
	for (int k = 0; k<n; k++) //repeat n times
	{
		angle += angleInc;
		cvs.lineTo(radius * cos(angle) + cx, radius + sin(angle) + cy);
	}
}
class array1   //to contain values for tweening operations
{
	public: Point2 A[500];
};
array1 drawElipse(Point2 C,float width, float height, int n)
{
	array1 Arr;
	glBegin(GL_LINE_STRIP);
	int count = 0;
	for (double t = 0; t <= TWOPI; t += TWOPI / n)
	{
		glVertex2f(C.getX() + width * cos(t), C.getY() + height * sin(t));
		Arr.A[count]=Point2(C.getX() + width * cos(t), C.getY() + height * sin(t));
		count++;
	}
	glEnd();
	glFlush();
	return Arr;
}

 array1 drawArc(Point2 center, float radius, float startAngle, float sweep)
{ // startAngle and sweep are in degrees
	const int n = 30; // number of intermediate segments in arc
	array1 Arr;
	float angle = startAngle * 3.14159265 / 180; // initial angle in radians
	float angleInc = sweep * 3.14159265 / (180 * n); // angle increment
	float cx = center.getX(), cy = center.getY();
	cvs.moveTo(cx + radius * cos(angle), cy + radius * sin(angle));
	Arr.A[1] = Point2(cx + radius * cos(angle), cy + radius * sin(angle));
	glBegin(GL_LINE_STRIP);
	for (int k = 1; k < n; k++, angle += angleInc)
	{
		glVertex2f(cx + radius * cos(angle), cy + radius * sin(angle));
		Arr.A[k+1]=Point2(cx + radius * cos(angle), cy + radius * sin(angle)); //copy currrent point in array that will be used for tweening
	}
		
	glEnd();
	glFlush();
	cvs.moveTo(cx + radius * cos(angle), cy + radius * sin(angle));
	return Arr;
}
array1 drawRoundRect(Point2 C, float W, float H, float g)
{
	array1 Arr;
	GLfloat r = W*g; // radius of circles at corners
	GLfloat x = C.getX() + W / 2 - r; // x coordinate of center of first arc
	GLfloat y = C.getY() + H / 2 - r; // y coordinate of center of first arc
	GLfloat x2 = C.getX() - (W / 2 - r); // x coordinate of center of second arc
	GLfloat y2 = C.getY() - (H / 2 - r); // y coordinate of center of third arc
	array1 A1 = drawArc(Point2(x, y), r, 350, 90);
	cvs.lineTo(Point2(x2, C.getY() + H / 2));
	array1 A2 = drawArc(Point2(x2, y), r, 90, 90);
	cvs.lineTo(Point2(C.getX() - W / 2, y2));
	array1 A3 = drawArc(Point2(x2, y2), r, 170, 90);
	cvs.lineTo(Point2(x, C.getY() - (H / 2)));
	array1 A4 = drawArc(Point2(x, y2), r, 260, 90);
	cvs.lineTo(Point2(C.getX() + W / 2, y));
	for (int i = 0;i < 120;i++)
	{	
		if (i < 30)
			Arr.A[i] = A1.A[i+1];
		else if (i < 60)
			Arr.A[i] = A2.A[i - 29];
		else if (i < 90)
			Arr.A[i] = A3.A[i - 59];
		else if (i < 120)
			Arr.A[i] = A4.A[i - 89];
	}
	return Arr;
}
void drawEars()
{
	Point2 C1(104, 280);
	Point2 C2(536, 280);
	drawElipse(C1, 20, 35, 100);
	drawElipse(C2, 20, 35, 100);

}
void drawMouth() //draw mouth
{
	
	Point2 C(320, 150);  //center of Mouth
	drawRoundRect(C, 200, 20, .055);
}
void drawEyes() //draw eyes
{
	Point2 C1(230, 290);
	Point2 C2(410, 290);
	glBegin(GL_POLYGON); //draw left pupil
	for (double t = 0; t <= TWOPI; t += TWOPI / 100)
	{
		glVertex2f(C1.getX() + 12 * cos(t), C1.getY() + 15 * sin(t));
	}
	glEnd();
	glBegin(GL_POLYGON); //draw right pupil
	for (double t = 0; t <= TWOPI; t += TWOPI / 100)
	{
		glVertex2f(C2.getX() + 12 * cos(t), C2.getY() + 15 * sin(t));
	}
	glEnd();
	drawElipse(C1, 45, 25, 100);
	drawElipse(C2, 45, 25, 100);

}


void drawEyebrows() //draw eyebrows
{
	Point2 C1(230, 330);  //center of left eyebrow
	Point2 C2(410, 330);	 //center of right eyebrow
	//drawRoundRect(C1, 90, 10, .05);
	//drawRoundRect(C2, 90, 10, .05);
	drawElipse(C1, 45, 5, 100);
	drawElipse(C2, 45, 5, 100);
}
void drawNose()  //draw nose
{
	Point2 C(320, 220); //center of nose
	drawRoundRect(C, 60, 35, .25);
}
void drawHead()  //draw head
{
	drawArc(Point2{ 320,240 }, 200, 150, 260); //draw bottom part
	glBegin(GL_POLYGON); //draw top part containing hair
	
		//glVertex2f(320 - 200 * sin(3.14159265 / 3), 240 + 200 * cos(3.14159265 / 3));
		//glVertex2f(220, 390);
		glVertex2f(420, 390);
		drawArc(Point2{ 320,240 }, 200, 30, 110);
	glBegin(GL_POLYGON);
		glVertex2f(220, 390);
		drawArc(Point2{ 320,240 }, 200, 130, 30);

	//glEnd();		
	
}

float lerp(float a, float b, float t)
{
	return a + (b - a) * t; // return a float
}
Point2 Tween(Point2 A, Point2 B, float t)
{

	float x = lerp(A.getX(), B.getX(), t);
	float y = lerp(A.getY(), B.getY(), t);
	return Point2(x, y);
}
void Canvas::drawPolygonTween(Point2 A[], Point2 B[], int n, float t)
{   // draw the tween at time t between polylines A and B
	glBegin(GL_POLYGON);
		for (int i = 0; i < n; i++)
		{
			Point2 P= Tween(A[i], B[i], t);
			if (i == 0) glVertex2f(P.getX(), P.getY());
			else 		glVertex2f(P.getX(), P.getY());
		}
		glVertex2f(Tween(A[0], B[0], t).getX(), Tween(A[0], B[0], t).getY());
	glEnd();
}
void Canvas::drawTween(Point2 A[], Point2 B[], int n, float t)
{   // draw the tween at time t between polylines A and B
	for (int i = 0; i < n; i++)
	{
		Point2 P;
		P = Tween(A[i], B[i], t);
		if (i == 0) 	cvs.moveTo(P.getX(), P.getY());
		else 		cvs.lineTo(P.getX(), P.getY());
	}
	cvs.lineTo(Tween(A[0], B[0], t).getX(), Tween(A[0], B[0], t).getY());
}
void drawFace() //draw whole face
{
	drawHead();
	drawEars();
	drawNose();
	drawEyes();
	drawMouth();
	drawEyebrows();
	drawArc(Point2{ 320,240 }, 200, 150, 260); //draw bottom part
}
void drawHead1(char ch)  //draw head
{
	
	Point2 p1 = Point2(420, 390);
	Point2 p2 = Point2(220, 390);
	glBegin(GL_POLYGON); //draw top part containing hair
	glVertex2f(p1.getX(), p1.getY());
	array1 Arr1 = drawArc(Point2{ 320,240 }, 200, 30, 110); 
	Arr1.A[0] = p1;
	glBegin(GL_POLYGON); // draw top part containing hair
	glVertex2f(p2.getX(), p2.getY());
	array1 Arr2 = drawArc(Point2{ 320,240 }, 200, 110, 50);
	Arr2.A[0] = p2;
	Point2 Arr3[50];
	Point2 Arr4[50];
	for (int i = 0; i < 31;i++)
	{
		Arr3[i] = Arr1.A[i];
		Arr4[i] = Arr2.A[i];
	}
	Arr3[0] = Point2(390, 240 + pow(pow(200, 2) - pow(70, 2), 0.5));
	Arr4[0] = Point2(240, 240 + pow(pow(200, 2) - pow(80, 2), 0.5));
	for (int i = 26; i < 31;i++)
	{
		Arr3[i]= Point2(250, 240 + pow(pow(200, 2) - pow(70, 2), 0.5));
	}
	if (ch == 'h')
	{
		for (double t = 0.0; t <= 1; t += 0.1)
		{
			cvs.clearScreen();
			glutSwapBuffers();
			drawEars();
			drawNose();
			drawEyes();
			drawMouth();
			drawEyebrows();
			drawArc(Point2{ 320,240 }, 200, 150, 260); //draw bottom part
			cvs.drawPolygonTween(Arr1.A, Arr3, 31, t);
			cvs.drawPolygonTween(Arr2.A, Arr4, 31, t);
			glFlush();
			_sleep(200);

		}
	}
	else if (ch == 'j')
	{
		for (double t = 0.0; t <= 1; t += 0.1)
		{
			cvs.clearScreen();
			glutSwapBuffers();
			drawEars();
			drawNose();
			drawEyes();
			drawMouth();
			drawEyebrows();
			drawArc(Point2{ 320,240 }, 200, 150, 260); //draw bottom part
			cvs.drawPolygonTween(Arr3, Arr1.A, 31, t);
			cvs.drawPolygonTween(Arr4, Arr2.A, 31, t);
			glFlush();
			_sleep(200);

		}
	}
	
}
void drawMouth1(char ch) //draw mouth
{

	Point2 C(320, 150);  //center of Mouth
	array1 Arr1 = drawRoundRect(C, 200, 20, .055);
	array1 Arr2 = drawRoundRect(C, 100, 50, .15);
	if (ch == 'm')
	{
		for (double t = 0.0; t <= 1; t += 0.1)
		{
			cvs.clearScreen();
			drawHead();
			drawEars();
			drawNose();
			drawEyes();
			drawEyebrows();
			drawArc(Point2{ 320,240 }, 200, 150, 260); //draw bottom part
			glutSwapBuffers();
			cvs.drawTween(Arr1.A, Arr2.A, 120, t);
			_sleep(200);
			glFlush();

		}
	}
	else if (ch == 'n' )
	{
		for (double t = 0.0; t <= 1; t += 0.1)
		{
			cvs.clearScreen();
			drawHead();
			drawEars();
			drawNose();
			drawEyes();
			drawEyebrows();
			drawArc(Point2{ 320,240 }, 200, 150, 260); //draw bottom part
			glutSwapBuffers();
			cvs.drawTween(Arr2.A, Arr1.A, 120, t);
			_sleep(200);
			glFlush();

		}
	}
	
}
void drawEyes1(char ch) //draw eyes
{
	Point2 C1(230, 290);
	Point2 C2(410, 290);
	int count=0;
	int count2= 0;
	array1 Arr;
	array1 Arr0;
	array1 Arr1;
	array1 Arr2;
	glBegin(GL_POLYGON); //draw left pupil
	for (double t = 0; t <= TWOPI; t += TWOPI / 100)
	{
		glVertex2f(C1.getX() + 12 * cos(t), C1.getY() + 15 * sin(t));
		Arr1.A[count]=Point2(C1.getX() + 12 * cos(t), C1.getY() + 15 * sin(t));
		if (count<51)
			Arr.A[count] = Point2(275 - count*180/101, 290); //array of points after tweening left eye
		else
		{
			Arr.A[100-count2] = Arr.A[count2];
			count2++;
		}
		count++;
	}
	glEnd();
		count = 0;
		count2 = 0;
	glBegin(GL_POLYGON); //draw right pupil
	for (double t = 0; t <= TWOPI; t += TWOPI / 100)
	{
		glVertex2f(C2.getX() + 12 * cos(t), C2.getY() + 15 * sin(t));
		Arr2.A[count] = Point2(C2.getX() + 12 * cos(t), C2.getY() + 15 * sin(t));
		if (count<51)
			Arr0.A[count] = Point2(455 - count*180/101, 290);	//array of points after tweening right eye
		else
		{
			Arr0.A[100-count2] = Arr0.A[count2];
			count2++;
		}
		count++;
	}
	glEnd();
	array1 Arr3= drawElipse(C1, 45, 25, 100);
	array1 Arr4= drawElipse(C2, 45, 25, 100);
	if (ch == 'e' )
	{
		for (double t = 0.0; t <= 1; t += 0.1) //tweening
		{
			cvs.clearScreen();
			glutSwapBuffers();
			drawHead();
			drawEars();
			drawNose();
			drawMouth();
			drawEyebrows();
			drawArc(Point2{ 320,240 }, 200, 150, 260); //draw bottom part
			cvs.drawPolygonTween(Arr1.A, Arr.A, 100, t);  //tween left pupil
			cvs.drawPolygonTween(Arr2.A, Arr0.A, 100, t); //tween right pupil
			cvs.drawTween(Arr3.A, Arr.A, 101, t);	 //tween left eye
			cvs.drawTween(Arr4.A, Arr0.A, 101, t); //tween right eye
			_sleep(200);
			glFlush();

		}
	}
	else if (ch == 'r')
	{
		for (double t = 0.0; t <= 1; t += 0.1) //tweening
		{
			cvs.clearScreen();
			glutSwapBuffers();
			drawHead();
			drawEars();
			drawNose();
			drawMouth();
			drawEyebrows();
			drawArc(Point2{ 320,240 }, 200, 150, 260); //draw bottom part
			cvs.drawPolygonTween(Arr.A, Arr1.A, 100, t);  //tween left pupil
			cvs.drawPolygonTween(Arr0.A, Arr2.A, 100, t); //tween right pupil
			cvs.drawTween(Arr.A, Arr3.A, 101, t);	 //tween left eye
			cvs.drawTween(Arr0.A, Arr4.A, 101, t); //tween right eye
			_sleep(200);
			glFlush();

		}
	}

}
void myKeyboard(unsigned char theKey, int mouseX, int mouseY)
{
	switch (theKey)
	{
	case 'h':
		drawHead1('h');
		break;
	case 'e':
		drawEyes1('e');
		break;
	case 'm':
		drawMouth1('m');
		break;
	case 'j':
		drawHead1('j');
		break;
	case 'r':
		drawEyes1('r');
		break;
	case 'n':
		drawMouth1('n');
		break;
	case 'X':
		exit(-1); //terminate the program
	default:
		break; // do nothing
	}
}



void display(void)
{
	drawFace();
	
}

void main(void)
{
	cvs.setWindow(0, 640, 0, 480);
	cvs.setViewport(0, 640, 0, 480);
	cvs.setBackgroundColor(1.0, 1.0, 1.0);
	cvs.setColor(0.0, 0.0, 0.0);
	glutKeyboardFunc(myKeyboard);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
	glutDisplayFunc(display);
	glutMainLoop();


}


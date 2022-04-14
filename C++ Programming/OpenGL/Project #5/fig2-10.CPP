#include <windows.h>
#include <gl/Gl.h>
#include <gl/Glu.h>
/*#include <iostream.h> */
#include "glut.h"


void Init()
{
	glClearColor(1.0, 1.0, 1.0, 0.0);
	glColor3f(0.0f, 0.0f, 0.0f);
	glPointSize(4.0);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluOrtho2D(1.0, 640.0, 0.0, 480.0);
}
struct GLPoint //Point type
{
	double x, y;
	GLPoint()
	{
		x = 0;
		y = 0;
	}
	GLPoint(double X, double Y)
	{
		x = X;
		y = Y;
	}
};
struct GLVector // vector type
{
	double x, y;
	GLVector()
	{
		x = 0;
		y = 0;
	}
	GLVector(double X, double Y)
	{
		x = X;
		y = Y;
	}
};
GLVector cwNormal(GLVector A)
{
	return GLVector(A.y, -A.x);
}
GLVector ccwNormal(GLVector A)
{
	return GLVector(-A.y, A.x);
}
double dot(GLVector A, GLVector B)
{
	return A.x*B.x + A.y*B.y;
}

GLPoint intersection(GLPoint A, GLPoint B, GLPoint C, GLPoint D) //return intersection point between 2 lines
{
	GLVector b(B.x - A.x, B.y - A.y);
	GLVector c(C.x - A.x, C.y - A.y);
	GLVector d(D.x - C.x, D.y - C.y);
	if (dot(b, ccwNormal(d)) != 0 && dot(d, ccwNormal(b)) != 0)
		return GLPoint(A.x + b.x*(dot(c, ccwNormal(d))) / dot(b, ccwNormal(d)), A.y + b.y*(dot(c, ccwNormal(d))) / dot(b, ccwNormal(d)));
	else
		return GLPoint(0, 0);

}
class LineSeg //Line segment type
{
public: GLPoint first, second;
		LineSeg() //default constructor
		{
			first = GLPoint(0, 0);
			second = GLPoint(0, 0);
		}
		LineSeg(GLPoint f, GLPoint s) //constructor
		{
			first = f;
			second = s;
		}
		GLVector ccwNorm() //return counter-clockwise norm to LineSegment
		{
			GLVector A = GLVector(second.x - first.x, second.y - first.y);
			return GLVector(-A.y, A.x);
		}
		GLVector cwNorm() //return clockwise norm to LineSegment
		{
			GLVector A = GLVector(second.x - first.x, second.y - first.y);
			return GLVector(A.y, -A.x);
		}
};
class LineList //LineList type
{
public: GLint num;
		LineSeg L[10];
		LineList(GLint n) // constructor
		{

			num = n;
			L[n];
		}
		LineSeg line(GLint i) //returns the line at index i in the list
		{
			return L[i];
		}
		void addLine(LineSeg Line, GLint index) //add a line in the list at index index
		{
			L[index] = Line;
		}

};
int chopCI(double numer, double denom, double& tIn, double& tOut)
{
	double tHit;
	if (denom < 0) // ray is entering
	{
		tHit = numer / denom;
		if (tHit > tOut) return 0; // early out
		else if (tHit > tIn) tIn = tHit; // take larger t
	}
	else if (denom > 0) // ray is exiting
	{
		tHit = numer / denom;
		if (tHit < tIn) return 0; // early out
		if (tHit < tOut) tOut = tHit; // take smaller t
	}
	else // denom is 0: ray is parallel
		if (numer <= 0) return 0; // missed the line
	return 1; // CI is still non-empty
}
int CyrusBeckClip(LineSeg& seg, LineList& L)
{
	double numer, denom;
	double tIn = 0.0, tOut = 1.0;
	GLVector c, tmp;
	c = GLVector(seg.second.x - seg.first.x, seg.second.y - seg.first.y);
	for (int i = 0; i < L.num; i++) // chop at each bounding line
	{
		tmp = GLVector(L.line(i).second.x - seg.first.x, L.line(i).second.y - seg.first.y);
		numer = dot(L.line(i).ccwNorm(), tmp);
		denom = dot(L.line(i).ccwNorm(), c);
		if (!chopCI(numer, denom, tIn, tOut)) return 0;		// early out
	}
	//  adjust the endpoints of the segment; do second one 1st.
	if (tOut < 1.0) // second endpoint was altered
	{
		seg.second.x = seg.first.x + c.x * tOut;
		seg.second.y = seg.first.y + c.y * tOut;
	}
	if (tIn > 0.0) // first endpoint was altered
	{
		seg.first.x = seg.first.x + c.x * tIn;
		seg.first.y = seg.first.y + c.y * tIn;
	}
	return 1; // some segment survives

}
void drawLineSeg( LineSeg L) //draws a segment line
{
	glBegin(GL_LINE_STRIP);
		glVertex2f(L.first.x, L.first.y);
		glVertex2f(L.second.x, L.second.y);
	glEnd();
}
void Display()
{
	LineSeg Line(GLPoint(4, 4), GLPoint(200, 300));
	LineSeg Line1(GLPoint(15, 130), GLPoint(20, 170));
	LineSeg Line2(GLPoint(20, 170), GLPoint(70, 200));
	LineSeg Line3(GLPoint(70, 200), GLPoint(120, 170));
	LineSeg Line4(GLPoint(120, 170), GLPoint(130, 130));
	LineSeg Line5(GLPoint(130, 130), GLPoint(70, 90));
	LineSeg Line6(GLPoint(70, 90), GLPoint(15, 130));
	LineList LL(6);
	LL.addLine(Line1, 0);LL.addLine(Line2, 1);LL.addLine(Line3, 2);LL.addLine(Line4, 3);LL.addLine(Line5, 4);LL.addLine(Line6, 5);
	glClear(GL_COLOR_BUFFER_BIT);
	CyrusBeckClip(Line, LL);
	drawLineSeg(Line);
	drawLineSeg(Line1);
	drawLineSeg(Line2);
	drawLineSeg(Line3);
	drawLineSeg(Line4);
	drawLineSeg(Line5);
	drawLineSeg(Line6);
	glFlush();
}

void main(int argc, char** argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
	glutInitWindowSize(640, 480);
	glutInitWindowPosition(100, 150);
	glutCreateWindow("my first attempt");
	glutDisplayFunc(Display);
	Init();
	glutMainLoop();
}
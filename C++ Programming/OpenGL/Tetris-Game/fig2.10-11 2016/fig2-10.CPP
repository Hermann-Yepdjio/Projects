
#include <windows.h>
#include <gl/Gl.h>
#include <gl/Glu.h>
/*#include <iostream.h> */
#include "glut.h"
#include <time.h>

#define WIDTH 80
#define HEIGHT 45

class game {
public:
	int delay;
	float scale;
	int WinScale;
	int WinPosX;
	int WinPosY;
	int StartPosX;
	int StartPosY;
	int StartSize;
	game() {
		StartSize = 3;
		StartPosX = (int)(WIDTH / 2);
		StartPosY = (int)(HEIGHT / 2);
		delay = 100;
		scale = 1;
		WinScale = 5;
		WinPosX = 200;
		WinPosY = 100;
	}
	void init();
}settings;

class block {
public:
	int x;
	int y;
	block() {
		x = -1;
		y = -1;
	}
	void draw();
}blocks[WIDTH*HEIGHT];

class food {
public:
	int x;
	int y;
	food() {
		x = -1;
		y = -1;
	}
	void draw_food();
	void spawn();
}food;

class snake {
public:
	int direction;
	int size;
	bool turn;
	snake() {
		turn = false;
		size = settings.StartSize;
		direction = GLUT_KEY_UP;
	}
	void DrawSnake();
	void move();
	void cut();
	void eat();
}player;

void food::draw_food() {
	glColor3f(1, 1, 1);
	glVertex2f(x*settings.scale, y*settings.scale);
	glVertex2f(x*settings.scale + settings.scale, y*settings.scale);
	glVertex2f(x*settings.scale + settings.scale, y*settings.scale + settings.scale);
	glVertex2f(x*settings.scale, y*settings.scale + settings.scale);
}

void block::draw() {
	glColor3f(1, 1, 1);
	glVertex2f(x*settings.scale, y*settings.scale);
	glVertex2f(x*settings.scale + settings.scale, y*settings.scale);
	glVertex2f(x*settings.scale + settings.scale, y*settings.scale + settings.scale);
	glVertex2f(x*settings.scale, y*settings.scale + settings.scale);
}

void snake::move() {
	for (int i = size; i > 0; i--) {
		blocks[i].x = blocks[i - 1].x;
		blocks[i].y = blocks[i - 1].y;
	}
	switch (direction) {
	case GLUT_KEY_UP:
		blocks[0].y++;
		break;
	case GLUT_KEY_DOWN:
		blocks[0].y--;
		break;
	case GLUT_KEY_LEFT:
		blocks[0].x--;
		break;
	case GLUT_KEY_RIGHT:
		blocks[0].x++;
		break;
	}
	if (blocks[0].x < 0)blocks[0].x += WIDTH;
	if (blocks[0].x >= WIDTH)blocks[0].x -= WIDTH;
	if (blocks[0].y < 0)blocks[0].y += HEIGHT;
	if (blocks[0].y >= HEIGHT)blocks[0].y -= HEIGHT;
}

void snake::DrawSnake() {
	for (int i = 0; i < size; i++) {
		blocks[i].draw();
	}
}

void game::init() {
	blocks[0].x = StartPosX;
	blocks[0].y = StartPosY;
}

void snake::eat() {
	if ((blocks[0].x == food.x) && (blocks[0].y == food.y)) {
		size++;
		food.spawn();
	}
}

void snake::cut() {
	for (int i = 3; i < size; i++) {
		if ((blocks[0].x == blocks[i].x) && (blocks[0].y == blocks[i].y)) {
			size = i;
			break;
		}
	}
}

void food::spawn() {
	x = (rand() % WIDTH);
	y = (rand() % HEIGHT);
	for (int i = 0; i < player.size; i++) {
		if ((x == blocks[i].x) && (y == blocks[i].y))spawn();
	}
}

void keyboard(int key, int x, int y) {
	switch (key) {
	case GLUT_KEY_UP:
		if ((player.direction != GLUT_KEY_DOWN) && (player.turn))player.direction = GLUT_KEY_UP;
		player.turn = false;
		break;
	case GLUT_KEY_DOWN:
		if ((player.direction != GLUT_KEY_UP) && (player.turn))player.direction = GLUT_KEY_DOWN;
		player.turn = false;
		break;
	case GLUT_KEY_LEFT:
		if ((player.direction != GLUT_KEY_RIGHT) && (player.turn))player.direction = GLUT_KEY_LEFT;
		player.turn = false;
		break;
	case GLUT_KEY_RIGHT:
		if ((player.direction != GLUT_KEY_LEFT) && (player.turn))player.direction = GLUT_KEY_RIGHT;
		player.turn = false;
		break;
	}
}

void Timer(int value) {
	player.turn = true;
	player.move();
	player.cut();
	player.eat();
	glutPostRedisplay();
	glutTimerFunc(settings.delay, Timer, 0);
}

void draw() {
	glClear(GL_COLOR_BUFFER_BIT);
	glBegin(GL_QUADS);
	food.draw_food();
	player.DrawSnake();
	glEnd();
	glFlush();
	glutSwapBuffers();
}

int main(int argc, char ** argv) {
	settings.init();
	food.spawn();
	srand(time(NULL));
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB);
	glutInitWindowSize(WIDTH*settings.WinScale, HEIGHT*settings.WinScale);
	glutInitWindowPosition(settings.WinPosX, settings.WinPosY);
	glutCreateWindow("***SNAKE***");
	glutDisplayFunc(draw);
	glutTimerFunc(settings.delay, Timer, 0);
	glutSpecialFunc(keyboard);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(0, WIDTH, 0, HEIGHT, -1, 1);
	glutMainLoop();
	return(0);
}

#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int i, j;
typedef struct point
{
	int x;
	int y;
}point;

void is_point_on_e_c(point p )
{
    int result = pow(p.y, 2) - pow(p.x, 3) - 11 * p.x - 19;
    if(!result)
        printf(" Point P = (%d , %d) is on the elliptic curve E.\n", p.x, p.y);
    else
        printf(" Point P = (%d , %d) is not on the elliptic curve E.\n", p.x, p.y);
}

int mod(int a, int b)
{
	return (a % b + b) % b;
}


int inv_mod(int a, int b)
{
	for (i = 0; i < b; i++)
	{
		if ( mod(a * i, b) == 1)
			return i;
	}


}


void add_on_e_c(point p1, point p2, point *p3, int a, int b, int p)
{
	int m;
	if (p1.x != p2.x || p1.y != p2.y)
	{
		m = mod((p2.y - p1.y) * inv_mod((p2.x - p1.x), p), p);
	}
	else
		m = mod((3 * pow(p1.x, 2) + a) * inv_mod((2 * p1.y), p), p);
	p3->x = mod(pow(m, 2) - p1.x - p2.x, p);
	p3->y = mod( m * (p1.x - p3->x) - p1.y, p);
	
}

void mult_on_e_c(point* pt, int a, int b, int p, int n)
{
	point tmp_pt = *pt; //tmp_pt.x = pt->x; tmp_pt.y = pt->y;
	
	for (j = 0; j < n - 1  ; j++)
		add_on_e_c(*pt, tmp_pt, pt, a, b, p);

	
}

void compute_shared_secret(point *pt, int a, int b, int p, int A, int B)
{
	point tmp_pt = *pt; 
	mult_on_e_c(pt, a, b, p, A);
	point Alice_sent = *pt;
	
	*pt = tmp_pt;
	mult_on_e_c(pt, a, b, p, B);
	point Bob_sent = *pt;

	mult_on_e_c(pt, a, b, p, A);
	point shared_secret = *pt;

	printf("What Alice sent to Bob is : (%i , %i)\n", Alice_sent.x, Alice_sent.y);
	printf("What Bob sent to Alice is : (%i , %i)\n", Bob_sent.x, Bob_sent.y);
	printf("The shared secret is : (%i , %i)\n", shared_secret.x, shared_secret.y);

}
int main()
{
	point *pt = malloc(sizeof(point)); pt->x = 2; pt->y = 7;
	int a = 11;
	int b = 19; 
	int p = 167;
	int A = 12;
	int B = 31;


	is_point_on_e_c(*pt);

	compute_shared_secret(pt, a, b, p, A, B);

	free (pt);

}	
    

int a = 10;
int b = 5;
int c = 3;

int main()
{
	a = b++ + c++;
	b = --c + a;
	c += a + b++;
	return a + b +c;
}

#include <stdio.h>
#include <iostream>
#include "matrix.h"
using namespace std;


void main()
{

		Matrix matrix_empty();
		Matrix matrix_identity(5);
		Matrix four_by_six(4, 6);
		Matrix matrix_copy(move(matrix_identity));
		matrix_copy.print_matrix();
		matrix_identity.print_matrix();
		matrix_copy.resize(7, 8);
		four_by_six.print_matrix();
		matrix_copy.print_matrix();
		cout << matrix_copy.getHeight() << " " <<matrix_copy.getWidth() << endl;
		matrix_copy.transpose();
		matrix_copy.print_matrix();
		cout << matrix_copy.getHeight() << " " << matrix_copy.getWidth() << endl;
		Matrix matrix_temp = matrix_copy + matrix_copy;
		matrix_temp.print_matrix();
		cout << matrix_temp.getHeight() << " " << matrix_temp.getWidth() << endl;
		matrix_temp.resize(10, 3);
		matrix_copy(1, 2) = 5; 
		matrix_copy(1, 3) = 6;
		matrix_copy(1, 4) = 7;
		cout << matrix_copy(1, 4) << endl;
		matrix_copy.print_matrix();
		Matrix matrix_temp2 = matrix_copy * 2;
		matrix_temp2 *= 2;
		matrix_copy *= matrix_temp;
		matrix_temp.print_matrix();
		matrix_copy.print_matrix();
		matrix_temp2.print_matrix();
		matrix_copy = move(matrix_temp);
		matrix_copy.print_matrix();
		matrix_temp.print_matrix();
		cout << matrix_copy.getHeight() << " " << matrix_copy.getWidth() << endl;
		cout << matrix_temp.getHeight() << " " << matrix_temp.getWidth() << endl;
		
		


	system("PAUSE");

}
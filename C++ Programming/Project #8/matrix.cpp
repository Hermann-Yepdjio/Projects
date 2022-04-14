#include <stdio.h>
#include <iostream>
#include "matrix.h"

using namespace std;

	// Creates an empty matrix of size 0 times 0.
    Matrix::Matrix()
	{
		width =0;
		height=0;
		values=nullptr;
	}

    // Creates an identity matrix of size <size> times <size>.
    Matrix::Matrix(int size)
	{
		width =size;
		height=size;
		values = new double*[height];  //set values to point to an array of pointers
		for (int i = 0; i < height; i++)
			values[i] = new double[width]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values
		for (int i=0; i<size; i++) //builds the identity matrix
		{
			for(int j=0; j<size; j++)
			{
				if(i==j)
					values[i][j]=1;
				else
					values[i][j]=0;
			}
		}
	}
    
    // Creates a matrix of size <height> times <width> filled with 0s.
    Matrix::Matrix(int Height, int Width)
	{
		width =Width;
		height=Height;
		values = new double*[height]; //set values to point to an array of pointers
		for (int i = 0; i < height; i++)
			values[i] = new double[width]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values
		for (int i=0; i<height; i++) //fills the matrix with zeros
		{
			for(int j=0; j<width; j++)
			{
					values[i][j]=0;
				
			}	
		}
	}

    // Copy constructor

    Matrix::Matrix(const Matrix& copy_matrix)
	{
		width = copy_matrix.width;
		height= copy_matrix.height;
		values = new double*[height]; //set values to point to an array of pointers
		for (int i = 0; i < height; i++)
			values[i] = new double[width]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values
		for (int i=0; i<height; i++)
		{
			for(int j=0; j<width; j++)
			{
				values[i][j]= copy_matrix.values[i][j];
				
			}	
		}

	}
    
    // Move constructor
    Matrix::Matrix(Matrix&& move_matrix)
	{
		width = move_matrix.width;
		height= move_matrix.height;
		values = new double*[height]; //set values to point to an array of pointers
		for (int i = 0; i < height; i++)
			values[i] = new double[width]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values
		for (int i=0; i<height; i++)
		{
			for(int j=0; j<width; j++)
			{
				values[i][j]= move_matrix.values[i][j];
				
			}	
		}
		 //delete move_matrix
		move_matrix.values = nullptr;
		move_matrix.height = NULL;
		move_matrix.width = NULL;
	}
    
    // Destructor
    Matrix::~Matrix()
	{
		values = nullptr;
		width = NULL;
		height = NULL;
		//cout << "destructor was called" << endl;
	};

	//return width of matrix
    int Matrix::getWidth() const
	{
		return width;
	};

	//return heigth of matrix
    int Matrix::getHeight() const
	{
		return height;
	};

	//changes the size of the matrix to height1 and width1
    void Matrix::resize(int height1, int width1)
	{
		
		double** values_temp = new double*[height1]; //create a new array of pointers 
		for (int i = 0; i < height1; i++)
			values_temp[i] = new double[width1]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values_temp
		for (int i = 0; i < height1; i++)
		{
				for (int j = 0; j < width1; j++)
				{
					if (j < width && i < height)  
						values_temp[i][j] = values[i][j];
					else //fill extra cells of the matrix with zeros
						values_temp[i][j] = 0;
				}
		}
		values = values_temp;  //copy address contained in values_temp into values
		height = height1;
		width = width1;
		values_temp=nullptr; //delete values_temp
		
	}
	void Matrix::transpose()
	{
		double** values_temp = new double*[width]; //create a new array of pointers 
		for (int i = 0; i < width; i++)
			values_temp[i] = new double[height]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values_temp
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
				values_temp[j][i] = values[i][j];
		}
		values = values_temp; //copy values contained in values_temp into values
		int temp = width;
		width = height;
		height = temp;
		temp = NULL;
		values_temp=nullptr; //delete values_temp

	}


    // Copy assignment
	Matrix& Matrix::operator= (const Matrix& matrix_copy_assignment)
	{
		if (this != &matrix_copy_assignment)
		{
			double** values_temp = new double*[matrix_copy_assignment.getHeight()]; // create a new array of pointers 
			for (int i = 0; i < matrix_copy_assignment.getHeight(); i++)
				values_temp[i] = new double[matrix_copy_assignment.getWidth()]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values_temp
			for (int i = 0; i < matrix_copy_assignment.getHeight(); i++)
			{
				for (int j = 0; j < matrix_copy_assignment.getWidth(); j++)
				{
					values_temp[i][j] = matrix_copy_assignment.values[i][j];

				}
			}
			values = values_temp; //copy address contained in values_temp into values
			width = matrix_copy_assignment.getWidth();
			height = matrix_copy_assignment.getHeight();
			values_temp = nullptr; //delete values_temp
		}
		return *this;
	}

    // Move assignment
	Matrix& Matrix::operator= (Matrix&& matrix_move_assignment)
	{
		if (this != &matrix_move_assignment)
		{
			double** values_temp = new double*[matrix_move_assignment.getHeight()]; // create a new array of pointers 
			for (int i = 0; i < matrix_move_assignment.getHeight(); i++)
				values_temp[i] = new double[matrix_move_assignment.getWidth()]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values_temp
			for (int i = 0; i < matrix_move_assignment.getHeight(); i++)
			{
				for (int j = 0; j < matrix_move_assignment.getWidth(); j++)
				{
					values_temp[i][j] = matrix_move_assignment.values[i][j];

				}
			}
			values = values_temp; //copy address contained in values_temp into values
			width = matrix_move_assignment.getWidth();
			height = matrix_move_assignment.getHeight();
			values_temp = nullptr; //delete values_temp	
			matrix_move_assignment.values = nullptr;
			matrix_move_assignment.height = NULL;
			matrix_move_assignment.width = NULL;
		}
		return *this;
	}

    
    // Returns the value at the specified position in the matrix.
	double& Matrix::operator()(const int row, const int col)
	{
	
			return values[row][col];
		
	}

	//sets the value at the specified position in the matrix to val and return that value or null if index out of bound
	double Matrix::operator()(const int row, const int col) const 
	{
		
			return values[row][col];
	}
    

    // Creates a new matrix which is the sum of this and another given matrix.
	Matrix Matrix::operator+(const Matrix& matrix_plus) const
	{
		int height_temp, width_temp;
		if (height > matrix_plus.getHeight())
			height_temp = matrix_plus.getHeight();
		else
			height_temp = height;
		if (width > matrix_plus.getWidth())
			width_temp = matrix_plus.getWidth();
		else
			width_temp = width;
		double** values_temp = new double*[height_temp]; // create a new array of pointers 
		for (int i = 0; i < height_temp; i++)
			values_temp[i] = new double[width_temp]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values_temp
		for (int i = 0; i < height_temp; i++)
		{
			for (int j = 0; j < width_temp; j++)
			{
				values_temp[i][j] = values[i][j] + matrix_plus.values[i][j];
			}
		}
		//create a new matrix which is the sum of this and matrix_plus
		Matrix matrix = Matrix();
		matrix.height = height_temp;
		matrix.width = width_temp;
		matrix.values = values_temp;
		return matrix;

	}

    // Adds a given matrix to the current.
	Matrix& Matrix::operator+=(const Matrix& matrix_plus)
	{
		int height_temp, width_temp;
		if (height > matrix_plus.getHeight())
			height_temp = matrix_plus.getHeight();
		else
			height_temp = height;
		if (width > matrix_plus.getWidth())
			width_temp = matrix_plus.getWidth();
		else
			width_temp = width;
		double** values_temp = new double*[height_temp]; // create a new array of pointers 
		for (int i = 0; i < height_temp; i++)
			values_temp[i] = new double[width_temp]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values_temp
		for (int i = 0; i < height_temp; i++)
		{
			for (int j = 0; j < width_temp; j++)
			{
				values_temp[i][j] = values[i][j] + matrix_plus.values[i][j];
			}
		}

		//replace values in original matrix
		height = height_temp;
		width = width_temp;
		values = values_temp;
		
		//release used memory
		height_temp = NULL; 
		width_temp = NULL;
		values_temp = nullptr;
		return *this;
	}


    // Creates a new matrix which is the product of this and another given matrix.
	Matrix Matrix::operator*(const Matrix& matrix_multiply) const
	{
		int width_temp;
		/*if (height > matrix_multiply.getWidth())
			height_temp = matrix_plus.getHeight();
		else
			height_temp = height;*/
		if (width > matrix_multiply.getHeight()) //check if #columns in matrix1 = #columns in matrix_multiply. if not, selects the largest sub-matrices which will work
			width_temp = matrix_multiply.getHeight();
		else
			width_temp = width;
		double** values_temp = new double*[height]; // create a new array of pointers 
		for (int i = 0; i < height; i++)
			values_temp[i] = new double[matrix_multiply.getWidth()]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values_temp
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < matrix_multiply.getWidth(); j++)
			{
				double temp = 0;
				for (int k = 0; k < width_temp; k++)
				{
					temp += values[i][k] * matrix_multiply.values[k][j];
				}
				values_temp[i][j] = temp;
			}
		}
		//create a new matrix which is the product of this and matrix_multiply
		Matrix matrix = Matrix();
		matrix.height = height;
		matrix.width = matrix_multiply.getWidth();
		matrix.values = values_temp;
		return matrix;
	}

    // Multiplies a given matrix with the current.
	Matrix& Matrix::operator*=(const Matrix& matrix_multiply)
	{
		int width_temp;
		/*if (height > matrix_multiply.getWidth())
		height_temp = matrix_plus.getHeight();
		else
		height_temp = height;*/
		if (width > matrix_multiply.getHeight()) //check if #columns in matrix1 = #columns in matrix_multiply. if not, selects the largest sub-matrices which will work
			width_temp = matrix_multiply.getHeight();
		else
			width_temp = width;
		double** values_temp = new double*[height]; // create a new array of pointers 
		for (int i = 0; i < height; i++)
			values_temp[i] = new double[matrix_multiply.getWidth()]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values_temp
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < matrix_multiply.getWidth(); j++)
			{
				double temp = 0;
				for (int k = 0; k < width_temp; k++)
				{
					temp += values[i][k] * matrix_multiply.values[k][j];
				}
				values_temp[i][j] = temp;
			}
		}
		//Assiign new values to the current matrix
		width = matrix_multiply.getWidth();
		values = values_temp;
		values_temp =nullptr ; //delete values_temp
		width_temp = NULL; //delete unused variable
		return *this;
	}
    
    // Creates a new matrix which is the product of this and given number.
	Matrix Matrix::operator*(double num_multiply) const
	{
		
		double** values_temp = new double*[height]; // create a new array of pointers 
		for (int i = 0; i < height; i++)
			values_temp[i] = new double[width]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values_temp
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				values_temp[i][j] = values[i][j] * num_multiply;
			}
		}
		//create a new matrix which is the product of this and num_multiply
		Matrix matrix = Matrix();
		matrix.height = height;
		matrix.width = width;
		matrix.values = values_temp;
		return matrix;
	}

    // Mutliplies the current matrix with a given number.
	Matrix& Matrix::operator*=(double num_multiply)
	{
		double** values_temp = new double*[height]; // create a new array of pointers 
		for (int i = 0; i < height; i++)
			values_temp[i] = new double[width]; //builds the rows by assigning an array of doubles to each pointer in the array pointed to by values_temp
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				values_temp[i][j] = values[i][j] * num_multiply;
			}
		}
		//assign new values to current matrix
		values = values_temp;
		values_temp = nullptr;
			return *this;
	}


    // Determines if two matrices are equal.
	bool Matrix::operator==(const Matrix& matrix_equal) const
	{
		if (height == matrix_equal.getHeight() && width == matrix_equal.getWidth())
		{
			for (int i = 0; i < height; i++)
			{
				for (int j = 0; j < width; j++)
				{
					if (values[i][j] != matrix_equal.values[i][j])
						return false;
				}
			}
		}
		else
			return false;
		return true;
	}

	/*void Matrix:: print_matrix()
	{
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				cout << values[i][j] << " ";
			}
			cout << endl;
		}
		cout << endl;
	}*/
    
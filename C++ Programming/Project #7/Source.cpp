#include <iostream>

using namespace std;

int fact(int num)
{
	int result=1;
	for (int i=num; i>0; i--)
	{
		result = result*i;
	}
	return result;
}

int main (int argc, char* argv[])
{
		int n = atoi (argv[1]); // converts first argument to int
		int k= atoi(argv[2]);   //converts second argument to int 
		if(n<1 || n>10 || k>n || k<1)  //check for valid input
		{
			cout<<"Invalid Arguments";
			return 0;
		}
		int num_perm = fact(n)/fact(n-k);
		int* nums = new int[n];
		for (int i =0; i<n; i++)
			nums[i] = i;
		for(int i=0; i<num_perm; i++)
		{

		}
		/*for (int i=0; i<=n; i++)
		{
			cout<<i;
			int temp = 0;
			for (int j=1; j<k; j++)
			{
				if(temp==i)
					temp = temp++;
				cout<<temp;
				temp=temp++;
			}
			cout<<" ";
		}*/
		system("pause");

		return 0;
}

#ifndef BINARY_NODE
#define BINARY_NODE

#include <iostream>
using namespace std;

class BinaryNode{
    public:
        int data;           //data stored in the node
        BinaryNode* left;   //pointer to the left child
        BinaryNode* right;  //pointer to the right child

        //constructs and initilizes an empty binary node
        BinaryNode(int _data=0, BinaryNode* _left=NULL, BinaryNode* _right=NULL){ 
            data = _data;
            left = _left;
            right = _right;
        }

};

#endif



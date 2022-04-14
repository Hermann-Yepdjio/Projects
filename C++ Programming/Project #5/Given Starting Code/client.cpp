#include <iostream>
#include "BST.h"

using namespace std;

int main(){
    BST tree;           //creates an empty tree
    tree.insert(10);    //inserts a key 10
    tree.insert(7);     
    tree.insert(20);
    tree.insert(8);
    tree.insert(23);
    tree.insert(21);
    tree.printInOrder();    //prints the keys in in-order
    cout<<endl;
    tree.printPostOrder();  //prints the keys in post-order
    cout<<endl;
    tree.printPreOrder();   //prints the keys in pre-order
    cout<<endl;
    cout<<"Height:"<<tree.height()<<endl;   //prints the height of the BST
    cout<<"Max:"<<tree.findMax()<<endl;     //prints the max key in the BST
    cout<<"Min:"<<tree.findMin()<<endl;     //prints the min key in the BST
    int x = 20;
    cout<<tree.find(x)<<endl;               //returns 0 if x is found, 1 otherwise
    tree.remove(x);                         //removes x from the BST
    cout<<tree.find(x)<<endl;

    return 0;
}
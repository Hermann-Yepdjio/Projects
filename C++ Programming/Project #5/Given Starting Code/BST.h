/*
    Author: Fatma C Serce
    Date: October, 2020
    Description: This is a simple implemetation 
    of Binary Search Tree which keeps integers
*/

#ifndef BST_H
#define BST_H
#include <iostream>
#include "BinaryNode.h"
using namespace std;

class BST{
    private:
        BinaryNode* root;       //root node of the binary search tree

    public:                     //PUBLIC INTERFACE: functions are visible to the client
        BST();                  //default no-argument constructor
        BST(const BST&);        //copy constructor
        BST& operator=(BST*);   //copy assignment operator
        void insert(int);       //insert an integer into BST
        int height();           //returns the height of the BST
        void remove(int);       //removes the node with the given integer value from the BST
        bool find(int);         //searchs for the given integer value
        int findMax();          //returns the maximum integer value stored in the BST
        int findMin();          //returns the mininum integer value stored in the BST
        void printPreOrder();   //prints the elements in the BST in pre-order 
        void printInOrder();    //prints the elements in the BST in in-order (sorted order)
        void printPostOrder();  //prints the elements in the BST in post-order
        void destroy();         //destroy the BST and make it an empty tree
        ~BST();                 //destructor
    
    
    private:                                //PRIVATE INTERFACE: not visible to client, only accessible within BST class
        BinaryNode* copy(BinaryNode*);      //a helper function to copy from one BST to the current BST
        void insert(BinaryNode*&, int);     //a recursive function for recursive insert operation
        int height(BinaryNode*);            //a recursive function to find the heigh of BST    
        void remove(BinaryNode*&, int);     //a recursive function to remove a node from BST 
        int max(int, int);                  //a helper function to find the maximum of two integers
        int findMax(BinaryNode*);           //a recursive functoin to find the maximum element in the BST
        int findMin(BinaryNode*);           //a recursive function to find the minimum element in the BST
        bool find(BinaryNode*, int);        //a recursive search function
        void printPreOrder(BinaryNode*);    //a recursive pre-order traversal function
        void printInOrder(BinaryNode*);     //a recursive in-order traversal function
        void printPostOrder(BinaryNode*);   //a recursive post-order traversal function
        void destroy(BinaryNode*&);         //recursive destroy function
};

//constructor for Binary Search Tree
//creates and empy tree (root = NULL) 
BST::BST(){
    root = NULL;
}

//copy constructor
BST::BST(const BST& tree){
    root = copy(tree.root);
}

//assignment operator
BST& BST::operator=(BST* ptr){
    if(ptr !=NULL){
        destroy();
        root = copy(ptr->root);
    }

    return *this;
}

// helper recursive function to copy from other BST object into the current one
BinaryNode* BST::copy(BinaryNode *ptr) {
    if (ptr != NULL){
        BinaryNode *newnode = new BinaryNode;
        newnode->data = ptr->data;
        newnode->left = copy(ptr->left);
        newnode->right = copy(ptr->right);
        return newnode;
    }
    return NULL;
}

//initiates call for recursive insert function 
//public function
void BST::insert(int item){
    insert(root, item);
}

//inserts a new node into the tree
//private function
void BST::insert(BinaryNode *& p, int item){
    if(p == NULL){
        p = new BinaryNode;
        p->data = item;
        p->left = NULL;
        p->right = NULL;
    }else if(item<p->data){     
        insert(p->left, item);
    }else if(item>p->data){
        insert(p->right, item);
    }
}

//initiates the call for the recursive pre-order function
//public function
void BST::printPreOrder(){
    printPreOrder(root);
}

//recursive function to traverse the tree in pre-order 
//Root Left Right
//private function
void BST::printPreOrder(BinaryNode * p){
    if(p != NULL){
        cout<<p->data<<" ";
        printPreOrder(p->left);
        printPreOrder(p->right);
    }
}

//initiates the call for the recursive in-order function
//public function
void BST::printInOrder(){
    printInOrder(root);
}

//recursive function to traverse the tree in-order 
//Left Root Right
//private function
void BST::printInOrder(BinaryNode * p){
    if(p != NULL){
        printInOrder(p->left);
        cout<<p->data<<" ";
        printInOrder(p->right);
    }
}

//initiates the call for the recursive post-order function
//public function
void BST::printPostOrder(){
    printPostOrder(root);
}

//recursive function to traverse the tree post-order 
//Left Right Root
//private function
void BST::printPostOrder(BinaryNode * p){
    if(p != NULL){
        printPostOrder(p->left);
        printPostOrder(p->right);
        cout<<p->data<<" ";
    }
}


//returns the height of the tree
//initiates a call to recursive function
//public function
int BST::height(){
    return height(root)-1;
}

//recursive function to find the heigh of the BST
//Assumption: when the tree is empty and there is only one node, 
//height is zero.
//private function
int BST::height(BinaryNode * p){
    if(p == NULL)
        return 0;
    return 1 + max(height(p->left), height(p->right));
}

//a helper function to find the maximum number between two integers
int BST::max(int x, int y){
    if(x>y) 
        return x;
    return y;
}

//returns the maximum key in the tree
//initiates a call to recursive function
//public function
int BST::findMax(){
   return findMax(root);
}

//recursive function to find the maximum key in the tree
//private function
int BST::findMax(BinaryNode* p){
    if(p != NULL){
        if(p->right==NULL)
            return p->data;
        else
            return findMax(p->right);
    }
    return -1; //exception
}

//returns the minimum key in the tree
//initiates a call to recursive function
//public function
int BST::findMin(){
   return findMin(root);
}

//recursive function to find the minimum key in the tree
//private function
int BST::findMin(BinaryNode* p){
    if(p != NULL){
        if(p->left==NULL)
            return p->data;
        else
            return findMin(p->left);
    }
    return -1; //exception
}

//searches for the item in the entire tree
//returns true of tree contains the key
//returns false if tree doesn't contain the key
//initiates a call to recursive function
//public function
bool BST::find(int item){
   return find(root, item);
}

//recursive search function
//private function
bool BST::find(BinaryNode * p, int item){
    if(p == NULL)
        return false;
    else if(item<p->data)
        return find(p->left, item);
    else if(item>p->data)
        return find(p->right, item);
    else //a match is found item== p->data
        return true; 
}

//removes a key from BST
//initiates call to recursive remove function 
//public function
void BST::remove(int item){
    remove(root, item);
}
//removes the item from the tree
//handles five cases:
//Case1: delete leaf node
//Case2: delete node with two children
//Case3: delete node with one left child
//Case4: delete node with one right child
//Case5: item is not found
//private function
void BST::remove(BinaryNode *& p, int item){
    if(p == NULL)   //item not found
        return; 
    else if(item<p->data)
        remove(p->left, item);
    else if(item>p->data)
        remove(p->right, item);
    else {//item found
        if((p->left==NULL) && (p->right==NULL)){  //Case1: delete leaf node
            delete p;
            p = NULL;
        }else if ((p->left!=NULL) && (p->right!=NULL)){ //Case2: delete node with two children
            int max = findMax(p->left);
            p->data = max;
            remove(p->left, max);
        }else {
            BinaryNode* old = p;
            if(p->left!=NULL){//Case3: delete node with one left child
                p = p->left;
            }else if(p->right!=NULL){//Case4: delete node with one right child
                p = p->right;
            }
            delete old;

        }
    }
}
//delete each treenode created to build the tree
//it traverses the tree using post-order
//initiatea a call to a recursive function
//public function
void BST::destroy(){
    destroy(root);
}

//delete each treenode created to build the tree
//it traverses the tree using post-order
void BST::destroy(BinaryNode*& p){
    if(p != NULL){
        destroy(p->left);
        destroy(p->right);
        delete p;
        p = NULL;

    }
}

//destructor for Binary Search Tree
//destroys the tree and deallocates the
//dynamically allocated memory for each tree node
BST::~BST(){
    destroy(root);
}

#endif //end of Binary Search Tree header file
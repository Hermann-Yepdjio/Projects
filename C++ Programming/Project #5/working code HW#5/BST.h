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
        void insert(Product);       //insert a product into BST
        string find(unsigned long long int);         //searchs for the given upc value and return the description of the corresponding product
	void destroy();         //destroy the BST and make it an empty tree
        ~BST();                 //destructor

	private:                                //PRIVATE INTERFACE: not visible to client, only accessible within BST class
        BinaryNode* copy(BinaryNode*);      //a helper function to copy from one BST to the current BST
        void insert(BinaryNode*&, Product);     //a recursive function for recursive insert operation
        string find(BinaryNode*, unsigned long long int);        //a recursive search function
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
void BST::insert(Product item){
    insert(root, item);
}

//inserts a new node into the tree
//private function
void BST::insert(BinaryNode *& p, Product item){
    if(p == NULL){
        p = new BinaryNode;
        p->data = item;
        p->left = NULL;
        p->right = NULL;
    }else if(item.get_upc12()<p->data.get_upc12()){     
        insert(p->left, item);
    }else if(item.get_upc12()>p->data.get_upc12()){
        insert(p->right, item);
    }
}




//searches for the item in the entire tree
//returns true of tree contains the key
//returns false if tree doesn't contain the key
//initiates a call to recursive function
//public function
string BST::find(unsigned long long int upc){
   return find(root, upc);
}

//recursive search function
//private function
string BST::find(BinaryNode * p, unsigned long long int upc){
    if(p == NULL)
        return "Not found!";
    else if(upc<p->data.get_upc12())
        return find(p->left, upc);
    else if(upc>p->data.get_upc12())
        return find(p->right, upc);
    else //a match is found item== p->data
        return p->data.get_name(); 
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

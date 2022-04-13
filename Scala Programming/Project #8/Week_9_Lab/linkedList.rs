#![allow(dead_code)]
#![allow(non_snake_case)]

#[path = "queue.rs"] mod queue;
use queue::Queue;
use std::{cell::RefCell, rc::Rc};

//type Link<T> = Option<Box<Node<T>>>;
#[derive(Debug, Clone)]
pub struct Node<T: Copy>
{
    value: T,
    next: Option<Rc<RefCell<Node<T>>>>,
}

#[derive(Debug, Clone)]
pub struct LinkedList<T: Copy>
{
    front: Option<Rc<RefCell<Node<T>>>>,   //pointer to next element to dequeue (null if queue empty)
    rear: Option<Rc<RefCell<Node<T>>>>    //pointer to next element to dequeue (null if queue empty)
}

impl <T: Copy> Queue<T> for LinkedList<T>
{

    //yields a new empty queue
    fn empty() -> LinkedList<T> 
    {
        return LinkedList{front: None, rear: None};
    }

    //answers "is queue empty?"
    fn isEmpty(&mut self) -> bool
    {
        match self.front
        {
            None => {return true;}
             _   => {return false;}
        }
        //return self.front.is_none();
    }

    //removes front item from queue and returns it; if queue is empty, returns None (without changing queue)
    fn dequeue(&mut self) ->Option<T>
    {
        if self.front.is_none()
        {
            return None;
        }
        else
        {
            let node = self.front.clone();
            self.front = node.clone().as_ref().unwrap().borrow().next.clone();
            if self.front.is_none()
            {
                self.rear = None;
            }
            return Some(node.as_ref().unwrap().borrow().value);
        }
            
    }

    fn enqueue(&mut self, v: T) -> ()
    {
        let n: Rc<RefCell<Node<T>>> = Rc::new(RefCell::new(Node{value: v, next: None}));
        match self.rear
        {
            None =>
            {
                self.front = Some(n.clone());
                self.rear = Some(n.clone());
            }
            _ =>
            {
                self.rear.as_ref().unwrap().borrow_mut().next = Some(n.clone());
                self.rear = Some(n.clone());
            }
        }
    }
}


fn main() {}


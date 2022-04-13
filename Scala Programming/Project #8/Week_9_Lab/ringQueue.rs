#![allow(dead_code)]
#![allow(non_snake_case)]

#[path = "queue.rs"] mod queue;
use queue::Queue;

const N : usize = 3; 
//use std::process;

#[derive(Debug, Clone)]
pub struct RingQueue<T: Copy> 
{
    values: Vec<T>,  //vector containing values of the queue
    front: usize,      //index of array slot containing next element to dequeue
    rear: usize       //index of array slot to store next enqueued element
}


impl <T: Copy> Queue<T> for RingQueue<T>
{
    //yields a new empty queue
    fn empty() -> RingQueue<T>
    {
        return RingQueue {values: Vec::<T>::with_capacity(N + 1), front:0, rear:0};

    }

    //answers "is queue empty?"
    fn isEmpty(&mut self) -> bool
    {
        return self.front == self.rear;
    }

    //removes front item from queue and returns it; if queue is empty, returns None (without changing queue)
    fn dequeue(&mut self) ->Option<T>
    {
        if self.front == self.rear
        {
            return None;
        }
        else
        {
            let v = self.values.get(self.front);
            self.front = (self.front + 1) % (N + 1);
            return Some(*v.unwrap());
        }
    }

    fn enqueue(&mut self, v: T) -> ()
    {
        if (self.rear + 1) % (N + 1) == self.front
        {
            panic!("Out of room") //process::abort(); //Out of room
        }
        else
        {
            self.values.push(v); //to increase the length the vector and avoid out of bound exception
            self.values[self.rear] = v;
            self.rear = (self.rear + 1)%(N+1)
        }
    }
}



fn main() {}

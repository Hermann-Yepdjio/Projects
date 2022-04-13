#![allow(dead_code)]
#![allow(non_snake_case)]

pub trait Queue<T: Copy>
{
    //yields a new empty queue
    fn empty() -> Self;
  
    //answers "is queue empty?"
    fn isEmpty(&mut self) -> bool;
  
    //removes front item from queue and returns it; if queue is empty, returns None (without changing queue)
    fn dequeue(&mut self) -> Option<T>;
  
    fn enqueue(&mut self, T) -> ();
}
fn main(){}


#![allow(non_snake_case)]

mod queue;
mod ringQueue;
mod linkedList;
pub use queue::Queue;
use ringQueue::RingQueue;
use linkedList::LinkedList;


fn create_queue_int_impl_A() ->()
{
    println!{"\n\n\n------------------------Creating and using a queue of integers using implementation A --------------------------------\n\n"};
    println!("Creating queue q1...");
    let mut q1: RingQueue::<isize> = Queue::<isize>::empty();
    println!("Enqueueing 25 to q1...");
    q1.enqueue(25);
    println!("Enqueueing 50 to q1...");
    q1.enqueue(50);
    println!("Enqueueing 75 to q1...");
    q1.enqueue(75);
    //let tmp = q1.values.get(0);  //Client can not directly access queue fields because they are private
    //q1.enqueue(100);   //results in panic because queue's len(queue) > capacity(queue) (First condition in method enqueue)
    println!("Dequeueing q1...");
    q1.dequeue();
    println!("Dequeueing q1...");
    q1.dequeue();
    println!("{}{}", "q1.isEmpty: ", q1.isEmpty().to_string());
    println!("Dequeueing q1...");
    let item = q1.dequeue();
    println!("{}{}", "Last element dequeued: ", item.unwrap().to_string());
    println!("{}{}", "q1.isEmpty: ", q1.isEmpty().to_string());
    println!("Enqueueing 125 to q1...");
    q1.enqueue(125);
    println!("{}{}", "q1.isEmpty: ", q1.isEmpty().to_string());
}

fn create_queue_str_impl_A() ->()
{
    println!{"\n\n\n------------------------Creating and using a queue of strings using implementation A --------------------------------\n\n\n"};
    println!("Creating queue q1...");
    let mut q1: RingQueue::<&str> = Queue::<&str>::empty();
    println!("Enqueueing 'Monday' to q1...");
    q1.enqueue("Monday");
    println!("Enqueueing 'Tuesday' to q1...");
    q1.enqueue("Tuesday");
    println!("Enqueueing 'Wednesday' to q1...");
    q1.enqueue("Wednesday");
    //let tmp = q1.values.get(0);  //Client can not directly access queue fields because they are private
    //q1.enqueue("Thursday");   //results in panic because queue's len(queue) > capacity(queue) (First condition in method enqueue)
    println!("Dequeueing q1...");
    q1.dequeue();
    println!("Dequeueing q1...");
    q1.dequeue();
    println!("{}{}", "q1.isEmpty: ", q1.isEmpty().to_string());
    println!("Dequeueing q1...");
    let item = q1.dequeue();
    println!("{}{}", "Last element dequeued: ", item.unwrap().to_string());
    println!("{}{}", "q1.isEmpty: ", q1.isEmpty().to_string());
    println!("Enqueueing 'Friday' to q1...");
    q1.enqueue("Friday");
    println!("{}{}", "q1.isEmpty: ", q1.isEmpty().to_string());
}


fn create_queue_int_impl_B() ->()
{
    println!{"\n\n\n------------------------Creating and using a queue of integers using implementation B --------------------------------\n\n"};
    println!("Creating queue q1...");
    let mut q1: LinkedList::<isize> = Queue::<isize>::empty();
    println!("Enqueueing 25 to q1...");
    q1.enqueue(25);
    println!("Enqueueing 50 to q1...");
    q1.enqueue(50);
    println!("Enqueueing 75 to q1...");
    q1.enqueue(75);
    //let tmp = q1.values.get(0);  //Client can not directly access queue fields because they are private
    //q1.enqueue(100);   //results in panic because queue's len(queue) > capacity(queue) (First condition in method enqueue)
    println!("Dequeueing q1...");
    q1.dequeue();
    println!("Dequeueing q1...");
    q1.dequeue();
    println!("{}{}", "q1.isEmpty: ", q1.isEmpty().to_string());
    println!("Dequeueing q1...");
    let item = q1.dequeue();
    println!("{}{}", "Last element dequeued: ", item.unwrap().to_string());
    println!("{}{}", "q1.isEmpty: ", q1.isEmpty().to_string());
    println!("Enqueueing 125 to q1...");
    q1.enqueue(125);
    println!("{}{}", "q1.isEmpty: ", q1.isEmpty().to_string());

  }
  
fn create_queue_str_impl_B() ->()
{
    println!{"\n\n\n------------------------Creating and using a queue of stringss using implementation B --------------------------------\n\n"};
    println!("Creating queue q1...");
    let mut q1: LinkedList::<&str> = Queue::<&str>::empty();
    println!("Enqueueing 'Monday' to q1...");
    q1.enqueue("Monday");
    println!("Enqueueing 'Tuesday' to q1...");
    q1.enqueue("Tuesday");
    println!("Enqueueing 'Wednesday' to q1...");
    q1.enqueue("Wednesday");
    //let tmp = q1.values.get(0);  //Client can not directly access queue fields because they are private
    //q1.enqueue("Thursday");   //results in panic because queue's len(queue) > capacity(queue) (First condition in method enqueue)
    println!("Dequeueing q1...");
    q1.dequeue();
    println!("Dequeueing q1...");
    q1.dequeue();
    println!("{}{}", "q1.isEmpty: ", q1.isEmpty().to_string());
    println!("Dequeueing q1...");
    let item = q1.dequeue();
    println!("{}{}", "Last element dequeued: ", item.unwrap().to_string());
    println!("{}{}", "q1.isEmpty: ", q1.isEmpty().to_string());
    println!("Enqueueing 'Friday' to q1...");
    q1.enqueue("Friday");
    println!("{}{}", "q1.isEmpty: ", q1.isEmpty().to_string());

}


fn main() 
{
    create_queue_int_impl_A();
    create_queue_str_impl_A();
    create_queue_int_impl_B();
    create_queue_str_impl_B();
      
}


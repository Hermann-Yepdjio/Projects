Code is written in Rust (Compiler version 1.51.0 (2fd73fabe 2021-03-23)), on a linux machine

a- To compile the code from terminal (Assuming a linux machine)
	1- place all files in the same directory d
	2- in the terminal, navigate to directory d
	2- run command "rustc Client.rs" to compile client's code (This command should compile all the other files automatically)
	4- run command "./Client" To run client's code
	5- Note: all the other .rs files (i.e interface (queue.rs), implementation A (ringQueue.rs) and implementation B (linkedList.rs)) can also be compiled separately using command "rustc file_name" 

b- To modify the driver code to change the choice of implementations for each queue, 
	1- just modify correspond driver file, then recompile client code (rustc Client.rs) and run it (./Client) as described in part a
	2- Note: No modification to the client code should be needed

c- I believe my code my code meets all the goals outlined
	1- interface is in a separete file and can be compiled separately as described in part a
	2- Implementaions are in separate files and can be compiled separetely as decribed in part a
	2- Queues are homogeneous (i.e contain elements of the same type) 
	3- Clients can not access queue fiels (see commented line (line 77) in Client.rs that checks this)
	4- choice of queue implementation does not affect client code. The latter may need to be recompiled but not to be changed.
	

#include <stdio.h>
#include <stdlib.h>


#define mem_size 2048
#define max_num_qs 64 //the maximum number of queues that can be created at once
#define num_dif_chars 128 //number of possible character values
#define block_size 16 // number of cells for one block of memory
#define num_blocks 123 //number of usable memory blocks 

unsigned char data[mem_size] = {0}; //array to be used for data storage


void on_out_of_memory()
{
	printf("Sorry! This operation could not be performed due to lack of available memory");
	exit(0);
}

void on_illegal_operation()
{
	printf("Sorry! The operation that you are trying to perform is not allowed.");
	exit(0);
}

//helper function to find exact index of where to find a specific block of memory
int id_to_index(unsigned char  block_id)
{
	return (max_num_qs + 1 + (block_id - 1) * block_size);
}

//helper function to find the next available block of memory
unsigned char next_free_block_id()
{
	for(int i  = 0; i < num_blocks; i++)
	{
		if ( data[max_num_qs + 1 + i * block_size] == 0)
                	return i + 1;
	}

	on_out_of_memory();

}

//helper function to shift left data of a memory block
void shift_left(unsigned char block_id)
{
	int block_index = id_to_index(block_id);
	for (int i = 2; i < data[block_index]; i++)
	{
		data[block_index + i - 1] = data[block_index + i];
	}
}

// Creates a FIFO byte queue, returning a handle to it.
unsigned char * create_queue()
{

	for(int i = 1; i <= max_num_qs; i++)
	{
		if (data[i] == 0)
		{
			if(data[0] == num_blocks) //check if all blocks of memory are used
				on_out_of_memory();
			else
			{
				char block_id = next_free_block_id();
				int  block_index = id_to_index(block_id);
				data[block_index] = 1; //set the first value of the block to 1 to notify that the block is taken
				data[0]++; //increment the number of memory block used
				data[i] = block_id; //record ID of block where to find the new created queue

				return &data[i];

			}
		}
	}

	printf("Sorry! This operation could not be performed because you have reached the total number of queues that can be created (64).");
	

}

// Destroy an earlier created byte queue.
void destroy_queue(unsigned char * q)
{
	unsigned char block_id = *q;
	int block_index = id_to_index(block_id);
	if (block_id == 0)
                on_illegal_operation();
	
	while(data[block_index] == block_size)
	{
		data[block_index] = 0;
		block_index = id_to_index(data[block_index + block_size - 1]);
		data[0]--;
	}

	data[block_index] = 0;
	data[0]--;
	*q = 0;

}

// Adds a new byte to a queue.
void enqueue_byte(unsigned char * q, unsigned char b)
{
	unsigned char block_id = *q;
	int block_index = id_to_index(block_id);
	if (block_id == 0)
                on_illegal_operation();
	while(data[block_index] == block_size) //check if current block is full and move to the next block of memory if true
	{
		block_id = data[block_index + block_size - 1];
		block_index = id_to_index(block_id);
	}
	if (data[block_index] == block_size - 1 && data[0] < num_blocks) //check if we need to allocate another block of memory
	{
		block_id = next_free_block_id();
		data[block_index]++;
		data[0]++;
		data[block_index + block_size - 1] = block_id; //update last cell of block with info where to find the next block of memory that belongs to the same queue
		block_index = id_to_index(block_id);
		data[block_index + 1] = b;  //insert char b at index 1 of the new allocated block of memory
		data[block_index] = 2; //update first cell of newly allocated block to let know that the block contains two elements
	}	
	else
	{
		data[block_index + data[block_index]] = b; 
		data[block_index]++;
	}
}

// Pops the next byte off the FIFO queue
unsigned char dequeue_byte(unsigned char * q)
{
	unsigned char block_id = *q;
	int block_index = id_to_index(block_id);
	int next_block_index = 0;
	if (block_id == 0 || data[block_index] == 1)
		on_illegal_operation();
	
	unsigned char ret_val = data[block_index + 1];
	if(data[block_index] < 16)
	{
		shift_left(block_id);
		data[block_index]--; //decrease the first value of block by 1 to record that one element was removed from the queue
		return ret_val;

	}
	
	
	while(data[block_index] == 16)
	{
		shift_left(block_id);
		block_id = data[block_index + block_size - 1];
		next_block_index = id_to_index(block_id);
		data[block_index + block_size - 2] = data[next_block_index + 1];
		if(data[next_block_index] == 2) //free block of memory if there is only 1 element in it before the left shifta
		{
			data[block_index]--;
			data[next_block_index] = 0;
			data[0]--;
			return ret_val;
		}
		block_index = next_block_index;
	}
	
	shift_left(block_id);
        data[block_index]--; //decrease the first value of block by 1 to record that one element was removed from the queue
        return ret_val;
}


int main()
{

	unsigned char * q0 = create_queue();
	enqueue_byte(q0, 0);
	enqueue_byte(q0, 1);
	unsigned char * q1 = create_queue();
	enqueue_byte(q1, 3);
	enqueue_byte(q0, 2);
	enqueue_byte(q1, 4);
	printf("%d", dequeue_byte(q0));
	printf("%d\n", dequeue_byte(q0));
	enqueue_byte(q0, 5);
	enqueue_byte(q1, 6);
	printf("%d", dequeue_byte(q0));
	printf("%d\n", dequeue_byte(q0));
	destroy_queue(q0);
	printf("%d", dequeue_byte(q1));
	printf("%d", dequeue_byte(q1));
	printf("%d\n", dequeue_byte(q1));
	destroy_queue(q1);
}


.global _start

_start:

	LDR R4, =i 		@ load address of i
	LDR R4, [R4]		@ load value of i
	LDR R5, =array		@ load address of array

  read_loop: CMP R4, #10
	BEQ end_read_loop 	@ stop loop if R4 == 10
	LDR R0, =message_1 	@ load address of message_1
	BL printf 		@ print message_1 (see below) to the screen
	LSL R6, R4, #2		@ multiple i by 4 to get offset of array[i]
	ADD R6, R5, R6		@ R6 now contains address of array[i] 
	BL _scanf 		@ call function _scanf (see below) to read user input and store it in R0
	STR R0, [R6]		@ store the input value to array[i]
	ADD R4, R4, #1		@ increment i by 1
	B read_loop		@ loop again

  end_read_loop:
	LDR R0, =message_2	@ load address of message_2
	BL printf		@ print message_2 (see below) to the screen
	BL _scanf		@ call function _scanf (see below) to read user input and store it in R0
	LDR R4, =i		@ load address of i
	LDR R4, [R4] 		@ load value of i
	B check_loop

  check_loop: CMP R4, #10
	BEQ end_check_loop	@ stop loop if R4 == 10
	LSL R6, R4, #2          @ multiple i by 4 to get offset of array[i]
	ADD R6, R5, R6          @ R6 now contains address of array[i]
	LDR R6, [R6]		@ load value of array[i]
	CMP R6, R0		@ compare value of array[i] and the input value
	BEQ print_message_3	@ if equal, print that value was found at index i
	ADD R4, R4, #1		@ else increment i by 1
	B check_loop

  end_check_loop:
	LDR R0, =message_4      @ load address of message_4
        BL printf               @ print message_4 (see below) to the screen
	B end			@ end the program

  print_message_3:
	LDR R0, =message_3      @ load address of message_3
	MOV R1, R4		@ mov value of R4 (=i) into R1 for printf
        BL printf               @ print message_3 (see below) to the screen
	MOV R0, R4
        B end
		
  _scanf:
	PUSH {LR}               @ store the return address
    	PUSH {R1, R2, R3}       @ backup regsiter value
        LDR R0, =input_format
        SUB sp, sp, #4
        MOV R1, SP
        BL scanf
        LDR R0, [SP]
	ADD SP, SP, #4 
	POP {R1, R2, R3}        @ restore register value
    	POP {PC}                @ restore the stack pointer and return
end:
        MOV R7, #1
        SWI 0
	
.data
	.balign 4  @ Declaring array a of size 40 bytes /4 bytes = 10 words
	array:	.skip	40
        i: .word 0 @ loop counter
	@ sum: .word 0

input_format:
    	.asciz "%d"

message_1:
    	.asciz "Enter a number:"
message_2:
	.asciz "Enter a number to search:"
message_3:
	.asciz "Number is found at index %d \n"
message_4:
        .asciz "Not found\n"

	



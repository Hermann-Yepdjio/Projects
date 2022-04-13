.global main

main:
	
	LDR R0, =message_1 	@ load address of message_1
	BL printf 		@ print message_1 (see below) to the screen
	BL _scanf 		@ call function _scanf (see below) to read user input and store it in R0
	LDR R6, =i		@ load i=0 into R6
	LDR R6, [R6]
	MOV R9, R0		@ move value of R0 (The value entered by the user) into R9
	

  outer_loop: CMP R6, R9 	@ start of outer loop
	BGE end_outer_loop
	ADD R6, R6, #1		@ increment i by 1
	LDR R8, =j              @ load j=0 into R8
        LDR R8, [R8]
	B inner_loop		@ branch to inner loop 

  inner_loop:
	ADD R8, R8, #1 		@  increment j by 1
	LDR R0, =message_2	@ load address of message_2
	MOV R1, R8		@ move value of j into R1 for printing
	BL printf               @ print value of j to the screen
        MOV R0, R4
	CMP R8, R6		@ compare value of i and value of j
	BLT inner_loop		@ loop again if j < i
	LDR R0, =message_3      @ else load address of message_3
	BL printf               @ print new line
        MOV R0, R4
	B outer_loop		@branch to outerloop

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

  end_outer_loop:

	MOV R0, R9
        MOV R7, #1
        SWI 0

.data
        i: .word 0
        j: .word 0

input_format:
    	.asciz "%d"

message_1:
    	.asciz "Enter a number:"
message_2:
	.asciz "%d"
message_3:
	.asciz "\n"

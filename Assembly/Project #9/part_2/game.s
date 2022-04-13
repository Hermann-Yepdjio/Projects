.global main

main:

	MOV R0, #0
	BL time			@ time(0)
	BL srand		@ srand(time(0))	
	BL rand			@ rand(), return a random value between 0 and RAND_MAX = 32767, and  save it in R0
	/*compute  rand() % 10 + 1*/
  modulo: CMP R0, #10 		@ compute rand() % 10
        BLT  end_modulo 	@ stop looping when R0 < 10, R0 = rand() % 10
	SUB R0, R0, #10		@ decrement R0, by 10 at each iteration of the loop
	B modulo
	end_modulo: 		@ end loop
	ADD R0, R0, #1		@ R0 = rand() % 10 + 1 (i.e a random value between 1 and 10 both included)

	MOV R5, R0		@ MOV the random value into R5 
        LDR R0, =message_1      @ load address of message_1
	MOV R6, #1		@ counter for the number of tries 
        BL printf               @ print message_1 (see below) to the screen
  loop:	
        BL _scanf               @ call function _scanf (see below) to read user input and store it in R0
	CMP R0, R5		@ compare user input to randon generated value to see if the user guessed right
	BEQ end_loop		@ end the loop if correct guess
	ADD R6, R6, #1		@ increment R6 (counter for the number of tries) by 1 at each iteration
	CMP R0, R5		@ compare user input to randon generated value to see if the user guessed right
	BLT print_message_2	@ print message_2 (see blow) if guessed value is lower than random generated value
	B print_message_3     	@ print message_3 (see blow) if guessed value is higher than random generated value

  end_loop:
	MOV R1, R6		@ move value of R6 (counter for the number of tries)  into R1 for printf
	CMP R1, #1		@ compare value in R1 (counter for the number of tries)
	BEQ print_message_4	@ print message_4 if only equal (i.e only one try)
	B print_message_5	@ else print message_5

	

  print_message_2:
	lDR R0, =message_2      @ load address of message_2
	BL printf		@ print message_2 (see below) to the screen
	B loop			@ return to the begining of loop

  print_message_3:
        lDR R0, =message_3      @ load address of message_3
        BL printf               @ print message_3 (see below) to the screen
        B loop                  @ return to the begining of loop

  print_message_4:
	lDR R0, =message_4      @ load address of message_4
        BL printf               @ print message_4 (see below) to the screen
        B end

  print_message_5:
	lDR R0, =message_5      @ load address of message_5
        BL printf               @ print message_5 (see below) to the screen
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

	MOV R0, R5
        MOV R7, #1
        SWI 0

input_format:
        .asciz "%d"

message_1:
        .asciz "Guess the number:"
message_2:
        .asciz "Too low. Guess again."
message_3:
        .asciz "Too high. Guess again."
message_4:
        .asciz "You guessed correctly in %d try!\n"
message_5:
        .asciz "You guessed correctly in %d tries!\n"


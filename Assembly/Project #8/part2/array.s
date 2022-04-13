.data
	.balign 4  /*Declaring array a of size 40 bytes /4 bytes = 10 words*/
	a:	.skip	40
	.balign 4
	b:	.skip	40 /*Declaring array a of size 40 bytes /4 bytes = 10 words*/
        i: .word 1
	sum: .word 0
.text
.global _start

_start:
	LDR R0, =sum 	/*hold the value of sum*/
	LDR R0, [R0] 
	LDR R1, =i  	/*hold the value of i*/
	LDR R1, [R1]

   for: CMP R1, #10
	BEQ end_for	/*end the loop*/
	LDR R2, =a	/*get address of array a*/
	LDR R3, =b	/*get address of array b*/
	LSL R4, R1, #2  /*multiply i by 4 (same as logical shift left by 2) to get offset*/
	ADD R5, R2, R4	/*add R2 and R4 to obtain address of a[i]*/  
	ADD R6, R3, R4  /*add R3 and R4 to obtain address of b[i]*/
	STR R1, [R5]  	/*copy i in a[i]*/
	STR R4, [R6] 	/*copy i<<2 in b[i]*/
	
	/*LDR R5, [R5]*/
	/*LDR R6, [R6]*/
	
	/*compute b[i] % 8*/
	LSR R8, R4, #3  /*divide b[i] (still in R4) by 8 (same as logical shift right by 3) and save quotient in R8*/
	LSL R8, R8, #3  /*multiply R8 by 8 (same as logical shift left by 3) and save result in R8*/
	SUB R8, R4, R8  /*substract R6 by R8 and save result in R8 which is the value of b[i]%8*/

  	CMP R8, #0 /*if block starts here*/
	BNE end_if
	LSR R9, R1, #1	/*divide i by 2 (same as logical right shift by 1) to get index i/2 and store result in R9*/
	LSL R9, R9, #2  /*multiply i by 4 (same as logical shift left by 2) to get offset of i/2*/
	ADD R9, R2, R9	/*add offset R9 to a location to find position of a[i/2]*/
	LDR R9, [R9]
	STR R9, [R6]	/*store value of R9 (a[i/2]) into R6 (b[i])*/
	B end_if /*end of if block*/

      end_if:
	LDR R5, [R5] /*load value at a[i] into R5*/
	LDR R6, [R6] /*load value at b[i] into R6*/
	ADD R0, R0, R5	/*sum+=a[i]*/
	ADD R0, R0, R6	/*sum+=b[i]*/
	ADD R1, R1, #1	/*increment R1 (i++)*/
	B for

   end_for:
        MOV R7, #1
        SWI 0

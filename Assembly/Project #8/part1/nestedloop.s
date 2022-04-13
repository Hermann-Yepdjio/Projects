.data
        i: .word 1
        j: .word 0
        x: .word 0
.text
.global _start

_start:
	/*load i and x into R1 and */
        LDR R1, =i
        LDR R1, [R1]
	LDR R0, =x
	LDR R0, [R0]

  outer_loop: CMP R1, #10 /*start of outer loop*/
	BGE end_outer_loop
	MOV R2, R1
	ADD R1, R1, #2 /*increment i by 2*/
	B inner_loop 

  inner_loop: CMP R2, #10
	BGE outer_loop
	ADD R0, R0, R1 /*x += i*/
	SUB R0, R0, #2 /*because we have already incremented R1 or i by two in the outer loop*/
	ADD R0, R0, R2 /*x+= y*/
	ADD R2, R2, #1 /*increment j by 1*/
	B inner_loop

  end_outer_loop:

        MOV R7, #1
        SWI 0

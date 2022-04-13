/*.section .data
	F0: .word 0
	F1: .word 1
.section .text
	.global _start*/

.global _start

_start:
	MOV R7, #4
	MOV R0, #1
	MOV R2, #12
	LDR R1, =message

end:
	MOV R7, #1
	SWI 0

.data
message:
	.ascii "Hello World\n"
_start:
	/*LDR R0, =F0
	LDR R1, =F1
	ADD R0, R0, R1
	ADD R1, R1, R0
	ADD R0, R0, R1
	ADD R1, R1, R0

SWI 0*/



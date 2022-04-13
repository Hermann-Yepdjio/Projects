.data
        a: .word 10
        b: .word 5
        c: .word 3
.text
.global _start

_start:
        /*load a, b, and c*/
        LDR R0, =a
        LDR R0, [R0]
        LDR R1, =b
        LDR R1, [R1]
        LDR R2, =c
        LDR R2, [R2]


        ADD R0, R1, R2 /*a = b + c*/
        ADD R1, R1, #1 /*b++*/
        ADD R2, R2, #1 /*c++*/
        SUB R2, R2, #1 /*--c*/
        ADD R1, R2, R0 /*b = c + a*/
        ADD R3, R0, R1 /*d = a + b*/
        ADD R2, R2, R3 /*c += a + b*/
        ADD R1, R1, #1 /*b++*/
        ADD R0, R0, R1 /*a += b*/
        ADD R0, R0, R2 /*a += c  => a = a + b + c*/

end:
        MOV R7, #1
        SWI 0

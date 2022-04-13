.data
        F0: .word 0
        F1: .word 1
.text
.global _start

_start:
        LDR R0, =F0 /* Load F0 into R0 */
        LDR R0, [R0]
        LDR R1, =F1 /* Load F1 into R1*/
        LDR R1, [R1]
        ADD R0, R0, R1 /* compute F2 and store it in R0 */
        ADD R1, R1, R0 /* compute F3 and store it in R1 */
        ADD R0, R0, R1 /* compute F4 and store it in R0 */
        ADD R1, R1, R0  /* compute F5 and store it in R1 */
        ADD R0, R0, R1 /* compute F6 and store it in R0 */

end:
        MOV R7, #1
        SWI 0


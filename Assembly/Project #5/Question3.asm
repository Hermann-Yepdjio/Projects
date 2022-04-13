            segment .data
A            dq   40      ; 1st grade
B            dq   50      ; 2nd grade
C            dq   25      ; 3rd grade
D            dq   99      ; 4th grade
Average      dq   0       ; Average grade
Remainder    dq   0       ; Remainder
Divisor      dq   4       ; divisor

            segment .text
            global main
main:
mov rax, [A]             ;move 40 into rax
add rax, qword[B]        ;add 50 to rax
add rax, qword[C]        ;add 25 to rax
add rax, qword[D]        ;add 99 to rax
mov rdx, 0               ;move 0 into rdx
idiv qword[Divisor]      ;divide rax by 4
mov [Average], rax       ;move rax into memory Average
mov [Remainder], rdx     ;move rdx into memore Remainder

xor rax,rax              ; zero out rax
 ret


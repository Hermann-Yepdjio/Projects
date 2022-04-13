            segment .data
A       dq   7,5      ; point A has for coordinates x=4 and y=5
B       dq   4,10     ; point B has for coordinates x=7 and y=10
xDiff   dq   0        ; difference in x coodirdinates of A and B
yDiff   dq   0        ; difference in y coodirdinates of A and B
zero    dq   0        ; to hold value 0
one     dq   1        ; to hold value 1

            segment .text
            global main
main:
mov rax, [A]             ; move 4 (x coordinate of point A) into rax
mov rbx, [A+8]           ;move 5(y coordinate of point B) int rbx
mov rcx, [B]             ;move 7 (x coordinate of point B) into rcx  
mov rdx, [B+8]           ;move 10(y coordiante of point B) into rdx
sub rbx, rdx             ;substract rdx from rbx  
sub rax, rcx             ;subtract rcx from rax
mov [yDiff], rbx         ;move rbx into memory yDiff
mov [xDiff], rax         ;move rax into memore xDiff
cmovz rax, qword[one]    ;move 1 into rax if xDiff is 0
cmovnz rax, qword[zero]  ;move 0 into rax if xDiff is different from 0

xor rax,rax              ; zero out rax
 ret


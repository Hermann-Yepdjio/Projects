            segment .data
A       dq   4,5      ; point A has for coordinates x=4 and y=5
B       dq   7,10     ; point B has for coordinates x=7 and y=10
AB      dq   0        ;distance betwen A and B

            segment .text
            global main
main:
mov rax, [A]             ; move 4 (x coordinate of point A) into rax
mov rbx, [A+8]           ;move 5(y coordinate of point B) int rbx
mov rcx, [B]              ;move 7 (x coordinate of point B) into rcx  
mov rdx, [B+8]           ;move 10(y coordiante of point B) into rdx
sub rax, rcx             ; subtract rcx from rax
sub rbx, rdx             ;substract rdx from rbx
imul rax, rax            ;multiplies rax by rax
imul rbx, rbx            ;multiplies rbx by rbx
add rax,rbx              ;add rbx to rax
mov [AB], rax            ; move rax into memory AB

xor rax,rax              ; zero out rax
 ret


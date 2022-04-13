 segment .data       
a       dw      112, 67, 121    ;array of 3 values
sum     dq      0               ;memory to store the sum
diff    dq      0               ;memory to store the difference 
        segment .text
        global   main         
main:
        mov     rax, [a]        ;move 112 to rax
        mov     rbx, [a+2]      ;move 67 to rbx
        mov     rcx, [a+4]      ;move 121 to rcx
        add     rax, rbx        ;add 67 and 112 and store result in rax
        add     rax, rcx        ;add 121 and the value in rax and store the result in rax
        mov     [sum], rax      ;move the value in rax to the variable sum
        mov     rax, [a]        ;move 112 to rax
        sub     rax, rbx        ;subtract 67 with 112 and store the result in rax
        sub     rax, rcx        ;subtract 121 with the value in rax and store the result in rax
        mov     [diff], rax     ;move the value in rax to the variable diff 
        xor     rax, rax        ; zero out rax
        ret             


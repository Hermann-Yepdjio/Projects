            segment .data
x dq 0                                      ; the number for comparison
scanf_format db "%ld",0
printf_format db "LargestFibonacciLessOrEqual(%ld) = %ld",0x0a,0

            segment .text
            
            global main                     ; let the linker know about main
            global fibonacci                ; declaration of fibonacci function
            extern scanf                    ; resolve write and exit from libc
            extern printf
            
main:
    push    rbp
    mov     rbp, rsp
    lea     rcx, [scanf_format]             ;sets arg 1
    lea     rdx,  [x]                       ;sets arg 2
    call    scanf                           ;call scan
    mov     rcx, [x]                        ;move x for fibonacci call
    mov     rax, 1                          ;move 1 into rax
    mov     rbx, 1                          ;move 1 into rbx
    call    Fibonacci                       ;call Fibonacci
    lea     rcx, [printf_format]            ;set arg 1
    mov     rdx, [x]                        ;set arg 2 for printf_format
    mov     r8, rax                         ;set arg 3 to be fibonnaci(x)
    call    printf                          ;call printf
    xor     eax, eax                        ; zero out eax
    leave
    ret
    
    
 Fibonacci:                                 ;recursive function                               
n   equ     8                               ;to store parameter 
    push    rbp                             ;push rbp onto the stack
    mov     rbp, rsp                        ;mov rsp into rbp 
    sub     rsp, 16                         ;substract 16 bytes from rsp
    cmp     rcx, 2                          ;compare rcx and 2
    jle     end                             ;jump to end if less or equal
    cmp     rcx, rbx                        ;compare rcx and rbx
    jge     greater                         ;jump to greater if greater or equal
    leave               
    ret
   
   
greater:
    mov [rsp+n], rax                        ;move rax into rsp+m
    mov rax, rbx                            ;move rbx into rax
    add rbx, [rsp+n]                        ;add rsp+n to rbx
    call Fibonacci                          ;call Fibonacci
    leave
    ret
end:
    leave
    ret
    
    

                segment .data
a       dd      2,4,1,5,7,9                    ;array of double words
size    dq      6                              ;size of array
swapped dq      "false"

                segment .text
                global main
            
main:
lea     rax, [a]                                ; load effective adress of a in rax
xor     ecx,ecx                                 ; zero out ecx
mov     rdx, [size]                             ;move size into rdx
mov     rbx, [swapped]                          ;move swapped in rbx
dec     rdx;                                    ;decriment rdx by 1
for:    cmp rcx, rdx                            ;compare rcx and rdx
        je  while                               ;jump to while if zero flag set
        mov r8d, [rax+4*rcx+4]                  ;move array value at index rcx+1 into r8d
        cmp [rax+rcx*4],r8d                     ;compare value at index rcx and at index rcx+1
        jg  swap                                ;jump to swap
        inc rcx                                 ;increment rcx by 1
        jmp for                                 ;jump to for
while:  cmp rbx, [swapped]                      ;compare rbx and swapped
        jz end_while                            ;jump to end_while if zero flag set
        xor ecx,ecx                             ;zero out ecx
        mov rbx, [swapped]                      ;mov swapped into rbx
        jmp for                                 ;jump to for
swap:
      mov r9d, [rax+rcx*4]                      ;move array value at index rcx into r9d
      mov [rax+rcx*4], r8d                      ;move r8d into array at index rcx
      mov [rax+rcx*4+4], r9d                    ;move r9d into array at index rcx+1
      mov rbx, "true"                           ;move "true" into rbx
      inc rcx                                   ;increament rcx
      jmp for                                   ;jump to for
end_while:
      xor eax,eax                               ;zero out eax
      ret

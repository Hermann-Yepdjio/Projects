            segment .data
A       db   0x45      ; memory A
result  dq   0         ;to hold the number of bit 1 in A
            segment .text
            global main
main:
movsx rax, byte[A]          ;sign move A into rax
mov rcx, rax                ;move rax into rcx
and rax, 0x1                ;and operation between rax and immediate value 0x1
mov rbx, 0                  ;move 0 into rbx
add rbx, rax                ;add rax to rbx
shr rcx, 1                  ;right shift rcx by 1
mov rax, rcx                ;move rcx into rax
and rcx, 0x1                ;and operation between rcx and immediate value 0x1
add rbx, rcx                ;add rbx to rbx
shr rax, 1                  ;right shift rax by 1         
mov rcx, rax                ;move rax into rcx
and rax, 0x1                ;and operation between rax and immediate value 0x1
add rbx, rax                ;add rax to rbx
shr rcx, 1                  ;right shift rcx by 1
mov rax, rcx                ;move rcx into rax
and rcx, 0x1                ;and operation between rcx and immediate value 0x1
add rbx, rcx                ;add rcx to rbx
shr rax, 1                  ;right shift rax by 1
mov rcx, rax                ;move rax into rcx
and rax, 0x1                ;and operation between rax and immediate value 0x1
add rbx, rax                ;add rax to rbx
shr rcx, 1                  ;right shift rcx by 1
mov rax, rcx                ;move rcx into rax
and rcx, 0x1                ;and operation between rcx and immediate value 0x1
add rbx, rcx                ;add rcx to rbx
shr rax, 1                  ;right shift rax by 1
mov rcx, rax                ;move rax into rcx
and rax, 0x1                ;and operation between rax and immediate value 0x1
add rbx, rax                ;add rax to rbx
shr rcx, 1                  ;right shift rcx by 1
mov rax, rcx                ;move rcx into rax
and rcx, 0x1                ;and operation between rcx and immediate value 0x1
add rbx, rcx                ;add rcx to rbx
mov [result], rbx           ;move rbx into memore result

xor rax,rax              ; zero out rax
 ret


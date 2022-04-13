                    segment .data
a   db  35                                      ;assign 35 using type db
b   dw  35                                      ;assign 35 using type dw
c   dd  35                                      ;assign 35 using type dd
d   dq  35                                      ;assign 35 using type dw
e   dw  "This is CS311 course"                  ;assign the string "This is CS311 course" using type dw
f   dd  24.125                                  ;assign 24.125 using dd type       
g   times  30 dw 5                              ;assign an array of 30 double words initialised to 5
h   dd  0xa46b0                                  ;assign 673456 using dw

                    segment .bss

i   resd   25                                     ;reserves an array of 25 double words
j   resb  100                                     ;reserves an array of 100 bytes 
k   resw   20                                     ;reserves 20 words

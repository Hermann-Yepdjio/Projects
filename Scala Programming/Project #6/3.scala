/*
The hidden library now contains a working solution to part (b), i.e., a correct interpreter for our pure functional language. Your task in this section:

(c) The solution template contains a program to compute and write out the prime numbers between 2 and 32. A similar program worked for the interpreter from Week 3’s lab, but it makes use of variable assignment and while loops, so it will not run (or even parse) under this week’s interpreter. Modify the program to be purely functional.

Use no more than one write statement.*/



object Solution { 



val answer = """
(letRec
    printPrimes
    (
        fun(x)
        (
            if(<= x 31)
            (
                letRec 
                    isPrime
                    (
                        fun(x d) 
                        (
                            if (<= d 1)
                            1
                            (
                                if(% x d)
                                (
                                    @ isPrime x (- d 1)
                                )
                                0
                            )
                        )

                    )
                    (
                        if (@ isPrime x (- x 1 )) 
                        (
                            block (write x) (@ printPrimes(+ x 1))
                        ) 
                        (
                            @ printPrimes(+ x 1)
                        )
                    )
                
            )
            (block)
        )
    )
    (
        @ printPrimes 2
    )

)
"""

}


object Solution { 

val answerWithComments = """

  { Function for enumerating the primes starting with `n` and going up to 32. }
  (letRec f
     (fun (n)
     
        { If n <= 32, continue... }
        (if (<= n 32)
        

          (block
          { This has 2 steps: first figure out whether n is prime and if so, print it. }
   	      (if 
   	      { start big conditional expression up through (@ g 2) }
   	      { this conditional expression defines then immediately applies g }
   	      (letRec g
   	         { g is a recursive function of 1 argument, the candidate "divisor"}
	           (fun (d)
	              { check divisors up through (n - 1) }
		            (if (<= d (- n 1))
		            
		                { d is "small enough" that it could divide n (we claim, for this inefficient algorithm) }
		                { if d divides n, then m == n; otherwise m < n.  }
		                (if (<= (% n d) 0)
                  		  { (% n d) == 0, so d does divide n, so n is not prime }
		 	                  0
		 	                  { d does not divide n. check the next candidate divisor! }
			                  (@ g (+ d 1)))

			             { else d >= (n - 1), so d certainly doesn't divide n, so no number divided n, so n is prime! }
		                1))
                     (@ g 2))
            { end big conditional expression }
            { The above conditional expression recursively checks each candidate divisor }
            { ... eventually returning 1 only if none of the candidate divisors divided n }
            { If (@ g 2) returned 1, n is prime. So print it! }
   	        (write n)
   	        { else (@ g 2) returned 0, and n is not prime. So do nothing.}
	          (block))
	          
	          { Then, increment n and check the next potential prime.}
    	      (@ f (+ n 1)))
    	 
    	 { Else, n > 32, so return immediately! } 
  	    0))
  	    
  	{ Now actually enumerate the primes, starting with 2 }
   (@ f 2)) 
"""


/* For those of you who just like to look at the code, here's an un-commented version: */

val answer = """
  (letRec f
     (fun (n)
        (if (<= n 32)
          (block
   	        (if (letRec g
	                (fun (d)
    		             (if (<= d (- n 1))
		                     (if (<= (% n d) 0)
                  		       0
			                       (@ g (+ d 1)))
		                     1))
                  (@ g 2))
   	            (write n)
	              (block))
    	      (@ f (+ n 1)))
  	    0))
   (@ f 2))
"""

}



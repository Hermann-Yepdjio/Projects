fun makeLst x = if x<1 then [] else makeLst (x-1) @ [x];

fun removeMult (x, n) = if (null x orelse n=0) then [] else if hd x mod n = 0 then removeMult(tl x, n) else hd x :: removeMult(tl x, n);

fun primes x =
  if x<2 then []
  else
    let
      val sqrt_x = floor (Math.sqrt(real x)) + 1
      val temp = tl (makeLst x)
      fun clean_list l =
        if null l then []
        else if hd l<sqrt_x then hd l::clean_list (removeMult(tl l, hd l)) 
        else l
    in 
        clean_list temp
    end;

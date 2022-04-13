fun last (lst) = if null (tl lst) then hd lst else last (tl lst);

fun middle  lst=
		 if null (tl lst) then hd lst
		 else 
			let
			      fun helper (lst1, lst2) = if (null (tl lst2) orelse null (tl (tl lst2)))  then hd lst1
							else helper (tl lst1, tl (tl lst2))
			in 
			      helper (lst, lst)
			end;

fun median (a, b, c) = 
			let
				fun smallest (x, y) = if x<y then x else y 
			in	
				if (a<b andalso a<c) then smallest (b,c)
				else if(a<b andalso a>c) then a
				else if(b<a andalso b<c) then smallest(a,c)
				else if(b<a andalso b>c) then b
				else if(c<a andalso c<b) then smallest(a,b)
				else c
			end;
		
fun partition (lst, p) =
			 let 
				fun helper (lst, lst1, lst2) = if null lst then (lst1, lst2) 
							       else if hd lst> p then helper (tl lst, lst1,  [hd lst]@lst2)
							       else helper(tl lst, [hd lst]@lst1, lst2)
			 in
				helper(lst, [], [])
			 end;
		
fun quicksort lst = 
		    let 
			fun remove ([], x)  = []
  			   |remove (lst, x) = if hd lst = x then tl lst 
		   			      else hd lst::remove (tl lst, x); 
		    in

		  	  if null lst then nil 
		  	  else if tl lst = nil then lst
		   	  else if #2 (partition(lst, median(hd lst, middle lst, last lst)))=nil
			       then quicksort(remove( #1(partition(lst, median(hd lst, middle lst, last lst))), 
							median(hd lst, middle lst, last lst))) @ [median(hd lst, middle lst, last lst)]
		          else quicksort ( #1 (partition(lst, median(hd lst, middle lst, last lst))))
			     @ quicksort(#2 (partition(lst, median(hd lst, middle lst, last lst))))
		    end;

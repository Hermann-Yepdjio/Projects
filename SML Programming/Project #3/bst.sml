(* left subtree, right subtree, key, value *)
datatype BST = Empty | Node of BST * BST * int * int;

fun parsePost [] = Empty
|   parsePost lst =
    let
        fun pP (stack, (0,k,v)::str) = pP(Node(Empty, Empty, k, v)::stack, str)
        |   pP (L::stack, (1,k,v)::str) = pP(Node(L, Empty, k, v)::stack, str)
        |   pP (R::stack, (2,k,v)::str) = pP(Node(Empty, R, k, v)::stack, str)
        |   pP (R::L::stack, (3,k,v)::str) = pP(Node(L, R, k, v)::stack, str)
        |   pP (T::stack, []) = T;
    in
        pP([],lst)
    end;

val exTree0 = []
val exTree1 = [(0,1,1),(0,3,3),(3,2,2)];
val exTree2 = [(0,2,2),(2,1,1),(0,4,4),(3,3,3),(0,6,6),(1,7,7),(3,5,5)];
val exTree3 = [(0,1,1),(0,4,4),(1,5,5),(3,2,2),(1,8,8),(0,15,15),(2,14,14),(3,11,11)];

fun insert(bst, key, value) =
  case bst of Empty  => Node(Empty, Empty, key, value)
  | Node(L, R, k, v) => if k = key then Node(L, R, k, value)
					   else if key < k then Node(insert(L, key, value), R,  k, v)
					   else Node(L, insert (R, key, value), k, v)

fun find(bst, key) =
  case bst of Empty => []
  | Node(L, R, k, v) => if k = key then [v]
		     else if k>key then find (L, key)
		     else find(R, key);

fun delete (bst, key) = 
  case bst of Empty => Empty
  | Node(Empty, R, k, v) => if k = key then R else Node(Empty, delete(R, key),k,v)
  | Node(L, Empty, k, v) => if k = key then L else Node(delete(L, key), Empty,k,v)
  | Node(L,R,k,v) => 		     
			let 
				fun removeHighest bst1 =
				   case bst1 of  Node(L1, Empty, k1, v1) => (L1,k1, v1)
				   |  Node(L1, Node(l,r,k2,v2), k1,v1) => if r = Empty then (Node(L1,l,k1,v1),k2,v2)
										    else (Node(L1, #1(removeHighest(Node(l,r,k2,v2))),k1,v1), #2(removeHighest(Node(l,r,k2,v2))), #3(removeHighest(Node(l,r,k2,v2))))
				(*fun removeLowest Node(Empty, R1, k1, v1) = (R1, k1, v1)
				  | removeLowest Node(Node(l,r,k2,v2), R1, k1,v1) = if l = Empty then (Node(l, R1, k1,v1), k2, v2)
										    else Node(removeLowest(Node(l,r,k2,v2)), R1, k1,v1)	*)
			in  
				if k = key then Node( #1( removeHighest L), R, #2 (removeHighest L), #3 (removeHighest L)) 
				else if key < k then Node(delete(L, key), R, k, v)
				else Node(L, delete(R, key), k, v)
			end;
	

fun postorder(bst) = 
	case bst of Empty => []
	| Node(Empty,Empty,k,v)=> [(0,k,v)]
	| Node(L,Empty,k,v)=>postorder(L)@[(1,k,v)]
	| Node(Empty,R,k,v) =>postorder(R)@[(2,k,v)]
	| Node(L,R,k,v)=> postorder(L)@postorder(R)@[(3,k,v)];

fun subtree (bst, minKey, maxKey) = 
	case bst of Empty => Empty
	| Node(L, R, k ,v) => if (k<minKey orelse k>maxKey) then subtree(delete(bst, k),minKey, maxKey) else Node(subtree(L,minKey,maxKey), subtree(R, minKey, maxKey),k,v);

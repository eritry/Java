%tree_map(Key, Value, Left, Right) is BST
%map_get(TreeMap, Key, Value) is checking if TreeMap contains Key by Value
map_get(tree_map(Key, Value, _, _), Key, Value) :- !.
map_get(tree_map(Key, Value, Left, Right), NewKey, NewValue) :- NewKey < Key, map_get(Left, NewKey, NewValue).
map_get(tree_map(Key, Value, Left, Right), NewKey, NewValue) :- NewKey > Key, map_get(Right, NewKey, NewValue).

%cut(Tree, Left, Right) is cutting Tree on Left and Right equal-length parts
cut(Tree, Left, Right) :-
	append(Left, Right, Tree),
    length(Left, LenL), length(Right, LenR),
    (LenL is LenR; LenL is LenR - 1).

%tree_build(ListMap, TreeMap) is building TreeMap from ListMap
tree_build([], nil) :- !.
tree_build(ListMap, tree_map(Key, Value, Left, Right)) :-
    cut(ListMap, L, [(Key, Value) | R]),
    tree_build(L, Left),
    tree_build(R, Right).
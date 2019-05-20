%tree_map(Key, Value, Priority, Left, Right) is treap
%map_get(TreeMap, Key, Value)
map_get(tree_map(Key, Value, _, _, _), Key, Value) :- !.
map_get(tree_map(Key, Value, _, Left, Right), NewKey, NewValue) :- NewKey < Key, map_get(Left, NewKey, NewValue).
map_get(tree_map(Key, Value, _, Left, Right), NewKey, NewValue) :- NewKey > Key, map_get(Right,NewKey, NewValue).

%merge(Left, Right, Tree)
merge([], R, R) :- !.
merge(L, [], L) :- !.
merge(L, R, T) :-
    L = tree_map(LeftK, LeftV, LeftP, LeftL, LeftR), R = tree_map(RightK, RightV, RightP, RightL, RightR),
    ((LeftP > RightP, merge(LeftR, R, NLeftR), T = tree_map(LeftK, LeftV, LeftP, LeftL, NLeftR));
    (LeftP =< RightP), merge(L, RightL, NRightL), T = tree_map(RightK, RightV, RightP, NRightL, RightR)).

%split(Tree, Key, Left, Right)
split([], _, [], []) :- !.
split(T, Key, L, R) :-
    T = tree_map(TK, TV, TP, TL, TR),
    ((Key < TK, split(TL, Key, NL, NTL), L = NL, R = tree_map(TK, TV, TP, NTL, TR));
    (Key >= TK, split(TR, Key, NTR, NR), L = tree_map(TK, TV, TP, TL, NTR), R = NR)).

%map_put(Tree, Key, Value, Result)
map_put(T, K, V, Result) :-
    map_remove(T, K, NT),
    rand_int(100000, P),
    N = tree_map(K, V, P, [], []),
    insert(NT, N, Result).

insert([], N, N) :- !.
insert(T, N, Result) :-
    T = tree_map(TK, TV, TP, TL, TR),
    N = tree_map(K, V, P, L, R),
    ((P > TP, split(T, K, NL, NR), Result = tree_map(K, V, P, NL, NR));
    (P =< TP,(
    		(K < TK, insert(TL, N, NL), Result = tree_map(TK, TV, TP, NL, TR));
    		(K > TK, insert(TR, N, NR), Result = tree_map(TK, TV, TP, TL, NR)))) ).

%map_remove(T, K, Result)
map_remove([], K, []) :- !.
map_remove(T, K, Result) :-
    T = tree_map(TK, TV, TP, TL, TR),
    ((TK is K, merge(TL, TR, Result));
    (TK > K, map_remove(TL, K, NL), Result = tree_map(TK, TV, TP, NL, TR));
    (TK < K, map_remove(TR, K, NR), Result = tree_map(TK, TV, TP, TL, NR))).

%tree_build(ListMap, TreeMap)
tree_build([], []) :- !.
tree_build([(FK, FV) | T], Result) :-
    tree_build(T, R1), map_put(R1, FK, FV, Result).
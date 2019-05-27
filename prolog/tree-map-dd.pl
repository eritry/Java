%tree_map(Key, Value, Priority, Left, Right) is treap
%map_get(TreeMap, Key, Value)
map_get(tree_map(Key, Value, _, _, _), Key, Value) :- !.
map_get(tree_map(Key, Value, _, L, R), K, V) :- K < Key, map_get(L, K, V).
map_get(tree_map(Key, Value, _, L, R), K, V) :- K > Key, map_get(R, K, V).

%merge(Left, Right, TreeMap)
merge([], R, R) :- !.
merge(L, [], L) :- !.
merge(L, R, T) :- L = tree_map(LK, LV, LP, LL, LR), R = tree_map(RK, RV, RP, RL, RR),
    LP > RP, merge(LR, R, NLR), T = tree_map(LK, LV, LP, LL, NLR);
    LP =< RP, merge(L, RL, NRL), T = tree_map(RK, RV, RP, NRL, RR).

%split(TreeMap, Key, Left, Right)
split([], _, [], []) :- !.
split(tree_map(TK, TV, TP, TL, TR), K, L, R) :-
    K < TK -> split(TL, K, NL, NTL), L = NL, R = tree_map(TK, TV, TP, NTL, TR);
              split(TR, K, NTR, NR), L = tree_map(TK, TV, TP, TL, NTR), R = NR.

%map_put(TreeMap, Key, Value, Result)
map_put(T, K, V, Result) :-
    map_remove(T, K, NT),
    rand_int(100000, P),
    N = tree_map(K, V, P, [], []),
    put(NT, N, Result).

put([], N, N) :- !.
put(tree_map(TK, TV, TP, TL, TR), tree_map(K, V, P, L, R), tree_map(K, V, P, NL, NR)) :-
    P > TP, split(tree_map(TK, TV, TP, TL, TR), K, NL, NR).

put(tree_map(TK, TV, TP, TL, TR), tree_map(K, V, P, L, R), Result) :- N = tree_map(K, V, P, L, R),
    P =< TP,
    K < TK, put(TL, N, NL), Result = tree_map(TK, TV, TP, NL, TR);
    K > TK, put(TR, N, NR), Result = tree_map(TK, TV, TP, TL, NR).

%map_remove(TreeMap, Key, Result)
map_remove([], K, []) :- !.
map_remove(tree_map(TK, TV, TP, TL, TR), K, Result) :- TK = K, merge(TL, TR, Result).
map_remove(tree_map(TK, TV, TP, TL, TR), K, Result) :-
    TK > K, map_remove(TL, K, NL), Result = tree_map(TK, TV, TP, NL, TR);
    TK < K, map_remove(TR, K, NR), Result = tree_map(TK, TV, TP, TL, NR).

%map_replace(TreeMap, Key, Value, Result)
map_replace([], _, _, []).
map_replace(tree_map(K, _, P, L, R), K, V, tree_map(K, V, P, L, R)) :- !.
map_replace(tree_map(TK, TV, TP, TL, TR), K, V, Result) :-
    K < TK, map_replace(TL, K, V, NL), !, Result = tree_map(TK, TV, TP, NL, TR);
    K > TK, map_replace(TR, K, V, NR), !, Result = tree_map(TK, TV, TP, TL, NR).

%map_floorKey(TreeMap, Key, FloorKey)
map_floorKey(tree_map(TK, _, _, _, _), TK, TK).
map_floorKey(tree_map(TK, _, _, TL, TR), K, FK) :-
    TK > K, map_floorKey(TL, K, FK);
    TK < K, (map_floorKey(TR, K, FK), !; FK = TK).


%tree_build(ListMap, TreeMap)
tree_build([], []) :- !.
tree_build([(FK, FV) | T], Result) :- tree_build(T, R1), map_put(R1, FK, FV, Result).

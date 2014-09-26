test :- catch((<QUERYSTRING>, <WRITERESULTS>), error(Err,_Context),
 (write('ERROR: '), write(Err))).

main :-
     test, nl.


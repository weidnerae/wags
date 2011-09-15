<?php

/**

Class GetProblems
Author Philip Meznar

This class was created for the WAGS project with
DST functionality.  This class replaces two classes
on the original DST - ProblemFetchService, which was
responsible for returning class names, and 
problemServiceImplementation, which was the fake
Database that held all of the problems.

This class returns an array of problems.  It is
worth noting that it does not return the actual
evalutaion, rules, or arguments, but rather integers
that map into the correct evaluation, rules, and 
arguments on the client side.

*/

class getProblems extends Command{
	
	public function execute(){
		$problems = Problem::getProblems();

		return JSON::success($problems);
	}

}

?>

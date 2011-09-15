<?php

/**
*Class GetProblemResults
*Author Philip Meznar
*
*This class is used in conjunction with the DST
*side of the WAGS project.  It returns an array
*of the users problem results.  At time of writing,
*WEStatus.java needs to be modified to handle
*arrays of OBJECTS, not just strings.
*/

class GetProblemResults extends Command
{
	public function execute(){
		$user = Auth::getCurrentUser();
		$problemResults = $user->getProblemResults();

		return JSON::success($problemResults);
	}
}


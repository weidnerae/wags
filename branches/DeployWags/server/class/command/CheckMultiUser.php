<?php

/**
*Class CheckMultiUser
*Author: Philip Meznar
*
*This class is meant to help with the partner functionality.
*When the user loads the editor page, this class is called
*to check for the existence of a multiUser exercise without
*a declared partner for this user.
*/

class CheckMultiUser extends Command
{
	public function execute()
	{
		$user = Auth::getCurrentUser();
		//admins don't need partners
		if($user->isAdmin()){
			return JSON::success("");
		}
		//Grab all exercises that require partners
		$multiUserExercises = Exercise::getMultiUsers();
		//If there are none, yay!
		if(!isSet($multiUserExercises[0])){
			return JSON::success("");
		}

		//Check to see if submissions exist for this exercises
		foreach($multiUserExercises as $exercise){
			$sub = Submission::getSubmissionByExerciseId($exercise->getId());

			//If there is no submission for this exercise yet
			//create one
			if(!$sub){
		        $submission = new Submission();
   	   		    $submission->setExerciseId($exercise->getId());
   	        	$submission->setFileId(0);
		        $submission->setUserId($user->getId());
        	    $submission->setSuccess(0);
          		$submission->setPartner("");
           		$submission->setAdded(time());
            	$submission->setUpdated(time());
				$submission->setNumAttempts(0);
				$submission->save();
			} else {
				$submission = $sub;
			}
	
			//If the submission doesn't have a partner
			if($submission->getPartner() == ""){
				return JSON::error($exercise->getTitle());
			}
		}

		return JSON::success("");
	}
}

?>

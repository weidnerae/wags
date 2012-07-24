<?php

class GetLogicalExercises extends Command
{
    public function execute()
    {
        $user = Auth::getCurrentUser();
        $section = Section::getSectionById($user->getSection());
        $exercises = $section->getLogicalExercises();
        $exercises = str_replace("\n", "", $exercises);
        $exerciseArray = explode("|", $exercises);
		sort($exerciseArray);
        $submissions = DSTSubmission::getAllSubmissionsByUserID();
		
		
		$result = array();
		
		// I haven't optimized this yet, but I don't think it's necessary 
		// since the datasets are going to be pretty small
		foreach ($exerciseArray as $exercise) {
			$hasSubmission = false;
			foreach ($submissions as $submission) {
				if ($exercise == $submission['title']) {
					$result[] = $exercise;
					$result[] = $submission['success'];
					
					$hasSubmission = true;
					break;
				}
			}
			
			// Set success to 0 if the problem hasn't been submitted yet
			// Also, ignore the empty first element in $exerciseArray
			if (!$hasSubmission && $exercise != "") {
				$result[] = $exercise;
				$result[] = 0;
			}
		}

        return JSON::success($result);
    }
}


?>

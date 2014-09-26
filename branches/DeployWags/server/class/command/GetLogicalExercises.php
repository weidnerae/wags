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

        //Get the ids for the logical microlabs problems using the titles. This is hacky
        //but short of redefining the database tables this is the best way to do it.
        $ids = LogicalMicrolab::getLogicalIdsFromTitles($exerciseArray);

        $submissions = DSTSubmission::getAllSubmissionsByUserID();
		$review = LogicalMicrolab::getAttempted();

        //Get the ids for the review problems 
        $reviewIds = LogicalMicrolab::getLogicalIdsFromTitles($review);
		
		$result = array();
		
		// I haven't optimized this yet, but I don't think it's necessary 
		// since the datasets are going to be pretty small
		foreach ($exerciseArray as $exercise) {
			$hasSubmission = false;
			foreach ($submissions as $submission) {
				if ($exercise == $submission['title']) {
					$result[$exercise] = $submission['success'];
					
					$hasSubmission = true;
					break;
				}
			}
			
			// Set success to 0 if the problem hasn't been submitted yet
			// Also, ignore the empty first element in $exerciseArray
			if (!$hasSubmission && $exercise != "") {
				$result[$exercise] = "0";
			}
		}
		
		//put it back in original order
		$originalOrder = explode("|", $exercises);
		$returnArray = array();

        //Sometimes (or everytime, not making assumptions), there is an empty string
        //at the end of the array, get rid of it.
        if (count($originalOrder) > count($ids)) {
            array_pop($originalOrder);
        }

        //need to index the ids with an integer, but the result with a string. 
        //Use a counter variable to index the ids
        $counter = 0;
		foreach ($originalOrder as $name) {
            if ($name) {
    			$returnArray[] = $name;
	    		$returnArray[] = $result[$name];
                $returnArray[] = $ids[$counter];
                $counter++;
            }
		}

        //Do the same through the review problems
        $counter = 0;
        if(!empty($review)){
            foreach($review as $r) {
                if ($r) {
                    $returnArray[] = $r;
                    $returnArray[] = "2";
                    $returnArray[] = $reviewIds[$counter];
                    $counter++;
                }
            }
        }

        //return the entire array
        return JSON::success($returnArray);
    }
}

?>

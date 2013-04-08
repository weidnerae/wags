<?php

class GetLMAssigned extends Command
{
    public function execute()
    {
        $user = Auth::getCurrentUser();
        $section = Section::getSectionById($user->getSection());
        $exercises = $section->getLogicalExercises();
        $exercises = str_replace("\n", "", $exercises);
        $exerciseArray = explode("|", $exercises);
		sort($exerciseArray);
        array_shift($exerciseArray); // remove leading empty
        $args = $_GET['args'];

        //---
        // If args = "", return a plain list without statuses
        //---
        if(empty($args)){
            return JSON::success($exerciseArray);
        }
        
        $returnArray = array();

        //---
        // If we want to populate a review list
        //---
        if($args == "getReview"){
            $review = LogicalMicrolab::getAttempted();
            if(!empty($review)){
                foreach($review as $r) {
                    if ($r) {
                        $returnArray[] = $r;
                    }
                }
            }
            
            return JSON::success($returnArray);
        }

        //---
        // Otherwise, return status of exercises
        //---
        if($args == "status"){
            $submissions = DSTSubmission::getAllSubmissionsByUserID();
            $result = array();
            
            // Cycle through each exercise
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
            
            foreach ($originalOrder as $name) {
                if ($name) {
                    $returnArray[] = $name;
                    $returnArray[] = $result[$name];
                }
            }
            
            return JSON::success($returnArray);
        }

        return JSON::error("Couldn't understand request");
    }
}


?>

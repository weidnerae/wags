<?php

# GetMagnetExercises.php
#
# Returns a list of all magnet problems for the users section
# Also returns a success value denoting whether or not the user has
# completed this exercise
#
# Philip Meznar '12

class GetAssignedExercises extends Command
{
    public function execute()
    {
        $user = Auth::getCurrentUser();
        $names = MagnetProblem::getAvailable();
        $review = MagnetProblem::getAttempted();
        $returnArray = array();

        $returnArray[] = "0";
        // Only occurs when no problems are available to the student
        if($names[0] == "0") {
           return JSON::success(array());
        }

        $counter = 0;
        // $names alternates id, name - starting with id
        for ($i = 0; $i < count($names) - 1; $i += 2) {
           // Creating new array with success values
           $returnArray[] = $names[$i];  // The id
           $returnArray[] = $names[$i + 1]; // The title
           
           // Grab the submission for this file, if it exists
           $sub = MagnetSubmission::getSubmissionByProblem($names[$i]);
           if($sub){
               $returnArray[] = $sub->getSuccess(); 
           } else {
               // Have to pass back success as String, not int, due
               // to passing as an array
               $returnArray[] = "0";
           }
           $counter++;
        }

        for ($i = 0; $i < count($review) - 1; $i += 2) {
        	$returnArray[] = $review[$i];	// The id
        	$returnArray[] = $review[$i + 1]; // The title
        	$returnArray[] = "2"; // This will denote that it is a review problem
            $counter++;
        }

        //Set the count
        $returnArray[0] = $counter . "";

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
                $returnArray[] = $ids[$counter];
                $returnArray[] = $name;
                $returnArray[] = $result[$name];
                $counter++;
            }
        }

        //Do the same through the review problems
        $counter = 0;
        if(!empty($review)){
            foreach($review as $r) {
                if ($r) {
                    $returnArray[] = $reviewIds[$counter];
                    $returnArray[] = $r;
                    $returnArray[] = "2";
                    $counter++;
                }
            }
        }

        //return the entire array
        return JSON::success($returnArray);

    }
}
?>

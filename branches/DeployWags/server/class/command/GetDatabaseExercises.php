<?php

# GetDatabaseExercises.php
#
# Returns a list of all database problems for the users section
# Also returns a success value denoting whether or not the user has
# completed this exercise
#
# Chris Hegre '14

class GetDatabaseExercises extends Command
{
    public function execute()
    {
        $user = Auth::getCurrentUser();
        $names = DatabaseProblem::getAvailable();
        $review = DatabaseProblem::getAttempted();
        $result = array();

        // Only occurs when no problems are available to the student
        if($names[0] == "0"){
           return JSON::success(array());
        }

        // $names alternates id, name - starting with id
        for ($i = 0; $i < count($names) - 1; $i += 2) {
           // Creating new array with success values
           $result[] = $names[$i];  // The id
           $result[] = $names[$i + 1]; // The title
           
           // Grab the submission for this file, if it exists
           $sub = DatabaseSubmission::getSubmissionByProblemId($names[$i]);
           if($sub){
               $result[] = $sub->getSuccess(); 
           } else {
                // Have to pass back success as String, not int, due
                // to passing as an array
                $result[] = "0";
           }
        }
        
        for ($i = 0; $i < count($review) - 1; $i += 2) {
        	$result[] = $review[$i];	// The id
        	$result[] = $review[$i + 1]; // The title
        	$result[] = "2"; // This will denote that it is a review problem
        }
 

        return JSON::success($result);
    }
}


?>

<?php

# GetDBAssigned.php
#
# Returns a list of all magnet problems for the users section
# Also returns a success value denoting whether or not the user has
# completed this exercise
#
# Philip Meznar '12

class GetDBAssigned extends Command
{
    public function execute()
    {
        $user = Auth::getCurrentUser();
        $names = DatabaseProblem::getAvailable();
        $result = array();
        $args = $_GET['args'];

        // Only occurs when no problems are available to the student
        if($names[0] == "0"){
           return JSON::success(array());
        }

        $titles = $this->stripIds($names);    

        //---
        // If args = "", return a plain list without statuses
        //---
        if(empty($args)){
            return JSON::success($titles);
        }

        //---
        // If we want to populate a review list
        //---
        if($args == "getReview"){
            $returnArray = array();
            $review = DatabaseProblem::getAttempted();
            if(!empty($review)){
                foreach($review as $r) {
                    if ($r) {
                        $returnArray[] = $r;
                    }
                }
            }
            
            $titles = $this->stripIds($returnArray);
            return JSON::success($titles);
        }

        //---
        // Otherwise, return status of exercises
        //---
        if($args == "status"){
            // += 2 implicitly removes ID so no stripIds
            for ($i = 0; $i < count($names) - 1; $i += 2) {
               // Creating new array with success values
               $result[] = $names[$i + 1]; // The title
               
               // Grab the submission for this file, if it exists
               $sub = DatabaseSubmission::getSubmissionByProblem($names[$i]);
               if($sub){
                   $result[] = $sub->getSuccess(); 
               } else {
                    // Have to pass back success as String, not int, due
                    // to passing as an array
                    $result[] = "0";
               }
            }

            return JSON::success($result);
        } 

        return JSON::error("Couldn't understand request");
    }

    private function stripIds($array){
        // For now, $names includes ID and title.  When we clean
        // out the old methods, we should change this to just
        // returning the name.  For now, that's what this section
        // of code does.
        $titles = array();
        for($i = 1; $i < count($array); $i+=2){
            $titles[] = $array[$i];
        }

        return $titles;
    }
}


?>

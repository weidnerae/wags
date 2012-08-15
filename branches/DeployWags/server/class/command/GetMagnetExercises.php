<?php

# GetMagnetExercises.php
#
# Returns a list of all magnet problem groups for the users section
# Also returns a success value denoting whether or not the user has
# completed this exercise
#
# Philip Meznar '12

class GetMagnetExercises extends Command
{
    public function execute()
    {
        $user = Auth::getCurrentUser();
        $names = MagnetProblem::getAvailable();
        $result = array();

        // $names alternates id, name - starting with id
        for($i = 0; $i < count($names) - 1; $i += 2){
           // Creating new array with success values
           $result[] = $names[$i];
           $result[] = $names[$i + 1];
           
           // Grab the submission for this file, if it exists
           $sub = MagnetSubmission::getSubmissionByProblem($names[$i]);
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
}


?>

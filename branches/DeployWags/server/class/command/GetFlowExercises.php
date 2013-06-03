<?php

# GetFlowExercises.php
#
# Returns a list of all flow problems
#
# Daniel Cook '13

class GetFlowExercises extends Command
{
    public function execute()
    {
        $user = Auth::getCurrentUser();
        $names = FlowProblem::getAvailable();
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
           
           // Have to pass back success as String, not int, due
           // to passing as an array
           $result[] = "0";
        }
        
        return JSON::success($result);
    }
}


?>

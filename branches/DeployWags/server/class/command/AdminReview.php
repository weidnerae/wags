<?php

/**
 * @class AdminReview
 * @author Philip Meznar
 *
 * This class was created for the WAGS project in
 * summer '11.  It's function is to return the user,
 * most recent file, and success value for a given
 * exercise.  It is used by the administrator for quick
 * and easy access to students success in a microlab.
 */

class AdminReview extends Command
{
    public function execute(){
        # Grab variables
        $ex = $_REQUEST['title'];
        $exercise = Exercise::getExerciseByTitle($ex);
        $subInfo = Exercise::getSubmissions($exercise->getId());
        $maxSubs = count($subInfo); # Used to not access outside of array
        $users = User::getUserNames();
        $subCount = 0; # Keeps track of position in subInfo array
        $result = array();
        # Create the row to add when there is no submission
        $emptyRow = array(
            'username' => "don't print",
            'name' => "N/A",
            'numAttempts' => "-1", # attempts get incremented by 1 on client
            'success' => "0",
            'partner' => " " );

        # Cycles through users, looks for a matching submission
        # These loops work ONLY BECAUSE both $subInfo and $users
        # are listed in alphabetical order by username
        foreach($users as $user){
           # I still don't totally understand when something is an array
           $user = $user[0]; 
           # Match -> add row from subInfo, iterate submission entry
           if($subCount < $maxSubs && $user == $subInfo[$subCount]['username']){
                $this->addRow($subInfo[$subCount], $result);
                $subCount = $subCount + 1;
           } else {
                $tmpRow = $emptyRow;
                $tmpRow['username'] = $user;  # Get the right username
                $this->addRow($tmpRow, $result);
           }
        }

        if(empty($subInfo)){
            return JSON::success("empty"); # bet this throws errors in client
        }
        return JSON::success($result);
    }
    
    # Actually adds the rows to the result array
    # In php 5.0, arrays are passed by reference, so we can do this!
    # Still must include preceding & though
    private function addRow($row, &$result){
            $result[] = $row['username'];
            $result[] = $row['name'];
            $result[] = $row['numAttempts'];
            $result[] = $row['success'];
            if($row['partner'] != NULL){
                $result[] = $row['partner'];
            }else{
                $result[] = "  ";
            }
    }
}

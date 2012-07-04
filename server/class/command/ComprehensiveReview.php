<?php

# ComprehensiveReview.php
#
# Invoked off of the Admin page, this file returns a CSV formatted
# record of all grades for that section, by exercise and by student
#
# Author: Philip Meznar, Appalachian State University

class ComprehensiveReview extends Command{
    
    public function execute(){
        # Set up the file header for CSV
        header("Content-type: text/csv");
        header("Content-Disposition: attachment; filename=gradeCheck.csv");
        header("Pragma: no-cache");
        header("Expires: 0");

        # Compose the array
        $this->getExercises($array, $progExercises, $logExercises);
        $this->getStudents($array, $usernames);
        $this->getGrades($array, $progExercises, $usernames);

        # Make the file
        $this->outputCSV($array);
    }   

    # outputCSV
    #
    # $data = The array that generates the CSV file
    #
    # Takes in an array, formats it to a file
    # From stackoverfow.com/questions/217414/create-a-csv-file-for-a-user-
    # in-php, which is itself based on http://www.php.net/manual/en/function.
    # fputcsv.php#100033
    function outputCSV($data){
        $outstream = fopen("php://output", "w");
        function __outputCSV(&$vals, $key, $filehandler){
            fputcsv($filehandler, $vals);
        }
        array_walk($data, "__outputCSV", $outstream);
        fclose($outstream);
    }
    
    # GetExercises
    #
    # array = The array to be filled
    #
    # Responsible for filling the first row of the array with the names
    # of the exercises for the section, both programming and logical
    function getExercises(&$array, &$progExercises, &$logExercises){
        $row = 0;
        $column = 0;
        
        $array[$row][$column] = "";
        $column++;
        
        // Get programming microlabs
        $titles = Exercise::getExerciseTitles();

        foreach($titles as $title){
            $array[$row][$column] = $title[0];  // Again, element of array
            $progExercises[$column - 1] = $title[0];
            $column++;
        }

        // Get logical microlabs
        unset($titles);
        $titles = DSTSubmission::getSubmissionTitles();
        $logCount = 0;

        foreach($titles as $title){
            $array[$row][$column] = $title[0];
            $logExercises[$logCount] = $title[0];
            $column++;
            $logCount++;
        }
    }

    # GetStudents
    #
    # array = The array to be filled
    #
    # Responsible for filling the first column of the array with the usernames
    # of the students in the section
    function getStudents(&$array, &$usernames){
        $users = User::getUserNames();
        $row = 1; $column = 0;

        foreach($users as $user){
            $array[$row][$column] = $user[0];
            $usernames[$row - 1] = $user[0];
            $row++;
        }   
    }

    # GetGrades
    #
    # array = The array to be filled
    #
    # Responsible for filling the rest of the array with the grades of
    # each student per exercise
    function getGrades(&$array, $progExercises, $usernames){
        $column = 1;

        // Add grades for all *programming* exercises
        foreach($progExercises as $progExercise){
            $exercise = Exercise::getExerciseByTitle($progExercise);
            $subInfo = Exercise::getSubmissions($exercise->getId());
            $maxSubs = count($subInfo);
            $subCount = 0;
            $row = 1;

            // Cycle through users for THIS exercise
            // Similar to AdminReview.php
            foreach($usernames as $username){
                if($subCount < $maxSubs && $username == $subInfo[$subCount]['username']){
                    if($subInfo[$subCount]['success'] == 0){
                        $text = " [Incorrect]";    
                    } else { 
                        $text = "";
                    }
                    $array[$row][$column] = $subInfo[$subCount]['numAttempts'] + 1 . "$text";
                    $subCount++; 
                } else {
                    $array[$row][$column] = "N/A";
                } 
                $row++;
            } 
            $column++; 
        } 
    }
}
?>

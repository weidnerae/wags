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
        $this->getExercises($array, $progExercises, $logExercises, $magExercises);
        $this->getStudents($array, $usernames);
        $this->getGrades($array, $progExercises, $logExercises, $magExercises, $usernames);

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
    function getExercises(&$array, &$progExercises, &$logExercises, &$magExercises){
        $row = 0;
        $column = 0;
        
        $array[$row][$column] = "";
        $column++;
        
        // Get programming microlabs
        $titles = Exercise::getExerciseTitles();
        
        if($titles != -1){
            foreach($titles as $title){
                $array[$row][$column] = $title[0];  // Again, element of array
                $progExercises[$column - 1] = $title[0];
                $column++;
            }
        }

        // Get logical microlabs
        unset($titles);
        $titles = DSTSubmission::getSubmissionTitles();
        $logCount = 0;

        if($titles != -1){
            foreach($titles as $title){
                $array[$row][$column] = $title[0];
                $logExercises[$logCount] = $title[0];
                $column++;
                $logCount++;
            }
		}
		
		// Get magnet microlabs
        unset($titles);
        $titles = MagnetSubmission::getExerciseTitles();
        $magCount = 0;

        if($titles != -1){
            foreach($titles as $title){
                $array[$row][$column] = $title[0];
                $magExercises[$magCount] = $title[1];
                $column++;
                $magCount++;
            }
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
    function getGrades(&$array, $progExercises, $logExercises, $magExercises, $usernames){
        $column = 1;

        // Add grades for all *programming* exercises
        if(!empty($progExercises)){
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

        // Add grades for all *logical* microlabs
        // Practically identical to programming exercise reporting
        if(!empty($logExercises)){
        foreach($logExercises as $logExercise){
            $submissions = DSTSubmission::getAllSubmissionsByTitle($logExercise);
            $maxSubs = count($submissions);
            $subCount = 0;
            $row = 1;

            // Cycle through users for THIS exercise
            foreach($usernames as $username){
                if($subCount < $maxSubs && $username == $submissions[$subCount]['username']){
                    if($submissions[$subCount]['success'] == 0){
                        $text = " [Incorrect]";
                    } else {
                        $text = "";
                    }
                    $array[$row][$column] = $submissions[$subCount]['numAttempts'] . "$text";
                    $subCount++;
                } else {
                    $array[$row][$column] = "N/A";
                }
                $row++;
            }
            $column++;
        }
        }
		
		
		// Add grades for all *magnet* microlabs
        // Practically identical to programming exercise reporting
        if(!empty($magExercises)){
        foreach($magExercises as $magExercise){
            $submissions = MagnetSubmission::getSubmissionsById($magExercise);
            $maxSubs = count($submissions);
            $subCount = 1;
            $row = 1;

            // Cycle through users for THIS exercise
            foreach($usernames as $username){
                if($subCount < $maxSubs && $username == $submissions[$subCount]['username']){
                    if($submissions[$subCount]['success'] == 0){
                        $text = " [Incorrect]";
                    } else {
                        $text = "";
                    }
                    $array[$row][$column] = $submissions[$subCount]['numAttempts'] . "$text";
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
}
?>

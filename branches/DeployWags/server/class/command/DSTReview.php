<?php

# Very similar in structure to AdminReview

class DSTReview extends Command
{
    public function execute(){
            $title = $_GET['title'];
            $type = $_GET['type'];

            if($type == "dst"){
                $submissions = DSTSubmission::getAllSubmissionsByTitle($title);
            } else if ($type == "magnet"){
                $id = MagnetProblem::getMagnetProblemByTitle($title)->getId();
                $submissions = MagnetSubmission::getSubmissionsById($id);
            }

            $maxSubs = count($submissions);
            $totalCorrect = 0;
            $subCount = 0;
            $users = User::getUserNames();  // Returns in alphabetical order
            $result = array();

            $emptyRow = array(
                'username' => "don't print",
                'success' => "0",
                'numAttempts' => "0");

            if(!$submissions){
                $result[] = "No"; $result[] = "Submissions"; $result[] = "Entered";
                return JSON::success($result);
            }

            foreach($users as $user){
                $user = $user[0];
                
                # If there is a submission for this user
                # Only works because both submissions and users are returned
                # alphabetically by username
                if($subCount < $maxSubs && $user == $submissions[$subCount]['username']){
                    $this->addRow($submissions[$subCount], $result, $totalCorrect);
                    $subCount = $subCount + 1;
                } else {
                    $tmpRow = $emptyRow;
                    $tmpRow['username'] = $user;
                    $this->addRow($tmpRow, $result, $totalCorrect);
                }
            }

            // Summary
            if($subCount > 0){
                $percentFinished = number_format(($totalCorrect * 100)/$subCount, 2);
                $result[] = "% finished: ";
                $result[] = " ";
                $result[] = "$percentFinished";
            } else {
                $result[] = "No"; $result[] = "Submissions"; $result[] = "Entered";
            }

            return JSON::success($result);    
    }

    private function addRow($row, &$result, &$totalCorrect){
        $result[] = $row['username'];
        $result[] = $row['success'];
        $result[] = $row['numAttempts'];

        if($row['success'] == 1) $totalCorrect++;
    }
}
?>

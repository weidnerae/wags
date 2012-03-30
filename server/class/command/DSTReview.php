<?php

# Very similar in structure to AdminReview

class DSTReview extends Command
{
    public function execute(){
            $title = $_GET['title'];
            $submissions = DSTSubmission::getAllSubmissionsByTitle($title);
            $maxSubs = count($submissions);
            $subCount = 0;
            $users = User::getUserNames();
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
                if($subCount < $maxSubs && $user == $submissions[$subCount]['username']){
                    $this->addRow($submissions[$subCount], $result);
                    $subCount = $subCount + 1;
                } else {
                    $tmpRow = $emptyRow;
                    $tmpRow['username'] = $user;
                    $this->addRow($tmpRow, $result);
                }
            }
            return JSON::success($result);    
    }

    private function addRow($row, &$result){
        $result[] = $row['username'];
        $result[] = $row['success'];
        $result[] = $row['numAttempts'];
    }
}

?>

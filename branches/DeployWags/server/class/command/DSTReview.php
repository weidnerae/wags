<?php

class DSTReview extends Command
{
    public function execute(){
            $title = $_GET['title'];
            $submissions = DSTSubmission::getAllSubmissionsByTitle($title);

            if(!$submissions){
                $result[] = "No"; $result[] = "Submissions"; $result[] = "Entered";
                return JSON::success($result);
            }

            foreach($submissions as $row){
                $userId = $row['userId'];
                $userName = User::getUserById($userId)->getUserName();

                $result[] = $userName;
                $result[] = $row['success'];
                $result[] = $row['numAttempts'];
            }

            return JSON::success($result);    
    }
}
?>

<?php

# Similar to AdminReview and DSTReview.  Review all exercises for individual Student
# Alex Weidner 2013

class StudentReview extends Command
{
    public function execute(){
            # fetch variable for user name
            $name = $_GET['name'];
            $user = User::getUserByUsername($name);
            $admin = Auth::getCurrentUser();
            $section = $admin->getSection();
            # fetch all submissions for that user
            $pSubs = $user->getProgrammingSubmissions($section);
            $lSubs = $user->getDstSubmissions($section);
            $mSubs = $user->getMagnetSubmissions($section);
			$dSubs = $user->getDatabaseSubmissions($section);


            $totalCorrect = 0; //used to calculate total completed
            $subCount = 0; 
            $result = array();
            $emptyRow = array(
                'exercise' => "don't print",
                'success' => "0",
                'numAttempts' => "0");

            //parse submissions into result array
            foreach($lSubs as $sub) {
                $row = array('exercise' => $sub['title'],
                             'numAttempts' => $sub['numAttempts'],
                             'success' => $sub['success']);
                $this->addRow($row, $result, $totalCorrect);
                $subCount++; //for each submision, increment subCount
            }
            
            //psubs and msubs must have the problem ID replaced with the problem name
            foreach($pSubs as $sub) {
                $problem = Exercise::getExerciseById($sub['exerciseId']);
                $name = $problem->getTitle();
                
                $row = array('exercise' => $name,
                             'numAttempts' => $sub['numAttempts'],
                             'success' => $sub['success']);
                $this->addRow($row, $result, $totalCorrect);
                $subCount++; //for each submission, increment subCount 
            }
                
            foreach($mSubs as $sub) {
                $problem = MagnetProblem::getMagnetProblemById($sub['magnetProblemId']);
                $name = $problem->getTitle();

                $row = array('exercise' => $name,
                             'numAttempts' => $sub['numAttempts'],
                             'success' => $sub['success']);
                $this->addRow($row, $result, $totalCorrect);
                $subCount++; //for each submission, increment subCount
            }
			
			foreach($dSubs as $sub) {
                $problem = DatabaseProblem::getDatabaseProblemById($sub['databaseProblemId']);
                $name = $problem->getTitle();

                $row = array('exercise' => $name,
                             'numAttempts' => $sub['numAttempts'],
                             'success' => $sub['success']);
                $this->addRow($row, $result, $totalCorrect);
                $subCount++; //for each submission, increment subCount
            }
            
            //return standard answer if there are no results to return
            if (!$result) {
                $result[] = "No"; $result[] = "Submissions"; $result[] = "Entered";
                return JSON::success($result);
            }
            
            //calculate the summary, only if there are results 
            $percentFinished = number_format(($totalCorrect * 100)/$subCount, 2);
            $result[] = "% finished: ";
            $result[] = " ";
            $result[] = "$percentFinished";
            //finally return the result
            return JSON::success($result);    
    }

    private function addRow($row, &$result, &$totalCorrect){
        $result[] = $row['exercise'];
        $result[] = $row['numAttempts'];
        $result[] = $row['success'];

        if($row['success'] == 1) $totalCorrect++;
    }
}
?>

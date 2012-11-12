<?php

/**
*MagnetSubmission
*
*Represents a submission for a magnet microlab
*
*Philip Meznar
*/

class MagnetSubmission extends Model
{
    protected $userId;
    protected $magnetProblemId;
    protected $sectionId;
    protected $numAttempts;
    protected $success;

    public function getTable(){
        return 'MagnetSubmission';
    }
    public function getUserId(){
        return $this->userId;
    }
    public function getSectionId(){
        return $this->sectionId;
    }
    public function getMagnetProblemId(){
        return $this->magnetProblemId;
    }
    public function getNumAttempts(){
        return $this->numAttempts;
    }
    public function getSuccess(){
        return $this->success;
    }

    public function setUserId($id){
        $this->userId = $id;
    }
    public function setSectionId($id){
        $this->sectionId = $id;
    }
    public function setMagnetProblemId($magnetProblemId){
        $this->magnetProblemId = $magnetProblemId;
    }
    public function setNumAttempts($num){
        $this->numAttempts = $num;
    }
    public function setSuccess($success){
        // If it was correct, can't become incorrect
        if($this->success == 1) return;

        $this->success = $success;
    }


    public static function getExerciseTitles(){
        require_once('Database.php');
        $db = Database::getDb();
        $user = Auth::getCurrentUser();

        $sth = $db->prepare('Select DISTINCT magnetProblem.title, MagnetSubmission.magnetProblemId 
		FROM MagnetSubmission LEFT OUTER JOIN magnetProblem ON MagnetSubmission.magnetProblemId = magnetProblem.id 
        WHERE sectionID = :section ORDER BY magnetProblemId');

        $sth->execute(array(':section' => $user->getSection()));

        $results = $sth -> fetchAll(PDO::FETCH_NUM);
        if(empty($results)) return -1;

        $vals = array_values($results);

        return $vals;
    }
	

    // getSubmissionByProblem
    // 
    // Returns at most 1 submission, for the current user and the
    // current problem.  Potentially returns null if there is no
    // current submission
    public static function getSubmissionByProblem($problemId){
        require_once('Database.php');
        $db = Database::getDb();
        $userId = Auth::getCurrentUser()->getId();
        
        $sth = $db->prepare('SELECT *
            FROM MagnetSubmission
            WHERE userId = :userId
            AND magnetProblemId = :problemId');
        $sth->execute(array(':userId' => $userId, ':problemId' => $problemId));
        
        return $sth->fetchObject('MagnetSubmission');
    }


    // getSubmissionsById
    // 
    // Gets all of the MagnetSubmissions for a certain magnetProblem
    // identified by Id for the given section, returning an associative
    // array of the user's username, their number of attempts, and whether or
    // not they ended up getting the problem correct
    public static function getSubmissionsById($id){
        require_once('Database.php');
        $db = Database::getDb();
        $section = Auth::getCurrentUser()->getSection();

        $sth = $db->prepare('SELECT user.username, MagnetSubmission.numAttempts,
        MagnetSubmission.success
        FROM MagnetSubmission
        JOIN user ON MagnetSubmission.userId = user.id
        WHERE MagnetSubmission.sectionId = :section
        AND user.admin = 0
        AND MagnetSubmission.magnetProblemId = :id
        ORDER BY username');

        $sth->execute(array(':section' => $section, ':id' => $id));
        $sth->setFetchMode(PDO::FETCH_ASSOC);

        return $sth->fetchAll();
    }

}

?>    

<?php

/**
*DatabaseSubmission
*
*Represents a submission for a database microlab
*
*Chris Hegre
*/

class DatabaseSubmission extends Model
{
    protected $userId;
	protected $sectionId;
    protected $databaseProblemId;
    protected $numAttempts;
    protected $success;

    public function getTable(){
        return 'DatabaseSubmission';
    }
    public function getUserId(){
        return $this->userId;
    }
	public function getSectionId(){
        return $this->sectionId;
    }
    public function getDatabaseProblemId(){
        return $this->databaseProblemId;
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
    public function setDatabaseProblemId($databaseProblemId){
        $this->databaseProblemId = $databaseProblemId;
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

        $sth = $db->prepare('Select DISTINCT databaseProblem.title, DatabaseSubmission.databaseProblemId 
		FROM DatabaseSubmission LEFT OUTER JOIN databaseProblem ON DatabaseSubmission.databaseProblemId = databaseProblem.id ORDER BY databaseProblemId');

        $results = $sth -> fetchAll(PDO::FETCH_NUM);
        if(empty($results)) return -1;

        $vals = array_values($results);

        return $vals;
    }
	

    // getSubmissionByProblemId
    // 
    // Returns at most 1 submission, for the current user and the
    // current problem.  Potentially returns null if there is no
    // current submission
    public static function getSubmissionByProblemId($problemId){
        require_once('Database.php');
        $db = Database::getDb();
		$userId = Auth::getCurrentUser()->getId();
        
        $sth = $db->prepare('SELECT *
            FROM DatabaseSubmission
            WHERE userId = :userId
            AND databaseProblemId = :problemId');
        $sth->execute(array(':userId' => $userId, ':problemId' => $problemId));
        return $sth->fetchObject('DatabaseSubmission');
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

        $sth = $db->prepare('SELECT user.username, DatabaseSubmission.numAttempts,
        DatabaseSubmission.success
        FROM DatabaseSubmission
        JOIN user ON DatabaseSubmission.userId = user.id
        WHERE user.admin = 0
        AND DatabaseSubmission.databaseProblemId = :id
        ORDER BY username');

        $sth->execute(array(':section' => $section, ':id' => $id));
        $sth->setFetchMode(PDO::FETCH_ASSOC);

        return $sth->fetchAll();
    }

}

?>    

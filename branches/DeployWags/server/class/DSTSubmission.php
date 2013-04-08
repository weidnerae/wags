<?php

/**
*DSTSubmission
*
*Represents a submission for a logical microlab
*
*Philip Meznar
*/

class DSTSubmission extends Model
{
    protected $userId;
    protected $sectionId;
    protected $title;
    protected $numAttempts;
    protected $success;

    public function getTable(){
        return 'dstSubmission';
    }
    public function getUserId(){
        return $this->userId;
    }
    public function getSectionId(){
        return $this->sectionId;
    }
    public function getTitle(){
        return $this->title;
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
    public function setTitle($title){
        $this->title = $title;
    }
    public function setNumAttempts($num){
        $this->numAttempts = $num;
    }
    public function setSuccess($success){
        if($this->success == 1) return;

        $this->success = $success;
    }

    
    public static function getSubmissionTitles(){
        require_once('Database.php');
        $db = Database::getDb();
        $user = Auth::getCurrentUser();

        $sth = $db->prepare('SELECT DISTINCT title FROM dstSubmission WHERE
            sectionId = :section ORDER BY title');
        $sth->execute(array(':section' => $user->getSection()));

        $results = $sth->fetchAll(PDO::FETCH_NUM);

        if(empty($results)) return -1;

        $vals = array_values($results);

        return $vals;
    }


    public static function getSubmissionByTitle($title){
        require_once('Database.php');
        $db = Database::getDb();
        $user = Auth::getCurrentUser();

        $sth = $db->prepare('SELECT * FROM dstSubmission WHERE title = :title AND userId = :id');
        $sth->execute(array(':title' => $title, ':id' => $user->getId()));

        return $sth->fetchObject('DSTSubmission');
    }

    public static function getAllSubmissionsByTitle($title){
        require_once('Database.php');
        $db = Database::getDb();
        $user = Auth::getCurrentUser();
        $section = $user->getSection();

        $sth = $db->prepare('SELECT user.username, dstSubmission.numAttempts,
            dstSubmission.success FROM dstSubmission JOIN user
            ON dstSubmission.userId = user.id WHERE sectionId = :section
            AND title = :title
            AND user.admin = 0
            ORDER BY username');
        $sth->execute(array(':section' => $section, ':title' => $title));
        $sth->setFetchMode(PDO::FETCH_ASSOC);

        return $sth->fetchAll();
     }
     
     public static function getAllSubmissionsByUserID() {
     	require_once('Database.php');
     	$db = Database::getDb();
     	$user = Auth::getCurrentUser();
     	$section = $user->getSection();
     	
     	$sth = $db->prepare('SELECT dstSubmission.title, dstSubmission.success 
     			FROM dstSubmission 
     			JOIN user
     			ON dstSubmission.userId = user.id 
     			WHERE sectionId = :section
     			AND userId = :id
     			ORDER BY title');
     	$sth->execute(array(':section' => $section, ':id' => $user->getId()));
     	$sth->setFetchMode(PDO::FETCH_ASSOC);
     	
     	return $sth->fetchAll();
     }
}

?>    

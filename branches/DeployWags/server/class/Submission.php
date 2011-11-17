<?php

/**
 * Submission
 *
 * Represents an entry in the submission table
 *
 */

class Submission extends Model
{
	protected $exerciseId;
	protected $fileId;
	protected $userId;
	protected $success;
	protected $partner;
	protected $numAttempts;

	public function getTable(){
		return 'submission';
	}
	public function getExerciseId(){
		return $this->exerciseId;
	}
	public function getFileId(){
		return $this->fileId;
	}
	public function getUserId(){
		return $this->userId;
	}
	public function getNumAttempts(){
		return $this->numAttempts;
	}

	public function setExerciseId($id){
		$this->exerciseId = $id;
	}
	public function setFileId($id){
		$this->fileId = $id;
	}
	public function setUserId($id){
		$this->userId = $id;
	}
	public function setNumAttempts($num){
		$this->numAttempts = $num;
	}
	public function getSuccess(){
		return $this->success;
	}
	public function setSuccess($successVal){
		$this->success = $successVal;
	}
	public function getPartner(){
		return $this->partner;
	}
	public function setPartner($partner){
		$this->partner = $partner;
	}


	public static function getSubmissionByExerciseId($exerciseId){
		require_once('Database.php');
		
		$user = Auth::GetCurrentUser();
		$db = Database::getDb();

		$sth = $db->prepare('SELECT * FROM submission WHERE exerciseId = :exId
			and userId = :userId');
		$sth->execute(array(':exId' => $exerciseId, ':userId' => $user->getId()));
		return $sth->fetchObject('Submission'); 
	}

}
?>

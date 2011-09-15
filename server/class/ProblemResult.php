<?php 

require_once("Model.php");

class ProblemResult extends Model
{
	protected $problemId;
	protected $userId;
	protected $attemptNumber;
	protected $currFeedback;
	protected $problemText;

	//Getters
	public function getTable(){
		return 'problemResult';
	}

	public function getProblemId(){
		return $this->problemId;
	}

	public function getUserId(){
		return $this->userId;
	}

	public function getAttemptNumber(){
		return $this->attemptNumber;
	}

	public function getCurrFeedback(){
		return $this->currFeedback;
	}

	public function getProblemText(){
		return $this->getProblemText;
	}

	//returns all nodes that have this result
	//as their problem result
	public function getNodes(){
		require_once('Database.php');
		$db = Database::getDb();

		$sth = $db->prepare('SELECT * FROM Node WHERE problemResult = :probResult');
		$sth->setFetchMode(PDO::FETCH_CLASS, 'Node');
		$sth->execute(array(':probResult' => $this->getId()));

		return $sth->fetchAll();
	}

	public function getEdges(){
		require_once('Database.php');
		$db = Database::getDb();

		$sth = $db->prepare('SELECT * FROM Edge WHERE problemResult = :probResult');
		$sth->setFetchMode(PDO::FETCH_CLASS, 'Edge');
		$sth->execute(array(':probResult' => $this->getId()));

		return $sth->fetchAll();
	}

	//Setters
	//None of these should be used, but are included
	//in case future uses arise
	public function setProblemId($probId){
		$this->problemId = $probId;
	}

	public function setUserId($userId){
		$this->userId = $userId;
	}

	public function setAttemptNumber($attemptNum){
		$this->attemptNumber = $attemptNum;
	}

	public function setCurrFeedback($feedBack){
		$this->currFeedback = $feedBack;
	}

	public function setProblemText($probText){
		$this->problemText = $probText;
	}
}

<?php

require_once("Model.php");

class Problem extends Model{
	protected $name;
	protected $problemText;
	protected $evaluation;
	protected $rules;
	protected $arguments;
	protected $section;

	//Getters
	public function getTable(){
		return 'problem';
	}

	public function getName(){
		return $this->name;
	}

	public function getProblemText(){
		return $this->problemText;
	}

	public function getEvaluation(){
		return $this->evaluation;
	}

	public function getRules(){
		return $this->rules;
	}

	public function getArguments(){
		return $this->arguments;
	}

	public function getSection(){
		return $this->section;
	}

	public function getNodes(){
		require_once('Database.php');
		$db = Database::getDb();

		$sth = $db->prepare('SELECT * FROM Node WHERE problem = :prob');
		$sth->setFetchMode(PDO::FETCH_CLASS, 'Node');
		$sth->execute(array(':prob' => $this->getId()));

		return $sth->fetchAll();
	}

	//This method returns all problems within 
	//the section of the user who is active during
	//the function call
	public static function getProblems(){
		require_once('Database.php');
		$db = Database::getDb();
		$user = Auth::getCurrentUser();

		$sth = $db->prepare('SELECT * FROM problems WHERE section like :section');
		$sth->setFetchMode(PDO::FETCH_CLASS, 'Problem');
		$sth->execute(array(':section' => $user->getSection()));

		return $sth->fetchAll();
	}

	//Setters
	//I'm gonna wait to implement these as,
	//once more, they shouldn't be used...

}

<?php

require_once("Model.php");

class Edge extends Model
{
	protected $problemResultId;
	protected $node1Key;
	protected $node2Key;

	//Getters
	public function getTable(){
		return 'edge';
	}

	public function getProblemResultId(){
		return $this->problemResultId;
	}

	//Node1 and Node2 should hold the
	//key for the node, rather than the
	//id, as keys for nodes should be distinct
	//(at least for now...)
	public function getNode1Key(){
		return $this->node1Key;
	}

	public function getNode2Key(){
		return $this->node2Key;
	}

	public function getNode1(){
		require_once('Database.php');
		$db = Database::getDb();

		$sth = $db->prepare('SELECT * FROM Node WHERE problemResult = :probResult AND
			Key LIKE :key');
		$sth->execute(array(':probResult' => $this->problemResult, 
			':key' => $this->node1Key));

		return $sth->fetchObject('Node');
	}

	public function getNode2(){
		require_once('Database.php');
		$db = Database::getDb();

		$sth = $db->prepare('SELECT * FROM Node WHERE problemResult = :probResult AND
			Key LIKE :key');
		$sth->execute(array(':probResult' => $this->problemResult, 
			':key' => $this->node2Key));

		return $sth->fetchObject('Node');
	}

		//Setters
	public function setProblemResultId($probResult){
		$this->problemResultId = $probResult;
	}

	public function setNode1($newNode){
		$this->node1 = $newNode;
	}

	public function setNode2($newNode){
		$this->node2 = $newNode;
	}
}

<?php

require_once("Model.php");

/**
* This class is used for grouping
* Specifically, this class allows the administrator
* to only view the information pertinent to users
* in their section.
*
* Thus, it's mainly used in WHERE clauses in 
* database queries
*/

class Section extends Model
{
	protected $name;
	protected $administrator;

	public function getTable(){
		return 'section';
	}
	public function getName(){
		return $this->name;
	}
	public function setName($name){
		$this->name = $name;
	}
	public function getAdmin(){
		return $this->administrator;
	}
	public function setAdmin($admin){
		$this->administrator = $admin;
	}

	//Used by getSections to fill listbox on
	//registration page
	public static function getSectionNames(){
		require_once('Database.php');
		$db = Database::getDb();

		$sth = $db->prepare('SELECT name FROM section;');
		$sth->setFetchMode(PDO::FETCH_NUM); 
		$sth->execute();

		return $sth->fetchAll();
	}

	//Used by RegisterUser to get the id from
	//the section name given by proxy.register
	public static function getIdByName($name){
		require_once('Database.php');
		$db = Database::getDb();

		$sth = $db->prepare('SELECT id FROM section WHERE name = :name');
		$sth->setFetchMode(PDO::FETCH_NUM);
		$sth->execute(array(':name' => $name));

		$idArray = $sth->fetch();
		$idValues = array_values($idArray);

		return $idValues[0];
	}
}

?>

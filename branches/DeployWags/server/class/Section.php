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
    protected $logicalExercises;

	public function getTable(){
		return 'section';
	}
	public function getName(){
		return $this->name;
	}
    public function getLogicalExercises(){
        return $this->logicalExercises;
    }
    public function setLogicalExercises($ex){
        $this->logicalExercises = $ex;
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

		$sth = $db->prepare('SELECT name FROM section ORDER BY name;');
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

    public static function getSectionById($id){
        require_once("Database.php");
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * FROM section WHERE id = :id');
        $sth->execute(array(':id' => $id));

        return $sth->fetchObject('Section');
    }

    public static function getSectionByName($name){
        require_once("Database.php");
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * FROM section WHERE name = :name');
        $sth->execute(array(':name' => $name));

        return $sth->fetchObject('Section');
    }
    
    /**
     * Check if the section exists in the database.
     *
     * @return boolean
     */
    public static function isSection($section)
    {
        require_once('Database.php');

        $db = Database::getDb();

        $sth = $db->prepare('SELECT count(*) as count FROM section WHERE name LIKE :section');
        $sth->execute(array(':section' => $section));

        $result = $sth->fetch(PDO::FETCH_ASSOC);
        
        return $result['count'] == 1;
    }
}

?>

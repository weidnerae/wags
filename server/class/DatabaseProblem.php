<?php

/**
 * DatabaseProblem.php 
 *
 * Represents an entry in the DatabaseProblem table, which
 * is an aggregate of information necessary for composing
 * a database problem on the client
 *
 */

class DatabaseProblem extends Model
{
	protected $timestamp;
	protected $title;
	protected $directions;
    protected $correct_query;
	protected $bogus_words;
	protected $group_name;

    ###############
    # GETTERS     #
    ###############
	public function getTable(){
		return 'databaseProblem';
	}

    public function getTitle(){
        return $this->title;
    }

    public function getDirections(){
        return $this->directions;
    }
	
    public function getCorrectQuery(){
        return $this->correct_query;
    }
	
	public function getBogusWords(){
        return $this->bogus_words;
    }
	
	public function getGroupName(){
        return $this->group_name;
    }

    public function isAssigned(){
        require_once('Database.php');
        $user = Auth::getCurrentUser();
        $db = Database::getDb();

        $sth = $db->prepare('SELECT status FROM SectionDB
            WHERE section = :section 
            AND databaseP = :id');

        $sth->setFetchMode(PDO::FETCH_ASSOC);
        $sth->execute(array(':section' => $user->getSection(),
            ':id' => $this->getId()));
        $result = $sth->fetch();

        if($result['status'] == 1) return true; 

        return false;
        
    }

    #############
    # SETTERS   #
    #############
    public function setTimestamp($time){
        $this->timestamp = $time;
    }

    public function setTitle($var){
         $this->title = $var;
    }

    public function setDirections($var){
         $this->directions = $var;
    }

    public function setCorrectQuery($var){
         $this->correct_query = $var;
    }
	public function setBogusWords($var){
         $this->bogus_words = $var;
    }
	public function setGroupName($var){
         $this->group_name = $var;
    }

    // Can't currently use JSON.php to encode objects,
    // and casting it to an array/get_object_vars() wasn't
    // working, so I cheat.
    //
    // Note: Some fields aren't sent, because they are not
    // used by the client
    public function toArray(){
        $objArray = array(
            "Object" => "DatabaseProblem", // used for client side creation
            "id" => $this->getId(),
            "timestamp" => $this->timestamp,
            "title" => $this->title,
            "directions" => $this->directions,
            "correct_query" => $this->correct_query,
			"bogus_words" => $this->bogus_words,
        );
        return $objArray;
    }

    ###########
    # STATIC  #
    ###########

	#  Takes an id of a database problem, returns the entire database problem
    #  Used for sending stuff to the client
    public static function getDatabaseProblemById($id){
        // Database set up
        require_once('Database.php');
        $user = Auth::GetCurrentUser();
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * FROM databaseProblem WHERE id = :id');
        $sth->execute(array(':id' => $id));
        return $sth->fetchObject('DatabaseProblem');
    }
	public static function sortById($exercises){
        require_once('Database.php');
        $db = Database::getDb();

        for($i = 0; $i < count($exercises); $i++){
            $tmp = $exercises[$i];
            $exercises[$i] = "'$tmp'";
        }

        $exercises = implode(",", $exercises);
        
        $sth = $db->query('SELECT title FROM databaseProblem
            WHERE title IN ('.$exercises.')
            ORDER BY id');

        $result = $sth->fetchAll(PDO::FETCH_COLUMN, 0);

        return $result;
    }
	public static function getDatabaseProblemByTitle($title){
        // Database set up
        require_once('Database.php');
        $user = Auth::GetCurrentUser();
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * FROM databaseProblem WHERE title = :title');
        $sth->execute(array(':title' => $title));
        return $sth->fetchObject('DatabaseProblem');
    }

	    # Set the entry in SectionMP with the given magnetP id to 
    # the given status - used in SetMagnetExercises.php
    public static function setProblemStatus($id, $status){
        require_once('Database.php');
        $db = Database::getDb();
        $user = Auth::getCurrentUser();
        $sth = $db->prepare('SELECT section, databaseP 
            FROM databaseProblem, SectionDB
            WHERE SectionDB.section = :section 
            AND SectionDB.databaseP = :id');

        $sth->setFetchMode(PDO::FETCH_ASSOC);
		$sth->execute(array(':section' => $user->getSection(),':id' => $id));
        $results = $sth->fetchAll();
		
		if (count($results) < 1)
		{
			$sth = $db->prepare('INSERT INTO SectionDB
				(section,databaseP,status) VALUES(:section,:id,:status)');
			$sth->execute(array(':status' => $status, ':id' => $id, ':section' => $user->getSection()));
		}
		else
		{
			$sth = $db->prepare('UPDATE SectionDB
				SET status = :status
				WHERE databaseP = :id
				AND section = :section');
			$sth->execute(array(':status' => $status, ':id' => $id, ':section' => $user->getSection()));
		}
    }
	
    // Returns all the magnetProblems for a group that are currently
    public static function getAvailable(){
        require_once('Database.php');
        $db = Database::getDb();
        $user = Auth::getCurrentUser();

        $sth = $db->prepare('SELECT DISTINCT databaseProblem.title, databaseProblem.id 
            FROM databaseProblem, SectionDB
            WHERE SectionDB.section = :section 
            AND SectionDB.status = 1
            AND SectionDB.databaseP = databaseProblem.id');

        $sth->setFetchMode(PDO::FETCH_ASSOC);
		$sth->execute(array(':section' => $user->getSection()));
        $results = $sth->fetchAll();

        // So, this is really like working through two levels of array
        // Results is an array of arrays, each result is an array
        foreach($results as $result){
            $values[] = $result['id'];
            $values[] = $result['title'];
        }

        // Used to indicate to GetDatabaseExercises.php that no 
        // exercises are assigned
        if($results == null){
            $values[] = "0";
            $values[] = "No Database Problems Assigned!";
        }

        return $values;
    }
	#  Used by GetMagnetExercises.php to fill the magnetProblems listbox
    #  on the administrative tab
    #
    #  Returns the names of all the magnetProblemGroups a section has access to
    public static function getDatabaseProblemGroups($source = null){
        require_once('Database.php');
        $user = Auth::GetCurrentUser();
        $db = Database::getDb();

        $sth = $db->prepare('SELECT DISTINCT name 
            FROM DatabaseProblemGroups');
        $sth->setFetchMode(PDO::FETCH_ASSOC);
		$sth->execute();
        $results = $sth->fetchAll();

        if(empty($results)) return null;
		
        foreach($results as $result){
            $values[] = $result['name'];
        }

        return $values;
    }
	
	
	public static function getDatabaseProblemsByGroup($group){
        require_once('Database.php');
        $user = Auth::GetCurrentUser();
        $db = Database::getDb();
        
        $sth = $db->prepare('SELECT DISTINCT title 
            FROM databaseProblem
            WHERE group_name = :group');
        $sth->setFetchMode(PDO::FETCH_ASSOC);
        $sth->execute(array(':group' => $group));

        $results = $sth->fetchAll();
        if(empty($results)) return null;
		
        foreach($results as $result){
            $values[] = $result['title'];
        }

        return $values;

    }
	# Changes all entries in SectionMP for the users section to
    # '2' = which is AVAILABLE but not IMPLEMENTED
    # used in SetMagnetExercises.php
    public static function unAssignAll(){
        require_once('Database.php');
        $db = Database::getDb();
        $user = Auth::getCurrentUser();

        $sth = $db->prepare('UPDATE SectionDB
            SET status = 2
            WHERE section = :section');
        $sth->execute(array(':section' => $user->getSection()));
    }
	
    // Returns all the magnetProblems for a group that are currently
    // assigned to the students (status = 1) in an array of id's and names
    public static function getAttempted(){
        require_once('Database.php');
        $db = Database::getDb();
        $user = Auth::getCurrentUser();

        $sth = $db->prepare('SELECT DISTINCT databaseProblem.title,
             databaseProblem.id, SUM(numAttempts) as attempts
            FROM databaseProblem, SectionDB, DatabaseSubmission, user
            WHERE SectionDB.section = :section
            AND SectionDB.magnetP = databaseProblem.id
			AND DatabaseSubmission.databaseProblemId = databaseProblem.id
            AND DatabaseSubmission.sectionId = :section
            AND user.admin = 0
            AND user.id = DatabaseSubmission.userId
			GROUP BY databaseProblem.id
    		HAVING attempts > 0');

        $sth->setFetchMode(PDO::FETCH_ASSOC);
		$sth->execute(array(':section' => $user->getSection()));
        $results = $sth->fetchAll();

        // So, this is really like working through two levels of array
        // Results is an array of arrays, each result is an array
        foreach($results as $result){
            $values[] = $result['id'];
            $values[] = $result['title'];
        }

        // Used to indicate to GetMagnetExercises.php that no 
        // exercises are assigned
        if($results == null){
            return null;
        }

        return $values;
    }

}

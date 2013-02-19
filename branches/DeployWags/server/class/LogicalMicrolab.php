<?php

/**
* LogicalMicrolab.php
*
* This used to be simply a utility class that provided some static methods for
* pulling data out of the database.
* 
* Now, it also replicates the LogicalMicrolabs table within the DB
*
* Author:  Philip Meznar '12
*/

class LogicalMicrolab extends Model
{
    protected $title;
    protected $problemText;
    protected $nodes;
    protected $xPositions;
    protected $yPositions;
    protected $insertMethod;
    protected $edges;
    protected $evaluation;
    protected $edgeRules;
    protected $arguments;
    protected $edgesRemovable;
    protected $nodesDraggable;
    protected $nodeType;
    protected $genre;
    protected $group;

    public function getTable(){
        return 'LogicalMicrolabs';
    }

    public function toArray(){
        $objArray = array(
            "Object" => "LogicalMicrolab",
            "id" => $this->getId(),
            "title" => $this->title,
            "problemText" => $this->problemText,
            "nodes" => $this->nodes,
            "xPositions" => $this->xPositions,
            "yPositions" => $this->yPositions,
            "insertMethod" => $this->insertMethod,
            "edges" => $this->edges,
            "arguments" => $this->arguments,
            "evaluation" => $this->evaluation,
            "edgeRules" => $this->edgeRules,
            "edgesRemovable" => $this->edgesRemovable,
            "nodesDraggable" => $this->nodesDraggable,
            "nodeType" => $this->nodeType,
            "genre" => $this->genre,
            "group" => $this->group,
        ); 
        return $objArray;
    }

    public static function getLogicalMicrolabByTitle($title){
        require_once('Database.php');
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * FROM LogicalMicrolabs
            WHERE title = :title');
        $sth->execute(array(':title' => $title));

        return $sth->fetchObject('LogicalMicrolab');

    }

    // Returns all implemented Logical Microlab subjects in alphabetical order
	public static function getSubjects(){
		require_once('Database.php');
		$db = Database::getDb();

		$sth = $db->prepare('SELECT subject FROM LMSubject ORDER BY subject;');
		$sth->setFetchMode(PDO::FETCH_NUM); 
		$sth->execute();

        $result = $sth->fetchAll();
        foreach($result as $entry){
            $values[] = $entry[0];
        }
		return $values;
	}

    // Returns all groups for a subject in the order stored in the database -
    // this should roughly estimate order of assignment in a classroom
	public static function getGroups($subject){
		require_once('Database.php');
		$db = Database::getDb();

		$sth = $db->prepare('SELECT LMGroup.group
            FROM LMGroup JOIN LMSubject
            ON LMSubject.id = LMGroup.Subject
            WHERE LMSubject.subject = :subject
            ');
		$sth->setFetchMode(PDO::FETCH_NUM);
		$sth->execute(array(':subject' => $subject));

		$result = $sth->fetchAll();

        foreach($result as $entry){
            $values[] = $entry[0];
        }
		return $values;
	}

    // Returns all exercises for a group in the order stored in the database - 
    // should roughly estimate order of assignment in a classroom
    public static function getExercises($group){
        require_once("Database.php");
        $db = Database::getDb();

        $sth = $db->prepare('SELECT LMExercise.Exercise
            FROM LMExercise JOIN LMGroup
            ON LMExercise.Group = LMGroup.id
            WHERE LMGroup.Group = :group
            ORDER BY LMExercise.id');
		$sth->setFetchMode(PDO::FETCH_NUM);
        $sth->execute(array(':group' => $group));

        $result = $sth->fetchAll();
        foreach($result as $entry){
            $values[] = $entry[0];
        }
		return $values;
    }
    
    public static function getAttempted($exercise) {
    	require_once("Database.php");
    	$db = Database::getDb();
    	$user = Auth::getCurrentUser();
		
		$exercise = str_replace("'", "\\'", $exercise);
		
		if (count($exercise) > 1) { //$exercise has a blank element at [0]
			array_shift($exercise);
		}
			
		$exercises = implode("','", $exercise);

    	$sth = $db->query('SELECT title, SUM(numAttempts) AS attempts
    					FROM dstSubmission
						WHERE title IN (\'' . $exercises . '\')
    					AND sectionId = ' . $user->getSection() . '
						GROUP BY dstSubmission.title
						HAVING attempts > 1');
						

    	$sth->setFetchMode(PDO::FETCH_NUM);
    	
    	$result = $sth->fetchAll();
    	foreach ($result as $entry) {
    		$values[] = $entry[0];
    	}
    	return $values;
    }

}

?>

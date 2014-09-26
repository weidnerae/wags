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
    protected $groupID;

    public function getTable(){
        return 'LogicalMicrolabs';
    }

    public function setTitle($title){
        $this->title = $title;
    }

    public function setProblemText($problemText){
        $this->problemText = $problemText;
    }

    public function setNodes($nodes){
        $this->nodes = $nodes;
    }

    public function setxPositions($xPositions){
        $this->xPositions = $xPositions;
    }

    public function setyPositions($yPositions){
        $this->yPositions = $yPositions;
    }

    public function setInsertMethod($insertMethod){
        $this->insertMethod = $insertMethod;
    }

    public function setEdges($edges){
        $this->edges = $edges;
    }

    public function setEvaluation($evaluation){
        $this->evaluation = $evaluation;
    }

    public function setEdgeRules($edgeRules){
        $this->edgeRules = $edgeRules;
    }

    public function setArguments($arguments){
        $this->arguments = $arguments;
    }

    public function setEdgesRemovable($edgesRemovable){
        $this->edgesRemovable = $edgesRemovable;
    }

    public function setNodesDraggable($nodesDraggable){
        $this->nodesDraggable = $nodesDraggable;
    }

    public function setNodeType($nodeType){
        $this->nodeType = $nodeType;
    }

    public function setGenre($genre){
        $this->genre = $genre;
    }

    public function setGroupID($group){
        $this->groupID = $group;
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
            "group" => $this->groupID,
        ); 
        return $objArray;
    }

    public static function getLogicalIdsFromTitles($exercises) {
        require_once('Database.php');
        $db = Database::getDb();

        for($i = 0; $i < count($exercises); $i++){
            $tmp = $exercises[$i];
            $exercises[$i] = "\"$tmp\"";
        }

        $exercises = implode(",", $exercises);
        $sth = $db->query('SELECT id FROM LogicalMicrolabs
                           WHERE title IN ('.$exercises.')' );

        $result = $sth->fetchAll(PDO::FETCH_COLUMN, 0);

        return $result;
    }

        
    public static function getLogicalMicrolabByTitle($title){
        require_once('Database.php');
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * FROM LogicalMicrolabs
            WHERE title = :title');
        $sth->execute(array(':title' => $title));

        return $sth->fetchObject('LogicalMicrolab');

    }

    public static function getLogicalMicrolabById($problemId){
        require_once('Database.php');
        $db = Database::getDb();
        
        $sth = $db->prepare('SELECT * from LogicalMicrolabs
                             WHERE id = :problemId;');
        $sth->execute(array(':problemId' => $problemId));

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

    // Organizes Logical Microlabs by id number
    // Used in SetLogicalExercises
    public static function sortById($exercises){
        require_once('Database.php');
        $db = Database::getDb();

        for($i = 0; $i < count($exercises); $i++){
            $tmp = $exercises[$i];
            $exercises[$i] = "'$tmp'";
        }

        $exercises = implode(",", $exercises);
        
        $sth = $db->query('SELECT title FROM LogicalMicrolabs
            WHERE title IN ('.$exercises.')
            ORDER BY id');

        $result = $sth->fetchAll(PDO::FETCH_COLUMN, 0);

        return $result;
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
        if(empty($result)){
            $values[] = "No exercises";
        } else {
            foreach($result as $entry){
                $values[] = $entry[0];
            }
        }

		return $values;
    }

    public static function addToLMExercise($title, $group){
        require_once("Database.php");
        $db = Database::getDb();
        $title = '"'.$title.'"';

        $sth = $db->prepare("INSERT INTO LMExercise
            VALUES ('',$title,$group)");
        $sth->execute();
    }
    
    public static function getAttempted() {
    	require_once("Database.php");
    	$db = Database::getDb();
    	$user = Auth::getCurrentUser();
		
    	$sth = $db->prepare('SELECT title, SUM(numAttempts) AS attempts
    					FROM dstSubmission, user
                        WHERE dstSubmission.sectionId = :section
    					AND user.admin = 0
                        AND user.id = dstSubmission.userId
						GROUP BY dstSubmission.title
						HAVING attempts > 0');

    	$sth->setFetchMode(PDO::FETCH_NUM);
        $sth->execute(array(':section' => $user->getSection()));
    	
    	$result = $sth->fetchAll();

        if(empty($result)){
            return null;
        }

    	foreach ($result as $entry) {
    		$values[] = $entry[0];
    	}
    	return $values;
    }
}

?>

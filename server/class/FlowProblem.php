<?php

/**
 * FlowProblem.php 
 *
 * Represents an entry in the FlowProblem table, which
 * is an aggregate of information necessary for composing
 * a flow problem on the client
 *
 */

class FlowProblem extends Model
{
    protected $timestamp;
	protected $title;
	protected $directions;
    protected $dropPointPositions;
    protected $arrowRelations;
    protected $variables;
    protected $operators;
    protected $conditions;
    protected $answerChoices;
    protected $solution;

    ###############
    # GETTERS     #
    ###############
	public function getTable(){
		return 'FlowProblem';
	}

    public function getTitle(){
        return $this->title;
    }

    public function getDirections(){
        return $this->directions;
    }

    public function getDropPointPositions(){
        return $this->dropPointPositions;
    }

    public function getArrowRelations(){
        return $this->arrowRelations;
    }

    public function getVariables(){
        return $this->variables;
    }

    public function getOperators(){
        return $this->operators;
    }

    public function getConditions(){
        return $this->conditions;
    }

    public function getAnswerChoices(){
        return $this->answerChoices;
    }

    public function getSolution(){
        return $this->solution;
    }


    #############
    # SETTERS   #
    #############
    public function setTimestamp($time){
        $this->timestamep = $time;
    }

    public function setTitle($var){
         $this->title = $var;
    }

    public function setDirections($var){
         $this->directions = $var;
    }

    public function setDropPointPositions($var){
        $this->dropPointPositions = $var;
    }

    public function setArrowRelations($var){
         $this->arrowRelations = $var;
    }

    public function setVariables($var){
         $this->variables = $var;
    }

    public function setOperators($var){
         $this->operators = $var;
    }

    public function setConditions($var){
         $this->conditions = $var;
    }

    public function setAnswerChoices($var){
         $this->answerChoices = $var;
    }

    public function setSolution($var){
         $this->solution = $var;
    }

    // Can't currently use JSON.php to encode objects,
    // and casting it to an array/get_object_vars() wasn't
    // working, so I cheat.
    //
    // Note: Some fields aren't sent, because they are not
    // used by the client
    public function toArray(){
        $objArray = array(
            "Object" => "FlowProblem", // used for client side creation
            "id" => $this->getId(),
            "timestamp" => $this->timestamp,
            "title" => $this->title,
            "directions" => $this->directions,
            "dropPointPositions" => $this->dropPointPositions,
            "arrowRelations" => $this->arrowRelations,
            "variables" => $this->variables,
            "operators" => $this->operators,
            "conditions" => $this->conditions,
            "answerChoices" => $this->answerChoices,
            "solution" => $this->solution,
        );
        return $objArray;
    }

    ###########
    # STATIC  #
    ###########

    #  Takes an id of a magnet problem, returns the entire magnet problem
    #  Used for sending stuff to the client
    public static function getFlowProblemById($id){
        // Database set up
        require_once('Database.php');
        $user = Auth::GetCurrentUser();
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * FROM FlowProblem WHERE id = :id');
        $sth->execute(array(':id' => $id));
        return $sth->fetchObject('FlowProblem');
    }

    #  Same as above function, but with the title of the magnet
    public static function getFlowProblemByTitle($title){
        // Database set up
        require_once('Database.php');
        $user = Auth::GetCurrentUser();
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * FROM FlowProblem WHERE title = :title');
        $sth->execute(array(':title' => $title));
        return $sth->fetchObject('FlowProblem');
    }
    
    public static function deleteFlowProblemByTitle($title){
        // Database set up
        require_once('Database.php');
        $db = Database::getDb();

        // Deletions should cascade through SectionMP, Simplefiles, and MPState
        $sth = $db->prepare('DELETE FROM FlowProblem WHERE title = :title');
        $sth->execute(array(':title' => $title));

        return 1;
    }

    public static function getAvailable(){
        require_once('Database.php');
        $db = Database::getDb();
        $user = Auth::getCurrentUser();
        $sth = $db->prepare('SELECT DISTINCT FlowProblem.title, FlowProblem.id
            FROM FlowProblem');

        $sth->setFetchMode(PDO::FETCH_ASSOC);
        $sth->execute(array());
        $results = $sth->fetchAll();
        foreach($results as $result){
            $values[] = $result['id'];
            $values[] = $result['title'];
        }

        if($results == null){
            $values[] = "0";
            $values[] = "No Flow Problems Assigned!";
        }

        return $values;

    }
}

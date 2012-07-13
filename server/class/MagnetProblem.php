<?php

/**
 * MagnetProblem.php 
 *
 * Represents an entry in the MagnetProblem table, which
 * is an aggregate of information necessary for composing
 * a magnet problem on the client
 *
 */

class MagnetProblem extends Model
{
	protected $timestamp;
	protected $title;
	protected $directions;
	protected $type;
	protected $creationStation;
	protected $mainFunction;
	protected $innerFunctions;
	protected $forLeft;
	protected $forMid;
	protected $forRight;
    protected $booleans;
    protected $statements;
    protected $solution;
    protected $section;

    ###############
    # GETTERS     #
    ###############
	public function getTable(){
		return 'magnetProblem';
	}

    public function getTitle(){
        return $this->title;
    }

    public function getDirections(){
        return $this->directions;
    }

    public function getProblemType(){
        return $this->type;
    }

    public function getCreationStation(){
        return $this->creationStation;
    }

    public function getMainFunction(){
        return $this->mainFunction;
    }

    public function getInnerFunctions(){
        return $this->innerFunctions;
    }

    public function getForLeft(){
        return $this->forLeft;
    }

    public function getForMid(){
        return $this->forMid;
    }

    public function getForRight(){
        return $this->forRight;
    }

    public function getBooleans(){
        return $this->booleans;
    }

    public function getStatements(){
        return $this->statements;
    }

    public function getSolution(){
        return $this->solution;
    }

    public function getSection(){
        return $this->section;
    }

    #############
    # SETTERS   #
    #############
    public function setTitle($var){
         $this->title = $var;
    }

    public function setDirections($var){
         $this->directions = $var;
    }

    public function setProblemType($var){
         $this->type = $var;
    }

    public function setCreationStation($var){
         $this->creationStation = $var;
    }

    public function setMainFunction($var){
         $this->mainFunction = $var;
    }

    public function setInnerFunctions($var){
         $this->innerFunctions = $var;
    }

    public function setForLeft($var){
         $this->forLeft = $var;
    }

    public function setForMid($var){
         $this->forMid = $var;
    }

    public function setForRight($var){
         $this->forRight = $var;
    }

    public function setBooleans($var){
         $this->booleans = $var;
    }

    public function setStatements($var){
         $this->statements = $var;
    }

    public function setSolution($var){
         $this->solution = $var;
    }

    public function setSection($var){
         $this->section = $var;
    }

    // Can't currently use JSON.php to encode objects,
    // and casting it to an array/get_object_vars() wasn't
    // working, so I cheat.
    public function toArray(){
        $objArray = array(
            "Object" => "MagnetProblem", // used for client side creation
            "id" => $this->getId(),
            "timestamp" => $this->timestamp,
            "title" => $this->title,
            "directions" => $this->directions,
            "type" => $this->type,
            "creationStation" => $this->creationStation,
            "mainFunction" => $this->mainFunction,
            "innerFunctions" => $this->getData(explode(",", $this->innerFunctions)),
            "forLeft" => $this->getData(explode(",", $this->forLeft)),
            "forMid" => $this->getData(explode(",", $this->forMid)),
            "forRight" => $this->getData(explode(",", $this->forRight)),
            "bools" => $this->getData(explode(",", $this->booleans)),
            "statements" => $this->getData(explode(",", $this->statements)),
            "solution" => $this->solution,
            "section" => $this->section,
        );
        return $objArray;
    }

    # This function takes the ids of "problemData" entries in the database,
    # and constructs an array of the contents of the entries whose ID's were
    # given.  It then returns that array of strings
    private function getData($ids){
        require_once('Database.php');
        $db = Database::getDb();

        $inQuery = implode(',', array_fill(0, count($ids), '?'));
        $sth = $db->prepare('SELECT data FROM magnetData WHERE id
            IN(' . $inQuery . ')');

        $sth->execute($ids);
        $results = $sth->fetchall(PDO::FETCH_NUM);

        return $results;
    }
    
    ###########
    # STATIC  #
    ###########

    #  Currently, these DO NOT use sections as filters,
    #  THIS WILL HAVE TO CHANGE
    public static function getMagnetProblemById($id){
        // Database set up
        require_once('Database.php');
        $user = Auth::GetCurrentUser();
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * FROM magnetProblem WHERE id = :id');
        $sth->execute(array(':id' => $id));
        return $sth->fetchObject('MagnetProblem');
    }

}
?>

<?php

require_once("Model.php");

class SimpleFile extends Model
{
    protected $className;
    protected $package;
    protected $contents;
    protected $magnetProblemId;
    protected $test;

    // Getters
    public function getTable(){
        return 'SimpleFiles';
    }

    public function getClassName(){
        return $this->className;
    }

    public function getPackage(){
        return $this->package;
    }

    public function getContents(){
        return $this->contents;
    }

    public function getMagnetProblemId(){
        return $this->magnetProblemId;
    }

    public function isTest(){
        return $this->test;
    }

    // Setters
    public function setTest($var){
         $this->test = $var;
    }

    public function setClassName($var){
         $this->className = $var;
    }

    public function setPackage($var){
         $this->package = $var;
    }

    public function setContents($var){
         $this->contents = $var;
    }

    public function setMagnetProblemId($var){
         $this->magnetProblemId = $var;
    }

    // Static functions
    public static function getFilesForMP($id){
        require_once('Database.php');
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * 
            FROM SimpleFiles
            WHERE magnetProblemId = :id');
        $sth->setFetchMode(PDO::FETCH_CLASS, 'SimpleFile');
        $sth->execute(array(':id' => $id));

        return $sth->fetchAll();
    } 

    public static function getTestFileForMP($id){
        require_once('Database.php');
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * 
            FROM SimpleFiles
            WHERE magnetProblemId = :id
            AND test = 1');
        $sth->setFetchMode(PDO::FETCH_CLASS, 'SimpleFile');
        $sth->execute(array(':id' => $id));

        return $sth->fetch();
    } 
}

?>    

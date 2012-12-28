<?php

/**
 * MagnetProblemState
 * 
 * Represents an entry in the MPState table
 *
 */

 class MagnetProblemState extends Model
 {
    protected $userID;
    protected $magnetID;
    protected $state;
    protected $success;

    public function getTable(){
        return 'MPState';
    }

    public function getUserID(){
        return $this->userID;
    }
    public function getMagnetId(){
        return $this->magnetID;
    }
    public function getState(){
        return $this->state;
    }
    public function getSuccess(){
        return $this->success;
    }
    
    public function setUserID($id){
        $this->userID = $id;
    }
    public function setMagnetID($id){
        $this->magnetID = $id;
    }
    public function setState($st){
        $this->state = $st;
    }
    public function setSuccess($su){
        $this->success = $su;
    }

    public static function getEntryByProblemId($magnetID){
        require_once('Database.php');

        $user = Auth::GetCurrentUser();
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * FROM MPState WHERE magnetID = :mID
                             AND userID = :uID');
        $sth->execute(array(':mID' => $magnetID, ':uID' => $user->getId()));
        return $sth->fetchObject('MagnetProblemState'); 
    }
}
?>

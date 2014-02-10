<?php

/**
 * CreatedMagnet
 *
 * Represents a magnet created through the MagnetMaker that is part of a 
 * saved state and thus needs to be saved into the database to be pulled
 * out when the state is restored.
 */

 class CreatedMagnet extends Model
 {
    protected $userID;
    protected $magnetProblemID;
    protected $magnetID;
    protected $magnetContent;

    public function getTable(){
        return 'MPCreatedMagnet';
    }

    public function getUserID(){
        return $this->userID;
    }

    public function getMagnetProblemID(){
        return $this->magnetProblemID;
    }

    public function getMagnetID(){
        return $this->magnetID;
    }

    public function getMagnetContent(){
        return $this->magnetContent;
    }

    public function setUserID($id){
        return $this->userID = $id;
    }

    public function setMagnetProblemID($id){
        return $this->magnetProblemID = $id;
    }

    public function setMagnetID($id){
        return $this->magnetID = $id;
    }

    public function setMagnetContent($content){
        return $this->magnetContent = $content;
    }

    public static function getCreatedMagnetByProblemAndID($magnetProblemID, $magnetID){
        require_once('Database.php');

        $user = Auth::GetCurrentUser();
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * from MPCreatedMagnet WHERE magnetID = :mID
                             AND userID = :uID AND magnetProblemID = :mpID');
        $sth-> execute(array(':mID' => $magnetID, ':uID' => $user->getId(),
        ':mpID' => $magnetProblemID));

        return $sth->fetchObject('CreatedMagnet');
    }
    
    public static function getCreatedMagnetsByProblem($magnetProblemID){
        require_once('Database.php');

        $user = Auth::GetCurrentUser();
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * from MPCreatedMagnet WHERE userID = :uID
                             AND magnetProblemID = :mpID');
        $sth-> execute(array(':uID' => $user->getId(),
        ':mpID' => $magnetProblemID));
    
        $results = $sth->fetchAll();
       
        // Sorting the magnets by ID's so they are in the right order. 
        for($j=1; $j < count($results);$j++){
            $temp = $results[$j];
            $i=$j;
            while(($i > 0) && ($results[$i-1][3] >= $temp[3])){
                $results[$i] = $results[$i-1];
                $i--;
            }
            $results[$i] = $temp;
        }

        foreach($results as $value){
            // The ".:3:." delimiter will be used by the client to pull out
            // created magnets.
            $values[] = ".:3:.".$value[3].".:|:.".$value[4];  // .:3:.ID.:|:.Content
        }
        if(empty($values)){
            return 0;
        }else{
            return $values;
        }
    
    }

    public static function deleteOldMagnets($magnetProblemID){
        require_once('Database.php');

        $user = Auth::GetCurrentUser();

        $db = Database::getDb();

        $sth = $db->prepare('DELETE from MPCreatedMagnet 
                            WHERE magnetProblemID  = :mpID AND
                            userID = :uID');

        $sth-> execute(array(':mpID' => $magnetProblemID, 
                             ':uID' => $user->getId()));

    }
}
?>

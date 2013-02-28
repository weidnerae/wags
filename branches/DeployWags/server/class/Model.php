<?php

/**
 * Model
 *
 * Basic model class. Holds the id for
 * index in the DB. Also holds added and updated
 * times.
 *
 * @author Robert Bost <bostrt at appstate dot edu>
 */

abstract class Model
{
    protected $id;
    protected $added;
    protected $updated;

    abstract function getTable();

    /**
     * Save this object to database.
     */
    public function save()
    {
        // Don't want guest account to save
        if(Auth::isLoggedIn()) {
            $user = Auth::getCurrentUser(); 
            if($user->isGuest()) return;
        }

        require_once('Database.php');
        $db = Database::getDb();
        $table = $this->getTable();
        $this->setUpdated(time());
        // Check if anything exists by this ID.
        $sth = $db->prepare("SELECT count(*) FROM $table WHERE id = :id");
        $sth->execute(array(':id' => $this->id));
        $result = $sth->fetch(PDO::FETCH_NUM);

                // need a try-catch block for the transaction handling
                try {
                        
                        // Begin transaction 
                        //      -if no erros, will commit entire transaction at bottom 
                        //      -else rollback
                        $db->beginTransaction(); #begin transaction
                        
                        /*
                         * If nothing exists in the database with this ID.
                         * Insert a new row into table.
                         */
                        if($result[0] == 0){
                                $sql = "INSERT INTO $table";
                                $sqlKeys = "( id, ";
                                $sqlVals = " VALUES (\"\" , ";
                                $vars = get_object_vars($this);
                                $classKeys = array_keys($vars);
                                $classVals = array_values($vars);
        
                                // Unset the ID. Database will automatically handle
                                // it since it's the PRIMARY KEY.
                                $index = array_search('id', $classKeys);
                                array_splice($classKeys, $index, 1);
                                array_splice($classVals, $index, 1);
        
                                // Loop over each attribute of the object creating
                                // a key and values in the SQL query string.
                                //
                                // The zeroth index and the rest are separated because
                                // of the comma needed before variables after the first.
                                // Blah blah it works.
                                $key = $classKeys[0];
                                $val = $classVals[0];
        
                                if(!isset($val)){
                                        $val = "NULL";
                                }else if(!is_numeric($val)){
                                        $val = "\"".addslashes($val)."\"";
                                }
                                $sqlKeys .= $key;
                                $sqlVals .= $val;
        
                                for($i = 1; $i < count($classKeys); $i++){
                                        $key = $classKeys[$i];
                                        $val = $classVals[$i];
                                        if(!isset($val)){
                                                $val = "NULL";
                                        }else if(!is_numeric($val)){
                                                // Quote val. No naughty strings allowed!
                                                $val = "\"".addslashes($val)."\"";
                                        }
        
                                        $sqlKeys .= ", ".$key;
                                        $sqlVals .= ", ".$val;
                                }
        
                                $sqlKeys .= ") ";
                                $sqlVals .= ")";
                                
                                $sqlComplete = $sql.$sqlKeys.$sqlVals;

                                // Useful for debugging
                                $file = '/tmp/sql.txt';
                                $file = fopen($file, "w");
                                fputs($file, $sqlComplete);
                                fclose($file);
                            
                                $sth = $db->prepare($sqlComplete);
                                if($sth == FALSE){
                                        throw new PDOException("Database Error1: ".$db->errorCode()." ".$db->errorInfo());
                                }
                                if(!$sth->execute()){
                                        // Throw exception. Something bad happened.
                                        throw new PDOException("Database Error2: $sqlComplete\nCode: ".$sth->errorCode()."\nInfo: ". $sth->errorInfo());
                                }
                                
                        }
                        else{
                                // Something already exists with this id.
                                // Update each of the variables.
                                foreach($this as $key=>$val){
                                        $sth = $db->prepare("UPDATE $table SET $key = :val WHERE id = :id");
                                        $sth->execute(array(':val' => $val, ':id' => $this->id));
                                }
                                
                        }
                        
                        // If nothing has gone wrong, commit the entire transaction to the database
                        $db->commit(); #commit transaction
                        
                        
                }catch (PDOException $e) {
                        // There was an error during the transaction, so the database will be rolled back
                        //  to previous state, and an error thrown
                        echo "There was an error";
                        $db->rollback();  #rollback the database
                        throw new PDOException("Database Error: $sqlComplete\nCode: ".$sth->errorCode()."\nInfo: ". $sth->errorInfo()."\nMessage: ".$e->getMessage());
                }
    }

    public function delete()
    {
                // need a try-catch block for the transaction handling
                try {
                        
                        // Begin transaction 
                        //      -if no erros, will commit entire transaction at bottom 
                        //      -else rollback
                        $db->beginTransaction(); #begin transaction
                        
                        $db = Database::getDb();
                        $table = $this->getTable();
        
                        $sth = $db->prepare("DELETE FROM $table WHERE id = :id");
                        $sth->execute(array(':id' => $this->id));
                        
                        // If nothing has gone wrong, commit the entire transaction to the database
                        $db->commit(); #commit transaction
                        
                }catch (PDOException $e) {
                        // There was an error during the transaction, so the database will be rolled back
                        //  to previous state, and an error thrown
                        $db->rollback();  #rollback the database
                        throw new PDOException("Database Error: $sqlComplete\nCode: ".$sth->errorCode()."\nInfo: ". $sth->errorInfo());
                }
    }

    public function toArray()
    {
        $vars = array();
        
        foreach($this as $key=>$val){
            $vars[$key] = $val;
        }

        return $vars;
    }

    public function getId(){
        return $this->id;
    }

    public function getAdded(){
        return $this->added;
    }
    
    public function getUpdated(){
        return $this->updated;
    }

    public function setUpdated($updated=null){
        $this->updated = time();
    }
    
    public function setAdded($added=null){
        if(is_null($added)){
            $this->added = time();
        }else{
            $this->added = $added;
        }
    }
}

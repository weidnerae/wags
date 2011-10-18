<?php

/**
 * Database
 *
 * This is a simple class for interfacing with the database.
 * It uses PHP's PDO class. Basically, this just keeps a single
 * database connection per request. (Singleton)
 * 
 * @author Robert Bost <bostrt at appstate dot edu>
 */

class Database 
{
    private static $_conn = NULL;

    public static function getDb()
    {
        if(!is_null(self::$_conn)){
            return self::$_conn;
        }else{
	try{	
        	    self::$_conn = new PDO(WE_DB_DSN, WE_DB_USER, WE_DB_PASS);
	    }catch(PDOException $e){
	    		x($e);
	    }
            return self::$_conn;
        }
    }

    public static function close()
    {
        // TODO: DO
    }
}

?>

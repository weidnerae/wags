<?php

/**
* LogicalMicrolab.php
*
* This is simply a utility class that provides some static methods for
* pulling data out of the database.
* 
* As it spans multiple tables, it does not extend model, and cannot be "saved"
*
* Author:  Philip Meznar '12
*/

class LogicalMicrolab 
{
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
            WHERE LMGroup.Group = :group');
		$sth->setFetchMode(PDO::FETCH_NUM);
        $sth->execute(array(':group' => $group));

        $result = $sth->fetchAll();
        foreach($result as $entry){
            $values[] = $entry[0];
        }
		return $values;
    }

}

?>
<?php

/**
 *RegisterStudents
 *
 *Allows the teacher to upload an array of student names, and registers them
 *with a null password to be set when the students login for the first time.
 *The generated usernames are just lastname.firstname
 *
 *@author PHilip Meznar
 */

class RegisterStudents extends Command
{
	public function execute(){

		if(!isset($_POST['first_name']) || !isset($_POST['last_name'])){
			return JSON::error("Error in posting names");
		}

		$first_name = $_POST['first_name'];
		$last_name = $_POST['last_name'];
		$numFirst = 0;
		$numLast = 0;

		for($i = 0; $i < count($first_name); $i++){
			if($first_name[$i] != "") $numFirst++;
			if($last_name[$i] != "") $numLast++;
		}

		if($numFirst != $numLast){
			return JSON::error("Must have same number first and last names");	
		}

		for($i = 0; $i < $numFirst; $i++){
			$user = new User();
			$user->setUsername("$last_name[$i].$first_name[$i]");
			$user->setEmail("unset");
			$user->setFirstName($first_name[$i]);
			$user->setLastName($last_name[$i]);
			$user->setPassword(md5("password"));
			$user->setSection(Auth::getCurrentUser()->getSection());
			$user->setLastLogin(time());
			$user->setAdded(time());
			$user->setUpdated(time());
			$user->setAdmin(0);

			try{
				$user->save();
			}catch(Exception $e){
				return JSON::error($e->getMessage());
			}
		}

		return JSON::success("Users added");
		
	}

}

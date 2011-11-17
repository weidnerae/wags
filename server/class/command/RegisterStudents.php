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

        #make sure the number of first names is the same
        #as the number of last names
        #For CSV, that the length % 2 = 0
		for($i = 0; $i < count($first_name); $i++){
			if($first_name[$i] != "") $numFirst++;
			if($last_name[$i] != "") $numLast++;
		}
		if($numFirst != $numLast){
			return JSON::error("Must have same number first and last names");	
		}

        #before processing any users, check to see if any of them are already
        #registered.  This is done to keep half of the students from being
        #registered and then creating this same error when the rest of the students
        #go to be registered
		for($i = 0; $i < $numFirst; $i++){
			if(User::isusername("$last_name[$i].$first_name[$i]")){
				return JSON::warn("$last_name[$i].$first_name[$i] is already registered");
			}
		}

        #Create new user profiles for every user.
        #The users will not have an e-mail, and their password
        #is currently set to password
        #TODO: Update first login to request e-mail
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

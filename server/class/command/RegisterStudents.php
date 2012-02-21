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
        $txtRegister = $csvRegister = false;

        if(isset($_POST['first_name']) && isset($_POST['last_name']))
            $txtRegister = true;

        if(isset($_FILES['csvReg']))
            $csvRegister = true;

        if(!$txtRegister && !$csvRegister){
            return JSON::warn("No one to register!");
        }

        // Handles adding users from the
        // text box entries
        if($txtRegister){
		    $first_name = $_POST['first_name'];
    		$last_name = $_POST['last_name'];
            $numUsers = 0;
            $err = "";

            if(!$this->validateCounts($first_name, $last_name, $numUsers))
                return JSON::error("Must have same number first and last names");

            if(!$this->allNewUsers($first_name, $last_name, $numUsers, $err))
    			return JSON::warn($err);

            #Create new user profiles for every user.
            #The users will not have an e-mail, and their password
            #is currently set to password
    		for($i = 0; $i < $numUsers; $i++){
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
		}

        // Handles adding users from
        // the csv file
        if($csvRegister){
            $csvReg = $_FILES['csvReg'];

        }

		return JSON::success("Users added");
		
	}



    #make sure the number of first names is the same
    #as the number of last names
    private function validateCounts($first_name, $last_name, &$numUsers){
        $numFirst = $numLast = 0;

		for($i = 0; $i < count($first_name); $i++){
			if($first_name[$i] != "") $numFirst++;
			if($last_name[$i] != "") $numLast++;
		}
		if($numFirst != $numLast){
			return false; 
		}
        
        $numUsers = $numFirst;
        return true;
    }   

    #before processing any users, check to see if any of them are already
    #registered.  This is done to keep half of the students from being
    #registered and then creating this same error when the rest of the students
    #go to be registered
    private function allNewUsers($first_name, $last_name, $number, &$err){
		for($i = 0; $i < $number; $i++){
			if(User::isusername("$last_name[$i].$first_name[$i]")){
                $err = "Failure: $last_name[$i].$first_name[$i] Already Exists";
                return false;
			}
		}
        return true;
    }
}

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
        $firstName = $_POST['first_name']; // array
        $lastName = $_POST['last_name']; // array

        if(!empty($firstName[0]) && !empty($lastName[0]))
            $txtRegister = true;

        if($_FILES['csvReg']['size'] > 0)
            $csvRegister = true;

        // If there is nothing to register
        if(!$txtRegister && !$csvRegister){
            return JSON::warn("No one to register!");
        }

        ######################################################
        #   TEXTBOX REGISTERING                              #
        ######################################################
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

        ######################################################
        #   CSV REGISTERING                                  #
        ######################################################
        if($csvRegister){
            // Declare some variables
            $names = array();
            $nameIndex = 0;

            // Open the file using its temporarly location (we aren't
            // interested in saving it)
            $csvReg = $_FILES['csvReg']['tmp_name'];
            if(($csvFile = fopen($csvReg, "r")) == FALSE){
                return JSON::error("Error opening file");
            }

            // Go through the file, checking and creating list
            // of names
            while(($line = fgetcsv($csvFile, 1000)) != FALSE){

                // If any line has more than 2 entries, report error
                if(count($line) > 3) {
                    return JSON::error("Too many fields!");

                // Add entries to $names, which will be used
                // to create accounts if there are no errors
                } else {
                    $names[$nameIndex][0] = $line[0]; // Lastname
                    $names[$nameIndex][1] = $line[1]; // Firstname
                    $names[$nameIndex][2] = $line[2]; // Email address

                    // Make sure username doesn't exist
                    $username = $line[2];
                    if(User::isUsername($username)){
                        return JSON::error("Account $username is taken");
                    }

                    $nameIndex++;
                }
            }
            $nameIndex--; // Loop increments index one extra time
            fclose($csvFile);

            if(!($this->createAccounts($names, $msg))){
                return JSON::error($msg);
            }
        }
         
		return JSON::success("Users added");
	}

    # createAccounts
    #
    # Creates accounts using the array populated by the csv file
    # Returns 0 and an error message on failure, 1 on success
    private function createAccounts($names, &$msg){
        $index = 0;

        while($index < count($names)){
            $first = $names[$index][1];
            $last = $names[$index][0];
            $username = $names[$index][2];

            // Define the user
            $user = new User();
            $user->setUsername($username);
            $user->setEmail("");
            $user->setFirstName($first);
            $user->setLastName($last);
            $user->setPassword(md5('password'));
            $user->setSection(Auth::getCurrentUser()->getSection());
            $now = time();
            $user->setLastLogin(0);
            $user->setAdded($now);
            $user->setUpdated($now);
            $user->setAdmin(0);
            
            // Try to create the user
            try{
                $user->save();
            }catch(Exception $e){
                $msg = "Failed to create $username: ".$e->getMessage();
                return 0;
            }
            
            $index++;
        }
        
        return 1;
    }

    # validateCounts
    #
    # make sure the number of first names is the same
    # as the number of last names
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

    # allNewUsers
    #
    # before processing any users, check to see if any of them are already
    # registered.  This is done to keep half of the students from being
    # registered and then creating this same error when the rest of the students
    # go to be registered
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

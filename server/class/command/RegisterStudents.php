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
        $csvRegister = false;

        if($_FILES['csvReg']['size'] > 0)
            $csvRegister = true;

        // If there is nothing to register
        if(!$csvRegister){
            return JSON::warn("No one to register!");
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
                // If any line has something other than 3 entries, report error
                if(count($line) != 3) {
                    return JSON::error("CSV file should have: Last, First, Username");
                   
                // Add entries to $names, which will be used
                // to create accounts if there are no errors
                } else {
                    $names[$nameIndex][0] = trim($line[0]); // Lastname
                    $names[$nameIndex][1] = trim($line[1]); // Firstname
                    $names[$nameIndex][2] = trim($line[2]); // Email address

                    // Make sure username doesn't exist
                    $username = $line[2];
                    if(User::isUsername($username)){
                        return JSON::error("Account $username already exists");
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
}

<?php

/**
 * RegisterUser
 *
 * Add a user to the databse as long as the username they
 * want is not taken.
 * 
 * @author Robert Bost <bostrt at appstate dot edu>
 */

class RegisterUser extends Command
{
    public function execute()
    {
        // Check that required REQUEST vars are set.
        if(!(isset($_REQUEST['username']) 
             && isset($_REQUEST['password'])
             && isset($_REQUEST['email']))){
            return JSON::error('Username, password, and email are needed to register.');
        }

        // Check if username is taken.
        if(User::isUsername($_REQUEST['username'])){
            return JSON::error('Username is taken.');
        }

        // Username is unique, create User object and save it. MD5 pass.
        $user = new User();
        $user->setUsername($_REQUEST['username']);
        $user->setEmail($_REQUEST['email']);
        $user->setFirstName($_REQUEST['firstName'] == '' ? NULL : $_REQUEST['firstName']);
        $user->setLastName($_REQUEST['lastName'] == '' ? NULL : $_REQUEST['lastName']);
        $user->setPassword(md5($_REQUEST['password'])); // MD5!
		$user->setSection(Section::getIdByName($_REQUEST['section']));
        $now = time();
        $user->setLastLogin($now);
        $user->setAdded($now);
        $user->setUpdated($now);
        $user->setAdmin(0);
	
        try{
            $user->save();
        }catch(Exception $e){
            x($e);
            return JSON::error($e->getMessage());
        }
        return JSON::success('Registered as '.$_REQUEST['username']);
    }
}

?>

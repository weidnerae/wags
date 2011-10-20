<?php

/**
*ChangePassword.php
*
*Author:Philip Meznar
*
*This class is used for the wags system.  The administrator
*has the ability to change the password for students who have
*forgotten theirs.
*/

class ChangePassword extends Command
{
	public function execute(){
		$userName = $_REQUEST['users'];
		$newPass = md5($_REQUEST['newPassword']);
		$checkPass = md5($_REQUEST['checkPassword']);

		if($newPass == md5("")){
			return JSON::error("Please enter a password");
		}

		if($newPass != $checkPass){
			return JSON::error("Passwords don't match");
		}

		$user = User::getUserByUsername($userName);
		if($user == null)
		{
			return JSON::error("User doesn't exist.  Weird.");
		}

		$user->setPassword($newPass);
		$user->save();

		return JSON::success("$userName has updated their password");
	}
}
?>

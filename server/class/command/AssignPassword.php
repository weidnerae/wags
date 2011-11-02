<?php

/**
 *Class AssignPassword
 *Author: Philip Meznar
 *
 *This class is used when a user had the password "password"
 *and needs to change it.  Typically, this is on the users first
 *log in.  It will happen multiple times, however, if the user
 *assigns "password" as their password again
 */

class AssignPassword extends Command
{
	public function execute()
	{
		$user = Auth::getCurrentUser();

		$password = $_POST['pass'];
		$user->setPassword(md5($password));

		$user->save();
		$name = $user->getUsername();

		return JSON::success("$name's password has been updated");
	}
}

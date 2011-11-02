<?php

/**
 *Class CheckPassword
 *Author: Philip Meznar
 *
 *This class checks for the current users password to see if it
 *is "password".  If so, it sends back an error which in turn asks
 *the client to change the password
 */

class CheckPassword extends Command
{
	public function execute()
	{
		$user = Auth::getCurrentUser();

		if($user->getPassword() == md5("password"))
			return JSON::error("bad password");

		return JSON::success("good password");
	}
}

<?php

class GetAllUsers extends Command
{
	public function execute()
	{
		$users = User::getUserNames();
		return JSON::success($users);
	}
}

?>

<?php

class DeleteUser extends Command
{
    public function execute(){
        $username = $_GET['name'];
        $user = User::getUserByUsername($username);
        if($user == null){
            return JSON::error("User does not exist");
        }
        $userId = $user->getId();

        User::deleteUser($userId);

        return JSON::success("User Deleted ".$userId);
    }
}

?>

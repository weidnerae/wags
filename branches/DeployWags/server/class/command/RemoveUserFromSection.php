<?php

class RemoveUserFromSection extends Command
{
    public function execute(){
        $username = $_GET['name'];
        $user = User::getUserByUsername($username);
        if ($user == null)
        {
            return JSON::error("User Does Not Exist");
        }
        $user->setSection();
        $user->save();

        return JSON::success("User Removed From Section".$username);
    }
}

?>

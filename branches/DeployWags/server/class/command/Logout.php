<?php

class Logout extends Command
{
    public function execute()
    {
        if(!Auth::isLoggedIn()){
            return JSON::warn("You were not logged in.");
        }

        Auth::logout();
        return JSON::success('Logged out.');
    }
}

?>
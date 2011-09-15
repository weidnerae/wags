<?php

/**
 * Login
 * 
 * @author Robert Bost <bostrt at appstate dot edu>
 */

class Login extends Command
{
    public function execute()
    {
        if(Auth::isLoggedIn()){
            return JSON::warn('Already logged in.');
        }

        if(isset($_REQUEST['username']) && isset($_REQUEST['password'])){
            $result = Auth::login($_REQUEST['username'],$_REQUEST['password']);
            if($result){
                return JSON::success('Successfully logged in.');
            }
        }

        return JSON::error('Login failed. Check username and password.');
    }
}
?>
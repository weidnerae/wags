<?php

/**
 *
 */

class GetUserDetails extends Command
{
    public function execute()
    {
        if(Auth::isLoggedIn()){
            JSON::success(Auth::getCurrentUser()->toArray());
        }else{
            JSON::error("Not logged in.");
        }
    }
}
?>
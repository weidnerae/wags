<?php

/**
 *
 */

class GetUserDetails extends Command
{
    public function execute()
    {
		if(Auth::isLoggedIn()){
            if(Auth::getCurrentUser()->isGuest()){
                JSON::warn("Logged as a guest");
                return;
            }
            JSON::success(Auth::getCurrentUser()->toArray());
        } else {
            return JSON::error("Not logged in.");
        }
    }
}

?>

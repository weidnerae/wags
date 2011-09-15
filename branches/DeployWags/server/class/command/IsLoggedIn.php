<?php

/**
 * IsLoggedIn
 *
 * Determine if someone is logged in. Echo JSON message.
 *
 * @author Robert Bost <bostrt at appstate dot edu>
 */

class IsLoggedIn extends Command
{
    public function execute()
    {
        if(Auth::isLoggedIn()){
            // They are logged in.
            JSON::success(true);
        }else{
            JSON::success(false);
        }
    }
}

?>
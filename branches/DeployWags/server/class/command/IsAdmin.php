<?php

class IsAdmin extends Command
{
    public function execute()
    {
        $user = Auth::getCurrentUser();
        $MAGNET_ADMIN = 263;
        
        // Root admin
        if($user->getId() == 1){
            return JSON::success('User is root admin');
        }

        if($user->isAdmin()){
            // Allow "Wags/Admin" account to upload magnet
            // problems more generally than regular admin
            if($user->getId() == $MAGNET_ADMIN){
                return JSON::warn('magnet');
            }
            return JSON::warn('User is regular admin');
        }else{
            return JSON::error('User is not admin');
        }
    }
}

?>

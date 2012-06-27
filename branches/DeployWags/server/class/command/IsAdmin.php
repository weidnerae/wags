<?php

class IsAdmin extends Command
{
    public function execute()
    {
        $user = Auth::getCurrentUser();
        
        // Root admin
        if($user->getId() == 1){
            return JSON::success('User is root admin');
        }

        if($user->isAdmin()){
            return JSON::warn('User is regular admin');
        }else{
            return JSON::error('User is not admin');
        }
    }
}

?>

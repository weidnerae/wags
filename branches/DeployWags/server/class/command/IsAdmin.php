<?php

class IsAdmin extends Command
{
    public function execute()
    {
        $user = Auth::getCurrentUser();
        
        if($user->isAdmin()){
            return JSON::success('User is admin');
        }else{
            return JSON::error('User is not admin');
        }
    }
}

?>
<?php

# SaveMagnetState
#
# Handles creation of MagnetProblemState objects, saves them in database
#
# Daniel Cook

class SaveMagnetState extends Command
{
    public function execute(){
        $user = Auth::getCurrentUser();
        $magnetId = $_GET['magnetId'];
        $state = $_GET['state'];
        $success = $_GET['success'];
        
        $entry = MagnetProblemState::getEntryByProblemId($magnetId);
        
        if($entry){
            if($entry->getSuccess() !=1){
                $entry->setState($state);
                if($success == 1){
                    $entry->setSuccess($success);
                }
            }
        } else{
            $entry = new MagnetProblemState();
            $entry->setUserId($user->getId());
            $entry->setMagnetId($magnetId);
            $entry->setState($state);
            $entry->setSuccess($success);
            $entry->setAdded(time());
            $entry->setUpdated(time());
        }
        if(!$user->isGuest()){
            $entry->save();
        }
        return JSON::success("State Saved");
    }
}
?>

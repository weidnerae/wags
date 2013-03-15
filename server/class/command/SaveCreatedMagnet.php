<?php

# SaveCreatedMagnet
#
# Handles creation of CreatedMagnet objects, saves them in database
#
# Daniel Cook

class SaveCreatedMagnet extends Command
{
    public function execute(){
        $user = Auth::getCurrentUser();
        $magnetContent = $_GET['magnetContent'];
        $magnetID = $_GET['magnetID'];
        $magnetProblemID = $_GET['magnetProblemID'];
        $magSub = MagnetSubmission::getSubmissionByProblem($magnetProblemID);
        $entry = new CreatedMagnet();
        
        if(empty($magSub) || $magSub->getSuccess() !=1){
           $entry->setUserID($user->getId());
           $entry->setMagnetProblemID($magnetProblemID);
           $entry->setMagnetID($magnetID);
           $entry->setMagnetContent($magnetContent);
           $entry->setAdded(time());
           $entry->setUpdated(time());
        
            if(!$user->isGuest()){
                $entry->save();
                return JSON::success("State Saved");
            }else{
                return JSON::warning("State not Saved - Guests cannot save");
            }
        } else{
            return JSON::warning("State not Saved: Error with Entry");
        }
    }
}
?>

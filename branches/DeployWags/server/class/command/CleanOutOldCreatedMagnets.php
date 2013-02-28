<?php

# CleanOutOldCreatedMagnets.php
#
# Deletes the old saved magnets for a magnet problem so that the
# database doesn't get all clutterd up every time someone saves.
#
# Daniel cook

class CleanOutOldCreatedMagnets extends Command
{
    public function execute(){
        $problemCorrect = 0;
        $magnetProblemID = $_GET['magnetProblemID'];
        
        $entry = MagnetSubmission::getSubmissionByProblem($magnetProblemID);
        if($entry){
            if($entry->getsuccess() == 1)
                $problemCorrect = 1;
        }
        if($problemCorrect == 0){
            CreatedMagnet::deleteOldMagnets($magnetProblemID);
        }
        return JSON::success("");
    }
}

?>

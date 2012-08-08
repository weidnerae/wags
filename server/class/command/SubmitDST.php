<?php

# SubmitDST
#
# Handles creation of DSTSubmission objects, saves them in database
#
# Philip Meznar

class SubmitDST extends Command
{
    public function execute(){
        $user = Auth::getCurrentUser();
        $title = $_GET['title'];
        $success = $_GET['success'];

        $sub = DSTSubmission::getSubmissionByTitle($title);

        if($sub){
            // Only increment number of attempts if they haven't
            // gotten it correct yet
            if($sub->getSuccess() == 0)
                $sub->setNumAttempts($sub->getNumAttempts() + 1);
            
            $sub->setSuccess($success);
        } else {
            $sub = new DSTSubmission();
            $sub->setUserId($user->getId());
            $sub->setSectionId($user->getSection());
            $sub->setTitle($title);
            $sub->setNumAttempts(1);
            $sub->setSuccess($success);
            $sub->setAdded(time());
            $sub->setUpdated(time());
        }

        $sub->save();
        return JSON::success("return");
    }
}

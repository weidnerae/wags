<?php

# SubmitDatabaseMicrolab
#
# Handles creation of DatabaseSubmission objects, saves them in database
#
# Chris Hegre

class SubmitDatabaseMicrolab extends Command
{
    public function execute(){
        $user = Auth::getCurrentUser();
        $id = $_GET['id'];
        $success = $_GET['success'];

        $sub = DatabaseSubmission::getSubmissionByProblemId($id);

        if($sub){
            // Only increment number of attempts if they haven't
            // gotten it correct yet
            if($sub->getSuccess() == 0)
                $sub->setNumAttempts($sub->getNumAttempts() + 1);
            
            $sub->setSuccess($success);
        } else {
            $sub = new DatabaseSubmission();
            $sub->setUserId($user->getId());
            $sub->setSectionId($user->getSection());
            $sub->setDatabaseProblemId($id);
            $sub->setNumAttempts(1);
            $sub->setSuccess($success);
            $sub->setAdded(time());
            $sub->setUpdated(time());
        }

        $sub->save();
        return "winner";
    }
}

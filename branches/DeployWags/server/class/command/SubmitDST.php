<?php

class SubmitDST extends Command
{
    public function execute(){
        $user = Auth::getCurrentUser();
        $title = $_GET['title'];
        $success = $_GET['success'];

        $sub = DSTSubmission::getSubmissionByTitle($title);

        if($sub){
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

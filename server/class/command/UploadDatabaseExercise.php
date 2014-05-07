<?php

class UploadDatabaseExercise extends Command{
    // Will have to add a fair amount of flexibility to this guy to get
    // all sorts of different logical microlabs uploading...
    // Really, just how to get it to store as null when a $_GET is 'empty'
    public function execute(){
		
        $checkLm = DatabaseProblem::getDatabaseProblemByTitle($_GET['title']);
        if(!empty($checkLm)){
            return JSON::warn("Microlab already exists");
        }
		
		
        // Find the correct group for this exercise
        // Depends on Administrator...
        $user = Auth::getCurrentUser();
        $group = null;


        $lm = new DatabaseProblem();
        $lm->setTitle($_GET['title']);
        $lm->setDirections($_GET['directions']);
        $lm->setCorrectQuery($_GET['correct_query']);
        $lm->setBogusWords($_GET['bogus_words']);
        $lm->setGroupName($_GET['group_name']);
        $lm->setAdded(time());

        $lm->save();
        $title = $_GET['title'];
		
        return JSON::success("Saved $title");
    }
}
?>

<?php

class DeleteExercise extends Command
{
    public function execute(){
        $exId = $_GET['exId'];

        Exercise::deleteExercise($exId);

        return JSON::success("Exercise Deleted ".$exId);
    }
}

?>

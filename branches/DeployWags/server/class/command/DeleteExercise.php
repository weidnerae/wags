<?php

class DeleteExercise extends Command
{
    public function execute(){
        $exTitle = $_GET['title'];
        $exercise = Exercise::getExerciseByTitle($exTitle);
        $exId = $exercise->getId();

        Exercise::deleteExercise($exId);

        return JSON::success("Exercise Deleted ".$exId);
    }
}

?>

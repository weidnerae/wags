<?php

class AddSkeletons extends Command
{
    public function execute(){
        $exName = $_GET['name'];
        $exercise = Exercise::getExerciseByTitle($exName);

        $exercise->addSkeletons();

        return JSON::success(""); // let client know all went well
    }
}

<?php

# SetMagnetExercises.php
#
# Handles changing what magnets are assigned
#
# Philip Meznar '12

class SetMagnetExercises extends Command
{
    public function execute()
    {
        $names = explode(",", $_GET['list']);
        // Set all exercises to "available" but not "implemented"
        MagnetProblem::unAssignAll();

        foreach($names as $name){
            $magnet = MagnetProblem::getMagnetProblemByTitle($name);

            // The table SectionMP doesn't hold magnetProblem names, only ids
            MagnetProblem::setProblemStatus($magnet->getId(), 1);
            // 1 = assigned
        }
        return JSON::success("Magnet exercises updated");
    }
}


?>

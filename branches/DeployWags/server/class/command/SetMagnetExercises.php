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

        // Message for removing all magnet exercises
        if($names[0] == "none") return JSON::success("All Magnet Microlabs Removed!");

        foreach($names as $name){
            $magnet = MagnetProblem::getMagnetProblemByTitle($name);

            // The table SectionMP doesn't hold magnetProblem names, only ids
            MagnetProblem::setProblemStatus($magnet->getId(), 1);
            // 1 = assigned
        }
        return JSON::success("Magnet Exercises Updated");
    }
}


?>

<?php

/**
*SetDatabaseExercises.php
*
*Author: Chris Hegre
*
*This class updates the databaseExercises column
*of an entry in the section table.
*
*
*This is so gross.  There's gotta be a better way..
*/

class SetDatabaseExercises extends Command
{
    public function execute()
    {
        $names = explode("|", $_GET['list']);

        // Set all exercises to "available" but not "implemented"
        DatabaseProblem::unAssignAll();

        // Message for removing all magnet exercises
        if($names[0] == "none") return JSON::success("All Database Microlabs Removed!");

        foreach($names as $name){
            $dbProb = DatabaseProblem::getDatabaseProblemByTitle($name);

            // The table SectionMP doesn't hold magnetProblem names, only ids
            DatabaseProblem::setProblemStatus($dbProb->getId(), 1);
            // 1 = assigned
        }
        return JSON::success("Database Exercises Updated");
    }
}
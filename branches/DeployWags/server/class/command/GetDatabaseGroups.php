<?php

# GetMagnetExercises.php
#
# Returns a list of all magnet problem groups for the users section
#
# Chris Hegre '14

class GetDatabaseGroups extends Command
{
    public function execute()
    {
        $names = DatabaseProblem::getDatabaseProblemGroups();
        return JSON::success($names);
    }
}


?>

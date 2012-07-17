<?php

# GetMagnetExercises.php
#
# Returns a list of all magnet problem groups for the users section
#
# Philip Meznar '12

class GetMagnetGroups extends Command
{
    public function execute()
    {
        $names = MagnetProblem::getMagnetProblemGroups();
        return JSON::success($names);
    }
}


?>

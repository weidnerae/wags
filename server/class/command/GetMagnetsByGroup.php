<?php

# GetMagnetsByGroup.php
#
# Returns a list of all magnet problem available for the section
# for a specific group - used to populate the vertical panel for 
# refrigerator magnets in the Exercise tab
#
# Philip Meznar '12

class GetMagnetsByGroup extends Command
{
    public function execute()
    {
        $groupName = $_GET['group'];
        $groupId = MagnetProblem::getGroupIdByName($groupName);
        $names = MagnetProblem::getMagnetProblemsByGroup($groupId);
        return JSON::success($names);
    }
}


?>

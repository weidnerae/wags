<?php

# GetMagnetProblem.php
# 
# A replacement for the "query.php" for the original magnets
#
# Philip Meznar, '12

class DeleteMagnetExercise extends Command
{
	public function execute(){
        $title = $_GET['title'];
        $magProb = MagnetProblem::getMagnetProblemByTitle($title); 
        $magnetGroupName = MagnetProblem::getGroupNameById($magProb->getGroup());
        $userGroup = Auth::getCurrentUser()->getMagnetProblemGroup();

        if($userGroup == $magnetGroupName){
           MagnetProblem::deleteMagnetExerciseByTitle($title);
           return JSON::success("Magnet Exercise $title Deleted"); 
        } else {
            return JSON::error('Can only delete problems you created');
        }
            
	}
}
?>

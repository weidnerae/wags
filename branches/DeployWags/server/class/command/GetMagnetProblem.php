<?php

# GetMagnetProblem.php
# 
# A replacement for the "query.php" for the original magnets
#
# Philip Meznar, '12

class getMagnetProblem extends Command
{
	public function execute(){
        if(!empty($_GET['id'])){
            $magProb = MagnetProblem::getMagnetProblemById($_GET['id']);
        } elseif(!empty($_GET['title'])){
            $magProb = MagnetProblem::getMagnetProblemByTitle($_GET['title']); 
        } else {
            return JSON::error("No magnet problem found");
        }

        $objArray = $magProb->toArray();
        $id = $magProb->getId(); // necessary for getting state
        
        if(!Auth::getCurrentUser()->isAdmin()){
            $state = MagnetProblemState::getEntryByProblemId($id);
        } else {
            $state = false;
        }
        
        if($state){
            $objArray['state'] = $state->getState();
        } else {
            $objArray[] = "";
        }
        return JSON::success($objArray);

	}
}
?>

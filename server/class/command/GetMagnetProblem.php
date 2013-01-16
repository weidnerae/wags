<?php

# GetMagnetProblem.php
# 
# A replacement for the "query.php" for the original magnets
#
# Philip Meznar, '12

class getMagnetProblem extends Command
{
	public function execute(){
        $id = $_GET['id'];
        
        $magProb = MagnetProblem::getMagnetProblemById($id);
        $objArray = $magProb->toArray();
        
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

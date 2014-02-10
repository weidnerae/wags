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
        $id = $magProb->getId(); // necessaryi for getting state
        
        // This disables state for Admins - We may want to change this.
        // The reason was we didn't want the answer accidentaly being displayed
        // Hopefully the instructor is able to quickly solve the problem.
        if(!Auth::getCurrentUser()->isAdmin()){
            // Retrieves state from DB for this user and problem if one exists
            $state = MagnetProblemState::getEntryByProblemId($id);
        } else {
            $state = false;
        }
        
        // If there is a saved state for this user then we fetch any created
        // magnets that got saved for this user and problem and add them
        // onto the end of the list of statements.
        // The created magnets will already have the right delimiter to 
        // identify them as being created magnets by the client.
        if($state){
            $objArray['state'] = $state->getState();
            $createdMagnets = CreatedMagnet::getCreatedMagnetsByProblem($id);
                if($createdMagnets != 0){
                $createdMagnets = implode($createdMagnets);
                $objArray['statements'] .= $createdMagnets;
            }
        } else {
            $objArray[] = "";
        }
        return JSON::success($objArray);

	}
}
?>

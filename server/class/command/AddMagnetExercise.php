<?php

class AddMagnetExercise extends Command
{
	public function execute(){
        $title = $_POST['title'];
        $desc = $_POST['desc'];
        $className = $_POST['class'];
        $functions = $_POST['functions'];
        $statements = $_POST['statements'];

        
        // Make sure there is no magnet exercises that already
        // has this title....
        /*  This is bad.  I should have implemented everything
            by id's, but currently SetMagnetExercises is run by
            name.  Meaning, the client may not even know the
            appropriate ids to make the switch...  I suppose
            first priority is just getting problem creation
            to exist, then we'll work on moving everything over
            to ids... */
        $checkEx = MagnetProblem::getMagnetProblemByTitle($title);
        if(!empty($checkEx)){
            return JSON::error("Problem with that title already exists");
        }

        // Check for a MagnetProblemGroup for this section
        // -  if it doesn't exist, create it
        $sectionId = Auth::getCurrentUser()->getSection();
        $sectionTitle = Section::getSectionById($sectionId)->getName();
        $mpGroup = $sectionTitle."MPs";

        $groupNames = MagnetProblem::getMagnetProblemGroups();
        if(!in_array($mpGroup, $groupNames)){
            MagnetProblem::createGroup($mpGroup);
        }

        // Get date in mySQL format
        $mysqlDate = date('Y-m-d H:i:s', time());


        // Create the magnet problem
        $newMP = new MagnetProblem();
        $newMP->setTimestamp($mysqlDate); // when working, remove
        $newMP->setTitle($title);
        $newMP->setDirections($desc);
        $newMP->setProblemType("text"); // Placeholder
        $newMP->setInnerFunctions($functions);
        $newMP->setForLeft("text");     // These lines are for
        $newMP->setForMid("text");      // creationStation
        $newMP->setForRight("text");    // problems, currently
        $newMP->setBooleans("text");    // unused
        $newMP->setStatements($statements);
        $newMP->setSolution($className); // Still badly named...
        $newMP->setGroup(1); // A temporary value, replaced in AddMagnetLinkage.php
        $newMP->setAdded(time());
        $newMP->setUpdated(time());

        $file = '/tmp/check.txt';
        $file = fopen($file, "w");

        $objArray = $newMP->toArray();
        $thisLine = print_r($objArray, true);
        fputs($file, $thisLine);

        fclose($file);
        // Attempt to save the new problem
        try{
            $newMP->save();
        } catch(Exception $e){
            logError($e);
            return JSON::error($e->getMessage());
        }

        // Return to client - client will perform a callback,
        // as it can now grab the ids of the new magnetproblem
        // and potentially the new magnetProblemGroup

        // All this is done in AddMagnetLinkage.php
        // $title is used to find the correct problem
        return JSON::success($title);
	}
}

?>

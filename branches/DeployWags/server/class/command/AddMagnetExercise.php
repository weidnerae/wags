<?php

class AddMagnetExercise extends Command
{
	public function execute(){
        $title = $_POST['title'];
        $desc = $_POST['desc'];
        $className = $_POST['class'];
        $functions = $_POST['functions'];
        $statements = $_POST['statements'];
        $overwrite = false;
        $mpGroup = Auth::getCurrentUser()->getMagnetProblemGroup();

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
            // If they already decided to overwrite
            if(!empty($_POST['overwrite'])){
                if($_POST['overwrite'] == 1){
                    $overwrite = true;
                }
            } else {
                // See if the problem is in this administrators group
                $magnetGroupId = $checkEx->getGroup();
                $magnetGroupName = MagnetProblem::getGroupNameById($magnetGroupId);
                if($magnetGroupName == $mpGroup){
                    return JSON::warn("Overwrite problem?");
                }

                return JSON::error("Problem with that title already exists");
            }
        }

        // If the magnet group for this section doesn't exist, 
        // then create it
        $groupNames = MagnetProblem::getMagnetProblemGroups();
        if(!in_array($mpGroup, $groupNames)){
            MagnetProblem::createGroup($mpGroup);
        }

        // Get date in mySQL format
        $mysqlDate = date('Y-m-d H:i:s', time());


        // Create the magnet problem
        if($overwrite) { 
            $newMP = MagnetProblem::getMagnetProblemByTitle($title);
        } else {
            $newMP = new MagnetProblem();
        }
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
        if(!$overwrite) $newMP->setGroup(1); // A temporary value, replaced in AddMagnetLinkage.php
        $newMP->setAdded(time());
        $newMP->setUpdated(time());

        /* Useful for debugging, but unneeded
        $file = '/tmp/check.txt';
        $file = fopen($file, "w");

        $objArray = $newMP->toArray();
        $thisLine = print_r($objArray, true);
        fputs($file, $thisLine);

        fclose($file);*/

        // Attempt to save the new problem
        try{
            $newMP->save();
        } catch(Exception $e){
            logError($e);
            return JSON::error("MP: ".$e->getMessage());
        }

        // Now, save uploaded files into database, linkage
        // will map them correctly
        if($_FILES['testClass']['size'] != 0){
            // If we have files AND we are overwriting the exercise
            // then we want to delete the old files
            if($overwrite){
                $files = SimpleFile::deleteFilesForMP($newMP->getId());
            }

            $result = $this->addSimpleFile($_FILES['testClass'], 1);
            if($result != 1){
                return JSON::error("TC: ".$result);
            }
        }

        if($_FILES['helperClass']['size'] != 0){
            $result = $this->addSimpleFile($_FILES['helperClass'], 0);
            if($result != 1){
                return JSON::error("HC: ".$result);
            }
        }

        // Return to client - client will perform a callback,
        // as it can now grab the ids of the new magnetproblem
        // and potentially the new magnetProblemGroup

        // All this is done in AddMagnetLinkage.php
        // $title is used to find the correct problem
        return JSON::success($title);
	}

    function addSimpleFile($file, $testValue){
        $fileName = pathinfo($file["name"], PATHINFO_FILENAME);
        $fileExtension = pathinfo($file["name"], PATHINFO_EXTENSION);

        // Get file contents
        $finfo = finfo_open(FILEINFO_MIME_TYPE);
        $type = finfo_file($finfo, $file['tmp_name']);
        if(strpos($type, 'text') === FALSE){
            return "Please only upload plain text or source files";
        }
        $fileContents = file_get_contents($file['tmp_name']);

        $newSF = new SimpleFile();
        $newSF->setClassName($fileName);
        $newSF->setPackage("");
        $newSF->setContents($fileContents);
        $newSF->setMagnetProblemId(0); // 'holding' for linkage
        $newSF->setTest($testValue);
        $newSF->setAdded();
        $newSF->setUpdated();

        try{
            $newSF->save();
        } catch(Exception $e){
            logError($e);
            return $e->getMessage();
        }

        return 1;
    }
}

?>

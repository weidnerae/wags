<?php

class AddMagnetExercise extends Command
{
	public function execute(){
        $title = $_POST['title'];
        $desc = $_POST['desc'];
        $type = $_POST['type'];
        $className = $_POST['class'];
        $functions = $_POST['functions'];
        $statements = $_POST['statements'];
        $forLeft = $_POST['forloop1'];
        $forMid = $_POST['forloop2'];
        $forRight = $_POST['forloop3'];
        $ifOptions = $_POST['ifs'];
        $whileOptions = $_POST['whiles'];
        $returnOptions = $_POST['returns'];
        $assignmentVars = $_POST['assignmentVars'];
        $assignmentVals = $_POST['assignmentVals'];
        $limits = [$_POST['forallowed'], 
                  $_POST['whileallowed'], 
                  $_POST['ifallowed'], 
                  $_POST['elseifallowed'], 
                  $_POST['elseallowed'],
                  $_POST['returnallowed'],
                  $_POST['assignmentallowed']];
        $lastProblemLoaded = $_POST['lastProblemLoaded'];
                  
        foreach($limits as $key => $value){
            if($value == ''){
                $limits[$key] = 0;
            }
        }
        $limits = implode(",", $limits);
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

        if (!empty($checkEx)) {
            // If they already decided to overwrite
            if (!empty($_POST['overwrite'])) {
                if ($_POST['overwrite'] == 1) {
                    $overwrite = true;
                }
            } else {
                // See if the problem is in this administrators group
                $magnetGroupId = $checkEx->getGroup();
                $magnetGroupName = MagnetProblem::getGroupNameById($magnetGroupId);
                if ($magnetGroupName == $mpGroup) {
                    return JSON::warn("Overwrite problem?");
                }

                return JSON::error("Problem with that title already exists");
            }
        }

        // If the magnet group for this section doesn't exist, 
        // then create it
        $groupNames = MagnetProblem::getMagnetProblemGroups(null);
        if (!in_array($mpGroup, $groupNames)) {
            MagnetProblem::createGroup($mpGroup);
        }

        // Get date in mySQL format
        $mysqlDate = date('Y-m-d H:i:s', time());


        // Create the magnet problem
        if ($overwrite) { 
            $newMP = MagnetProblem::getMagnetProblemByTitle($title);
        } else {
            $newMP = new MagnetProblem();
        }
        $newMP->setTimestamp($mysqlDate); // when working, remove
        $newMP->setTitle($title);
        $newMP->setDirections($desc);
        $newMP->setProblemType($type); // Placeholder
        $newMP->setInnerFunctions($functions);
        $newMP->setForLeft($forLeft);      // These lines are for
        $newMP->setForMid($forMid);        // creationStation
        $newMP->setForRight($forRight);
        $newMP->setIfOptions($ifOptions);    // problems, currently
        $newMP->setWhileOptions($whileOptions);    // unused
        $newMP->setReturnOptions($returnOptions);
        $newMP->setAssignmentVars($assignmentVars);
        $newMP->setAssignmentVals($assignmentVals);
        $newMP->setStatements($statements);
        $newMP->setLimits($limits);
        $newMP->setSolution($className);   // Still badly named...
        if (!$overwrite) $newMP->setGroup(1); // A temporary value, replaced in AddMagnetLinkage.php
        $newMP->setAdded(time());
        $newMP->setUpdated(time());
        
        $file = '/tmp/check.txt';
        $file = fopen($file, "w");

        $objArray = $newMP->toArray();
        $thisLine = print_r($objArray, true);
        fputs($file, $thisLine." $mpGroup");

        fclose($file);

        // Attempt to save the new problem
        try {
            $newMP->save();
        } catch(Exception $e) {
            logError($e);
            return JSON::error("MP: ".$e->getMessage());
        }

        // Now, save uploaded files into database, linkage
        // will map them correctly

        $hasFiles = false;
        if ($_FILES['testClass']['size'] != 0) {
            // If we have files AND we are overwriting the exercise
            // then we want to delete the old files
            if ($overwrite) {
                $files = SimpleFile::deleteFilesForMP($newMP->getId());
            }

            $result = $this->addSimpleFile($_FILES['testClass'], 1);
            if ($result != 1) {
                return JSON::error("TC: ".$result);
            }
            
            $hasFiles = true;
        }

        $helperId = 1;
        $helperName = 'helperClass1';
        while ($helperId < count($_FILES) && $_FILES[$helperName]['size'] != 0) {
            $result = $this->addSimpleFile($_FILES[$helperName], 0);
            if ($result != 1) {
                return JSON::error("HC: ".$result);
            }
            $helperId++;
            $helperName = "helperClass$helperId";

            $hasFiles = true;
        }

        // If no files were uploaded with this problem then this will check
        // to see if there are already files associated with this problem
        // and if there aren't any then  we assign the files associated
        // with the last exercise that was loaded.

        if(!$hasFiles){
          SimpleFile::assignFiles($title, $lastProblemLoaded);
        }

        // Return to client - client will perform a callback,
        // as it can now grab the ids of the new magnetproblem
        // and potentially the new magnetProblemGroup

        // All this is done in AddMagnetLinkage.php
        // $title is used to find the correct problem
        return JSON::success($title);
	}

    function addSimpleFile($file, $testValue) {
        $fileName = pathinfo($file["name"], PATHINFO_FILENAME);
        $fileExtension = pathinfo($file["name"], PATHINFO_EXTENSION);

        // Get file contents
        $finfo = finfo_open(FILEINFO_MIME_TYPE);
        $type = finfo_file($finfo, $file['tmp_name']);
        if (strpos($type, 'text') === FALSE) {
            return "Please only upload plain text or source files";
        }
        $fileContents = file_get_contents($file['tmp_name']);

        $newSF = new SimpleFile();
        if($fileExtentsion === "pl"){
            $newSF->setClassName("$fileName.$fileExtension");
        }else{
            $newSF->setClassName($fileName);
        }
        $newSF->setPackage("");
        $newSF->setContents($fileContents);
        $newSF->setMagnetProblemId(0); // 'holding' for linkage
        $newSF->setTest($testValue);
        $newSF->setAdded();
        $newSF->setUpdated();

        try {
            $newSF->save();
        } catch(Exception $e) {
            logError($e);
            return $e->getMessage();
        }

        return 1;
    }
}

?>

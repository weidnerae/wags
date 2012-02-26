<?php

/** TEST EDITING
*AddExercise
*
*Heavily based off of UploadFile.php
*Should receive two files as well as a 
*text description from the url
*Hopefully will add contents to database
*/

class AddExercise extends Command
{
    public function execute()
    {
		try {
			
        if(!Auth::isLoggedIn()){
            return JSON::error('Must be logged in as administrator
                to log an exercise');   
		}
		
		$user = Auth::getCurrentUser();

		#Grab all exercise information
		$name = $_POST['fileName'];
		$openDate = $_POST['openDate'];
		$closeDate = $_POST['closeDate'];
       	$solutionFile = $_FILES['Solution'];
      	$skeletonFile = $_FILES['Skeleton'];
		$testFile = $_FILES['TestClass'];
		//$description = $_POST['desc'];
		
		// Get original file names for uploaded solution, test, and student files
		$solutionFileName = pathinfo($solutionFile["name"], PATHINFO_FILENAME);
		$testFileName = pathinfo($testFile["name"], PATHINFO_FILENAME);
		$skeletonFileName = pathinfo($skeletonFile["name"], PATHINFO_FILENAME);
		
		// Get original file extensions for solution, test, and student files
		$solutionFileExtension = pathinfo($solutionFile["name"], PATHINFO_EXTENSION);
		$testFileExtension = pathinfo($testFile["name"], PATHINFO_EXTENSION);
		$skeletonFileExtension = pathinfo($skeletonFile["name"], PATHINFO_EXTENSION);

		#If there are open and close dates, check that they are
		#parsable
        if($closeDate != ""){
            $closeDate = strtotime($closeDate);
            if($openDate == ""){
                $openDate = time();
            }else{
                $openDate = strtotime($openDate);
            }

            if(!$closeDate || !$openDate){
                return JSON::error("Dates are not in the correct format");
            }
        }

        #If the exercise is being altered, not added
		$e = Exercise::getExerciseByTitle($name);
		if($e){
			$update = true;

			#Check each entry to see if it was changed, it so, save the change
            #checking the open date - if it has changed, grab both the new
            #open and closed date
			if($openDate != ""){
				$e->setOpenDate($openDate);
				$e->setCloseDate($closeDate);
			}else{
                $e->setOpenDate("");
                $e->setCloseDate("");
            }

            #If there is a new solution class, replace the old contents
       		$finfo = finfo_open(FILEINFO_MIME_TYPE);
  			$type = finfo_file($finfo, $solutionFile['tmp_name']);
      		if(strpos($type, 'text') !== FALSE){
				$solutionFileContents = file_get_contents($solutionFile['tmp_name']);

				$file = CodeFile::getCodeFileById($e->getSolutionId());
				$file->setContents($solutionFileContents);
				
				// update name and file extensions
				$file->setOriginalFileName($solutionFileName);
				$file->setOriginalFileExtension($solutionFileExtension);

				$file->save();
			}

            #If there is a new skeleton class, replace the old contents
       		$finfo = finfo_open(FILEINFO_MIME_TYPE);
  			$type = finfo_file($finfo, $skeletonFile['tmp_name']);
      		if(strpos($type, 'text') !== FALSE){
				$skeletonFileContents = file_get_contents($skeletonFile['tmp_name']);

				$file = CodeFile::getCodeFileById($e->getSkeletonId());
				$file->setContents($skeletonFileContents);
				
				// update name and file extensions
				$file->setOriginalFileName($skeletonFileName);
				$file->setOriginalFileExtension($skeletonFileExtension);
				
				$file->save();
   	    	}
        
            #If there is a new test class, replace the old contents
			$finfo = finfo_open(FILEINFO_MIME_TYPE);
  			$type = finfo_file($finfo, $testFile['tmp_name']);
      		if(strpos($type, 'text') !== FALSE){
   	    		$testFileContents = file_get_contents($testFile['tmp_name']);

				$file = CodeFile::getCodeFileById($e->getTestClassId());
				$file->setContents($testFileContents);
				
				// update name and file extensions
				$file->setOriginalFileName($testFileName);
				$file->setOriginalFileExtension($testFileExtension);
				
				$file->save();
			}
			
        #A new exercise altogether
		}else{
       		//check all files for plain text
       		$finfo = finfo_open(FILEINFO_MIME_TYPE);
  			$type = finfo_file($finfo, $solutionFile['tmp_name']);
      		if(strpos($type, 'text') === FALSE){
	       	    return JSON::error('Please only upload plain text or source files (sol)');
   	    	}
        
  	     	$type = finfo_file($finfo, $skeletonFile['tmp_name']);
   	    	if(strpos($type, 'text') === FALSE){
   	    	    return JSON::error('Please only upload plain text or source files (skeleton)');
   	    	}	

    	   	$type = finfo_file($finfo, $testFile['tmp_name']);
       		if(strpos($type, 'text') === FALSE){
       	    return JSON::error('Please only upload plain text or source files (skeleton)');
      	 	}	

            #TODO: Remove this terrible files need exercise needs files loop of death
            #Create the new solution class file
      	 	$solutionFileContents = file_get_contents($solutionFile['tmp_name']);
				$sol = new CodeFile();
				$sol->setContents($solutionFileContents);
				$sol->setName("/".$name."/Solution");
				$sol->setUpdated(time());
				$sol->setAdded(time());
				$sol->setOwnerId($user->getId());
				$sol->setSection($user->getSection());
				$sol->setOriginalFileName($solutionFileName);
				$sol->setOriginalFileExtension($solutionFileExtension);
				$sol->setExerciseId(1);
		     	$sol->save();
				$sol = CodeFile::getCodeFileByName("/".$name."/Solution");

			#Create the new skeleton class file
            $skeletonFileContents = file_get_contents($skeletonFile['tmp_name']);
                $skeletonFileContents = str_replace("<", "&lt;", $skeletonFileContents);
                $skeletonFileContents = str_replace(">", "&gt;", $skeletonFileContents);
				$skel = new CodeFile();
				$skel->setContents($skeletonFileContents);
				$skel->setName("/".$name."/AdminSkeleton");
				$skel->setUpdated(time());
				$skel->setAdded(time());
				$skel->setOwnerId($user->getId());
				$skel->setSection($user->getSection());
				$skel->setOriginalFileName($skeletonFileName);
				$skel->setOriginalFileExtension($skeletonFileExtension);
				$skel->setExerciseId(1);
				$skel->save();
				$skel = CodeFile::getCodeFileByName("/".$name."/AdminSkeleton");
				
            #Create the new test class file
			$testFileContents = file_get_contents($testFile['tmp_name']);
				$test = new CodeFile();
				$test->setContents($testFileContents);
				$test->setName("/".$name."/TestClass");
				$test->setUpdated(time());
				$test->setAdded(time());
				$test->setOwnerId($user->getId());
				$test->setSection($user->getSection());
				$test->setOriginalFileName($testFileName);
				$test->setOriginalFileExtension($testFileExtension);
				$test->setExerciseId(1);
				$test->save();
				$test = CodeFile::getCodeFileByName("/".$name."/TestClass");

            #Put together the exercise
			$e = new Exercise;
    	    $e->setTitle($name);
       		$e->setAdded(time());
			$e->setOpenDate($openDate);
			$e->setCloseDate($closeDate);
			$e->setSection($user->getSection());
			$e->setSolutionId($sol->getId());
			$e->setTestClassId($test->getId());
			$e->setSkeletonId($skel->getId());
		    $e->setMultiUser(0); //The default is no partners
			$e->setDescription(""); //This should die when descriptions become pdfs
		}
		
		}catch(Exception $e){
	    	logError($e);
			return JSON::error($e->getMessage());
        }

		#The following is ALWAYS updated, so they
		#aren't within the if/else block
		if($openDate <= time()) $visible = 1;
		else $visible = 0;
		$e->setVisible($visible);

		$now = time();
		$e->setUpdated($now);	
        try{
		    $e->save();
            if(isset($update) && $update){
                JSON::warn('Overwrote exercise '.$e->getTitle());
			}
            else{
                JSON::success('Uploaded exercise '.$e->getTitle());
			}

        }catch(Exception $e){
			logError($e);
            return JSON::error($e->getMessage());
//	    logError($f);
//	    JSON::error($f);
        }

		finfo_close($finfo);
    }

}


?>

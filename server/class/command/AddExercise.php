<?php

/**
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
        if(!Auth::isLoggedIn()){
            return JSON::error('Must be logged in as administrator
                to log an exercise');   
		}
		
		$user = Auth::getCurrentUser();

		#Grab all exercise information
		$name = $_POST['fileName'];
		$openDate = $_POST['openDate'];
		$closeDate = $_POST['closeDate'];
       	$solution = $_FILES['Solution'];
      	$skeleton = $_FILES['Skeleton'];
		$testClass = $_FILES['TestClass'];
		$description = $_POST['desc'];

		#If there are open and close dates, check that they are
		#parsable
        #TODO: Make it so exercises can only have a "closedate"
        #i.e., the default is exercises open when added, but can still
        #have a specific expiration date
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
  			$type = finfo_file($finfo, $solution['tmp_name']);
      		if(strpos($type, 'text') !== FALSE){
				$solutionContents = file_get_contents($solution['tmp_name']);

				$file = CodeFile::getCodeFileById($e->getSolutionId());
				$file->setContents($solutionContents);

				$file->save();
			}

            #If there is a new skeleton class, replace the old contents
       		$finfo = finfo_open(FILEINFO_MIME_TYPE);
  			$type = finfo_file($finfo, $skeleton['tmp_name']);
      		if(strpos($type, 'text') !== FALSE){
				$skeletonContents = file_get_contents($skeleton['tmp_name']);

				$file = CodeFile::getCodeFileById($e->getSkeletonId());
				$file->setContents($skeletonContents);

				$file->save();
   	    	}
        
            #If there is a new test class, replace the old contents
			$finfo = finfo_open(FILEINFO_MIME_TYPE);
  			$type = finfo_file($finfo, $testClass['tmp_name']);
      		if(strpos($type, 'text') !== FALSE){
   	    		$testClassContents = file_get_contents($testClass['tmp_name']);

				$file = CodeFile::getCodeFileById($e->getTestClassId());
				$file->setContents($testClassContents);
				
				$file->save();
			}
		}else{
       		//check all files for plain text
       		$finfo = finfo_open(FILEINFO_MIME_TYPE);
  			$type = finfo_file($finfo, $solution['tmp_name']);
      		if(strpos($type, 'text') === FALSE){
	       	    return JSON::error('Please only upload plain text or source files (sol)');
   	    	}
        
  	     	$type = finfo_file($finfo, $skeleton['tmp_name']);
   	    	if(strpos($type, 'text') === FALSE){
   	    	    return JSON::error('Please only upload plain text or source files (skeleton)');
   	    	}	

    	   	$type = finfo_file($finfo, $testClass['tmp_name']);
       		if(strpos($type, 'text') === FALSE){
       	    return JSON::error('Please only upload plain text or source files (skeleton)');
      	 	}	

            #TODO: Remove this terrible files need exercise needs files loop of death
            #Create the new solution class file
      	 	$solutionContents = file_get_contents($solution['tmp_name']);
				$sol = new CodeFile();
				$sol->setContents($solutionContents);
				$sol->setName("/".$name."/Solution");
				$sol->setUpdated(time());
				$sol->setAdded(time());
				$sol->setOwnerId($user->getId());
				$sol->setSection($user->getSection());
				$sol->setExerciseId(1);
		     	$sol->save();
				$sol = CodeFile::getCodeFileByName("/".$name."/Solution");

			#Create the new skeleton class file
            $skeletonContents = file_get_contents($skeleton['tmp_name']);
                $skeletonContents = str_replace("<", "&lt;", $skeletonContents);
                $skeletonContents = str_replace(">", "&gt;", $skeletonContents);
				$skel = new CodeFile();
				$skel->setContents($skeletonContents);
				$skel->setName("/".$name."/AdminSkeleton");
				$skel->setUpdated(time());
				$skel->setAdded(time());
				$skel->setOwnerId($user->getId());
				$skel->setSection($user->getSection());
				$skel->setExerciseId(1);
				$skel->save();
				$skel = CodeFile::getCodeFileByName("/".$name."/AdminSkeleton");
				
            #Create the new test class file
			$testClassContents = file_get_contents($testClass['tmp_name']);
				$test = new CodeFile();
				$test->setContents($testClassContents);
				$test->setName("/".$name."/TestClass");
				$test->setUpdated(time());
				$test->setAdded(time());
				$test->setOwnerId($user->getId());
				$test->setSection($user->getSection());
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
			$e->setDescription("please"); //This should die when descriptions become pdfs
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
                JSON::success('Overwrote exercise '.$e->getTitle());
			}
            else{
                JSON::success('Uploaded exercise '.$e->getTitle());
			}

			//Update files with correct exercise ID
            //This is terrible terrible terrible stuff
			$Sol = CodeFile::getCodeFileById($e->getSolutionId());
			$Sol->setExerciseId($e->getId());
			$Sol->save();

			$Skel = CodeFile::getCodeFileById($e->getSkeletonId());
			$Skel->setExerciseId($e->getId());
			$Skel->save();

			$TestClass = CodeFile::getCodeFileById($e->getTestClassId());
			$TestClass->setExerciseId($e->getId());
			$TestClass->save();


        }catch(Exception $e){
            return JSON::error($f->getMessage());
	    logError($f);
	    JSON::error($f);
        }

		finfo_close($finfo);

    }

}


?>

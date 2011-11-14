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

//		if($openDate != ""){
//			$openDate = strtotime($openDate);
//			$closeDate = strtotime($closeDate);

//			if(!$openDate || !$closeDate){
//				return JSON::error("Dates are not in the correct format");
//			}
//		}

		$e = Exercise::getExerciseByTitle($name);
		if($e){
			$update = true;

			#Check each entry to see if it was changed, it so, save the change
			if($openDate != ""){
				$e->setOpenDate($openDate);
				$e->setCloseDate($closeDate);
			}else{
                $e->setOpenDate("");
                $e->setCloseDate("");
            }

       		$finfo = finfo_open(FILEINFO_MIME_TYPE);
  			$type = finfo_file($finfo, $solution['tmp_name']);
      		if(strpos($type, 'text') !== FALSE){
				$solutionContents = file_get_contents($solution['tmp_name']);

				$file = CodeFile::getCodeFileById($e->getSolutionId());
				$file->setContents($solutionContents);

				$file->save();
			}

       		$finfo = finfo_open(FILEINFO_MIME_TYPE);
  			$type = finfo_file($finfo, $skeleton['tmp_name']);
      		if(strpos($type, 'text') !== FALSE){
				$skeletonContents = file_get_contents($skeleton['tmp_name']);

				$file = CodeFile::getCodeFileById($e->getSkeletonId());
				$file->setContents($skeletonContents);

				$file->save();
   	    	}
        
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

      	 	$solutionContents = file_get_contents($solution['tmp_name']);
				$sol = new CodeFile();
				$sol->setContents($solutionContents);
				$sol->setName("/".$name."/Solution");
				$sol->setUpdated(time());
				$sol->setAdded(time());
				$sol->setOwnerId($user->getId());
				$sol->setSection($user->getSection());
				$sol->setExerciseId(0);
				$sol->save();
				$sol = CodeFile::getCodeFileByName("/".$name."/Solution");
			
      	 	$skeletonContents = file_get_contents($skeleton['tmp_name']);
				$skel = new CodeFile();
				$skel->setContents($skeletonContents);
				$skel->setName("/".$name."/AdminSkeleton");
				$skel->setUpdated(time());
				$skel->setAdded(time());
				$skel->setOwnerId($user->getId());
				$skel->setSection($user->getSection());
				$skel->setExerciseId(0);
				$skel->save();
				$skel = CodeFile::getCodeFileByName("/".$name."/AdminSkeleton");
				
			$testClassContents = file_get_contents($testClass['tmp_name']);
				$test = new CodeFile();
				$test->setContents($testClassContents);
				$test->setName("/".$name."/TestClass");
				$test->setUpdated(time());
				$test->setAdded(time());
				$test->setOwnerId($user->getId());
				$test->setSection($user->getSection());
				$test->setExerciseId(0);
				$test->save();
				$test = CodeFile::getCodeFileByName("/".$name."/TestClass");

			$e = new Exercise;
    	    $e->setTitle($name);
       		$e->setAdded(time());
			$e->setOpenDate($openDate);
			$e->setCloseDate($closeDate);
			$e->setSection($user->getSection());
			$e->setSolutionId($sol->getId());
			$e->setTestClassId($test->getId());
			$e->setSkeletonId($skel->getId());
			$e->setDescription("please");

			$idSol = $e->getSolutionId();
			$idSkel = $e->getSkeletonId();
			$idTest = $e->getTestClassId();

		}

		#The following is ALWAYS updated, so they
		#aren't within the if($e) block
		$visible = $_POST['visible'];
		if($visible == "on" || $openDate <= time()) $visible = 1;
		else $visible = 0;
		$e->setVisible($visible);

		$multi = $_POST['multiUser'];
		if($multi == "on") $multiUser = 1;
		else $multiUser = 0;
		$e->setMultiUser($multiUser);

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

		//Seems to only work on second "adding" of exercise...
		//Problem seems to be within the loop at the end of 
		//addSkeletons
        if($visible == 1){
			$e->addSkeletons();
        }


		finfo_close($finfo);

    }

}


?>

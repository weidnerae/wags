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
		if($openDate != ""){
			$openDate = strtotime($openDate);
			$closeDate = strtotime($closeDate);

			if(!$openDate || !$closeDate){
				return JSON::error("Dates are not in the correct format");
			}
		}

		$e = Exercise::getExerciseByTitle($name);
		if($e){
			$update = true;

			#Check each entry to see if it was changed, it so, save the change
			if($openDate != ""){
				$e->setOpenDate($openDate);
				$e->setCloseDate($closeDate);
			}

       		$finfo = finfo_open(FILEINFO_MIME_TYPE);
  			$type = finfo_file($finfo, $solution['tmp_name']);
      		if(strpos($type, 'text') !== FALSE){
   	    		$solutionContents = file_get_contents($solution['tmp_name']);
				$e->setSolution($solutionContents);
			}

       		$finfo = finfo_open(FILEINFO_MIME_TYPE);
  			$type = finfo_file($finfo, $skeleton['tmp_name']);
      		if(strpos($type, 'text') !== FALSE){
				$skeletonContents = file_get_contents($skeleton['tmp_name']);
				$e->setSkeleton($skeletonContents);
   	    	}
        
			$finfo = finfo_open(FILEINFO_MIME_TYPE);
  			$type = finfo_file($finfo, $testClass['tmp_name']);
      		if(strpos($type, 'text') !== FALSE){
   	    		$testClassContents = file_get_contents($testClass['tmp_name']);
				$e->setTestClass($testClassContents);
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
      	 	$skeletonContents = file_get_contents($skeleton['tmp_name']);
			$testClassContents = file_get_contents($testClass['tmp_name']);

			$e = new Exercise;
        	$e->setSolution($solutionContents);
	        $e->setSkeleton($skeletonContents);
	        $e->setDescription($description);
    	    $e->setTitle($name);
       		$e->setAdded(time());
			$e->setOpenDate($openDate);
			$e->setCloseDate($closeDate);
			$e->setSection($user->getSection());
			$e->setTestClass($testClassContents);//Temporary server side handling
			//of TestClass as client side hasn't been updated yet
		}

		#The following is ALWAYS updated, so they
		#aren't within the if($e) block
		$visible = $_POST['visible'];
		if($visible == "on") $visible = 1;
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
            else
                JSON::success('Uploaded exercise '.$e->getTitle());
        }catch(PDOException $e){
            echo $f->getMessage();
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

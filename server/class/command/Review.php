<?php

/**
 * Review
 *
 * Compile code submitted by the user together with
 * the solution class, test class, and whatever 
 * helper classes were provided.
 */

define("EXEC_ERROR", 1);
define("EXEC_SUCCESS", 0);

class Review extends Command
{	
	public function execute(){
		// catch all exceptions -- May remove this
		try {
			
		$user = Auth::getCurrentUser();
        $admin = $user->isAdmin();
		$section = $user->getSection();

		//Define the regular expressions used
		//for finding package name,
		//and success of program
		// -allows for nested packages
		$packageRegex = "/^package\s+([^\d]\w+(?:.\w+)*)/m";
		$successRegex = "/Success<br \/>/";

		//Grab posted information
		$code = $_POST['code'];  // code that student has inputed
		$exerciseTitle = $_POST['title'];
		$codeFileName = $_POST['name'];  // this is just the name Wags uses for storing
									     // and retrieve to/from database
		
		// Get exercise, associated solution and test codefiles
		//  and student codefile
		$exercise = Exercise::getExerciseByTitle($exerciseTitle);
		$exerciseId = $exercise->getId();
        $exerciseSkeleton = $exercise->getSkeleton();  // get rid of this
		$solutionCodeFile = CodeFile::getCodeFileById($exercise->getSolutionId());
		$testCodeFile = CodeFile::getCodeFileById($exercise->getTestClassId());
		$studentCodeFile = CodeFile::getCodeFileByName($codeFileName);
		
		// Get original file names for solution, test, and student files
		// 	-This will be used to replace the regular expressions for finding
		//   class names, since class names are specific to Java, and Java files
		//   must have same name as contained class anyways
		$solutionFileName = $solutionCodeFile->getOriginalFileName();
		$testFileName = $testCodeFile->getOriginalFileName();
		$studentFileName = $studentCodeFile->getOriginalFileName();
		
		// Get original file extensions for solution, test, and student files
		$solutionFileExtension = $solutionCodeFile->getOriginalFileExtension();
		$testFileExtension = $testCodeFile->getOriginalFileExtension();
		$studentFileExtension = $studentCodeFile->getOriginalFileExtension();
		
		// current time
		$now = time();
		
		// Invisible exercises = expired
        if(!$exercise->getVisible() && !$admin){
            return JSON::error("Exercise not currently visible");
        }
        
        //If the exercise has expired:
		$closed = $exercise->getCloseDate();
		if($closed != '' && $closed < $now && $closed != 0 && !$admin){
			return JSON::error("This exercise has expired.");
		}
		
		// ***CHOOSE LANGUAGE***
		// Determine the language being used
		//	- The language of the solution file will dictate the protocol being
		//	  being used, and other files will simply use their file extensions
		switch($solutionFileExtension)
		{
			case "fs":	
				$lang = "FSharp";
				break;
			
			case "java":
				$lang = "Java";
				
				break;
				
			case "pl":
				$lang = "Prolog";
				
				break;
				
			default:
				// if not able to match language, return error 
				return JSON::error("Error in matching solution file extension");
				break;
		}


		// Update or create submission for user/exercise pairing
		$sub = Submission::getSubmissionByExerciseId($exerciseId);
		$sub_num = 0;
		if($sub){
			$sub->setFileId($studentCodeFile->getId());
			$sub->setUpdated($now);
			if($sub->getNumAttempts() == null){
				$sub->setNumAttempts(1);
				$sub_num = 1;
			} else {
				$sub_num = $sub->getNumAttempts() + 1;
				$sub->setNumAttempts($sub_num);
			}
		} else {
			$sub = new Submission();
			$sub->setExerciseId($exerciseId);
			$sub->setFileId($studentCodeFile->getId());
			$sub->setUserId($user->getId());
			$sub->setUpdated($now);
			$sub->setAdded($now);
			$sub->setSuccess(0);
			$sub->setNumAttempts(0);
		}

	// SAVE VERSIONS
		// Check to see if this file is a version or AdminSkeleton instead of the main file.
		//	- We don't want to save a version of a version, or a version of the AdminSkeleton.
		$prev_sub_num = $sub_num - 1;
		$subString = strstr($codeFileName, "_Versions", true);
		$isAdminSkel = strstr($codeFileName, "AdminSkeleton", true);
		if ($subString === FALSE && $isAdminSkel === FALSE) // if not found and NOT an Admin skeleton, then main file, so we need to save a new version
		{
		 	//	- Just get the bare name of the file, NOT the /EXERCISE/codeFileName string by
			//	 searcing for the second occurrence of "/", and then taking the string afterward
			//		- Probably a better way to do this instead of taking a substring twice
			$codeFileName_portion = substr($codeFileName, strpos($codeFileName, "/") + 1);
			$codeFileName_portion = substr($codeFileName_portion, strpos($codeFileName_portion, "/") + 1);
			
			$save_version = TRUE;
			if ($sub_num > 0) // if a version already exists,
			{
				// get the previous version and see if the current code has changed from the last version
				$prev_version = CodeFile::getCodeFileByName("$codeFileName"."_Versions/"."$codeFileName_portion"."_Version_"."$prev_sub_num");
				if (strcmp($prev_version->getContents(), $code) == 0)
				{
					$save_version = FALSE; // if same code, then don't save new version
					$sub->setNumAttempts($prev_sub_num); // Reset submission attempt to previous number
				}
			}
			
			// Save version if necessary
			if ($save_version === TRUE)
			{	
				$new_version = new CodeFile();
				$new_version->setContents($code);
				// File needs to be under same exercise, under a codeFileName_VERSIONS/ folder, 
				//	and have same name as normal file, but with _Version_# attached
				$new_version->setName("$codeFileName"."_Versions/"."$codeFileName_portion"."_Version_"."$sub_num");
				$new_version->setExerciseId($exerciseId);
				$new_version->setOwnerId($user->getId());
				$new_version->setSection($section);
				$new_version->setOriginalFileName($studentCodeFile->getOriginalFileName());
				$new_version->setOriginalFileExtension($studentCodeFile->getOriginalFileExtension());
				$new_version->setUpdated($now);
				$new_version->setAdded($now);
				$new_version->save();
			}
		} else {
			// Don't add a new submission if running a version or the AdminSkeleton
			$sub->setNumAttempts($prev_sub_num); // Reset submission attempt to previous number
		}

	// END SAVE VERSIONS


	// CONSTRUCTION OF PATHS
		
		$code = utf8_decode($code);
	
		// Check for the package statement for Java files
		//	-in effect, this tells us whether or not we'll be 
		//	 using an inner class in this microlab, and allows 
		//	 us to take the appropriate actions
		preg_match($packageRegex, $solutionCodeFile->getContents(), $matches);
		$codeFileName = substr($codeFileName, 1); //files start with a repetitive '/'
		
		if(empty($matches)){ 			// No package, no inner class
			$path = "/tmp/section$section/$codeFileName"."Dir";
			$pkg = FALSE;
		} else {						// Package, so inner class
			$pkgName = $matches[1];
			// we want to allow nested packages, so replace the dots in the Java package
			//  statement with slashes to create a directory
			$pkgName = str_replace(".", "/", $pkgName);
			$path = "/tmp/section$section/$pkgName";
			$compilePath = "/tmp/section$section";
			$pkg = TRUE;
		}
		
		// remove any white space from the path 
		// 	- no real reason to have any since spaces just cause problems
		//	 and this is just a temporary path
		$path = str_replace(' ', '', $path);

		// Construct paths as admin, or if directory not already created
		// 	-construct solution, test, and helper class paths
		if(!is_dir($path) || $user->isAdmin()) {
			
			//Now that we know what the directory is, we check
	    	//to see if it already exists.  
			//	-If so, we remove contents completely
			//	-Else, create the directory and fail on error
			if(is_dir($path)) {
				exec("rm -rf $path/*");
			}
			else if(!mkdir($path, 0777, true) && !$user->isAdmin()){ //will need to edit permissions
				throw new Exception(error_get_last());
				return JSON::error("There was an internal error");          
         	}

			//Create solution file
			$solutionPath = "$path/$solutionFileName.$solutionFileExtension";
			$solutionFile = fopen($solutionPath, "w+");
			$solutionResult = fwrite($solutionFile, $solutionCodeFile->getContents());
			fflush($solutionFile);
			fclose($solutionFile);
	
			//Create test file
			$testPath = "$path/$testFileName.$testFileExtension";
			$testFile = fopen($testPath, "w+");
			$testResult = fwrite($testFile, $testCodeFile->getContents());
			fflush($testFile);
			fclose($testFile);
	
			//Create any helper files
			$helpers = $exercise->getHelperClasses();
			$helperResult = TRUE;
			$helperPaths = "";
			foreach($helpers as $helper){
				$helperPath = "$path/".$helper->getOriginalFileName().".".$helper->getOriginalFileExtension();
				$helperFile = fopen($helperPath, "w+");
				$result = fwrite($helperFile, $helper->getContents());
	
				if(!$result){
					$helperResult = FALSE;
					$error = error_get_last();
					$errorMsg = $error['message'];
				}
	
				fflush($helperFile);
				fclose($helperFile);
			}
	
			//if any files weren't properly written, exit
			if(!($solutionResult && $testResult && $helperResult)){
				return JSON::error("Administrative file error while writing: $errorMsg");
			}
		} 

        // Construct paths for Student
		// 	-This section is for students to create theirn own file, and grab solution, 
		//	 test, and helper files
		
		// Create and grab student file
		//	-each time this is run, the student class will be different,
		//	 so it's not lumped in with the other classes
		$studentPath = "$path/$studentFileName.$studentFileExtension";
		$studentFile = fopen($studentPath, "w+");
		$classResult = fwrite($studentFile, $code);
		fflush($studentFile);
		fclose($studentFile);

        // Grab solutionPath as student
		//	- This file should already be present, so no need to write it
		$solutionPath = "$path/$solutionFileName.$solutionFileExtension";

		// Grab the test class as student
		$testPath = "$path/$testFileName.$testFileExtension";
        
        // Grab helper classes as student 
		$helpers = $exercise->getHelperClasses();
        $helperPaths = "";
		foreach($helpers as $helper){
			$helperPath = "$path/".$helper->getOriginalFileName().".".$helper->getOriginalFileExtension();
            $helperPaths = $helperPaths." ".$helperPath;
        }

		// Make sure student class was written
		if(!$classResult) return JSON::error("Problem writing student file");
		
		// Escape spaces in all paths -> *** SPACES HAVE BEEN REMOVED ABOVE ALREADY, SO FOLLOWING NOT NEEDED - Mike ***
		//$solutionPath = str_replace(' ', '\ ', $solutionPath);
		//$testPath = str_replace(' ', '\ ', $testPath);
		//$studentPath = str_replace(' ', '\ ', $studentPath);
		//$helperPaths = str_replace(' ', '\ ', $helperPaths);

	// END CONSTRUCTION OF PATHS


	// COMPILATION OF FILES
		
		// ***CHOOSE LANGUAGE***
		// Different protocols for compilation based on the language of the solution class
		// -All final executables should be same name as original file name, without the file extension
		switch($lang)
		{
			case "FSharp":
				// Test class is Java still
				//
				// The F# '.fs' files will need to be compiled
				//	-We run 'fsharpc', which is a script that uses Mono and the Microsoft F# compiler 'fsc.exe'
				//	-This will create '.exe' files with same name as original file
				//	-Also, remove any old '.exe' files first
				exec("rm -f $path/*.exe");
				exec("cd $path; /usr/local/bin/fsharpc --nologo $solutionPath");
				exec("cd $path; /usr/local/bin/fsharpc --nologo $studentPath");
				
				// Compile the Java test class
				exec("/usr/bin/javac $testPath 2>&1", $output, $result);
				
				break;
				
			case "Java":
				// Compile the Java classes
                $compileCmd = "$solutionPath $testPath $helperPaths $studentPath"; 

				// test, soluton, helper, and student files are all Java
				exec("/usr/bin/javac $compileCmd 2>&1", $output, $result);
				
				break;
				
			case "Prolog":
				// Test class is Java still
				//
				// The prolog files will just be scripted now instead of compiled and run as executables
				// 	-Only need to pass the solution and student file names to RunCodeNew, where the proper
				//	 strings to run the files will be created.
				//	-There were too many issues with permissions to be able to compile and run -- Will be a problem when wanting to 
				//	 implement C functionality
				//
				// Compile the Java test class
				exec("/usr/bin/javac $testPath 2>&1", $output, $result);
				
				break;
				
			default:
				// if not able to match language, return error
				return JSON::error("Error in matching language for compilation");
				break;
		}
		
	// END COMPILATION OF FILES
	
	
	// RUN FILES
		
		// If compilation was successful, then run code
		// Else, failure
		if ($result == EXEC_SUCCESS){

            $nonce = $this->genRandomString(); // for validation

			//Running of microlab
			if($pkg){ 		//Within a package
				$output = $this->runCode($compilePath, $pkgName.".$testFileName", $solutionFileName, $studentFileName, $lang, $nonce);
			}
			else{			//Not within a package
				$output = $this->runCode($path, $testFileName, $solutionFileName, $studentFileName, $lang, $nonce);
			}
			
			/******************
            *Check for success*
            ******************/

            // "" to make string
            $noncePos = strpos($output[0], $nonce."");
            $output[0] = str_replace("\t", "<tab/>", $output[0]);
            $chkNonce = (FALSE !== $noncePos); // Basically, make 0 = true

            # Testing moving strictly to nonce validation
			//if(preg_match($successRegex, $output[0]) || $chkNonce){
			if($chkNonce){
				$sub->setSuccess(1);

                // Don't print nonce
                $output[0] = substr($output[0], 0, $noncePos);

				JSON::success($output);
			} else {
				$sub->setSuccess(0);
                $output[0] = $output[0];
				JSON::warn($output);
			}

		} else {
			$error = "Compilation Error: <br />";
			foreach($output as $line){
                $line = str_replace("\t", "<tab/>", $line);
				$error .= $line."<br />";
				$sub->setSuccess(0); //failure to compile is failure for lab
			}
			JSON::error($error);
		}

		$sub->save();
		
		}catch(Exception $e){
	    	logError($e);
			JSON::error("There was a compilation error.");
        }
	}

	
	/**
	 *
	 * Pass in directory containing all files, name of test, solution, and student files, and language of solution file
	 *
	 */
	private function runCode($dir, $testFileName, $solutionFileName, $studentFileName, $lang, $nonce){
		exec("/usr/bin/php class/command/RunCodeNew.php $dir $testFileName $solutionFileName $studentFileName $lang $nonce 2>&1", $output);
		return $output;
	}

    private function genRandomString() {
        $length = 10;
        $characters = '0123456789abcdefghijklmnopqrstuvwxyz';
        $string = "";    
        for ($p = 0; $p < $length; $p++) {
			// the length of $characters is 36, but indices range from 0 - 35
			// mt_rand chooses random number between given min and max, INCLUSIVE
			// so, have to subtract one from the string length of $characters so that max index is 35 instead of 36
            $string .= $characters[mt_rand(0, (strlen($characters) - 1) )];
        }
        return $string;
    }

}

?>


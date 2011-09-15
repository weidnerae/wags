<?php

/**
*Review
*
*Compile code submitted by the user together with
*the solution class, test class, and whatever 
*helper classes were provided.
*/

define("EXEC_ERROR", 1);
define("EXEX_SUCCESS", 0);

class Review extends Command
{
	public function execute(){
		$user = Auth::getCurrentUser();
		$section = $user->getSection();

		//Define the regular expressions used
		//for finding class and package name,
		//success of program
		$classRegex = "/public\sclass\s+([^\d]\w+)/";
		$packageRegex = "/package\s+([^\d]\w+)/";
		$successRegex = "/Success<br>/";

		//Grab posted information
		$code = $_POST['code'];
		$exerciseId = $_POST['id'];
		$fileName = $_POST['name'];
		$exercise = Exercise::getExerciseById($exerciseId);

		//If the exercise has expired:
		$closed = $exercise->getCloseDate();
		if($closed != '' && $closed < time()){
			return JSON::error("This exercise has expired.");
		}

		//If, for some strange reason, this code is
		//being used for a different exercise than before,
		//update it's exid
		$file = CodeFile::getCodeFileByName($fileName);
		if(!empty($file) && $file instanceof CodeFile){
			$file->setExerciseId($exerciseId);
			$file->save();
		}

		//Update or create submission for user/exercise pairing
		$sub = Submission::getSubmissionByExerciseId($exerciseId);
		if($sub){
			$sub->setFileId($file->getId());
			$sub->setUpdated(time());
		} else {
			$sub = new Submission();
			$sub->setExerciseId($exerciseId);
			$sub->setFileId($file->getId());
			$sub->setUserId($user->getId());
			$sub->setUpdated(time());
			$sub->setAdded($now);
			$sub->setSuccess(0);
		}

		//Check for the package statement -> in effect,
		//this tells us whether or not we'll be using an inner
		//class in this microlab, and allows us to take the
		//appropriate actions
		//Note:: We use the exercise solution to determine
		//whether or not to use a pkg rather than the code in case
		//the student uploads the skeleton into the wrong exercise.
		//We overwrite SKELETONS, not Solutions/Testclasses, so
		//Skeletons shouldn't be calling the shots...
		$code = str_replace("%2B", "+", $code);
		preg_match($packageRegex, $exercise->getSolution(), $matches);
		$fileName = substr($fileName, 1); //files start with a repetitive '/'
		
		if(empty($matches)){ 			// No package, no inner class
			$path = "/tmp/section$section/$fileName"."Dir";
			$pkg = FALSE;
		} else {						// Package, so inner class
			$pkgName = $matches[1];
			$path = "/tmp/section$section/$pkgName";
			$compilePath = "/tmp/section$section";
			$pkg = TRUE;
		}

		//Now that we know what the package would be, we check
		//to see if it already exists.  If so, great! If not, 
		//we create it, and fill it with the files
		if(!is_dir($path)){
			if(!mkdir($path, 0777, true)){ //will need to edit permissions
				$error = error_get_last();
				return JSON::error($error['message']);		
			}

			//Create solution class
			preg_match($classRegex, $exercise->getSolution(), $matches);
			$className = $matches[1];
			$solutionPath = "$path/$className.java";
			$solutionFile = fopen($solutionPath, "w+");
			$solutionResult = fwrite($solutionFile, $exercise->getSolution());
			fflush($solutionFile);
			fclose($solutionFile);

			//Create tester class
			preg_match($classRegex, $exercise->getTestClass(), $matches);
			$testName = $matches[1];
			$testPath = "$path/$testName.java";
			$testFile = fopen($testPath, "w+");
			$testResult = fwrite($testFile, $exercise->getTestClass());
			fflush($testFile);
			fclose($testFile);

			//create any helper classes
			$helpers = $exercise->getHelperClasses();
			$helperResult = TRUE;
			foreach($helpers as $helper){
				preg_match($classRegex, $helper->getContents(), $matches);
				$helperName = $matches[1];
				$helperPath = "$path/$helperName.java";
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
				return JSON::error("Administrative class error while writing: $errorMsg");
			}

		}
		//Create student class - 
		//each time this is run, the student class will be different,
		//so it's not lumped in with the other classes
		preg_match($classRegex, $code, $matches);
		if(empty($matches)){
			return JSON::error("Please check class name");
		}
		$className = $matches[1];
		$classPath = "$path/$className.java";
		$studentFile = fopen($classPath, "w+");
		$classResult = fwrite($studentFile, $code);
		fflush($studentFile);
		fclose($studentFile);

		//Likewise, each time it is run we need to locate the
		//testclass
		preg_match($classRegex, $exercise->getTestClass(), $matches);
		$testName = $matches[1];


		//Make sure student class was written
		if(!$classResult) return JSON::error("Problem writing student file");

		//Compilation
		exec("/usr/bin/javac $path/*.java 2>&1", $output, $result);
		if($result == EXEC_ERROR){
			foreach($output as $line){
				$error .= $line."<br>";
				$sub->setSuccess(0); //failure to compile is failure for lab
			}
			JSON::error($error);
		} else if ($result == EXEC_SUCCESS){

			//Running of microlab
			if($pkg){ 		//Within a package
				$output = $this->runCode($compilePath, $pkgName.".$testName");
			}
			else{			//Not within a package
				$output = $this->runCode($path, $testName);
			}
			//retain formatting
			$output[0] = "<pre>".$output[0]."</pre>";

			//Check for success
			if(preg_match($successRegex, $output[0])){
				$sub->setSuccess(1);
				JSON::success($output);
			} else {
				$sub->setSuccess(0);
				JSON::warn($output);
			}

		}

		$sub->save();
	}

	private function runCode($dir, $className){
		exec("/usr/bin/php class/command/runcode.php $dir $className 2>&1", $output);
		return $output;
	}

}

?>


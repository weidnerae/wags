<?php

define("EXEC_SUCCESS", 0);

class MagnetReview extends Command
{
	public function execute(){
		$code = $_POST['code'];
        $title = $_POST['title'];
        $magnetProblem = MagnetProblem::getMagnetProblemByTitle($title);
        $user = Auth::getCurrentUser();

        $type = $magnetProblem->getProblemType();

        if(strpos($type, "prolog") !== FALSE){
            $lang = "pl";
        } else if(strpos($type, "c") !== FALSE) {
            $lang = "c";
        } else {
            $lang = "java";
        }


        // Grab the necessary simple files
        $simpleFiles = SimpleFile::getFilesForMP($magnetProblem->getId());
  
        // Find the correct directory (i.e., package structure or not) to
        // place the files
        $dir = "/tmp/magnets/".$user->getUsername();
        $dir = $dir . $simpleFiles[0]->getPackage();
        // Create a directory for the files if it doesn't exist
        if(!is_dir($dir)) mkdir($dir, 0777, true);

        // Create the files associated with this problem
        // givenFiles is constructed out of the SimpleFiles so that we can
        // compile the student code LAST, which gives more clear compile 
        // errors than when things are compiled in a random order
        $givenFiles = "";
        foreach($simpleFiles as $simpleFile){
            $fileName = $simpleFile->getClassName();
            if(strpos($fileName, ".pl") !== FALSE ){
                $filePath = "$dir/$fileName.pl";
            } else if(strpos($fileName, ".c") !== FALSE) {
                $filePath = "$dir/$fileName.c";
            } else {
                $filePath = "$dir/$fileName.java";
            }
            $file = fopen($filePath, "w+");
            $fileResult = fwrite($file, $simpleFile->getContents());
            fflush($file);
            fclose($file);
            
            if(!$fileResult){
                return JSON::error("Couldn't write $filePath");
            }

            $givenFiles = $givenFiles." $filePath"; // Keep track of files
        }

        // Create the file with student code
        $studentPath = "$dir/Student.".$lang;
       
        $file = fopen($studentPath, "w+b"); // Will change to default
        $code = str_replace('\r\n', PHP_EOL, $code);
        fwrite($file, $code);
        fflush($file);
        fclose($file);
        
        // Find the test class (the driver/main class)
        $driver = SimpleFile::getTestFileForMP($magnetProblem->getId());
        $driverName = $driver->getClassName();

        // Compile the code
        if($lang == "java"){
            exec("/usr/bin/javac $givenFiles $studentPath 2>&1", $output, $result);
        } else if($lang == "pl"){
            // Assuming the only java file is the driver
            exec("/usr/bin/javac $dir/$driverName.java 2>&1", $output, $result);
        } else if ($lang == "c") {
            exec("/usr/bin/gcc $dir/$driverName -o $dir/$driverName.o 2>&1", $output, $result);
        }
        // Check compilation -- Success 
        if($result == EXEC_SUCCESS){
            $nonce = $this->genRandomString();            
            if($lang == "java"){
                exec("/usr/bin/php class/command/RunCodeNew.php $dir $driverName blank blank Java $nonce 2>&1", $stdout); 
            }else if($lang == "pl"){
                // We have to do more if it's prolog because not all the
                // Test/Helper classes are Java, we have a Prolog Solution in 
                // there as well. We check for the ".pl" extension on the ClassName
                // that we left in there in AddMagnetExercise.
                $givenFilesArr = explode(" ",$givenFiles);
                foreach($givenFilesArr as $filePath){
                    if(strpos($filePath, ".pl") !== false){
                        $solutionPath = $filePath;
                    }
                }
                
                // RunCodeNew adds the directory and file extension for prolog
                // so we have to pull out the filename
                $studentPath = pathinfo($studentPath)['filename'];
                $solutionPath = pathinfo($solutionPath)['filename'];

                // driverName is the uploaded Java Test file that will run
                //     and compare the student and solution prolog files.
                // solutionPath is the path of the uploaded Prolog solution.
                // studentPath is the path of the file generated from the
                //     student's magnets.
                exec("/usr/bin/php class/command/RunCodeNew.php $dir $driverName $solutionPath $studentPath Prolog $nonce 2>&1",$stdout);
            } else if ($lang == "c") {
                exec("/usr/bin/php class/command/RunCodeNew.php $dir $driverName.o blank blank C $nonce 2>&1", $stdout); 
            } else{
                JSON::warn("Error determining Language");
            }

            $stdout = str_replace("\t", "<tab/>", $stdout);
            $noncePos = strpos($stdout[0], $nonce."");
            $chkNonce = (FALSE !== $noncePos); // Basically, make 0 = true

            // Generates a submission using the chkNonce for success
            // makes sure problem is assigned first, so "Review" problems won't
            // allow students to finish assignments late
            if($magnetProblem->isAssigned()){
                $this->generateSubmission($chkNonce, $magnetProblem->getId());
            }

            if($chkNonce){
                // Don't print nonce
                $stdout[0] = substr($stdout[0], 0, $noncePos); 
                return JSON::success($stdout);
            }else
                return JSON::warn($stdout);
        // Check compilation -- Failure 
        } else {
            // Generates a submission that was unsuccessful
            $this->generateSubmission(false, $magnetProblem->getId());

            $error = "Compilation Error: <br />";
            foreach($output as $line){
                $error .= $line."<br />"; 
            }
            $error = str_replace("\t", "<tab/>", $error);
            return JSON::error($error);
        }
    }

    private function genRandomString(){
        $length = 10;
        $characters = '0123456789abcdefghijklmnopqrstuvwxyz';
        $string = "";
        for ($p = 0; $p < $length; $p++){
            $string .= $characters[mt_rand(0, (strlen($characters) - 1) )];
        }

        return $string;
    }

    // generateSubmission
    //
    // Handles updating or creating a submission for this user and problem
    // pair.  In short -> If a successful submission exists, nothing happens.
    // If an unsuccessful submission exists, it gets updated (numAttempts
    // increments, success value may change).  If no submission exists, 
    // one gets created.
    private function generateSubmission($success, $id){
        // Basically, if ($success) it equals 1, otherwise 0
        $success = $success ? 1 : 0;
        $sub = MagnetSubmission::getSubmissionByProblem($id);
  
        // There is already a submission for this problem
        if($sub){
            // If they got it right, return
            if($sub->getSuccess() == 1){
                return;
            } else {
                // Increment the number of attempts to get it right
                $sub->setNumAttempts($sub->getNumAttempts() + 1);
                $sub->setSuccess($success);
                $sub->save();
            }
        } else {
            // Create a new submission for this user, exercise pairing
            $user = Auth::getCurrentUser();
            $sub = new MagnetSubmission();
            $sub->setUserId($user->getId());
            $sub->setSectionId($user->getSection());
            $sub->setMagnetProblemId($id);
            $sub->setNumAttempts(1); // the first attempt
            $sub->setSuccess($success);
            $sub->setAdded(time());
            $sub->setUpdated(time());
            $thisUser = $sub->getUserId();
            $thisSection = $sub->getSectionId();
            $thisProblem = $sub->getMagnetProblemId();
            $thisAttempts = $sub->getNumAttempts();
            $thisAdded = $sub->getAdded();
            $thisUpdated = $sub->getUpdated();

            $sub->save();
        }
        return;
    }
}
?>

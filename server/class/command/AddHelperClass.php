<?php

/**
*AddHelperClass
*
*Based off of UploadFile.php
*Uploads the helper classes for an exercise,
*setting the owner to 0, the exercise to
*whatever is selected on the client side,
*and the section to the current users section
*
*This is used for testing microlabs that require more
*than a solution, skeleton, and testclass
*/

class AddHelperClass extends Command
{
	public function execute()
	{
		$user = Auth::getCurrentUser();
		$exerciseTitle = $_POST['Exercises'];
		$exercise = Exercise::getExerciseByTitle($exerciseTitle);
        $aFile = FALSE; # Flag for presence of a file
            	
        # If there is a helperclass   
        if($_FILES['HelperClass']['size'] != 0){
            $aFile = TRUE;

            # Addhelper returns 1 on success, 0 on failure, already echoes
            if(!$this->addHelper())
                return;
        } 


        # If there is a description
        if($_FILES['descriptionPDF']['size'] != 0){
           # Descriptions don't work yet....  c'mon Darryl, lemme use
           # the server...
           /* $aFile = TRUE;

            if(!$this->addDesc())
                return;*/

           echo "Desc Uploading Broken. ";
           return;
        }

        if(!$aFile){
            echo "No Files to Upload"; 
            return;
        }

        # The client looks for "Class Uploaded" and treats
        # it differently
		echo "Class Uploaded";

	}
    
    # addHelper
    # Handles the adding of a helper class
    # Both this function and addDesc return TRUE or FALSE for 
    #   success.  If FALSE, an ECHO statement should immediately
    #   precede the "return FALSE;" line with an error message
    private function addHelper(){
        # Grab variables
        $user = Auth::getCurrentUser();
		$exerciseTitle = $_POST['Exercises'];
		$exercise = Exercise::getExerciseByTitle($exerciseTitle);
		$helper = $_FILES['HelperClass'];

		# Get original file name and extension for helper file
		$helperFileName = pathinfo($helper["name"], PATHINFO_FILENAME);
		$helperFileExtension = pathinfo($helper["name"], PATHINFO_EXTENSION);

        # Make sure correct type of file
		$finfo = finfo_open(FILEINFO_MIME_TYPE);
		$type = finfo_file($finfo, $helper['tmp_name']);

		if(strpos($type, 'text') === FALSE){
			echo 'Please only upload plain text or source files';
			return FALSE;
		}

		$helperContents = file_get_contents($helper['tmp_name']);
   		$helperName = "/".$exerciseTitle."/".$_FILES['HelperClass']['name'];

        # Actually create the file, save it
   		$file = new CodeFile();
   		$file->setContents($helperContents);
   		$file->setName($helperName);
   		$file->setExerciseId($exercise->getId());
   		$file->setOwnerId(CodeFile::getHelperId()); #0 is used specifically for helper classes
   		$file->setSection($user->getSection());
		$file->setOriginalFileName($helperFileName);
		$file->setOriginalFileExtension($helperFileExtension);
    	$file->setAdded(time());
   		$file->setUpdated(time());
        $id = CodeFile::getHelperId();

   		$file->save();
        return TRUE;
    }

    # addDesc
    # Handles the adding of a description
    # Both this function and addHelper return TRUE or FALSE for 
    #   success.  If FALSE, an ECHO statement should immediately
    #   precede the "return FALSE;" line with an error message
    private function addDesc(){
        # Get Variables
		$description = $_FILES['descriptionPDF'];
		$descTmp = $description['tmp_name'];
		$descName = $description['name'];
        $user = Auth::getCurrentUser();

        # Check for right type
		$finfo = finfo_open(FILEINFO_MIME_TYPE);
		$type = finfo_file($finfo, $description['tmp_name']);

		if(strpos($type, 'pdf') === FALSE){
			echo 'Please only upload plain text or source files';
			return FALSE;
		}

        # These will have to change when deployed publicly - should be extracted
        $section = $user->getSection();
        $path = "/usr/local/apache2/htdocs/cs/wags/Test_Version/descriptions/section".$section;

        # Cannot currently construct the needed directory,
        # must add by hand
        /*if(!is_dir($path)){
            mkdir("$path/", 0777, TRUE);
        }*/

        # Set up current file location, final location variables
        $moveTo = "$path/$descName";
        $truncName = str_replace(".pdf", ".jpg", $descName); 
        $fileLoc = $path;
        $urlLoc = "http://cs.appstate.edu/wags/descriptions/section$section/$truncName";

        # Currently, descriptions can't be overwritten.  Temporary
		if(file_exists($moveTo)){
			echo "Desc file already exists. Please change filename";
			return FALSE;
		}

//-----># Breaks on move_uploaded_file, doesn't seem to even continue
        # Or we would be seeing that error message - same symptoms as
        # mkdir
		if(!move_uploaded_file($_FILES['descriptionPDF']['tmp_name'], $moveTo)){
			echo "Error uploading description file";
			return FALSE;
		}else{
            exec("convert $fileLoc/$descName $fileLoc/$truncName"); 
           
			$exercise->setDescription($urlLoc);
			$exercise->save();
		}

        return TRUE;
    }
}

?>

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
            $aFile = TRUE;

            if(!$this->addDesc())
                return;
        }

        if(!$aFile){
            echo "No Files to Upload"; 
            return;
        }

        # The client looks for "Upload Successful" and treats
        # it differently
		echo "Upload Successful";

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
		$exerciseTitle = $_POST['Exercises'];
		$exercise = Exercise::getExerciseByTitle($exerciseTitle);
		$exerciseName = $exercise->getTitle();
        $user = Auth::getCurrentUser();
        $section = $user->getSection();

        # Check for right type
		$finfo = finfo_open(FILEINFO_MIME_TYPE);
		$type = finfo_file($finfo, $description['tmp_name']);

		if(strpos($type, 'pdf') === FALSE){
			echo 'Please only upload plain text or source files';
			return FALSE;
		}

        # Set up current file location, final location variables
        $moveTo = "/tmp/descriptions/$descName";
        $truncName = str_replace(".pdf", ".jpg", $descName);
        $urlLoc = WE_ROOT."/descriptions/desc/descriptions/$section.$truncName";

        # Currently, descriptions can't be overwritten.  Temporary
		if(file_exists($moveTo)){
			echo "Desc file already exists. Please change filename";
			return FALSE;
		}
		
		# Make sure the directory exists
		if (!is_dir("/tmp/descriptions")) {
			mkdir("/tmp/descriptions");
		}

		# Make sure the PDF gets uploaded correctly, then convert it to a JPG and store
		# the URL and image in the database
		if(!move_uploaded_file($_FILES['descriptionPDF']['tmp_name'], $moveTo)){
			echo "Error uploading description file";
			return FALSE;
		} else {
            exec("convert /tmp/descriptions/$descName /tmp/descriptions/$section.$truncName");
            
            $image = file_get_contents("/tmp/descriptions/$section.$truncName");
           
			$exercise->setDescription($urlLoc);
			$exercise->setDescriptionJPG($image);
			$exercise->save();
		}

        return TRUE;
    }
}

?>

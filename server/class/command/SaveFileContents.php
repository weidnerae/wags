<?php

/**
 * SaveFileContents
 *
 * Save a file with the given filename. Put text passed though REQUEST
 * into DB.
 */

class SaveFileContents extends Command
{
    public function execute()
    {

        if(!Auth::isLoggedIn()){
            return JSON::error('Must be logged in to save a file.');
        }
        
        if(!isset($_POST['name'])){
            return JSON::error('File name needed.');
        }
		
		// VERSIONS
		// 	- Don't overwrite verions, but instead save to main file
		//	- To do this, get the main file name, which is found before the "_VERSIONS"
		//		part of the the name
		$fileName = $_POST['name'];
        $contents = $_POST['contents'];
		
			// only get the first part of the string
		$subString = strstr($fileName, "_Versions", true);
		if ($subString != FALSE) // if not found, would be false
			$fileName = $subString;
        
        $user = Auth::getCurrentUser();
		$file = CodeFile::getCodeFileByName($fileName);
        if($file->getOwnerId() == CodeFile::getHelperId()) return;
	
        $contents = utf8_decode($contents);
	
		if(!empty($file) && $file instanceof CodeFile){
			// Update CodeFile.
			$file->setContents($contents);
			$file->setUpdated(time());
			$file->save();
		}else{
			// Create new CodeFile.
			$file = new CodeFile();
			$file->setContents($contents);
			$now = time();
			$file->setName($fileName);
			$file->setExerciseId(0);
			$file->setOwnerId($user->getId());
			$file->setSection($user->getSection());
			$file->setUpdated($now);
			$file->setAdded($now);
			$file->save();
		}

		return JSON::success('File '.$fileName.' saved');
	}
}
?>

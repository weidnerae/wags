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
        
        if(!isset($_REQUEST['name'])){
            return JSON::error('File name needed.');
        }
        
        $user = Auth::getCurrentUser();
	$file = CodeFile::getCodeFileByName($_REQUEST['name']);
	$contents = $_REQUEST['contents'];

	/**
	 * For some reason, the URL encoding on the client side does
	 * not encode "+" correctly.  Thus, it is manually encoded
	 * on the client side as %2B -> the correct HTML encoding.
	 * Of course, now %2B is encoded and decoded, so this next
	 * line manually replaces it with "+" again
	 */
	$contents = str_replace("%2B", "+", $contents);

    #A quick fix for partners -- SHOULD NOT BE PERMANENT
    $exercise = Exercise::getExerciseById($file->getExerciseId());
    if($exercise->isMultiUser() && !$user->isAdmin()){
        $submission = Submission::getSubmissionByExerciseId($exercise->getId());
        #If the submission exists
        if($submission->getPartner() != null){
            $partnerName = $submission->getPartner();
            $partner = User::getUserByUsername($partnerName);
            $partnerId = $partner->getId();
        }
    }

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
	    $file->setName($_REQUEST['name']);
	    $file->setExerciseId(0);
            $file->setOwnerId($user->getId());
			$file->setSection($user->getSection());
            $file->setUpdated($now);
            $file->setAdded($now);
            $file->save();
        }

        #More TEMPORARY fix
        if(isSet($partnerId)){
            $partnerFile = CodeFile::getPartnerFile($partner->getId(), $file->getName());
            if(!empty($partnerFile) && $partnerFile instanceof CodeFile){
                $partnerFile->setContents($contents);
                $partnerFile->setUpdated(time());
                $partnerFile->save();
            } else{
                $partnerFile = new CodeFile();
                $partnerFile->setContents($contents);
                $partnerFile->setName($_REQUEST['name']);
                $partnerFile->setExerciseId($file->getExerciseId());
                $partnerFile->setOwnerId($partner->getId());
                $partnerFile->setSection($user->getSection());
                $partnerFile->setUpdated(time());
                $partnerFile->setAdded(time());
                $partnerFile->save();
            }
        }

        return JSON::success('File '.$_REQUEST['name'].' saved');
    }
}
?>

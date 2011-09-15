<?php

/**
 * UploadFile
 *
 * Receive a file uploaded from the user's browser.
 * Only accept plain text files. Read contents of 
 * file and insert into database.
 *
 * @author Robert Bost <bostrt at appstate dot edu>
 */

class UploadFile extends Command
{
    public function execute()
    {
        if(!Auth::isLoggedIn()){
            return JSON::error('Must be logged in to upload file.');
        }
        if(!isset($_FILES['the_file'])){
            return JSON::Error('No file');
        }        

        $file = $_FILES['the_file'];
        $user = Auth::getCurrentUser();

        if($file == null){
            return JSON::Error('No file');
        }

        // Check that mimetype is plain text.
        $finfo = finfo_open(FILEINFO_MIME_TYPE);
        $type = finfo_file($finfo, $file['tmp_name']);
        if(strpos($type, 'text') === FALSE){
            // mimetype is not acceptable.
            return JSON::error('Please only upload plain text or source files.');
        }

        $contents = file_get_contents($file['tmp_name']);

        $directory = $_POST['curDir'];
        
        // Directory should begin and end with /
        if(substr($directory, 0, 1) != '/'){
            $directory = '/'.$directory;
        }
        if(substr($directory, -1) != '/'){
            $directory .= '/';
        }

        // If file exists with the same full path, just update that one
        $f = CodeFile::getCodeFileByName($directory.$file['name']);
        if($f){
            $update = true;
        }else{
            $f = new CodeFile();
            $f->setName($directory.$file['name']);
			$f->setSection($user->getSection());
	    $f->setOwnerId($user->getId());
	    $f->setExerciseId(0);
        }

        $f->setContents($contents);
        $now = time();
        $f->setAdded($now);

        try{
            $f->save();
            if(isset($update) && $update)
                JSON::success('Overwrote file '.$f->getName());
            else
                JSON::success('Uploaded file '.$f->getName());
        }catch(PDOException $e){
            echo $e->getMessage();
            logError($e);
        }

        finfo_close($finfo);
    }
}

?>

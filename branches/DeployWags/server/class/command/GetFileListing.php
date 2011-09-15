<?php

class GetFileListing extends Command
{
    public function execute()
    {
        // Use needs to be logged in to save a file.
        if(!Auth::isLoggedIn()){
            return JSON::error('Must be logged in to save a file.');
        }

        $user = Auth::getCurrentUser();
        
        $files = CodeFile::getCodeFilesByUser($user);
        $fileNames = "";

        // Comma separated file names.
        foreach($files as $file){
            $fileNames[] = $file->getName();
        }

        return JSON::success($fileNames);
    }
}

?>
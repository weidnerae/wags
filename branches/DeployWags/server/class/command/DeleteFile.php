<?php

class DeleteFile extends Command
{
    public function execute(){
        if(!Auth::isLoggedIn()){
            return JSON::error('Must be logged in to save a file.');
        }
        
        if(!isset($_REQUEST['name'])){
            return JSON::error('File name needed.');
        }

        $file = CodeFile::getCodeFileByName($_REQUEST['name']);

        if(!empty($file) && $file instanceof CodeFile){
            $file->delete();
        }
    }
}

?>

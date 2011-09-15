<?php

/**
 * RenameFile
 *
 * Rename an existing file.
 *
 * @author Robert Bost <bostrt at appstate dot edu>
 */

class RenameFile extends Command
{

    public function execute()
    {
        // permission check
        if(!Auth::isLoggedIn()){
            return JSON::error('Must be logged in to rename a file.');
        }

        // required request variables check
        if(!isset($_REQUEST['old']) || !isset($_REQUEST['new'])){
            return JSON::error('Old filename and new filename needed.');
        }

        $file = CodeFile::getCodeFileByName($_REQUEST['old']);

        if(!empty($file) && $file instanceof CodeFile){
            $file->setName($_REQUEST['new']);
            $file->save();
            return JSON::success($_REQUEST['old'].' renamed to '.$_REQUEST['new']);
        }else{
            return JSON::error('Error renaming '.$_REQUEST['old'].' to '.$_REQUEST['new']);
        }
    }

}

?>

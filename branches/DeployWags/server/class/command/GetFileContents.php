<?php

/**
 * GetFileContents
 *
 * Get file contents by name.
 *
 * @author Robert Bost <bostrt at tux dot appstate dot edu>
 */

class GetFileContents extends Command 
{
    public function execute()
    {
        // Use needs to be logged in to save a file.
        if(!Auth::isLoggedIn()){
            return JSON::error('Must be logged in to get a file.');
        }

        $user = Auth::getCurrentUser();

        // A file name must be asked for.
        if(isset($_REQUEST['name'])){
            $name = $_REQUEST['name'];
            // File name must begin with '/'
            if(substr($name, 0, 1) != '/')
                $name = '/'.$name;
            $file = CodeFile::getCodeFileByName($name);

            if(empty($file)){
                return JSON::warn("File not found with name ".$name);
            }

			$status = 1;
            /* If it's a helper class and the user isn't an administrator, they can't alter it */
			if($file->getOwnerId() == CodeFile::getHelperId() && !($user->isAdmin())) 
				$status = 0;

			//Grab the entire program
			$wholeCode = $file->getContents();
            # These shouldn't be necessary, but not what I'm testing atm
            $wholeCode = str_replace("%2A", "&", $wholeCode);
            $wholeCode = str_replace("&lt;", "<", $wholeCode);
            $wholeCode = str_replace("&gt;", ">", $wholeCode);

			$all = $status . $wholeCode;

			echo $all;

        }else{
            return JSON::error("No file name given.");
        }
    }
}

?>

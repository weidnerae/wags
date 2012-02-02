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
			if($file->getOwnerId() == CodeFile::getHelperId() && !($user->isAdmin())) $status = 0;

			#must parse skeleton files into three text sections delimited
			#by "//<end!TopSection>" and "//<end!MidSection>"

			$topNeedle = "//<end!TopSection>";
			$midNeedle = "//<end!MidSection>";

			//Grab the entire program
			$wholeCode = $file->getContents();
            $wholeCode = str_replace("%2A", "&", $wholeCode);
            $wholeCode = str_replace("&lt;", "<", $wholeCode);
            $wholeCode = str_replace("&gt;", ">", $wholeCode);


			$top = "";
			$mid = $wholeCode;
			$bot = "";

			//find the location of the delimiting comments
			$endofTop = strpos($wholeCode, $topNeedle);
			$endofMid = strpos($wholeCode, $midNeedle);

			if($endofTop){
				$top = substr($wholeCode, 0, $endofTop)."//<end!TopSection>";
				$mid = substr($wholeCode, $endofTop + strlen($topNeedle));
			}

			if($endofMid){
				$bot = substr($wholeCode, $endofMid);
				$mid = substr($wholeCode, $endofTop + strlen($topNeedle), strlen($wholeCode) - 
					strlen($top) - strlen($bot));
			}
			
			$all = $status.$top.$mid.$bot;
            //$all = $top.$mid.$bot;

			echo $all;

        }else{
            return JSON::error("No file name given.");
        }
    }
}

?>

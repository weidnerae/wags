<?php

/**
 * Login
 * 
 * @author Robert Bost <bostrt at appstate dot edu>
 */

class Login extends Command
{
    public function execute()
    {
        if(Auth::isLoggedIn()){
            return JSON::warn('Already logged in.');
        }

        if(isset($_REQUEST['username']) && isset($_REQUEST['password'])){
            $result = Auth::login($_REQUEST['username'],$_REQUEST['password']);
            if($result){
                # logUse is a research utility only, doesn't affect users
                $this->logUse();
                return JSON::success('Successfully logged in.');
            }
        }

        return JSON::error('Login failed. Check username and password.');
    }

# Everything here below (minus closing brackets, etc) is for RESEARCH and
# seeing what devices are used to access Wags.  That is all.  Doesn't affect
# the users (except that it may slow things down when logging in...)
    private function logUse(){
        # Temporarily working in my directory, where things
        # don't pass ownership to root
        if(!chdir('/u/csgs/meznarpr/Wags/iBook')){
            echo 'Cannot change working directory';
            return;
        }

        # Check for directory
        if(!is_dir('byUser')){
            mkdir('byUser');
            return;
        }

        $host = gethostbyaddr($_SERVER['REMOTE_ADDR']);
        $username = strtolower($_REQUEST['username']);
        # Construct filename
        $hostFile = "/u/csgs/meznarpr/Wags/iBook/byUser/$username";
        if(file_exists($hostFile)) {
            $this->updateFile($hostFile, $host);
        } else {
            $this->createFile($hostFile, $host);
        }

    }

    # Could have incorporated into updateFile, but this just keeps the flow
    # cleaner looking
    private function createFile($hostFile, $host){
        $fileHandle = fopen($hostFile, "w+");
        fwrite($fileHandle, "$host 1\n");
        fclose($fileHandle);
    }

    # Gets file contents as array of lines, look for the website,
    # increment the visit count, write to file
    private function updateFile($hostFile, $host){
        $found = FALSE;
        $lineNum = 0;
        $lines = file($hostFile); # Creates an array of lines from the file

        foreach ($lines as $line){
            sscanf($line, "%s %d", $device, $count);

            if($device == $host){ # If they have used this device before
                $count = $count + 1;
                $newLine = "$device $count\n";
                $lines[$lineNum] = $newLine; # Increment visit count
                $found = TRUE;                 
            }

            $lineNum++;
        }

        # If new to the website, they have visited 1 time
        if(!$found) $lines[] = "$host 1\n";

        $newFile = implode($lines); # Reconstruct one string from array
        $fileHandle = fopen($hostFile, "w+"); # Wipe file, open for writing
        fwrite($fileHandle, $newFile);
        fclose($fileHandle);
    }
}
?>

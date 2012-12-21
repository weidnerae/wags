<?php

class AddMagnetExercise extends Command
{
	public function execute(){
        $title = $_POST['title'];
        $desc = $_POST['desc'];
        $className = $_POST['class'];
        $functions = $_POST['functions'];
        $statements = $_POST['statements'];

        
        // Make sure there is no magnet exercises that already
        // has this title....
        /*  This is bad.  I should have implemented everything
            by id's, but currently SetMagnetExercises is run by
            name.  Meaning, the client may not even know the
            appropriate ids to make the switch...  I suppose
            first priority is just getting problem creation
            to exist, then we'll work on moving everything over
            to ids... */


        // Check for a MagnetProblemGroup for this section
        // -  if it doesn't exist, create it

        // Create the magnet problem

        // Return to client - client will perform a callback,
        // as it can now grab the ids of the new magnetproblem
        // and potentially the new magnetProblemGroup

        // All this is done in AddMagnetLinkage.php

        $file = '/tmp/check.txt';
        $file = fopen($file, "w");

        foreach($_POST as $key=>$val){
            $thisLine = "$key : $val\n";
            fputs($file, $thisLine);
        }

        fclose($file);
         
        return JSON::success('ok');
	}
}

?>

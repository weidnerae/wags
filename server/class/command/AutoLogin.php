<?php

/**
 * AutoLogin
 * 
 * @author Robert Bost <bostrt at appstate dot edu>
 * Automatically logs in the user, redirects to wags site
 */

class AutoLogin extends Command
{
    public function execute()
    {
        if(!empty($_REQUEST['username']) && !empty($_REQUEST['password'])){
            $result = Auth::login($_REQUEST['username'],$_REQUEST['password']);
            if($result){

                // To only show one exercise
                if(isset($_REQUEST['exercise'])){
                    $exTitle = $_REQUEST['exercise'];
                    $exercises = Exercise::getVisibleExercises();
                    // Cycle through all available exercises
                    foreach($exercises as $exercise){
                        // Make the right one visible
                        if($exercise->getTitle() == $exTitle){
                            $exercise->setVisible(1);
                        } else {
                            // all others invisible
                            $exercise->setVisible(0);
                        }

                        $exercise->save();
                    }

                }

                // To show up to a certain screen of WAGS
                if(isset($_REQUEST['loc'])){
                    $location = $_REQUEST['loc'];
                    header("Location: http://www.cs.appstate.edu/wags/Test_Version/?loc=$location");
                    return;
                }

                // Exercise::getVisibleExercises() returns all exercises
                // Set all invisible except one with given title...
                header("Location: http://www.cs.appstate.edu/wags/Test_Version/");
                return;
            }
       
        return JSON::error('Login failed. Check username and password.');
        }

        // No username/password provided
        Auth::logout();
        header("Location: http://www.cs.appstate.edu/wags/Test_Version/");
    }
}
?>

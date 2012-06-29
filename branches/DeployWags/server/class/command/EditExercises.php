<?php

require_once('DefInvis.php');

class EditExercises extends Command
{
	public function execute(){
		$exTitle = $_GET['title'];
		$attribute = $_GET['attribute'];

		$exercise = Exercise::getExerciseByTitle($exTitle);
		$text = "No change";

		#toggles visibility
		if($attribute == "vis"){
            # If invisible, become visible
            if($exercise->getVisible() == INVISIBLE){
                $exercise->setVisible(VISIBLE);
                $text = "Invisible -> Visible";
            }

            # If expired
            elseif($exercise->getVisible() == EXPIRED){
                # and closeDate is past current date, become visible
                if($exercise->getCloseDate() >= time()){
                    $exercise->setVisible(VISIBLE);
                    $text = "Expired -> Visible";
                # otherwise, mention that they need to change closeDate
                } else {
                    return JSON::warn("Change closing date to".
                         " alter an expired exercise");
                }
            }

            # If visible, become invisible
            elseif($exercise->getVisible() == VISIBLE){
                if($exercise->getOpenDate() >= time()){
                    $exercise->setVisible(PREOPEN);
                    $text = "Exercise waiting to become available";
                } else {
                    $exercise->setVisible(INVISIBLE);
                    $text = "Visible -> Invisible";
                }
            }
            
            # If waiting to open, open early
            elseif($exercise->getVisible() == PREOPEN){
                $exercise->setVisible(VISIBLE);
                $text = "Opened Early";
            }
		}

        ##############################################
		#  update student skeletons
        ##############################################
        if($attribute == "skel"){
            // Still have to add skeletons in case new
            // users have been added
            $exercise->addSkeletons();

            // Grab the skeletons from the exercise
            $skelName = "/$exTitle/skeleton";
            $skeletons = Exercise::getSkeletons($exercise->getId(), $skelName);

            // Grab the adminSkeleton
            $fileName = "/$exTitle/AdminSkeleton";
            $adminSkel = CodeFile::getCodeFileByName($fileName);
            $newContents = $adminSkel->getContents();
            
            $num = count($skeletons);

            foreach($skeletons as $skeleton){
                $skeleton->setContents($newContents);
                $skeleton->save();
                $txt = $skeleton->getContents();
            }

			$text = "Skeletons Updated";
		}

        #############################################
		#  Toggles partners
        #############################################
		if($attribute == "partner"){
			if($exercise->isMultiUser() == False) {
				$exercise->setMultiUser(1);
				$text = "Partners enabled";
			}
			else {
				$exercise->setMultiUser(0);
				$text = "Partners disabled";
			}
		}

		$exercise->save();
		return JSON::success($text);
	}
}

?>

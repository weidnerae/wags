<?php

class EditExercises extends Command
{
	public function execute(){
		$exTitle = $_GET['title'];
		$attribute = $_GET['attribute'];

		$exercise = Exercise::getExerciseByTitle($exTitle);
		$text = "No change";


		#toggles visibility
		if($attribute == "vis"){
			if($exercise->getVisible() == 0){
				$exercise->setVisible(1);
				$text = "Visibility turned on";
			}
			else {
				$exercise->setVisible(0);
				$text = "Visibility turned off";
			}
		}

		#creates student skeletons
		if($attribute == "skel"){
			$exercise->addSkeletons();
			$text = "Skeletons added";
		}

		#Toggles partners
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

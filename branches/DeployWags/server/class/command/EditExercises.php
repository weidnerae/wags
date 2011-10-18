<?php

class EditExercises extends Command
{
	public function execute(){
		$exId = $_GET['id'];
		$attribute = $_GET['attribute'];

		$exercise = Exercise::getExerciseById($exId);
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

		#creates skeletons
		if($attribute == "skel"){
			$exercise->addSkeletons();
			$text = "Skeletons added";
		}

		#enables partners
		if($attribute == "partner"){
			$exercise->setMultiUser(1);
			$text = "Partners enabled";
		}

		$exercise->save();
		return JSON::success($text);
	}
}

?>

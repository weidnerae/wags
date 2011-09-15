<?php

/**
 *Class GetVisibleExercises
 *Author Philip Meznar
 *
 * This class was created for the WAGS project in
 * summer '11.  It's function is to return an array
 * of exercise names and id's to be used by the client
 * side, primarily for filling a hashMap and listbox.
 */

class GetVisibleExercises extends Command
{
	public function execute()
	{
		$exercises = Exercise::getVisibleExercises();
		$exerciseTitles = "";

		foreach($exercises as $exercise){
			$exerciseIds[] = $exercise->getId();
			$exerciseTitles[] = $exercise->getTitle();
		}

		$exerciseArray = array_merge($exerciseIds, $exerciseTitles);
	
		if(!empty($exerciseArray)){
			return JSON::success($exerciseArray);
		}

		return JSON::success("No Assignments");
	}

}


?>

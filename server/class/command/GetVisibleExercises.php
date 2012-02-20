<?php

/**
 *Class GetVisibleExercises
 *Author Philip Meznar
 *
 * This class was created for the WAGS project in
 * summer '11.  It's function is to return an array
 * of exercise names and id's to be used by the client
 * side, primarily for filling a hashMap and listbox.
 *
 *
 * This class is kind of ironically named now, as we
 * decided that administrators should be able to view all exercise
 * and made that change within Exercise::getVisibleExercises()
 */

class GetVisibleExercises extends Command
{
	public function execute()
	{
		$exercises = Exercise::getVisibleExercises();
		$exerciseTitles = "";

		foreach($exercises as $exercise){
			$title = $exercise->getTitle();

			if(!$exercise->getVisible()){
				$title = $title."[i]";
			}

			$exerciseTitles[] = $title; 
		}
	
		if(!empty($exerciseTitles)){
			return JSON::success($exerciseTitles);
		}

		return JSON::success("No Assignments");
	}

}


?>

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
 * This class is kind of poorly named now, as we
 * decided that administrators should be able to view all exercise
 * and made that change within Exercise::getVisibleExercises()
 */

require_once('DefInvis.php');

class GetVisibleExercises extends Command
{
	public function execute()
	{
        # Actually gets all exercises
		$exercises = Exercise::getVisibleExercises();
		$exerciseTitles = "";

		foreach($exercises as $exercise){
			$title = $exercise->getTitle();
            $oldVis = $exercise->getVisible();
            $newVis = $this->transition($exercise);

            # Performs transition if necessary
            if($oldVis != $newVis){
                $exercise->setVisible($newVis);
                $exercise->save();
            }

			$exerciseTitles[] = $title; 
		}

		if(!empty($exerciseTitles)){
			return JSON::success($exerciseTitles);
		}

		return JSON::success("No Assignments");
	}

    # Checks to see if the exercise should move to a new state of visibility
    # Returns the state visibility should be in
    private function transition($ex){
        # If it was waiting to open, and has passed the open date, make it
        #  visibile
        if($ex->getVisible() == PREOPEN && $ex->getOpenDate() <= time()){
            $ex->addSkeletons();
            return VISIBLE;
        }
        
        # If it was visible, but has passed the close date, make it expired
        elseif($ex->getVisible() == VISIBLE && $ex->getCloseDate() <= time()){
            if($ex->getCloseDate() != 0) # No dates get stored as zero
                return EXPIRED; 
        }

        # Otherwise, it should remain in whatever state it was as toggling
        #   handles all other state transitions, found in EditExercise.php
        return $ex->getVisible();
    }
}


?>

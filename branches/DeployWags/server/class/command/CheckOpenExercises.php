<?php

/**
*
*Class CheckOpenExercises
*Author: Philip Meznar
*
*This class is called before the main Wags page is loaded
*for the client.  It grabs all exercises with a set
*open and close time for the users section.  If the current
*time falls within the window, it makes the exercise visible.
*Otherwise, the exercise is invisible.
*
*It will also add skeletons for the exercise if it is visible
*
*Submission times are checked in Review.php
*/

class CheckOpenExercises extends Command
{
	public function execute()
	{
		foreach($timedExercises as $exercise){
			//opens
			if($exercise->getOpenDate() < time()){
				$exercise->setVisible(1);
			}

			//closes
			if($exercise->getCloseDate() < time()){
				$exercise->setVisible(0);
			}
		
			$exercise->save();

			//adds skeletons
			if($exercise->getVisible() == 1){
				$exercise->addSkeletons();
			}
		}
	}

}
?>


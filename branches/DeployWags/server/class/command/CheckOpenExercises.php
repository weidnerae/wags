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

define("VISIBLE", 1);
define("INVISIBLE", 0);

class CheckOpenExercises extends Command
{
	public function execute()
	{
		// Following line was missing
		//	-- Commented out so that function still fails until
		//	   it is decided what to do.  This script will have
		// 	   bad consequences if the admin has manually edited the
		//     visibility, regardless of open/close dates.  May want
		//     to add in extra logic, as I began to below, to account
		//	   for this.
		//$timedExercises = Exercise::getVisibleExercises(); // this line was missing
		
		$open = 0;  // added variables
		$close = 0;
		foreach($timedExercises as $exercise){
			$open = $exercise->getOpenDate();   // just pulled these out of IF
			$close = $exercise->getCloseDate(); // statements
			
			// Will want to look at case when open/close haven't
			// been set, in which case they will both be '0'.
			//	-In this case, may want to just leave the visibility alone,
			//	 as admin is likely manually changing the visibility
			//if($open != 0 && $close != 0)
			
			//opens - if already open, set visible
			if($open < time()){
				$exercise->setVisible(VISIBLE);
			}

			//closes - if already closed, set invisible
			if($close < time()){
				$exercise->setVisible(INVISIBLE);
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


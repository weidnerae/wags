<?php

/**
 * @class AdminReview
 * @author Philip Meznar
 *
 * This class was created for the WAGS project in
 * summer '11.  It's function is to return the user,
 * most recent file, and success value for a given
 * exercise.  It is used by the administrator for quick
 * and easy access to students success in a microlab.
 */

class AdminReview extends Command
{
	public function execute(){
		$ex = $_REQUEST['title'];
        $exercise = Exercise::getExerciseByTitle($ex);

		$subInfo = Exercise::getSubmissions($exercise->getId());

		foreach($subInfo as $row){
			$result[] = $row['username'];
			$result[] = $row['name'];
            $result[] = $row['numAttempts'];
			$result[] = $row['success'];
			if($row['partner'] != NULL){
				$result[] = $row['partner'];
			}else{
				$result[] = "  ";
			}

		}

		if(empty($subInfo)){
			return JSON::success("empty");
		}
		return JSON::success($result);
	}
}

<?php

class GetLogicalExercises extends Command
{
    public function execute()
    {
        $user = Auth::getCurrentUser();
        $section = Section::getSectionById($user->getSection());
        $exercises = $section->getLogicalExercises();
        $exercises = str_replace("\n", "", $exercises);
        $exerciseArray = explode("|", $exercises);
		sort($exerciseArray);
        
        $submissions = DSTSubmission::getAllSubmissionsByUserID();
		
		
		$result = array();
		
		foreach ($exerciseArray as $exercise) {
			foreach ($submissions as $submission) {
				if ($exercise == $submission['title']) {
					$row = array('title' => $submission['title'],
								 'success' => $submission['success']);
					$result[] = $row;
					break;
				}
			}
		}

        return JSON::success($result);
    }
}


?>

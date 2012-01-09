<?php

class GetLogicalExercises extends Command
{
    public function execute()
    {
        $user = Auth::getCurrentUser();
        $section = Section::getSectionById($user->getSection());
        $exercises = $section->getLogicalExercises();

        return JSON::success($exercises);
    }
}


?>

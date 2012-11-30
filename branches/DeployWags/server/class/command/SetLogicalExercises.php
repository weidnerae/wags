<?php

/**
*SetLogicalExercises.php
*
*Author: Philip Meznar
*
*This class updates the logicalExercises column
*of an entry in the section table.
*
*
*This is so gross.  There's gotta be a better way..
*/

class SetLogicalExercises extends Command
{
    public function execute(){
        $exercises = $_GET['list'];

        if($exercises == "none") $exercises = "";

        $sectionNumber = Auth::getCurrentUser()->getSection();
        $section = Section::getSectionById($sectionNumber);

        $section->setLogicalExercises($exercises);
        $section->save();

        if($exercises == "") return JSON::success("All Logical Microlabs Removed!");
        return JSON::success("Logical Microlabs Updated");
    }   
}

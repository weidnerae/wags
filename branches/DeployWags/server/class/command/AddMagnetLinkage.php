<?php

/*
 * AddMagnetLinkage.php
 *
 * Called after AddMagnetExercise returns to the client
 * Makes sure the magnet group is available to the section,
 * adds the new problem to the magnet group and makes it 'available'
 *
 * Philip Meznar '13
*/ 

class AddMagnetLinkage extends Command
{
    public function execute(){
        $title = $_GET['title'];

        // Get the id of the magnet problem
        $mp = MagnetProblem::getMagnetProblemByTitle($title);
        $mpID = $mp->getId();

        // Get the id of the magnet problem group
        // Should be guaranteed to exist due to AddMagnetExercise.php
        $sectionId = Auth::getCurrentUser()->getSection();
        $sectionTitle = Section::getSectionById($sectionId)->getName();
        $mpGroup = $sectionTitle."MPs";
        $mpGroupID = MagnetProblem::getGroupIdByName($mpGroup); 

        // Make sure the magnetProblemGroup is associated with the section
        $groupList = MagnetProblem::getMagnetProblemGroups();
        if(!in_array($mpGroup, $groupList)){
            MagnetProblem::addGroup($mpGroupID); 
        }

        // Update the groupID for the magnetProblem
        $mp->setGroup($mpGroupID); // temporary for creation of radix sort labs
        try{
            $mp->save();
        } catch(Exception $e){
            logError($e);
            return JSON::error($e->getMessage());
        }

        // Update the problem status for the mp
        MagnetProblem::addExercise($mpID);
        
        // Bind the SimpleFiles added in AddMagnetExercises
        // correctly (they are currently 'holding')
        $files = SimpleFile::getHoldingSimpleFiles();
        foreach($files as $file){
            $file->setMagnetProblemId($mpID);
            $file->save();
        }

        return JSON::success('MagnetProblem Added');
    }
}

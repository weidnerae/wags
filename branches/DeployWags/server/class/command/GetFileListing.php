<?php

require_once("DefInvis.php");

class GetFileListing extends Command
{
    public function execute()
    {
        // Use needs to be logged in to save a file.
        if(!Auth::isLoggedIn()){
            return JSON::error('Must be logged in to save a file.');
        }

        $user = Auth::getCurrentUser();
        
        # Check exercises for opening or expiration
        $exercises = Exercise::getVisibleExercises();
        foreach($exercises as $exercise){
            $exercise->transition();
        }

        # Get the users files
        $files = CodeFile::getCodeFilesByUser($user);
        $fileNames = "";

        # Comma separated file names.
        foreach($files as $file){
            $curEx = Exercise::getExerciseById($file->getExerciseId());

            if($curEx){
                $curVis = $curEx->getVisible();
                
                # Coming from iBook router, limits visible files
                # Hijacks visibility post database access, so doesn't alter
                # anything for anyone else
                if(isset($_SESSION['exercise'])){
                    if($curEx->getTitle() != $_SESSION['exercise']){
                        $curVis = INVISIBLE;
                    } else {
                        $curVis = VISIBLE;
                    }
                }

                # Students only get visible or expired exercises, admins get all
                if($curVis == VISIBLE || $curVis == EXPIRED || $user->isAdmin()){
                    $name = $file->getName();
                    $fileNames[] = "$name$curVis";
                }
            # Administrative classes link to non-existant exercise, so curEx
            # resolves to false.  In this case, just boot out the name
            } else {
                $name = $file->getName();
                $fileNames[] = "$name"."1"; # 1 = visible.  Good for Admin
            }
        }

        return JSON::success($fileNames);
    }
}

?>

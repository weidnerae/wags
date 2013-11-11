<?php

# GetFileTime.php
#
# Returns a simple file's time and update fields
#
# author  - Brian Clee
# version - 10/14/2013

class GetFileTime extends Command
{
	public function execute()
	{
		if(!empty($_GET['title']))
		{
            $magProb = MagnetProblem::getMagnetProblemByTitle($_GET['title']);
            $id = $magProb->getId();
            $simpFile = SimpleFile::getFilesForMP($id);
        } 
        else if(!empty($_GET['id']))
        {
            $simpFile = SimpleFile::getFilesForMP($_GET['id']);
        }
        else 
        {
            return JSON::error("No file found for title");
        }
       
        $testTime = -1;
        $helpTimes[0] = -1;
        $count = 0;
        foreach($simpFile as $file)
        {
            $test = $file->isTest();
            if($test > 0)
            {
                $testTime = $file->getAdded();
            }
            else
            {
                $helpTimes[$count] = $file->getAdded();
                $count++;
            }   
        }
        
        //Creates a times array where first element is test and the rest 
        //are help times, formatted in date styles.
        $testTime = date("m-d-y g:i a", $testTime);
        $times[] = $testTime;
        for($i = 1; $i < count($helpTimes); $i++)
        {
            $times[] = $helpTimes[$i];
        }
		return JSON::success($times);
	}
}

?>
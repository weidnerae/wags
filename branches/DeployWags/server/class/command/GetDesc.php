<?php

class getDesc extends Command
{
	public function execute(){
		
		$exTitle = $_GET['title'];
		
		$exercise = Exercise::getExerciseByTitle($exTitle);
		$description = $exercise->getDescription();
		
		/*
		 * $description points to the soft link to the actual JPG, so this line
		 * parses out the file name and prepends /tmp/ before it so we can get 
		 * the actual path of the JPG
		 */
		$path = str_replace(WE_ROOT."/descriptions/desc/", '/tmp/', $description);
		
		/*
		 * If the file doesn't exist, we need to recreate it from the JPG stored
		 * in the database
		 */
		if (!file_exists($path)) {
			$image = $exercise->getDescriptionJPG();
			file_put_contents($path, $image);
		}
		
		// replace the location on disk with the specific URL,
		// I think there is a better way to do this, but it works
		$url = str_replace(WE_ROOT, WE_URL, $description);
		
		return JSON::success($url);
		

	}
}

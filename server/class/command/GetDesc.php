<?php

class getDesc extends Command
{
	public function execute(){
		$exTitle = $_GET['title'];

		$exercise = Exercise::getExerciseByTitle($exTitle);
		$description = $exercise->getDescription();
		
		$path = str_replace(WE_ROOT."/descriptions/", '/tmp/', $description);
		
		if (!file_exists($path)) {
			$image = $exercise->getDescriptionJPG();
			file_put_contents($path, $image);
		}
		
		$url = str_replace(WE_ROOT, WE_URL, $description);
		
		return JSON::success($url);

	}
}

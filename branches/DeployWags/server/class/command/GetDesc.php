<?php

class getDesc extends Command
{
	public function execute(){
		$exTitle = $_GET['title'];

		$exercise = Exercise::getExerciseByTitle($exTitle);
		return JSON::success($exercise->getDescription());

	}
}

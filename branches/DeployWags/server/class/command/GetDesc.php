<?php

class getDesc extends Command
{
	public function execute(){
		$exId = $_GET['id'];

		$exercise = Exercise::getExerciseById($exId);
		return JSON::success($exercise->getDescription());

	}
}

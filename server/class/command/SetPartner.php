<?php

class SetPartner extends Command
{
	public function execute()
	{
		$exTitle = $_GET['ex'];
		$partner = $_GET['partner'];
		$partner = substr($partner, 1, strlen($partner)-2);

		$exercise = Exercise::getExerciseByTitle($exTitle);

		$submission = Submission::getSubmissionByExerciseId($exercise->getId());
		$submission->setPartner($partner);
		
		$submission->save();

		return JSON::success($partner." is your partner for ".$exTitle);
	}
}

<?php

class GetSections extends Command
{
	public function execute(){
		$sections = Section::getSectionNames();
		
		if(!empty($sections)){
			return JSON::success(array_values($sections));
		}

		return JSON::success("No sections exist");
	}
}


?>

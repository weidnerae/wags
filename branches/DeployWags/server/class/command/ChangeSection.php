<?php

# ChangeSection.php
#
# Philip Meznar, Summer '12
#
# This class is invoked from the section tab on the superadmin
# page, and changes the superAdmin's section.  The section tab
# appears depending on UserId, so the superAdmin can always change
# his section back

class ChangeSection extends Command
{
	public function execute(){
        $user = Auth::getCurrentUser();
        
		$newSection = $_POST["lstCurSections"];
		$section = Section::getSectionByName($newSection);
        $user->setSection($section->getId());
        $user->save();


		return JSON::success("Section changed to $newSection");
	}
}


?>

<?php
# LinkNewSection
#   By Philip Meznar
#
# This class is called after a successful return from "AddSection" (it is
# called in the CompleteHandler for the form).  This class then takes
# the created administrator, section, and guest, and passes up their
# names as use for identification.
#
# Then, we correctly link the accounts and section together, finishing the
# overall process for adding a new section

class LinkNewSection extends Command{

    public function execute(){
        # Grab all the information
        $sectName  = $_GET["sect"];
        $adminName = $_GET["admin"];
        $guestName = $_GET["guest"];

        # Get the section
        $id = Section::getIdByName($sectName);
        $section = Section::getSectionById($id);

        # Get the administrator
        $admin = User::getUserByUsername($adminName);

        # Get the gust
        $guest = User::getUserByUsername($guestName);

        # Add administrator and guest to the section
        $admin->setSection($id);
        $guest->setSection($id);

        # Set the administrator for the section
        $section->setAdmin($admin->getId());

        # Add the default magnetExercises for the section
        MagnetProblem::addDefaults($id);

        if(strpos($sectName, "WorkshopUser") !== false){
            MagnetProblem::addWorkshopGroups($id);
        }

        # Save all the changes
        $admin->save();
        $guest->save();
        $section->save();

        return JSON::success("Section Created, Accounts Linked");
    }

}

?>

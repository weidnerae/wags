<?php
#   AddSection.php
#
#   This class is invoked when the superAdmin chooses "AddSection"
#   on the "Sections" tab.
#
#   It creates a new section, and saves it into the database, while
#   automatically creating an Administrative Account with a supplied password,
#   and a guest account with the generic password "guest"
#
#   When looking at this code, you'll notice that the section needs the
#   administrator and the administrator needs the section.  This is handled
#   in the submitCompleteHandler event, which calls another server Command
#   which then correctly binds the administrator, guest, and section.
#
#   You can see a similar method of operation in the "addSkeletons" call
#   on the client side in the SubmitCompleteHandler for AddExercise (in Admin)
#
#   Written by Philip Meznar

class AddSection extends Command
{
    public function execute(){
        $sectName  = $_POST["txtSectName"];
        $adminName = $_POST["txtAdminName"];
        $adminPass = $_POST["checkPassword"];
        $checkPass = $_POST["check2Password"];
        $guestName = $_POST["txtGuestName"];

        # Make sure the passwords match
        if($checkPass != $adminPass){
            return JSON::error("Passwords don't match");
        }
        
        # Make sure there isn't already a section with that name
        if(Section::isSection($sectName)){
            return JSON::error("Section with that name already exists");
        }

        # Make sure there isn't already a user with that username
        if(User::isUsername($adminName)){
            return JSON::error("Administrator username already used");
        }
        if(User::isUsername($guestName)){
            return JSON::error("Guest username already used");
        }


        # Create the section!
        $newSect = new Section();
        $newSect->setName($sectName);
        $newSect->setLogicalExercises("");
        $newSect->setAdded(time());
        $newSect->setAdmin(1);                  // This will be tricky...
        
        try{
            $newSect->save();
        } catch(Exception $e){
            logError($e);
            return JSON::error($e->getMessage());
        }  

        # Create the administrator
        $this->createUser($adminName, $adminPass, 1);
        # Create the guest
        if($guestName == "") $guestName = "$sectName"."_Guest";
        $this->createUser($guestName, "guest", 2);

        return JSON::success("$sectName $adminName $guestName");
    }

    // Creates users with given username, password, and administrative
    // level
    private function createUser($name, $password, $type){
        $user = new User();
        $user->setUsername($name);
        $user->setEmail("");
        if($type == 2) $user->setFirstName("Guest");
        else $user->setFirstName("Admin");
        $user->setLastName("");
        $user->setPassword(md5($password));
        $user->setSection(1);                   // Also tricky...
        $now = time();
        $user->setLastLogin($now);
        $user->setAdded($now);
        $user->setUpdated($now);
        $user->setAdmin($type);

        try{
            $user->save();
        } catch(Exception $e){
            x($e);
            return JSON::error($e->getMessage());
        }  
        return 0;
    }
}
?>

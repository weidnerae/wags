<?php

/**
 * User
 * 
 * Models a user. (username, password, last login time, ...)
 *
 * @author Robert Bost <bostrt at appstate dot edu>
 */

require_once("Model.php");

class User extends Model
{
    protected $id;
    protected $username;
    protected $firstName;
    protected $lastName;
    protected $email;
    protected $lastLogin;
    protected $password;
    protected $admin;
	protected $section;

    public function getTable(){
        return 'user';
    }

    /**
     * Get all code files related to this user.
     *
     * @return CodeFile[]
     */
    public function getCodeFiles()
    {
        require_once('Database.php');
        require_once('CodeFile.php');
        
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * FROM file WHERE ownerId = :ownerId');
        $sth->execute(array(':ownerId' => $this->id));
        
        return $sth->fetchAll(PDO::FETCH_CLASS, 'CodeFile');
    }

    /**
     * Getters, Setters.
     */
    public function getId(){
        return $this->id;
    }
    public function setUsername($username){
        $this->username = $username;
    }
    public function getUsername(){
        return $this->username;
    }
    public function getFirstName(){
        return $this->firstName;
    }
    public function getLastName(){
        return $this->lastName;
    }
    public function getEmail(){
        return $this->email;
    }
	public function getSection(){
		return $this->section;
	}
    public function setEmail($email){
        $this->email = $email;
    }
    public function setFirstName($name){
        $this->firstName = $name;
    }
    public function setLastName($name){
        $this->lastName = $name;
    }
    public function setPassword($password){
        $this->password = $password;
    }
    public function getPassword(){
        return $this->password;
    }
    public function getLastLogin(){
        return $this->lastLogin;
    }

    public function setLastLogin($time){
        $this->lastLogin = $time;
    }
    public function isAdmin(){
        // HACK for workshops.
        return ($this->admin == 1 || $this->section == 55);
    }
    public function setAdmin($admin=1){
        $this->admin = $admin;
    }
    public function isGuest(){
        return $this->admin == 2;
    }
	public function setSection($section=0){
		$this->section = $section;
	}
    // Returns the magnet problem group individualized to 
    // this users section, with the form of "<<sectionname>>MPs"
    // Used in AddMagnetExercise.php, DeleteMagnetExercise.php
    public function getMagnetProblemGroup(){
        $sectionId = $this->getSection();
        $sectionTitle = Section::getSectionById($sectionId)->getName();
        $mpGroup = $sectionTitle."MPs";
        return $mpGroup;
    }

	//Returns all DST problem results from that user
    //There is no problemResult table in database anymore?  As far as I can tell - Alex
	public function getProbResults(){
		require_once('Database.php');
		$db = Database::getDb();

		$sth = $db->prepare('SELECT * FROM problemResult WHERE userId = :id');
		$sth->execute(array(':id' => $this->getId()));
		$sth->setFetchMode(PDO::FETCH_CLASS, 'ProblemResult');

		return $sth->fetchAll();
	}

    //Returns all submissions by a student based on the student's ID
    public function getMagnetSubmissions($sectionId){
         require_once('Database.php');
         $db = Database::getDb();

         $sth = $db->prepare('SELECT magnetProblemId, numAttempts, success
                              FROM  MagnetSubmission
                              WHERE userId = :userId
                              AND sectionId = :sectionId');
         $sth->execute(array(':userId' => $this->getId(), ':sectionId' => $sectionId));
         $sth->setFetchMode(PDO::FETCH_ASSOC);
         
         return $sth->fetchAll();
     }
	 
	public function getDatabaseSubmissions($sectionId){
         require_once('Database.php');
         $db = Database::getDb();

         $sth = $db->prepare('SELECT databaseProblemId, numAttempts, success
                              FROM  DatabaseSubmission
                              WHERE userId = :userId
                              AND sectionId = :sectionId');
         $sth->execute(array(':userId' => $this->getId(), ':sectionId' => $sectionId));
         $sth->setFetchMode(PDO::FETCH_ASSOC);
         
         return $sth->fetchAll();
     }

     //Returns all dst submissions by a stuent based on the Student's ID
     public function getDstSubmissions($sectionId) {
        require_once('Database.php');
        $db = Database::getDb();

        $sth = $db->prepare('SELECT title, numAttempts, success
                             FROM dstSubmission
                             WHERE userId = :userId
                             AND sectionId = :sectionId');
        $sth->execute(array(':userId' => $this->getId(), ':sectionId' => $sectionId));
        $sth->setFetchMode(PDO::FETCH_ASSOC);
        
        return $sth->fetchAll();
     }
     
     //Returns all programming microlab submissions by a student based on Student's ID
     public function getProgrammingSubmissions($sectionId) {
        require_once('Database.php');
        $db = Database::getDb();
        
        $sth = $db->prepare('SELECT exerciseId, numAttempts, success
                             FROM submission
                             WHERE userId = :userId
                             AND sectionId = :sectionId');
        $sth->execute(array(':userId' => $this->getId(), ':sectionId' => $sectionId));
        $sth->setFetchMode(PDO::FETCH_ASSOC);
        
        return $sth->fetchAll();
     }   

     // Tries to return group id for this administrators created logical
     // microlabs.  If that group doesn't exist, it creates it instead.
     public function getCreatedLMGroup(){
        if(!$this->isAdmin()) return -1;

        //HACK - if user is in the workshop section then put all the problems
        //       into the same group.   
        if($this->section == 55){
            $groupName = "9.14.13_Workshop_LMs";
            $groupName = '"'.$groupName.'"';
        }
        else{
            $groupName = $this->getUsername()."_LMs";
            $groupName = '"'.$groupName.'"';
        }

        $db = Database::getDb();
        $sth = $db->prepare("SELECT id FROM LMGroup WHERE
            LMGroup.Group = $groupName");
        $sth->execute();
        $result = $sth->fetch();

        if(empty($result)){
            // Create the group, 7 is the "created" subject
            $insert = $db->prepare("INSERT INTO LMGroup
                VALUES ('', $groupName, 7)");
            $insert->execute();
            return null;
        } else {
            return $result[0];
        }
     }

    /************
     * Static helpers
     ************/
    
    public static function deleteUser($id)
    {
        require_once('Database.php');
        $user = User::getUserById($id);
        $db = Database::getDb();

        $sth = $db->prepare('DELETE FROM user WHERE id = :id');
        $sth->execute(array('id' => $id));

        return 1;
    }

    /**
     * Check if a user exists in DB by their username.
     *
     * @return boolean - TRUE if exists, FALSE otherwise.
     */
    public static function isUserValid(User $user)
    {
        require_once('Database.php');

        $sql = 'SELECT count(*) FROM user WHERE username = :username';

        $db = Database::getDb();
        $sth = $db->prepare($sql);
        $sth->execute(array(':username' => $user->getUsername()));
        $result = $sth->fetch();

        // The returned count should be only 1.
        return $result[0] == 1 ? TRUE : FALSE;
    }

    /**
     * Get a User object from the databse by the given username.
     *
     * @return User
     */
    public static function getUserByUsername($username)
    {
        require_once('Database.php');
        
        $db = Database::getDb();
        $sth = $db->prepare('SELECT * FROM user WHERE username LIKE :username');
        $sth->execute(array(':username' => $username));

        $result = $sth->fetchObject('User');

        if($result instanceof User){
            return $result;
        }

        return NULL;
    }


    public static function getUserById($id){
        require_once('Database.php');

        $db = Database::getDb();
        $sth = $db->prepare('SELECT * FROM user WHERE id LIKE :id');
        $sth->execute(array(':id' => $id));

        return $sth->fetchObject('User');
    }

    /**
     * Check if the username exists in the database.
     *
     * @return boolean
     */
    public static function isUsername($username)
    {
        require_once('Database.php');

        $db = Database::getDb();

        $sth = $db->prepare('SELECT count(*) as count FROM user WHERE username LIKE :username');
        $sth->execute(array(':username' => $username));

        $result = $sth->fetch(PDO::FETCH_ASSOC);
        
        return $result['count'] == 1;
    }

    /**
     * Check if $password is the password of the user with $username.
     *
     * @param password - plain text password.
     * @return boolean
     */
    public static function passwordMatches($username, $password)
    {
        require_once('Database.php');
        
        $db = Database::getDb();
        $sth = $db->prepare('SELECT count(*) as count FROM user WHERE username = :username AND password = :password');
        $sth->execute(array(':username' => $username, ':password' => md5($password)));

        $result = $sth->fetch(PDO::FETCH_ASSOC);      
        
        return $result['count'] == 1;
    }

	public static function getUserNames(){
		require_once('Database.php');
		$user = Auth::getCurrentUser();

		$db = Database::getDb();
		$sth = $db->prepare("SELECT username FROM user WHERE section = :section AND admin = 0
			ORDER BY username");
		$sth->execute(array(':section' => $user->getSection()));

		$results = $sth->fetchAll(PDO::FETCH_NUM);
		$vals = array_values($results);

		return $vals;
	}

    public static function getUserIds(){
      require_once('Database.php');
      $user = Auth::getCurrentUser();

      $db = Database::getDb();
      $sth = $db->prepare('SELECT id FROM user WHERE id = :id AND admin = 0 ORDER BY username');
      $sth->execute(array(':id' => $id->getSection()));

      $results = $sth->fetchAll(PDO::FETCH_NUM);
      $vals = array_values($results);

      return $vals;
    }
}

?>

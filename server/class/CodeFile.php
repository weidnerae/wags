<?php

/**
 * CodeFile
 * 
 * Represents a code file on the server side. (this side)
 *
 * @author Robert Bost <bostrt at appstate dot edu>
 */

class CodeFile extends Model
{
    protected $name; // the name of this codeFile that Wags uses internally
    protected $ownerId;
    protected $contents;
    protected $exerciseId;
	protected $section;
	protected $originalFileName; // the actual name of the upladed file - Used for compiling, etc.
	protected $originalFileExtension;
    
    public function getTable(){
        return 'file';
    }
    public function getContents(){
        return $this->contents;
    }
    public function getExerciseId(){
		return $this->exerciseId;
    }
	public function getOriginalFileExtension() {
		return $this->originalFileExtension;
	}
	public function getOriginalFileName() {
		return $this->originalFileName;
	}
	public function getName(){
        return $this->name;
    }
    public function getOwnerId(){
        return $this->ownerId;
    }
	public function getSection(){
		return $this->section;
	}


    public function  setContents($text){
        $this->contents = $text;
    }
    public function setExerciseId($id){
	    $this->exerciseId = $id;
    }
	public function setOriginalFileExtension($fileExtension) {
		$this->originalFileExtension = $fileExtension;
	}
	public function setOriginalFileName($fileName) {
		$this->originalFileName = $fileName;
	}
	 public function setName($name){
        $this->name = $name;
    }
    public function setOwnerId($id){
        $this->ownerId = $id;
    }
	public function setSection($section){
		$this->section = $section;
	}
   
	public static function getHelperId(){
		return 100;
	}

    /**
     * Static helpers
     */

    /**
     * Get a CodeFile object from database by it's internal Wags name.
     *
     * @return CodeFile 
     */
    public static function getCodeFileByName($name)
    {
        require_once('Database.php');

        $db = Database::getDb();
		$user = Auth::getCurrentUser();
        
        $sth = $db->prepare('SELECT * FROM file WHERE name = :name AND ownerId = :id OR ownerId = :helper 
			AND name = :name');
        $sth->execute(array(':name' => $name, ':helper' => CodeFile::getHelperId(), ':id' => $user->getId()));

        return $sth->fetchObject('CodeFile');
    }

    /**
     * Get an array of CodeFile objects by user.
     */
    public static function getCodeFilesByUser(User $user)
    {
        require_once('Database.php');
        $db = Database::getDb();
        
        $sth = $db->prepare('SELECT * FROM file WHERE ownerId = :id 
			OR ownerId = :helper and section = :section ORDER BY name');
        $sth->execute(array(':id' => $user->getId(), ':helper' => CodeFile::getHelperId(), ':section' => $user->getSection()));
        
        return $sth->fetchAll(PDO::FETCH_CLASS, 'CodeFile');
    }

    public static function getPartnerFile($partnerId, $fileName){
        require_once('Database.php');

        $db = Database::getDb();

        $sth = $db->prepare('SELECT * FROM file WHERE name = :name AND ownerId = :id');
        $sth->execute(array(':name' => $fileName, ':id' =>  $partnerId));

        return $sth->fetchObject('CodeFile');
    }

	public static function getCodeFileById($id){
		require_once('Database.php');

		$db = Database::getDb();

		$sth = $db->prepare('SELECT * FROM file WHERE id = :id');
		$sth->execute(array(':id' => $id));

		return $sth->fetchObject('CodeFile');
	}

}
?>

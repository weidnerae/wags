<?php

require_once("Model.php");

class SimpleFile extends Model
{
    protected $className;
    protected $package;
    protected $contents;
    protected $magnetProblemId;
    protected $test;

    // Getters
    public function getTable(){
        return 'SimpleFiles';
    }

    public function getClassName(){
        return $this->className;
    }

    public function getPackage(){
        return $this->package;
    }

    public function getContents(){
        return $this->contents;
    }

    public function getMagnetProblemId(){
        return $this->magnetProblemId;
    }

    public function isTest(){
        return $this->test;
    }

    // Setters
    public function setTest($var){
         $this->test = $var;
    }

    public function setClassName($var){
         $this->className = $var;
    }

    public function setPackage($var){
         $this->package = $var;
    }

    public function setContents($var){
         $this->contents = $var;
    }

    public function setMagnetProblemId($var){
         $this->magnetProblemId = $var;
    }

    // Static functions
    public static function getFilesForMP($id){
        require_once('Database.php');
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * 
            FROM SimpleFiles
            WHERE magnetProblemId = :id');
        $sth->setFetchMode(PDO::FETCH_CLASS, 'SimpleFile');
        $sth->execute(array(':id' => $id));

        return $sth->fetchAll();
    }
    
    public static function deleteFilesForMP($id){
        require_once('Database.php');
        $db = Database::getDb();

        $sth = $db->prepare('DELETE FROM SimpleFiles
            WHERE magnetProblemID = :id');
        $sth->execute(array(':id' => $id));
        return;
    }

    public static function getTestFileForMP($id){
        require_once('Database.php');
        $db = Database::getDb();

        $sth = $db->prepare('SELECT * 
            FROM SimpleFiles
            WHERE magnetProblemId = :id
            AND test = 1');
        $sth->setFetchMode(PDO::FETCH_CLASS, 'SimpleFile');
        $sth->execute(array(':id' => $id));

        return $sth->fetch();
    } 

    // Used in AddMagnetLinkage in order to correctly
    // associate uploaded SimpleFiles with the new problem
    public static function getHoldingSimpleFiles(){
        require_once('Database.php');
        $db = Database::getDb();

        $sth = $db->prepare('SELECT *
            FROM SimpleFiles
            WHERE magnetProblemId = 0');
        $sth->setFetchMode(PDO::FETCH_CLASS, 'SimpleFile');
        $sth->execute();
        return $sth->fetchAll();
    }

    // Checks if any files are associated with a Magnet Problem, if there aren't
    // any then it associates any files from another MagnetProblem to this 
    // problem as well
    // @param destMagnetTitle The title of the Magnet Problem we may add files to
    // @param srcMagnetTitle The title of the problem that we would copy
    //        any files from
    public static function assignFiles($dstMagnetTitle, $srcMagnetTitle){
        $dstMagnetProb = MagnetProblem::GetMagnetProblemByTitle($dstMagnetTitle);
        $dstMagnetId = $dstMagnetProb->getId();
        

        $prevFiles = SimpleFile::getFilesForMP($dstMagnetId);
        $noFilesExist = !is_null($prevFiles) && count($prevFiles) == 0;
        
        // Check to see if this problem already has any files
        if($noFilesExist){
            # Get Id of srcMagnetTitle
            $srcMagnetProb = MagnetProblem::GetMagnetProblemByTitle($srcMagnetTitle);
            $srcMagnetId = $srcMagnetProb->getId();
           
            # Get SimpleFiles with that magnetProblemId
            $simpleFiles = SimpleFile::getFilesForMP($srcMagnetId);
           
            # For each file that we found create a new SimpleFile
            # We're settinghte problemId to 0 because the
            # AddMagnetLinkage will be running after this and will take care of it
            foreach($simpleFiles as $file){
                $newSF = new SimpleFile();

                $newSF->setClassName($file->getClassName());
                $newSF->setTest($file->isTest());
                $newSF->setPackage($file->getPackage());
                $newSF->setContents($file->getContents());
                $newSF->setMagnetProblemId(0);
                $newSF->setAdded(time());
                $newSF->setUpdated(time());

                try{
                    $newSF->save();
                } catch(Exception $e){
                    logError($e);
                }
            }
        }
    }
}

?>    

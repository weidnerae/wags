<?php
# DownloadMagnetFiles.php
#
# Invoked from Magnet Problem Creation, this will return a zip file
# containing all the test/helper classes associated with the magnet
# problem that was loaded
#
# Author: Daniel Cook
class DownloadMagnetFiles extends Command{
    public function execute(){
        $magnetTitle = $_POST['lastProblemLoadedDownload'];
        
        // Lets just go ahead and make sure that we'll have a valid path
        // since we're basing it off of the filename...
        $archiveName = $magnetTitle."_Files";
        $archiveName = str_replace(array(":","<",">","\\","\"","|","?","*"),"",$archiveName);
        $archiveName = urlencode($archiveName);
        
        // Get all the SimpleFiles associated with the last problem that was
        // loaded.
        $magnetProb = MagnetProblem::getMagnetProblemByTitle($magnetTitle);
        $magnetId = $magnetProb->getId();
        $simpleFiles = SimpleFile::getFilesForMP($magnetId);
       
        // Create a temp directory where we will put all of the files.
        $dir = "/tmp/magnets/".$archiveName;
        if(!is_dir($dir)){
          mkdir($dir);
        }
       
        // Put all the files associated with the problem into our new
        // temp directory  
        $this->addFiles($dir, $simpleFiles);

        // Zip up the files in the tmp directory into a zip file with
        // the same name as the directory.
        $command = "zip -rmTj ".$dir." ".$dir."/";
        exec($command);
        
        // Send the http headers and then send the zip file down the output buffer
        // A good number
        header("Content-Type: application/zip");
        header("Expires: 0");
        header('Last-Modified: '.gmdate('D, d M Y H:i:s', filemtime($dir.".zip")).' GMT');
        header('Content-Disposition: attachment; filename="'.$archiveName.'.zip"');
        header("Content-Length: ".filesize($dir.".zip"));
        header("Cache-Control: no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        header("Pragma: no-cache");
        ob_clean();
        flush();
        readfile($dir.".zip");
        flush();
        ob_end_flush();

        // Doing some cleanup - remove zip file and directory that stored the files
        unlink($dir.".zip");
        if(is_dir($dir)){
            rmdir($dir);
        }
    }


    /* 
    * Creates files out of an array of SimpleFiles($files) and puts
    * them into the given directory($dir).
    */
    function addFiles($dir, $files){
        foreach($files as $file){
            ob_start();
            $name = $file->getClassName();
            $contents = $file->getContents();
            
            // As of right now if it doesn't have an extension then it's Java
            // if it does have an extension then it's Prolog.
            if(pathinfo($name, PATHINFO_EXTENSION) === "pl"){
                $name = $dir.'/'.$name;
            }else{
                $name = $dir.'/'.$name.".java";
            }
            
            $file = fopen($name, "w");
            fwrite($file, $contents);
            fflush($file);
            fclose($file);

            ob_end_flush();
        }
    }
}
?>

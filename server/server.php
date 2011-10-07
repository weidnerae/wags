<?php

if(!file_exists('we_conf.php')){
    echo "<b>Run ./install.sh</b>";
    return;
}else{
    require_once('we_conf.php');
}

// Require everything!!!!
foreach(glob("class/*.php") as $file){
    require_once($file);
}

Auth::begin();

// Default command : none
$cmd = null;

// Check if a command is set
if(isset($_REQUEST['cmd'])){
    $cmd = CommandFactory::get($_REQUEST['cmd']);
}

if($cmd){
    try{
        $cmd->execute();
    } catch (Exception $e){
        // CATCH ALL!
        logError($e);
    }
}

Auth::end();
?>

<?php

/**
 * Since you cannot use pcntl_fork()
 * from within an Apache PHP script
 * we'll do this. 
 *
 * This script will be called from class/command/Review.php.
 * 
 * Spawn a process. Parent will wait around for about WAIT_TIME seconds
 * and if it has been longer than WAIT_TIME seconds and the child is not 
 * done running KILL IT! 
 */

define('WAIT_TIME', 10);

/* Get arguments passed in */
$dir = $argv[1];
$className = $argv[2];

/* fork it! */
$pid = pcntl_fork();

if($pid == -1){
    echo 'Could not run code';
}
//parent
else if ($pid){
    /* 
     * Manage group.
     * Wait WAIT_TIME seconds
     */
	$now = time()+WAIT_TIME;
	$result = null;
    while(time() <= $now && $result == null){
        $result = pcntl_waitpid($pid, $status, WNOHANG);
    }
//    print $result.' '.$status;
    if($result == $pid){
        /* Exited child ID */
//        print $result;
    }else{
        /* Kill the group */
	//$pgid = posix_getpgid($pid);
	//exec("kill -9 -$pgid"); 

	/*For some reason, the system recognizes the actual pid
	 * as two higher than the reported pid.  As such, this works,
	 * although it is very stange */    
	print "Ran too long - check for efficiency/infinite loops. Following error message due to killed process -> ";
	posix_kill($pid+2, 15);
    }
}
//child
else{
	exec("/usr/bin/java -cp $dir $className 2>&1", $output);
	foreach($output as $line){
		echo $line."<br>";
	}
	//print_r($output);
}

?>

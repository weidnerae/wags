<?php


// RunCodeNew.php
// 
// Executes program and does not allow it to hang.
// 	-Will terminate if process runs longer than WAIT_TIME seconds
//
// Completely rewritten.
//	-This will user proc_open() as a replacement for pcntl_fork()
// 
// @author Mike Dusenberry
// @date Oct 2011
//
// Wags code to keep processes from running indefinitely

# define a time (in seconds) to wait for a program to finish
define('WAIT_TIME', 3);

# Get arguments passed in
$dir = $argv[1];
$testFileName = $argv[2];
$solutionFileName = $argv[3];
$studentFileName = $argv[4];
$lang = $argv[5]; // which language is going to be run
$nonce = $argv[6]; // the value to check for correctness

# This contains the pipes that can read and write to the process
$descriptorspec = array(
   0 => array("pipe", "r"),	// stdin is a pipe that the child will read from
   1 => array("pipe", "w"),	// stdout is a pipe that the child will write to
   2 => array("pipe", "a")	// stderr is a file to write to
);

# determine which language we are using, and execute the testing file as a process
switch($lang)
{
	case "FSharp":
		# Need to create strings for executing the files in format == "/usr/local/bin/mono fileName.exe"
		# 	-This calls Mono to run the '.exe' file, which is compiled F# code
		#		-F# runs on Linux using Mono (a .NET implementation for Linux and OSX, since .NET only runs
		#		 natively on Windows), and the normal F# Microsoft codebase
		#			-Red Hat Linux (CS machine) is not officially supported by Mono, but version 2.8.2 (an older version) 
		#			 was finally able to be installed
		#			-F# was downloaded from Microsoft's website
		$solutionExecString = "\"/usr/local/bin/mono $dir/$solutionFileName.exe\"";
		$studentExecString  = "\"/usr/local/bin/mono $dir/$studentFileName.exe\"";
		
		# Open the process
		#	-A Java test class will be used to run the F# executables
		#	-The process will stay open in the background and the php script will continue running.
		$process = proc_open("exec /usr/bin/java -cp $dir $testFileName $nonce $solutionExecString $studentExecString 2>&1", $descriptorspec, $pipes);
		
		break;
	
	case "Java":
		# define security manager parameters
		$security_stmt = "-Djava.security.manager"
			." -Djava.security.policy==/usr/local/apache2/htdocs/cs/wags/class/command/WagsSecurity.policy";
        
		# Open the process
		#	-The process will stay open in the background and the php script will continue running.
		#	-The java process will run with a Security Manager and a set of defined permissions
		$process = proc_open("exec /usr/bin/java $security_stmt -cp $dir $testFileName $nonce 2>&1", $descriptorspec, $pipes);
		
		break;
		
	case "Prolog":
		# Need to create strings for executing the files in format == "/usr/local/bin/swipl -q -t main -f fileName.pl"
		# 	-This just calls the prolog executable 'swipl', suppresses all extra info with '-q', causes the 'main' rule to 
		#	 be run within the prolog file with '-g main', allows the program to die correctly by halting when 'main' finishes
		#    or fails with '-t halt', and runs the given file as a script with '-f fileName.pl'

        //SoutionFilename is going to hold the counter for how many pairs there are to execute
        //StudentFilename is going to hold the test queries deliminated by 'L#L'
        // Java test file will append the ending of the file.
		$solutionExecString = "\"/usr/local/bin/swipl -q -g main -t halt -f $dir/solutionProlog\"";
		$studentExecString = "\"/usr/local/bin/swipl -q -g main -t halt -f $dir/studentProlog\"";
		
		# Open the process
		#	-A Java test class will be used to run the Prolog scripts
		#	-The process will stay open in the background and the php script will continue running.
		$process = proc_open("exec /usr/bin/java -cp $dir $testFileName $nonce $solutionExecString $studentExecString $solutionFileName \"$studentFileName\" 2>&1", $descriptorspec, $pipes);
		
		break;
		
    case "C":
		$process = proc_open("exec $dir/$testFileName $nonce 2>&1", $descriptorspec, $pipes);
        break;    
	default:
		// if not able to match language, return error
		return JSON::error("Error in matching language to execution");
		break;
}

# Make sure that the process is ready. If not print an error
if (is_resource($process))
{   
	# Give a normal process a moment (2/10ths of a sec) to run before deciding that it may be hanging
	usleep(200000);

    # Check to see if the process is still running.
    #   -allow the process up to WAIT_TIME seconds to finish
    #   -otherwise, declare it to be hung and terminate
    $hung = FALSE;
    $sleep_number = 0;
    $still_running = proc_get_status($process);
    while ($still_running["running"] && !$hung) 
    {
        # sleep for one second, then check on process again
        sleep(1);
        $sleep_number++;
        $still_running = proc_get_status($process);

        # if WAIT_TIME seconds have elapsed, the program is considered hung
        if ($sleep_number == WAIT_TIME)
        {
            $hung = TRUE;
        }
    }

    # if the process finished, get the output, close the pipe, and print output
    if (!$hung)
    {
        # Get the results out of the output stream
        $outputs = stream_get_contents($pipes[1]);

        # close the pipe
        fclose($pipes[1]);

        # Split the string up by newlines
        $outputs = explode("\n", $outputs);

        # now print results line by line
        foreach ($outputs as $output)
        {
			// have to use '<br />' because it gets transmitted incorrectly back to client,
			//  but the client will replace with '\n' characters
            print("$output<br />");
        }   

        # now close the process
        proc_close($process);
    }

    # otherwise, terminate the process and let the user know
    else 
    {
		/* 
		 * There is a bug with proc_terminate, whereas child processes of the process we executed above 
		 *  will not be terminated, so we will kill all processes with a parent pid the same as the process 
		 *  id of the process we started, in addition to killing the initial process
		 *
		 */
		 
		// First, get the status of the intial process executed above
		$status = proc_get_status($process);
		
		//  Get the id of the intial process executed above
		//  - will be the parent id of any possible children it may have spawned
		//  - convert the string to an int
		$ppid = intval( $status['pid'] );

		// Now kill the main process
		//  - this will make sure it doesn't try to keep processing after we kill any possible children
		//  - if this process doesn't spawn children, then this is all that is needed to terminate
        proc_terminate($process);
		
		//  Now cleanup any possible children that may be left still
		//	- use the "ps" command to get all processes with the parent id of the initial process, 
		//     then trim and split it to get an array
		$childrenPids = preg_split('/\s+/', trim( shell_exec("ps -o pid --no-heading --ppid $ppid") ) );
		
		//	Now go through and kill each process
		foreach($childrenPids as $pid)
		{	
			// convert string to int
			$pid = intval($pid);
			
			// kill the child process by sending it the SIGKILL (9) signal
			//  - this signal forces the process to terminate
			//  - also, a pid of '0' is bogus, and usually relates to an empty set, or NULL characters,
			//     so ignore it
			if ($pid != 0)
				shell_exec("kill -s 9 $pid");
		}
		
		// Give the user an error message
        print("Ran too long - check for efficiency/infinite loops.");
    }
}

else
    echo "There was a problem opening the process";

?>

<?php

/**
 * Print's passed data all pretty-like.
 * If $exit is true then exit after the print.
 */
function x($var, $exit=false){
    echo "<pre>";
    print_r($var);
    echo "</pre>";
    if($exit)
        exit();
}

function logError(Exception $e)
{
	$handle = fopen("/tmp/WagsError.log", 'w');
    // TODO: NOOOOOOOOOOO!!!!!!!!!!!!
    date_default_timezone_set('America/New_York');
    fwrite($handle, date("D M j, g:i:s A Y\n", time()));
    fwrite($handle, $e."\n\n\n");
    fflush($handle);
    fclose($handle);
}

// Convert all errors to exceptions so that they can be logged
function exception_error_handler($errno, $errstr, $errfile, $errline ) {
		$e = new ErrorException($errstr, 0, $errno, $errfile, $errline);
		logError($e);
		throw $e;
}
set_error_handler("exception_error_handler");

?>
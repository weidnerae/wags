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
    $handle = fopen('error.log', 'a+b');
    // TODO: NOOOOOOOOOOO!!!!!!!!!!!!
    date_default_timezone_set('America/New_York');
    fwrite($handle, date("D M j, g:i:s A Y\n", time()));
    fwrite($handle, $e."\n\n\n");
    fflush($handle);
    fclose($handle);
}

?>
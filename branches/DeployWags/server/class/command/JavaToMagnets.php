<?php
class JavaToMagnets extends Command
{
    public function execute(){
        //$returnString = "";

        $parseFile = $_FILES['toParse'];
        $fileName = $parseFile['tmp_name'];
        
        chdir("class/command");
//        exec("/usr/bin/java JavaToMagnets2 $fileName 2>&1", $output);
        $output = shell_exec("/usr/bin/java JavaToMagnets2 $fileName out 2>&1");
        echo $output;
//        $returnString = $output[0]."\n".$output[1]."\n".$output[2]."\n";
//        echo $returnSting;
        // Can't use JSON as it can't decode the html we pass down, e.g.,
        // <!-- panel -->.  So, we're passing it down as text and letting
        // the client parse it (ProblemCreationPanel fileParseForm's submit
        // complete handler) manually
        //echo $test;
    }
}

?>

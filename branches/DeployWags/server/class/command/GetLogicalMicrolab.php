<?php

class getLogicalMicrolab extends Command
{
    public function execute(){
        $title = $_GET['title'];
        // Returns the objArray that represents this LogicalMicrolab
        $lm = LogicalMicrolab::getLogicalMicrolabByTitle($title);
        if($lm != null){
            return JSON::success($lm->toArray());
        } else {
            return JSON::error("Logical Microlab $title does not exist on server");
        }
    }
}

?>

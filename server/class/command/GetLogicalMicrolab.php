<?php

class getLogicalMicrolab extends Command
{
    public function execute(){
        $title = $_GET['title'];
        // Returns the objArray that represents this LogicalMicrolab
        return JSON::success(LogicalMicrolab::getLogicalMicrolabByTitle($title)->toArray());
    }
}

?>

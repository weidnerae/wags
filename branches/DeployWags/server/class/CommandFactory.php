<?php

require_once("Command.php");

class CommandFactory
{
    public static function get($cmd)
    {
        if(empty($cmd)){
            return JSON::warn("No command");
        }
        $root = WE_ROOT.'/class/';
        if(is_file("$root/command/".$cmd.".php")){
            require("$root/command/".$cmd.".php");
            return new $cmd;
        }else{
            return JSON::error("Command '$cmd' not found.");
        }
    }
}

?>

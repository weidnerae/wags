<?php

// Define success, error, warn.
define('JSON_ERROR', 0);
define('JSON_SUCCESS', 1);
define('JSON_WARN', 2);

// echos random messages
class JSON
{
    public static function warn($msg=null)
    {
        $vars = array('stat' => JSON_WARN);
        self::doEcho($vars, $msg);
        return false;
    }
    public static function success($msg=null)
    {
        $vars = array('stat' => JSON_SUCCESS);
        self::doEcho($vars, $msg);
        return true;
    }

    public static function error($msg=null)
    {
        $vars = array('stat' => JSON_ERROR);
        self::doEcho($vars, $msg);
        return false;
    }

    private static function doEcho($vars, $msg=null)
    {
        if(!is_null($msg)){
            $vars['msg'] = $msg;
        }
        echo json_encode($vars);
    }
}

?>

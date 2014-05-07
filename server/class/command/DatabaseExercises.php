<?php
# DatabaseExercises
#
# This is class meant to handle selecting what databases exercises to assign
# It handles getting groups, and exercises, as specified by the
# GET 'request' parameter.
#
# If requesting exercises, 'group' must be provided
#
# Author:  Chris Hegre '14

class DatabaseExercises extends Command
{
	public function execute(){
        $result = array();

        if(!isset($_GET['request'])){
            return JSON::error("Request not specified");
        }

        $req = $_GET['request'];

		if($req == 'groups'){
            $result = $this->getGroups();
        } else if($req == 'exercises'){
            if(!isset($_GET['group'])) return $this->error($req, 'group');
            $result = $this->getExercises($_GET['group']);
        } else {
            return JSON::error("Malformed GET Request");
        }
        
        return JSON::success($result);
	}

    private function getGroups(){
        $result = DatabaseProblem::getDatabaseProblemGroups();
        return $result;
    }

    private function getExercises($group){
        $result = DatabaseProblem::getDatabaseProblemsByGroup($group);
        return $result;
    }

    private function error($req, $param){
        $str = "To request " . $req . " you must provide " . $param;
        return JSON::error($str);
    }
}
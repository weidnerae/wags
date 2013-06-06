<?php
# LogicalExercises
#
# This is class meant to handle selecting what logical exercises to assign
# It handles getting subjects, groups, and exercises, as specified by the
# GET 'request' parameter.
#
# If requesting groups, 'subject' must be provided
# If requesting exercises, 'group' must be provided
#
# Author:  Philip Meznar '12

class LogicalExercises extends Command
{
	public function execute(){
        $result = array();

        if(!isset($_GET['request'])){
            return JSON::error("Request not specified");
        }

        $req = $_GET['request'];

        if($req == 'subjects'){
            $result = $this->getSubjects();
        } else if($req == 'groups'){
            if(!isset($_GET['subject'])) return $this->error($req, 'subject');

            $result = $this->getGroups($_GET['subject']);
        } else if($req == 'exercises'){
            if(!isset($_GET['group'])) return $this->error($req, 'group');
            
            $result = $this->getExercises($_GET['group']);
        } else if($req == 'initial'){
            $result = $this->getInitial();
        } else {
            return JSON::error("Malformed GET Request");
        }
        
        return JSON::success($result);
	}

    private function getSubjects(){
        $result = LogicalMicrolab::getSubjects();
        return $result;
    }

    private function getGroups($subject){
        $result = LogicalMicrolab::getGroups($subject);
        return $result;
    }

    private function getExercises($group){
        $result = LogicalMicrolab::getExercises($group);
        return $result;
    }

    // To keep from requiring multiple interactions when first loading
    // the page, getInitial returns all the subjects, then the groups for
    // the first subject, then the exercises for the first group of that
    // subject, separated by "GROUP" and "EXERCISES" as entries in the array
    private function getInitial(){
        $result = LogicalMicrolab::getSubjects();
        $result[] = "GROUPS";
        $groups = LogicalMicrolab::getGroups($result[0]);
        $result = array_merge($result, $groups);
        $result[] = "EXERCISES";
        $exercises = LogicalMicrolab::getExercises($groups[0]);
        $result = array_merge($result, $exercises);

        return $result;
    }

    private function error($req, $param){
        $str = "To request " . $req . " you must provide " . $param;
        return JSON::error($str);
    }
}

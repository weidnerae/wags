<?php

# GetFlowProblem.php
# 
# Daniel Cook, '13

class getFlowProblem extends Command
{
	public function execute(){
        if(!empty($_GET['id'])){
            $flowProb = FlowProblem::getFlowProblemById($_GET['id']);
        } elseif(!empty($_GET['title'])){
            $flowProb = FlowProblem::getFlowProblemByTitle($_GET['title']); 
        } else {
            return JSON::error("No flow problem found");
        }

        $objArray = $flowProb->toArray();
        $id = $flowProb->getId(); // necessary for getting state
        
        return JSON::success($objArray);

	}
}
?>

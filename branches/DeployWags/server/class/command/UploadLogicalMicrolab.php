<?php

class UploadLogicalMicrolab extends Command{
    // Will have to add a fair amount of flexibility to this guy to get
    // all sorts of different logical microlabs uploading...
    // Really, just how to get it to store as null when a $_GET is 'empty'
    public function execute(){
        $checkLm = LogicalMicrolab::getLogicalMicrolabByTitle($_GET['title']);
        if(!empty($checkLm)){
            return JSON::warn("Microlab already exists");
        }

        $lm = new LogicalMicrolab();
        $lm->setTitle($_GET['title']);
        $lm->setProblemText($_GET['problemText']);
        $lm->setNodes($_GET['nodes']);
        $lm->setXPositions($_GET['xPositions']);
        $lm->setYPositions($_GET['yPositions']);
        $lm->setInsertMethod($_GET['insertMethod']);
        $lm->setEdges($_GET['edges']);
        $lm->setEvaluation($_GET['evaluation']);
        $lm->setEdgeRules($_GET['edgeRules']);
        $lm->setArguments($_GET['arguments']);
        $lm->setEdgesRemovable($_GET['edgesRemovable']);
        $lm->setNodesDraggable($_GET['nodesDraggable']);
        $lm->setNodeType($_GET['nodeType']);
        $lm->setGenre($_GET['genre']);
        $lm->setGroupID($_GET['group']);
        $lm->setAdded(time());

        $lm->save();


        LogicalMicrolab::addToLMExercise($_GET['title'], $_GET['group']);

        $title = $_GET['title'];
        return JSON::success("Saved $title");
    }
}
?>

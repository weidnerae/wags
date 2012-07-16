<?php

# GetMagnetProblem.php
# 
# A replacement for the "query.php" for the original magnets
# Right now, only used for testing, and so the id is hardcoded in
#
# Philip Meznar, '12

class getMagnetProblem extends Command
{
	public function execute(){
        $id = $_GET['id'];
        $magProb = MagnetProblem::getMagnetProblemById($id);
        $objArray = $magProb->toArray();

        return JSON::success($objArray);

	}
}
?>

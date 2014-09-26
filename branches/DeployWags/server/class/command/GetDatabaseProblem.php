<?php

# GetDatabaseProblem.php
#
# Philip Meznar, '12
# Chris Hegre, '14

class GetDatabaseProblem extends Command
{
	public function execute(){
        if(!empty($_GET['id'])){
            $dbProblem = DatabaseProblem::getDatabaseProblemById($_GET['id']);
        }  else {
            return JSON::error("No database problem found");
        }

		$objArray = $dbProblem->toArray();
        return JSON::success($objArray);

	}
}
?>

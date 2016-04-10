<?php
	$con = mysqli_connect("mysql12.000webhost.com","a1738747_Shri1","Suchitra123","a1738747_UserFin");
	$position= $_POST["position"];
        $experience = $_POST["experience"];
	$statement = mysqli_prepare($con, "SELECT * FROM JobList WHERE domain= ? OR experience = ?");
	mysqli_stmt_bind_param($statement, "ss", $position, $experience);	
	mysqli_stmt_execute($statement);
	mysqli_stmt_store_result($statement);
	mysqli_bind_result($statement, $serial, $id, $domain, $position, $type, $description, $experience, $location, $remarks);
	
	$job= array();

	while(mysqli_stmt_fetch($statement)){
	$job[]= array( "id" => $id, "domain" => $domain, "position" => $position,"type" => $type,"description" => $description,"experience" => $experience,"location" => $location,"remarks" => $remarks);
	}

	echo json_encode($job);
	mysqli_stmt_close($statement);
	mysqli_close($con);
?>
<?php
	$con = mysqli_connect("mysql12.000webhost.com","a1738747_Shri1","Suchitra123","a1738747_UserFin");
	$id= $_POST["ID"];
	$statement = mysqli_prepare($con, "SELECT id,domain,position,type,description,experience,location,remarks FROM JobList WHERE id= ?");
	mysqli_stmt_bind_param($statement, "s", $id);	
	mysqli_stmt_execute($statement);
	mysqli_stmt_store_result($statement);
	mysqli_bind_result($statement, $id, $domain, $position, $type, $description, $experience, $location, $remarks);
	
	$job= array();

	while(mysqli_stmt_fetch($statement)){
	$job[ID] = $id;
        $job[domain] = $domain;
	$job[position] = $position;
	$job[type] = $type;	
	$job[description] = $description;
        $job[experience] = $experience;
        $job[location] = $location;
        $job[remarks] = $remarks;
	}

	echo json_encode($job);
	mysqli_stmt_close($statement);
	mysqli_close($con);

?>
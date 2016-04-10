<?php
	$con = mysqli_connect("mysql12.000webhost.com","a1738747_Shri1","Suchitra123","a1738747_UserFin");
	
	$id= $_POST["ID"];
	$position = $_POST["position"];
	$type= $_POST["type"];
        $domain = $_POST["domain"];
	$description= $_POST["description"];	
	$experience= $_POST["experience"];	
	$location= $_POST["location"];	
	$remarks = $_POST["remarks"];	
		

	$statement = mysqli_prepare($con , "UPDATE JobList SET domain=?, position=? ,type=?, description=?, experience=?, location=?, remarks=? WHERE id=?");
	mysqli_stmt_bind_param($statement, "ssssssss", $domain, $position, $type, $description, $experience, $location, $remarks, $id);
	mysqli_stmt_execute($statement);
	mysqli_stmt_close($statement);
	mysqli_close($con);
?>
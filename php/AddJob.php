<?php
	$con = mysqli_connect("mysql12.000webhost.com","a1738747_Shri1","Suchitra123","a1738747_UserFin");
	
	$id= $_POST["ID"];
	$position = $_POST["position"];
	$domain= $_POST["domain"];
        $type = $_POST["type"];
	$description= $_POST["description"];	
	$experience= $_POST["experience"];	
	$location= $_POST["location"];	
	$remarks = $_POST["remarks"];	
		

	$statement = mysqli_prepare($con , "INSERT INTO JobList (id, domain, position, type, description, experience, location, remarks) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
	mysqli_stmt_bind_param($statement, "ssssssss", $id, $domain, $position, $type, $description, $experience, $location, $remarks);
	mysqli_stmt_execute($statement);
	mysqli_stmt_close($statement);
	mysqli_close($con);

$to = "app@noavhro.com";
$subject = "NEW JOB ADDED";
$mail_body = " This is an automated email for your information.\n A new user has been registered to the database.\n\n Following are the job listing details:\n  ID: $id\n  Position: $position\n Domain: $domain\n Type: $type\n  Description: $description\n  Experience: $experience\n  Location: $location\n  Remarks: $remarks\n\nAuto Generated by Nova App";
$headers  = "From:DONOTREPLY@NovaHRO.com\n";
$headers .= "Content-type: text\n";
mail($to, $subject, $mail_body, $headers);
?>
<?php
	$con = mysqli_connect("mysql12.000webhost.com","a1738747_Shri1","Suchitra123","a1738747_UserFin");
	$searchterm= $_POST["searchterm"];
	$statement = mysqli_prepare($con, "SELECT * FROM UserInfo WHERE CONCAT(name, position, experience, desloc) LIKE ? ");
	mysqli_stmt_bind_param($statement, "s", $searchterm);	
	mysqli_stmt_execute($statement);
	mysqli_stmt_store_result($statement);
	mysqli_bind_result($statement, $username, $name, $password, $email, $age, $phone, $position, $experience, $curloc, $desloc, $imageuri, $com1name, $com1pos, $com1from, $com1to, $com1resp, $com2name, $com2pos, $com2from, $com2to, $com2resp, $com3name, $com3pos, $com3from, $com3to, $com3resp);
	
	$user = array();

	while(mysqli_stmt_fetch($statement)){
	$user[]= array( "username" => $username, "name" => $name, "password" => $password,"email" => $email,"age" => $age,"phone" => $phone,"position" => $position, "experience" => $experience,"curloc" => $curloc,"desloc" => $desloc,"imageuri" => "NA", "com1name" => $com1name,"com1pos" => $com1pos,"com1from" => $com1from,"com1to" => $com1to,"com1resp" => $com1resp,"com2name" => $com2name,"com2pos" => $com2pos,"com2from" => $com2from,"com2to" => $com2to,"com2resp" => $com2resp,"com3name" => $com3name,"com3pos" => $com3pos,"com3from" => $com3from,"com3to" => $com3to,"com3resp" => $com3resp);
	}

	echo json_encode($user);
	mysqli_stmt_close($statement);
	mysqli_close($con);
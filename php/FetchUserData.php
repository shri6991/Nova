<?php
	$con = mysqli_connect("mysql12.000webhost.com","a1738747_Shri1","Suchitra123","a1738747_UserFin");
	$username = $_POST["username"];
	$password = $_POST["password"];
	$statement = mysqli_prepare($con, "SELECT * FROM UserInfo WHERE username = ? AND password = ?");
	mysqli_stmt_bind_param($statement, "ss", $username, $password);	
	mysqli_stmt_execute($statement);
	mysqli_stmt_store_result($statement);
	mysqli_bind_result($statement, $username, $name, $password, $email, $age, $phone, $position, $experience, $curloc, $desloc,$imageuri, $com1name, $com1pos, $com1from, $com1to, $com1resp, $com2name, $com2pos, $com2from, $com2to, $com2resp, $com3name, $com3pos, $com3from, $com3to, $com3resp);
	
	$user = array();

	while(mysqli_stmt_fetch($statement)){
	$user[username] = $username;
	$user[password] = $password;	
	$user[email] = $email;
	$user[age] = $age;
        $user[name] = $name;
        $user[phone] = $phone;
        $user[position] = $position;
        $user[experience] = $experience;
        $user[curloc] = $curloc;
        $user[desloc] = $desloc;
        $user[imageuri] =$imageuri;  
        $user[com1name] = $com1name;
        $user[com1pos] = $com1pos;
        $user[com1from] = $com1from;
        $user[com1to] = $com1to;
        $user[com1resp] = $com1resp;
        $user[com2name] = $com2name;
        $user[com2pos] = $com2pos;
        $user[com2from] = $com2from;
        $user[com2to] = $com2to;
        $user[com2resp] = $com2resp;
        $user[com3name] = $com3name;
        $user[com3pos] = $com3pos;
        $user[com3from] = $com3from;
        $user[com3to] = $com3to;
        $user[com3resp] = $com3resp;
	}

	echo json_encode($user);
	mysqli_stmt_close($statement);
	mysqli_close($con);

?>
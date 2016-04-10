<?php

$username = $_POST["username"];

$con = mysqli_connect("mysql12.000webhost.com","a1738747_Shri1","Suchitra123","a1738747_UserFin");
$statement = mysqli_prepare($con, "DELETE FROM UserInfo WHERE username = ?");
mysqli_stmt_bind_param($statement,"s", $username);
mysqli_stmt_execute($statement);
mysqli_stmt_close($statement);
mysqli_close($con);

?>
<?php
$con = mysqli_connect("mysql12.000webhost.com","a1738747_Shri1","Suchitra123","a1738747_UserFin");
              
              $editable = $_POST["editable"];
              $username = $_POST["username"];
	      $variable = $_POST["variable"];
    $statement = mysqli_prepare($con, "UPDATE UserInfo SET $editable = ? WHERE username = ?");
              mysqli_stmt_bind_param($statement,"ss", $variable, $username);
              mysqli_stmt_execute($statement);
?>
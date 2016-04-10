<?php
$con = mysqli_connect("mysql12.000webhost.com","a1738747_Shri1","Suchitra123","a1738747_UserFin");
              
    $position = $_POST["position"];
    $experience = $_POST["experience"];
    $username = $_POST["username"];
    $curloc = $_POST["curloc"];
    $desloc = $_POST["desloc"];

    $statement = mysqli_prepare($con, "UPDATE UserInfo SET position = ?, experience  = ?, curloc = ?, desloc = ?  WHERE username = ?");
              mysqli_stmt_bind_param($statement,"sssss", $position, $experience, $curloc, $desloc, $username);
              mysqli_stmt_execute($statement);
?>

<?php

$email = $_POST["email"];
$password = $_POST["password"];
$username = $_POST["username"];

$con = mysqli_connect("mysql12.000webhost.com","a1738747_Shri1","Suchitra123","a1738747_UserFin");

$statement = mysqli_prepare($con, "UPDATE UserInfo SET password = ? WHERE email = ? AND username = ?");
mysqli_stmt_bind_param($statement, "sss" , $password, $email, $username);
mysqli_stmt_execute($statement);
mysqli_stmt_close($statement);
mysqli_close($con);

$to = $email;
$subject = "RESET ACCOUNT PASSWORD";
$mail_body = "Dear user,\nYou have requested for a change of password.\n Your new password is: \r\r$password\n\nPlease use it to login and set a new password.\n\nWebmaster,\nNovaHRO.com";
$headers  = "From:Webmaster@NovaHRO.com\n";
$headers .= "Content-type: text\n";
mail($to, $subject, $mail_body, $headers);

?>


<?php
$con = mysqli_connect("mysql12.000webhost.com","a1738747_Shri1","Suchitra123","a1738747_UserFin");
              
              $oldpassword= $_POST["oldpassword"];
              $newpassword= $_POST["newpassword"];

    $statement = mysqli_prepare($con, "SELECT username FROM UserInfo WHERE password = ?");
              mysqli_stmt_bind_param($statement,"s", $oldpassword);
              mysqli_stmt_execute($statement);
              mysqli_stmt_store_result($statement);
              mysqli_bind_result($statement, $username);

              $user = array();

              while(mysqli_stmt_fetch($statement)){
              
               $user[username] = $username;
              }

              echo json_encode($user);
              mysqli_stmt_close($statement);


$statement1 = mysqli_prepare($con, "UPDATE UserInfo SET password = ? WHERE password= ?");
mysqli_stmt_bind_param($statement1, "ss" , $newpassword, $oldpassword);
mysqli_stmt_execute($statement1);
mysqli_stmt_close($statement1);
              mysqli_close($con);


?>

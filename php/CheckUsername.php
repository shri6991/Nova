<?php
              $con = mysqli_connect("mysql12.000webhost.com","a1738747_Shri1","Suchitra123","a1738747_UserFin");
              $username = $_POST["username"];

              $statement = mysqli_prepare($con, "SELECT username, password FROM UserInfo WHERE username = ?");
              mysqli_stmt_bind_param($statement,"s", $username);
              mysqli_stmt_execute($statement);
              mysqli_stmt_store_result($statement);
              mysqli_bind_result($statement, $username, $password);

              $user = array();

              while(mysqli_stmt_fetch($statement)){
              $user[username] = $username;
              $user[password] = $password;
              }

              echo json_encode($user);
              mysqli_stmt_close($statement);
              mysqli_close($con);

?>
 
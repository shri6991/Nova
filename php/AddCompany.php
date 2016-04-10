<?php
$con = mysqli_connect("mysql12.000webhost.com","a1738747_Shri1","Suchitra123","a1738747_UserFin");
              
              $comno = $_POST["comno"];
              $username = $_POST["username"];
              $comname = $_POST["comname"];
              $compos = $_POST["compos"];
	      $comfrom = $_POST["comfrom"];
              $comto = $_POST["comto"];
	      $comresp = $_POST["comresp"];
              $columnname = "com".$comno."name";
              $columnpos = "com".$comno."pos";
              $columnfrom = "com".$comno."from";
              $columnto = "com".$comno."to";
              $columnresp = "com".$comno."resp";


    $statement = mysqli_prepare($con, "UPDATE UserInfo SET $columnname = ?, $columnpos = ?, $columnfrom = ?, $columnto = ?, $columnresp = ? WHERE username = ?");
              mysqli_stmt_bind_param($statement,"ssssss", $comname, $compos, $comfrom, $comto, $comresp, $username);
              mysqli_stmt_execute($statement);
   ?>
<?php

$email = $_POST["email"];
$name = $_POST["name"];
$ID = $_POST["ID"];
$position = $_POST["position"];

$to = "shrikantbhaskar1@gmail.com";
$header = "From: $email";
$body_final = "POSITION APPLIED FOR: $position\nAPPLICANT DETAILS\nName: $name\nEmail: $email";
mail($to, "NEW JOB APPLICATION for ID: $ID", $body_final, $header);

$subject1 = "Job Application Received";
$name1 = $_POST["name"];

$to1 = $email;
$header1 = "From: DONOTREPLY@info.novahro.com\n ";
$body_final1 = "Dear $name, we have received your application for the Job Listing ID: $ID for Position: $position/nWe thank you for you interest.";
mail($to1, $subject1, $body_final1, $header1);

?>
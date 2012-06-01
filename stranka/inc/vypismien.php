<?php 
include('../funkcie.php');

$klub=$_GET["klub"];
$odpoved='';
	if ($link = prihlas_sql()) {
		//kazdy klub bude mat svoju table cize potom tam pilepim nazov...
	$sql = "
SELECT id, meno, priezvisko, respekt, vek
FROM `hraci` ";
   	 	$result = mysql_query($sql, $link);			
   	 while ($row = mysql_fetch_assoc($result)) {
		   $odpoved.=$row['id'];
		   $odpoved.= ','.$row['meno'].' '.$row['priezvisko'] .'  ('.$row['vek'].")~";
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link); 


echo $odpoved;




?>
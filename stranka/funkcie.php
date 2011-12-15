<?php
function prihlas_sql() {
	if ($link = mysql_connect('localhost', 'kuchynar1', 'fuyei')) {
		if (mysql_select_db('kuchynar1', $link)) {
			mysql_query("SET CHARACTER SET 'utf8'", $link); 
			return $link;
		} else {
			return false;
		}
	} else {
		return false;
	}
}

function insertDatum($datum) {
if ($link = prihlas_sql()) {	 
		$sql = "INSERT INTO treningy SET zapasy='" . $datum  . "'";
		//echo "sql = $sql <br/>"; 
		$result = mysql_query($sql, $link);
		if ($result) {
			 echo '<p class="chyba"><strong>trening Ulozeny! </strong></p>';
			  } 
			else {
				echo '<p class="chyba">Uuups ,trening NEUlozeny</p>';
				 }
			}
		else {
			echo '<p class="chyba">NEpodarilo sa vybraإ¥ databأ،zu</p>';
				}
mysql_close($link);
	}
	
function insertnovyhrac($meno, $priezvisko, $vek, $respekt) {
if ($link = prihlas_sql()) {	 
		$sql = "INSERT INTO hraci SET meno ='" . $meno . "', priezvisko ='" . $priezvisko .  "', respekt ='" . $respekt  .  "', vek ='" . $vek .  "'";
		//echo "sql = $sql <br/>"; 
		$result = mysql_query($sql, $link);
		if ($result) {
			 echo '<p class="chyba"><strong>Hrac Ulozeny! </strong></p>';
			  } 
			else {
				echo '<p class="chyba">Uuups ,Hrac NEUlozeny</p>';
				 }
			}
		else {
			echo '<p class="chyba">NEpodarilo sa vybrat databأ،zu</p>';
				}
mysql_close($link);
	}
	
function dajIdHraca($meno, $priezvisko) {
		if ($link = prihlas_sql()) {
   	 	$result = mysql_query("SELECT *
   	 								FROM `hraci` 
   	 								", $link);
   	 								 
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	if (($row['meno']==$meno) && ($row['priezvisko']==$priezvisko) ) return $row['id'];
   	 	}
			
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	}
	
function pridajTRening($datum, $poznamka, $pocetkurtov, $dochadzka, $zapasy) {
if ($link = prihlas_sql()) {	 
		$datum=new DateTime('now');
		$sql = "INSERT INTO treningy SET datum ='" . $datum->format('Y-m-d H:i:s') . "', poznamka ='" . $poznamka .  "', pocetkurtov ='" .
		 $pocetkurtov  .  "', dochadzka ='" . $dochadzka .  "', zapasy ='" . $zapasy .  "'";
		echo "sql = $sql <br/>"; 
		$result = mysql_query($sql, $link);
		if ($result) {
			 echo '<p class="chyba"><strong>Hrac Ulozeny! </strong></p>';
			  } 
			else {
				echo '<p class="chyba">Uuups ,Hrac NEUlozeny</p>';
				 }
			}
		else {
			echo '<p class="chyba">NEpodarilo sa vybrat databأ،zu</p>';
				}
mysql_close($link);
	}
	
	
/////////////////////////////////////////////////////////////////////
	
function mame_dovolenku($datum) {
	//$datum=new datetime($datum);
	if ($link = prihlas_sql()) {
   	 	$result = mysql_query("SELECT *
   	 								FROM `optika_dovolenka` 
   	 								WHERE  $datum <= optika_dovolenka.cas_od AND $datum<=optika_dovolenka.cas_do ", $link);
   	 if ($row = mysql_fetch_assoc($result)) return 1;
			else return 0;
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	}
	
	function vymaz_objednavku($id){
	if ($link = prihlas_sql()) {
		$sql = "DELETE FROM objednavky WHERE id='" . $id . "'";
		//echo "sql = $sql <br/>";
		$result = mysql_query($sql, $link);  
		} 
	mysql_close($link);	
	}
	
	function zmen_objednavku($id,$meno,$email,$mobil,$cas) { 
	if ($link = prihlas_sql()) {
		$sql = "UPDATE `objednavky` SET meno='" . $meno  . "', email='" . $email . "', telefon='" . $mobil . "', cas='" . $cas . "'";
		$sql .= " WHERE id='" . $id . "'";
		$result = mysql_query($sql, $link);
		if ($result) echo '<p class="chyba">Udaje Zmenene!<p>' ;
		  else echo '<p class="chyba" >Chyba!(chyba pri ukladani)</p>';
		}
		mysql_close($link); 
	}
?>
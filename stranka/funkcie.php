<?php
function kotrolatextu($text) { 
	//$text = addslashes(strip_tags(trim($text)));
	return preg_match("/^[a-ž,A-Ž, ]{2,}$/", $text ); 
	}
	
function kontrolaheslo($text) { 
	//$text = addslashes(strip_tags(trim($text)));
	return preg_match("/^[a-ž,A-Ž,0-9]{6,}$/", $text ); 
	}
	
function kotrolafeedback($text) { 
//vo feedbacku moze byt secko toto je len aby mi nenapisal dakto </body> alebo daco podobne a mne koli tomu spadne stranka
	$text = addslashes(strip_tags(trim($text)));
	return $text;  
	}	
	
	function kontrolatelefon($text) { 
	//$text = addslashes(strip_tags(trim($text)));
	return preg_match("/^[+][0-9]{12}$/", $text ); 
	}
	
	function kontrolaemail($text) { 
	//$text = addslashes(strip_tags(trim($text)));
	return preg_match("/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,3})$/", $text ); 
	}
	
	function kontrolameno($text) { 
	//$text = addslashes(strip_tags(trim($text)));
	return preg_match("/^[a-ž,A-Ž]{3,}$/", $text ); 
	}
	
	function je_cislo($text) {
	//$text = addslashes(strip_tags(trim($text)));
	 return preg_match("/^[0-9]{1,}$/", $text );
		}

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
	
function insertnovyhractemp($id, $meno, $priezvisko, $vek, $respekt) {
if ($link = prihlas_sql()) {	 
		$sql = "INSERT INTO hraci_temp SET meno ='" . $meno . "', priezvisko ='" . $priezvisko . 
		 "', respekt ='" . $respekt  .  "', vek ='" . $vek .  "', id ='" . $id .  "'";
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

function pridajzapastemp($id,$typ,$teamA,$teamB,$datum,$vysledok,$vytaz){
if ($link = prihlas_sql()) {	 
		$sql = "INSERT INTO zapasy_temp SET id ='" . $id . "', typ ='" . 
		$typ .  "', teamA ='" . $teamA  .  "', teamB ='" . $teamB .
		  "', datum ='" . $datum .  "', vysledok ='" . $vysledok . 
		   "', vytaz ='" . $vytaz . "'";
		   
		//echo "sql = $sql <br/>"; 
		$result = mysql_query($sql, $link);
		if ($result) {
			 echo '<p class="chyba"><strong>Zapas Ulozeny! </strong></p>';
			  } 
			else {
				echo '<p class="chyba">Uuups ,Zapas NEUlozeny</p>';
				 }
			}
		else {
			echo '<p class="chyba">NEpodarilo sa vybrat databأ،zu</p>';
				}
mysql_close($link);
	}
	
function pridajzapas($typ,$teamA,$teamB,$datum,$vysledok,$vytaz){
//upravenie respektu hracom v zapase
upravRespekt($teamA,$teamB,$vytaz, $vysledok);
//nahodenie zapasu do DB
if ($link = prihlas_sql()) {	 
		$sql = "INSERT INTO zapasy SET  typ ='" . 
		$typ .  "', teamA ='" . $teamA  .  "', teamB ='" . $teamB .
		  "', datum ='" . $datum .  "', vysledok ='" . $vysledok . 
		   "', vytaz ='" . $vytaz . "'";
		   
		//echo "sql = $sql <br/>"; 
		$result = mysql_query($sql, $link);
		if ($result) {
			 echo '<p class="chyba"><strong>Zapas Ulozeny! </strong></p>';
			 return mysql_insert_id(); //vrati co posledne pridal
			  } 
			else {
				echo '<p class="chyba">Uuups ,Zapas NEUlozeny</p>';
				 }
			}
		else {
			echo '<p class="chyba">NEpodarilo sa vybrat databأ،zu</p>';
				}
mysql_close($link);
	}
	
function potrebne($zapas,$mnozinaPotrebnych) {
	
	$mnozinaPotrebnych= explode("/",$mnozinaPotrebnych);
	
	foreach ($mnozinaPotrebnych as &$jeden) {
		//echo 'if '.$jeden.'contains'.$zapas;
		if ($jeden==$zapas) {
			return true;
			}
		}

	return false;
	}
	
function dajIdHraca2($meno, $priezvisko, $vek) {
		if ($link = prihlas_sql()) {
   	 	$result = mysql_query("SELECT *
   	 								FROM `hraci` 
   	 								", $link);
   	 								 
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	if (($row['meno']==$meno) && ($row['priezvisko']==$priezvisko)  &&  ($row['vek']==$vek)) return $row['id'];
   	 	}
   	 	return -1;
			
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	}
	
function getHrac($id) {
		if ($link = prihlas_sql()) {
   	 	$result = mysql_query("SELECT *
   	 								FROM `hraci` 
   	 								WHERE id= ". $id. "", $link);
   	 								 
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	return $row;
   	 	}
   	 	return null;
			
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	}
	
function getTreningyKlubu($klub) {
//zisti id klbu	
	if ($link = prihlas_sql()) {
   	 	$result = mysql_query("SELECT id
   	 								FROM `kluby` 
   	 								WHERE Meno='". $klub ."'", $link);
   	 								 
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	$idKlubu= $row['id'];
   	 	}
			
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	if ($idKlubu == null) return null;	
	//vyber ich trenerov
		if ($link = prihlas_sql()) {
   	 	$result = mysql_query("SELECT id, klub
   	 								FROM `trening_user` 
   	 								WHERE klub=".$idKlubu."", $link);
   	 $i=0;
   	 while ($row = mysql_fetch_assoc($result)) {
   	 		$trenery[$i]=$row['id'];
   	 		$i++;
   	 	}
   	 
			
		 mysql_free_result($result);
		 	 }
		 	  
	 mysql_close($link);	
	 
	for ($j=0; $j<$i; $j++) {
		if ($link = prihlas_sql()) {
			
			$sql = "
			SELECT id 
			FROM `treningy`
			Where Trener = ".$trenery[$j]."";
			
   	 	$result = mysql_query($sql, $link);		
   
   	 while ($row = mysql_fetch_assoc($result)) {

			vypisTrening($row['id']);	
			//echo '/////////////////////////////////////////////\n';
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);			
	}
	}
	

	
function dajIdHraca($meno, $priezvisko) {
		if ($link = prihlas_sql()) {
   	 	$result = mysql_query("SELECT *
   	 								FROM `hraci` 
   	 								", $link);
   	 								 
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	if (($row['meno']==$meno) && ($row['priezvisko']==$priezvisko)  ) return $row['id'];
   	 	}
   	 	return -1;
			
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	}
	
function pridajTRening($datum, $poznamka, $pocetkurtov, $dochadzka, $zapasy , $trener, $uid) {
if ($link = prihlas_sql()) {	 
		$sql = "INSERT INTO treningy SET datum ='" . $datum . "', popis ='" . $poznamka .  "', pocetkurtov ='" .
		 $pocetkurtov  .  "', hraci ='" . $dochadzka .  "', zapasy ='" . $zapasy .  "', trener= '".$trener."', uid= '".$uid ."'";
		//echo "sql = $sql <br/>"; 
		$result = mysql_query($sql, $link);
		if ($result) {
			 echo '<p class="chyba"><strong>Trening Ulozeny! </strong></p>';
			  } 
			else {
				echo '<p class="chyba">Uuups ,Trening NEUlozeny</p>';
				 }
			}
		else {
			echo '<p class="chyba">NEpodarilo sa vybrat databأ،zu</p>';
				}
mysql_close($link);
}
	
	 		   
function pridajTReningtemp($datum, $poznamka, $pocetkurtov, $dochadzka, $zapasy, $trener, $uid) {
if ($link = prihlas_sql()) {	 
		$sql = "INSERT INTO treningy_temp SET datum ='" . $datum . "', popis ='" . $poznamka .  "', pocetkurtov ='" .
		 $pocetkurtov  .  "', hraci ='" . $dochadzka .  "', zapasy ='" . $zapasy .  "', trener= '".$trener . "', uid= '".$uid ."'"; //pridat 
		//echo "sql = $sql <br/>"; 
		$result = mysql_query($sql, $link);
		if ($result) {
			 echo '<p class="chyba"><strong>Trening Ulozeny! </strong></p>';
			  } 
			else {
				echo '<p class="chyba">Uuups ,Trening NEUlozeny</p>';
				 }
			}
		else {
			echo '<p class="chyba">NEpodarilo sa vybrat databأ،zu</p>';
				}
mysql_close($link);
	}
	
function novyHrac() {
	if ($link = prihlas_sql()) {
   	 	$result = mysql_query("SELECT meno, priezvisko, vek
			FROM `hraci_temp`", $link);
			
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	//echo "id: ".dajIdHraca($row['meno'],$row['priezvisko'],$row['vek'])."<br />";
				 if (dajIdHraca($row['meno'],$row['priezvisko'],$row['vek'])<0) 
{				 		insertnovyhrac($row['meno'],$row['priezvisko'],$row['vek']);
						//echo "novyhrac".$row['meno'].$row['priezvisko'].$row['vek'];
}
				 		
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	}
	
function  vyriesKonflikty() {
	if ($link = prihlas_sql()) {
	$sql = "
SELECT t.id as idStare, c.id as idNove \n"
    . "FROM `hraci_temp` t, `hraci` c\n"
    . "WHERE t.meno=c.meno AND t.priezvisko=c.priezvisko AND t.vek=c.vek AND t.id <>c.id";
   	 	$result = mysql_query($sql, $link);
			
   	 while ($row = mysql_fetch_assoc($result)) {
				poprepisuj($row['idStare'],$row['idNove']);
			//	echo "prepisuj".$row['idStare']." na ".$row['idNove'];
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	}
	
function  poprepisuj($idStare,$idNove) {
	//treningy
	if ($link = prihlas_sql()) {
	$sql = "
SELECT hraci, id
FROM `treningy_temp` ";
   	 	$result = mysql_query($sql, $link);			
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	$boltu=false;
				$hraci= explode("/",$row['hraci']);
				foreach ($hraci as &$hrac) {
					if ($hrac==$idStare) {
						$hrac=$idNove;
						$boltu=true;
						}
					}
				$row['hraci']= implode("/", $hraci);
				if ($boltu) updateTreningTemp($row['id'],$row['hraci']);
				//echo "update".$row['id']." na ".$row['hraci'];
				unset($hrac); 
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	 //zapasy
if ($link = prihlas_sql()) {
	$sql = "
SELECT teamA, teamB, id, typ
FROM `zapasy_temp` ";
   	 	$result = mysql_query($sql, $link);
			
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	$hralZapas=false;
   	 	if ($row['typ']==1){
   	 			if ($row['teamA']==$idStare){
   	 				 $row['teamA']=$idNove;
   	 				 $hralZapas=true;
   	 				 }
   	 			if ($row['teamB']==$idStare){
   	 				 $row['teamB']=$idNove;
   	 				 $hralZapas=true;
   	 				 }
   	 		}
   	 	else {
				$teamA= explode("/",$row['teamA']);
				foreach ($teamA as &$hrac) {
					if ($hrac==$idStare) {
						$hrac=$idNove;
						$hralZapas=true;
						}
					}
				$row['teamA']= implode("/", $teamA);
				unset($hrac); 
				
				$teamB= explode("/",$row['teamB']);
				foreach ($teamB as &$hrac) {
					if ($hrac==$idStare) {
						$hrac=$idNove;
						$hralZapas=true;
						}
					}
				$row['teamB']= implode("/", $teamB);				
				unset($hrac);
				}
	if ($hralZapas)	updateZapasTemp($row['id'],$row['teamA'],$row['teamB']);
			//echo "update".$row['id']." na ".$row['hraci'];	
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	}	
	
function updateZapasTemp($id,$teamA,$teamB) {
	if ($link = prihlas_sql()) {
		$sql = "UPDATE `zapasy_temp` SET teamA='" . $teamA . "', teamB='" . $teamB . "'";
		$sql .= " WHERE id='" . $id . "'";
		$result = mysql_query($sql, $link);
		if ($result) echo '<p class="chyba">Udaje Zmenene!<p>' ;
		  else echo '<p class="chyba" >Chyba!(chyba pri ukladani)</p>';
		}
		mysql_close($link); 
	}

function updateTreningTemp($id,$hraci) { 
	if ($link = prihlas_sql()) {
		$sql = "UPDATE `treningy_temp` SET hraci='" . $hraci  . "'";
		$sql .= " WHERE id='" . $id . "'";
		$result = mysql_query($sql, $link);
		if ($result) echo '<p class="chyba">Udaje Zmenene!<p>' ;
		  else echo '<p class="chyba" >Chyba!(chyba pri ukladani)</p>';
		}
		mysql_close($link); 
	}

function vycistiTempDb() {
	if ($link = prihlas_sql()) {
   	 	$result = mysql_query("TRUNCATE TABLE `hraci_temp` ", $link);
		 mysql_free_result($result);
		 $result = mysql_query("TRUNCATE TABLE `zapasy_temp` ", $link);
		 mysql_free_result($result);
		 		 $result = mysql_query("TRUNCATE TABLE `treningy_temp` ", $link);
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	}
	
function upravRespekt($hracA,$hracB, $vytaz,$kolko) {
 	if ($vytaz !=1 && $vytaz !=2  ) return null;
	
	if ($link = prihlas_sql()) {
   	 	$result = mysql_query("SELECT respekt
   	 								FROM `hraci` 
   	 								WHERE id= ". $hracA. "", $link);
   	 								 
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	$staryRespektA=$row['respekt'];
   	 	}

			
		 mysql_free_result($result);
		 	 }
		 	 
if ($link = prihlas_sql()) {
   	 	$result = mysql_query("SELECT respekt
   	 								FROM `hraci` 
   	 								WHERE id= ". $hracB. "", $link);
   	 								 
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	$staryRespektB=$row['respekt'];
   	 	}

			
		 mysql_free_result($result);
		 	 }		 	 
 
	if ($vytaz==1) {
		$respektA= intval( $staryRespektA) + (intval( $staryRespektB)/100) *((21 -intval($kolko)/15)) ; //vypocet je: o tolko percent siprilesi o kolko bodov vyhral
  		$respektB= intval( $staryRespektB) - (intval( $staryRespektB)/100) *((21 -intval($kolko)/15)) ;

  	}
  	
  	if ($vytaz==2) {
		$respektA= intval( $staryRespektA) - (intval( $staryRespektA)/100) *((21 -intval($kolko)/15)) ; //vypocet je: o tolko percent siprilesi o kolko bodov vyhral
  		$respektB= intval( $staryRespektB) + (intval( $staryRespektA)/100) *((21 -intval($kolko)/15)) ;
  	}
  
	if ($link = prihlas_sql()) {
		$sql = "UPDATE `hraci` SET respekt='" . $respektA  . "'";
		$sql .= " WHERE id='" . $hracA . "'";
		$result = mysql_query($sql, $link);
		if ($result) echo '<p class="chyba">Udaje Zmenene!<p>' ;
		  else echo '<p class="chyba" >Chyba!(chyba pri ukladani)</p>';
		}
		mysql_close($link); 
		
	if ($link = prihlas_sql()) {
		$sql = "UPDATE `hraci` SET respekt='" . $respektB  . "'";
		$sql .= " WHERE id='" . $hracB . "'";
		$result = mysql_query($sql, $link);
		if ($result) echo '<p class="chyba">Udaje Zmenene!<p>' ;
		  else echo '<p class="chyba" >Chyba!(chyba pri ukladani)</p>';
		}
		mysql_close($link);
	}

function nahod() {
	if ($link = prihlas_sql()) {
	$sql = "
SELECT *
FROM `zapasy_temp` ";
   	 	$result = mysql_query($sql, $link);			
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	$zapasy.=pridajzapas($row['typ'],$row['teamA'],$row['teamB'],$row['datum'],$row['vysledok'],$row['vytaz']).'/';
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	
	
	if ($link = prihlas_sql()) {
	$sql = "
SELECT *
FROM `treningy_temp` ";
   	 	$result = mysql_query($sql, $link);			
   	 while ($row = mysql_fetch_assoc($result)) {

   	 	 pridajTRening($row['datum'],$row['popis'], $row['pocetkurtov'], $row['hraci'], $zapasy, $row['trener'] ,$row['uid']);
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	 	
	}
	
function  vypisujhracskudb() {
	echo '<br /><div><b> Hracska db: </b> <br /> <br />';
		if ($link = prihlas_sql()) {
	$sql = "
SELECT meno as m, priezvisko as p, respekt as r,vek as v, id
FROM `hraci`";
   	 	$result = mysql_query($sql, $link);			
   	 	echo "<table  cellspacing=\"0\" cellpadding=\"0\" border=\"1\">";
   	 	echo '<tr>'.'<th>Meno</th>';
   	 	echo '<th>Priezvisko</th>';
   	 	echo '<th>Hodnotenie</th>';
   	 	echo '<th>Vek</th>';
   	 	echo '<th></th>'.'</tr>';
   	 while ($row = mysql_fetch_assoc($result)) {
			echo '<tr>'.'<td>' 
			.'<a href="'.$_SERVER['PHP_SELF'].'?p=system&a=profil&id='. $row['id'] .'" >'.$row['m'].'</a>'.'</td>';
			echo '<td>'. '<a href="'.$_SERVER['PHP_SELF'].'?p=system&a=profil&id='. $row['id'] .'" >'.$row['p'].'</a>'.'</td>';		
			echo '<td>'. $row['r'].'</td>';
			echo '<td>'. $row['v'].'</td>'.'</tr>';
	    }
	    		echo "</table>";
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 echo '</div>';
	}
	
function  kotrolaDuplicityTreningu( $datum, $uid) {
		if ($link = prihlas_sql()) {
	$sql = "
SELECT datum, uid 
FROM `treningy` 
Where uid= '". $uid ."' AND  datum='".$datum."'";   
   	 	$result = mysql_query($sql, $link);		
   
   	 if ($row = mysql_fetch_assoc($result)) {
				return true;
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 return false;
	}
	


function  vypisujTReningydb() {
	echo '<div id="trening">';
		if ($link = prihlas_sql()) {
	$sql = "
SELECT id 
FROM `treningy`";
   	 	$result = mysql_query($sql, $link);		
   
   	 while ($row = mysql_fetch_assoc($result)) {
			//echo '<a href="" >'; vypisuj linky
			//echo $row['id'].'a';
			vypisTrening($row['id']);	
			echo '/////////////////////////////////////////////\n';
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 echo '</div>';
	}
	
function  vypisTRening($id) {
	echo '<div>';
		if ($link = prihlas_sql()) {
	$sql = "
SELECT datum as d, popis as p, pocetKurtov as k, hraci as h, zapasy as z 
FROM `treningy` 
WHERE id= " .$id.  "";
   	 	$result = mysql_query($sql, $link);		
   
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	
			echo 'Dna: '.$row['d'].'<br />';
			echo 'Popis: '.$row['p'].'<br />';
			echo 'pocet kurtov: '.$row['k'].'<br /><br />';
			
			$hraci= explode("/",$row['h']);
			echo 'Zucastnili sa: <br />';
			
			foreach ($hraci as &$hrac) {
					vypisHraca($hrac);						
			}
			unset($hrac);
			
			$zapasy= explode("/",$row['z']);
			echo '<br /><br /> hrali sa nasledujuce zapasy: <br />';
			foreach ($zapasy as &$zapas) {
					vypisZapasy($zapas);						
			}
			unset($zapasy);
			
			/*echo '<table summary="" >';
			echo '<tr>';
			echo '<td>'.$row['d'].'</td>';
			echo '<td>'.''.$row['p'].'</td>';
			echo '<td>'.''.$row['k'].'</td>';
			echo '</tr>';	
			$hraci= explode("/",$row['h']);
			echo '<tr>';
			
			echo '</tr>';
			echo '</table>';		*/
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 echo '</div>';
}

function  getTreningy() {
	echo '<div id="trening">';
		if ($link = prihlas_sql()) {
	$sql = "
SELECT * 
FROM `treningy`";
   	 	$result = mysql_query($sql, $link);		
   
   	 while ($row = mysql_fetch_assoc($result)) {
			echo $row['datum'].'~'.$row['popis'].'~'.$row['pocetkurtov'].'~'.$row['hraci'].'~'.$row['zapasy'].'~'.$row['Trener'].'~'.$row['uid'];
			echo '<br><br>';
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 echo '</div>';
}

function  getZapasy() {
	echo '<div id="zapasy">';
		if ($link = prihlas_sql()) {
	$sql = "
SELECT * 
FROM `zapasy`";
   	 	$result = mysql_query($sql, $link);		
   
   	 while ($row = mysql_fetch_assoc($result)) {
			echo $row['id'].'~'.$row['typ'].'~'.$row['teamA'].'~'.$row['teamB'].'~'.$row['datum'].'~'.$row['vysledok'].'~'.$row['vytaz'];
			echo '<br><br>';
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 echo 'zapasend</div>';
}

function  gethraci() {
	echo '<div id="hraci">';
		if ($link = prihlas_sql()) {
	$sql = "
SELECT * 
FROM `hraci`";
   	 	$result = mysql_query($sql, $link);		
   
   	 while ($row = mysql_fetch_assoc($result)) {
			echo $row['id'].'~'.$row['meno'].'~'.$row['priezvisko'].'~'.$row['respekt'].'~'.$row['vek'];
			echo '<br><br>';
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 echo '</div>';
}


function  vypisHraca($id) {
	if ($link = prihlas_sql()) {
	$sql = "
SELECT `meno` , `priezvisko` , `respekt` , `vek`
FROM `hraci`
WHERE `id` =". $id ." ";
   	 	$result = mysql_query($sql, $link);
			
   	 while ($row = mysql_fetch_assoc($result)) {
				echo 	$row['meno']. ' '.$row['priezvisko'].'<br />';		
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	}
	
function  vypisZapasy($id) {
	if ($link = prihlas_sql()) {
	$sql = "
SELECT `typ` , `teamA` , `teamB` , `vysledok`, `vytaz`
FROM `zapasy`
WHERE `id` =". $id ." ";
   	 	$result = mysql_query($sql, $link);
			
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	if ($row['typ']=="1"){
   	 		vypisHraca($row['teamA']);
   	 		vypisHraca($row['teamB']);
   	 		echo 'vysledok: '.$row['vysledok']. ", vytaz:". $row['vytaz'] ;
   	 		}
   	 	if ($row['typ']=="2"){
   	 		
			$teamA= explode("/",$row['teamA']);
			echo 'Team A:';
			foreach ($teamA as &$hrac) {
					vypisHraca($hrac);						
			}
			unset($hrac);			
			
			$teamB= explode("/",$row['teamB']);
			echo 'versus Team B:';
			foreach ($teamB as &$hrac) {
					vypisHraca($hrac);						
			}			
			unset($hrac);
			
			echo 'vysledok: '.$row['vysledok']. ", vytaz:". $row['vysledok'] ;			
				}
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
	}
	
function napisdalsiehodochdzka($id) {
	echo '<select size="1" id="dochadzka" name="dochadzka[]" ><option value="-1">---</option>';
	if ($link = prihlas_sql()) {
	$sql = "
SELECT meno as m, priezvisko as p, respekt as r,vek as v, id  
FROM `hraci`";
   	 	$result = mysql_query($sql, $link);			
   	 while ($row = mysql_fetch_assoc($result)) {
		   echo '<option value='. $row[id];
		   if($id==$row['id']) {
		   echo ' selected="selected"';
		   }
		   echo '>'.$row['p'].' '.$row['m'].'( <strong><em>'.$row['v'].', '.$row['r'].")</em></strong></option>\n";
	    }
	    echo '</select>';
	    		
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link); 
}

function vypiskluby() {
	
	if ($link = prihlas_sql()) {
	$sql = "
SELECT Meno as m, sport as s , id
FROM `kluby`";
   	 	$result = mysql_query($sql, $link);			
   	 while ($row = mysql_fetch_assoc($result)) {
		   echo '<option value='. $row['id'];
		   echo '>'.$row['m'].'( <strong><em>'.$row['s'].")</em></strong></option>\n";
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link); 
}

function napisdalsiehozapasy($id1,$id2) {
	//echo '		<input type="hidden" value="0" id="theValue2" />';
echo 'Team A: <select size="1"  name="zapasyA[]" id="zapas" ><option value="-1">---</option>';
	if ($link = prihlas_sql()) {
	$sql = "
SELECT meno as m, priezvisko as p, respekt as r,vek as v, id  
FROM `hraci`";
   	 	$result = mysql_query($sql, $link);			
   	 while ($row = mysql_fetch_assoc($result)) {
		   echo '<option value='. $row['id'];
		   if ($id1==$row['id']){ echo ' selected="selected"';} 
		   echo '>'.$row['p'].' '.$row['m'].'( <strong><em>'.$row['v'].', '.$row['r'].")</em></strong></option>\n";
	    }
	    echo ' </select>  ';
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 
	 echo ' Team B:<select size="1" id="zapas" name="zapasyB[]" ><option value="">---</option>';
	if ($link = prihlas_sql()) {
	$sql = "
SELECT meno as m, priezvisko as p, respekt as r,vek as v, id  
FROM `hraci`";
   	 	$result = mysql_query($sql, $link);			
   	 while ($row = mysql_fetch_assoc($result)) {
		   echo '<option value='. $row['id'];
		    if ($id2==$row['id']){ echo ' selected="selected"';} 
		   echo '>'.$row['p'].' '.$row['m'].'( <strong><em>'.$row['v'].', '.$row['r'].")</em></strong></option>\n";
	    }
	    echo ' </select>';
	    		
	    		?>

	    		<?php
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	}
	
	
function napisdalsiehozapasyupdate($id) {
	if ($link = prihlas_sql()) {
	$sql = '
SELECT *  
FROM `zapasy`
WHERE typ=1 AND id='.$id.'';
   	 	$result = mysql_query($sql, $link);			
   	 while ($row = mysql_fetch_assoc($result)) {
		   	napisdalsiehozapasy($row['teamA'],$row['teamB']); //pre vsetky zapasy jeden riadok kde hral ten proti tomu
		   	   
	        
	    echo '</td>
                    <td> <select name="vytaz" >
                   
								 <option value="1" ';
		if ($row['vytaz']==1) echo ' selected="selected"';
		echo '>Team A</option>';
		echo '<option value="2" ';
		if ($row['vytaz']==2) echo ' selected="selected"'; 
		echo '>Team B</option>
  								 </select>  
  						  </td>
                    <td><input type="text" class="mini" id="skore" name="skore" value="'.$row['vysledok'].'" /></td>';
       }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	}

function novyTreningForm(){
	echo '<h2>Zadaj zakladne info o TReningu</h2>';
	echo '<br /> <form id="test" action=" '.$_SERVER['REQUEST_URI'].'" method="post" name="odosliNovyTrening">';
		echo '<input type="Text" id="demo1" maxlength="25" size="25"><a href="javascript:NewCal(\'demo1\',\'ddmmmyyyy\',true,24)"><img src="images/cal.gif" width="16" height="16" border="0" alt="date"></a><br />';
		echo '<textarea rows="2" name="popis" onchange="addrow()" cols="30">napis poznaku k treningu</textarea> <br />';
		echo 'Pcet kurtov: <input type="text"  name="pocetKurtovForm[]" value="0" /><br />';

	 echo '<h2>Na Treningu boli</h2>';
	 napisdalsiehodochdzka();
	 
	 echo '<h2>Na Treningu sa odohrali zapasy</h2>';
	 napisdalsiehozapasy();
	 
	 echo '<input name="odoslinovyTRening" type="submit" id="odoslinovyTRening" value="Odosli novy trening" />';
	 echo '</form>';
	}

function registrujTrenera($login, $meno, $email, $pass, $klub, $nic) {	
	if ($nic==null) $nic=-1; //prava -1 znaci neautorizovany trener
	if ($link = prihlas_sql()) {	 
		$sql = "INSERT INTO trening_user SET meno ='" . $meno .
		 "', login ='" . $login .  "', email ='" . $email  .  
		 "', heslo ='" . MD5($pass) . "', prava ='" . $nic .
		 "', token ='" . $nic . "', klub ='" . $klub  .  "'";
		 
		//echo "sql = $sql <br/>"; 
		$result = mysql_query($sql, $link);
		if ($result) {
			 //echo '<p class="chyba"><strong>Trener Ulozeny! </strong></p>';
			  } 
			else {
				echo '<p class="chyba">Uuups ,Trener NEUlozeny</p>';
				 }
			}
		else {
			echo '<p class="chyba">NEpodarilo sa vybrat databazu</p>';
				}
mysql_close($link);
	}
	
	
function registrujHraca($login, $meno, $email, $pass, $klub, $nic) {	
	if ($nic==null) $nic=-2; //prava -2 znaci neautorizovany hrac
	if ($link = prihlas_sql()) {	 
		$sql = "INSERT INTO trening_user SET meno ='" . $meno .
		 "', login ='" . $login .  "', email ='" . $email  .  
		 "', heslo ='" . MD5($pass) . "', prava ='" . $nic .
		 "', token ='" . $nic . "', klub ='" . $klub  .  "'";
		 
		//echo "sql = $sql <br/>"; 
		$result = mysql_query($sql, $link);
		if ($result) {
			 //echo '<p class="chyba"><strong>Trener Ulozeny! </strong></p>';
			  } 
			else {
				echo '<p class="chyba">Uuups ,Trener NEUlozeny</p>';
				 }
			}
		else {
			echo '<p class="chyba">NEpodarilo sa vybrat databazu</p>';
				}
mysql_close($link);
	}
	
function duplicitaTrenera($login,$email) {
	if ($link = prihlas_sql()) {
	$sql = "
SELECT login, email
FROM `trening_user`";
   	 	$result = mysql_query($sql, $link);			
   	 while ($row = mysql_fetch_assoc($result)) {
		  			if (( $row['login']==$login ) || ( $row['email']==$email ) ) return false; 
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 return true;
	}
	
function registrujKlub($menok,$sport) {
	$nic=-1;
	if ($link = prihlas_sql()) {	 
		$sql = "INSERT INTO kluby SET Meno ='" . $menok .
		 "', sport ='" . $sport .  "', superUser ='" . $nic  .  "'";
		 
		//echo "sql = $sql <br/>"; 
		$result = mysql_query($sql, $link);
		if ($result) {
			// echo '<p class="chyba"><strong>Klub Ulozeny! </strong></p>';
			  } 
			else {
			//	echo '<p class="chyba">Uuups ,Klub NEUlozeny</p>';
				 }
			}
		else {
			echo '<p class="chyba">NEpodarilo sa vybrat databazu</p>';
				}
mysql_close($link);
	}
	
function najdiklub($meno) {
	if ($link = prihlas_sql()) {
	$sql = '
SELECT id
FROM `kluby` k
WHERE k.meno = "'.$meno.'"';
   	 	$result = mysql_query($sql, $link);			
   	 while ($row = mysql_fetch_assoc($result)) {
		  			return $row['id']; 
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 return null;
	}
	
function najdiTrenera($meno) {
	if ($link = prihlas_sql()) {
	$sql = '
SELECT id
FROM `trening_user` k
WHERE k.login = "'.$meno.'"';
   	 	$result = mysql_query($sql, $link);			
   	 while ($row = mysql_fetch_assoc($result)) {
		  			return $row['id']; 
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 return null;
	}

function updateKlub($id,$superUser) { 
	if ($link = prihlas_sql()) {
		$sql = "UPDATE `kluby` SET superUser='" . $superUser ."'";
		$sql .= " WHERE id='" . $id . "'";
		//echo "sql = $sql <br/>"; 
		$result = mysql_query($sql, $link);
		/*if ($result) echo '<p class="chyba">Udaje Zmenene!<p>' ;
		  else echo '<p class="chyba" >Chyba!(chyba pri ukladani)</p>';
		}
		*/
		mysql_close($link); 
	}
}	

function token($email) {
	 $nadpis="Potvrdenie registracie na Coach note's"; 
	 $body=  "Pekný deň, \n práve ste sa registrovali na webe projektu Coach note's, pre aktiváciu vášho konta na mobilnom telefone prosím prejdite na tento link: 
   http://www.st.fmph.uniba.sk/~kuchynar1/rp/index.php?pridajToken=". $email .  "  ,
   po zobrazenie sa vam prideli unikatny kod ktori je potrebne prepisat do aplikacie v mobilnom telefone.  
   Ak ste sa neregistrovali Vy prosim ignorujte tento email. \n \n Admin webu Coach note's"; 
	 $headers = "From: Choachnotes@uniba.sk\r\n" . "X-Mailer: php";//nastavenie odosielatela spravy 			 
	 $headers = $headers. 'MIME-Version: 1.0' . "\r\n" . 'Content-type: text/plain; charset=UTF-8' . "\r\n"; //nastavenie UTF-8 
	 mail($email, $nadpis, $body, $headers);
	}
	
function potvrdenie($email) {
if ($link = prihlas_sql()) {
	$sql = '
SELECT prava as p
FROM `trening_user` k
WHERE k.email = "'.$email.'"';
   	 	$result = mysql_query($sql, $link);			
   	 while ($row = mysql_fetch_assoc($result)) {
		  		if ($row['p']=='-1') $buduceprava=2;
		  		elseif ($row['p']=='-2') $buduceprava=1;
		  		else   $buduceprava=$row['p']; //lebo ide iba o zmenu tokenu nie potrvdenie usera
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	
     $token= md5(time().$email);
	   
		if ($link = prihlas_sql()) {
		$sql = "UPDATE `trening_user` SET token='" . $token ."' , prava='".$buduceprava  . "'";
		$sql .= " WHERE email='" . $email . "'";
		//echo "sql = $sql <br/>"; 
		$result = mysql_query($sql, $link);
	/*	if ($result) echo '<p class="chyba">Udaje Zmenene!<p>' ;
		  else echo '<p class="chyba" >Chyba!(chyba pri ukladani)</p>';*/
		}
		
		mysql_close($link); 
	 return $token;	
}	

function vypislinky() {
	if ($link = prihlas_sql()) {
	$sql = '
SELECT datum as d, id
FROM `treningy`
';
   	 	$result = mysql_query($sql, $link);	
   	 	$i=1;	
   	 echo '<p>';	
   	 while ($row = mysql_fetch_assoc($result)) {
		  			//$row['d'];
		  			echo '<a href="'.$_SERVER['PHP_SELF'].'?p=system&amp;p2=edituj&amp;id='. $row['id'] .'" >'.$i.'. dna: '.$row['d'] .'</a>';
		  			echo '<br \>';
		  			$i++;
		  			
	    }
	    echo '</p>';
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 return null;
	}
	
	function updatetreninguvypis($id) {
		if ($link = prihlas_sql()) {
	$sql = '
SELECT *
FROM `treningy`
WHERE id= '. $id .'
';
   	 	$result = mysql_query($sql, $link);		
   	 while ($row = mysql_fetch_assoc($result)) {
		  			echo '<p>Datum Treningu: <input id="datepicker2" type="text" name="datum" value="'.$row['datum'].'"></p>';
		  			echo '<p><textarea rows="2" name="popis" id="popis" onclick="erase2();" cols="30">'.$row['popis'].'</textarea> </p>';
		  			echo '<p>Pocet kurtov: <input type="text" onclick="erase1();" id="pocKurtov" name="pocetKurtov" value="'.$row['pocetkurtov'].'" /></p>';
		  			echo '<p><table cellpadding="0" cellspacing="0"> 
                  <tr>
                    <th></th>
                    <th><label for="dochadzka">na treningu sa zucastnili:</label></th>
                    <th></th>
                  </tr>';
                  $dochadzka=explode("/", $row['hraci']); 
               foreach ($dochadzka as &$hrac) {
               	//if ($hrac>0){
                echo ' 
                  <tr>
                    <td><span class="rowNumber">1</span></td>
                    <td>';
                    //echo $hrac.': z '. $row['dochadzka'];
               napisdalsiehodochdzka($hrac);
               echo '</td>
                    <td><input type="button" class="delRow delete" id="del" value=""/></td>
                  </tr>
                  
                ';
                }//}
                unset($dochadzka);
                echo '<tr>
                    <td colspan="10">
                      <div class="left"><input type="button" class="addRow submit" value="addHrac" /></div>
                    </td>
                  </tr>
                </table></p>';
                
               echo '<p><table cellpadding="0" cellspacing="0">
                  <tr>
                    <th></th>
                    <th><label for="zapas">na treningu sa hrali tieto zapasy:</label></th>
                    <th><label for="vytaz">vytaz</label>  </th>
                    <th><label for="skore">skore</label> </th>
                    <th></th>
                  </tr>';
             //echo 'zapasy:'.$row['zapasy'];
      		$zapasy=explode("/", $row['zapasy']);
      		//echo 'zapas:'.$zapasy[0];
      		foreach ($zapasy as &$zapas) {
      			if ($zapas!='') {
					echo '<tr>
                    <td><span class="rowNumber">1</span></td>
                    <td>';
              napisdalsiehozapasyupdate($zapas);
              //prazdny formular pre vyplnenie
              echo '<td><input type="button" class="delRow delete" id="del" value=""/></td>
                  </tr>';						
                            echo '                  <tr>  
                    <td><span class="rowNumber">1</span></td>
                    <td>';
                    napisdalsiehozapasy();
              echo     ' </td>
                    <td> <select name="vytaz" >
                    			 <option value="-1">---</option>
								 <option value="1">Team A</option>
  								 <option value="2">Team B</option>
  								 </select>  
  						  </td>
                    <td><input type="text" class="mini" id="skore" name="skore" value="" /></td>
                    <td><input type="button" class="delRow delete" id="del" value=""/></td>
                  </tr>';
			}}
			unset($zapasy);
      	echo          '
                  <tr>
                    <td colspan="10">
                      <div class="left"><input type="button" class="addRow submit" value="addZapas" /></div>
                    </td>
                  </tr>
                </table><p>            ';
        echo      
             '   <p><input type="submit" name="odoslat" value="Continue" class="submit" /></p>
        ';
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	}
	
	
	function vymazZapasy($idTreningu) {
		if ($link = prihlas_sql()) {
   	 	$result = mysql_query('SELECT zapasy as z
   	 								FROM `treningy` 
   	 								Where id='.$idTreningu.' 
   	 								', $link);
   	 								 
   	 while ($row = mysql_fetch_assoc($result)) {
  					return $row['z']; 	 	
   	 	}
   	 	return null; //add null chceck
			
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);	
		}

		
	function updateTRening($id,$datum, $poznamka, $pocetkurtov, $dochadzka, $zapasy) {
if ($link = prihlas_sql()) {	 
		$sql = "UPDATE treningy SET datum ='" . $datum . "', popis ='" . $poznamka .  "', pocetkurtov ='" .
		 $pocetkurtov  .  "', hraci ='" . $dochadzka .  "', zapasy ='" . $zapasy .  
		 "' Where id='".$id."'";
		//echo "sql = $sql <br/>"; 
		$result = mysql_query($sql, $link);
		if ($result) {
			 echo '<p class="chyba"><strong>Trening Upraveny! </strong></p>';
			  } 
			else {
				echo '<p class="chyba">Uuups ,Trening NEUpraveny</p>';
				 }
			}
		else {
			echo '<p class="chyba">NEpodarilo sa vybrat databأ،zu</p>';
				}
mysql_close($link);
	}
	
function loginToId($login){
	if ($link = prihlas_sql()) {
	$sql = '
SELECT id FROM `trening_user` where login= "'.$login.'"
';
   	 $result = mysql_query($sql, $link);		
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	return $row['id'];	
	    }
	    
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 return null;
	}	
	
	
	function getToken($login){
		if ($link = prihlas_sql()) {
	$sql = '
SELECT token FROM `trening_user` where login= "'.$login.'"
';
   	 $result = mysql_query($sql, $link);		
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	return $row['token'];		
	    }
	    
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 return null;
		}
	
	function vypisTreningyHraca($id) {
		echo '<h2> Hrac sa zucastnil na treningoch: </h2>';
			if ($link = prihlas_sql()) {
	$sql = '
SELECT datum as d, id, hraci
FROM `treningy`
';
   	 	$result = mysql_query($sql, $link);
   	 	  	 while ($row = mysql_fetch_assoc($result)) {
   	 	$hraci=explode("/", $row['hraci']);
   	 	foreach ($hraci as &$hrac){
				if (($hrac == $id) && ($hrac != null ) ) {
					echo '<a href="'.$_SERVER['PHP_SELF'].'?p=system&amp;a=vypis&amp;id='. $row['id'] .'" >'.$i.'.trening dna: '.$row['d'] .'</a> <br \>';
					$i++;
					}
   	 		}		
	    }
	    echo '</p>';
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 return null;
		}


		
	function vypisTreningyKlubu($id) {
		echo '<h2> vsetky treng nasho klubu: </h2>';
			if ($link = prihlas_sql()) {

	$sql = '
SELECT datum as d, id, hraci
FROM `treningy`
';
   	 	$result = mysql_query($sql, $link);	
   	 	$i=1;	
   	 echo '<p>';	
   	 while ($row = mysql_fetch_assoc($result)) {
					echo '<a href="'.$_SERVER['PHP_SELF'].'?p=system&amp;a=vypis&amp;id='. $row['id'] .'" >'.$i.'.trening dna: '.$row['d'] .'</a> <br \>';
					$i++;
	    }
	    echo '</p>';
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
	 return null;
		}
		
		function ZdrojeGraf($id) {
			if ($link = prihlas_sql()) {
	$sql = 'create temporary table vytaz as 
(
SELECT *
FROM `zapasy`
WHERE (teamB ='.$id.'
AND vytaz =2) OR (teamA ='.$id.'
AND vytaz =1)
);
';
   	 	$result = mysql_query($sql, $link);
   	 	
   	 	$sql = '
select count(*) as pocVytaztiev
from vytaz
limit 1;
';
   	 	$result = mysql_query($sql, $link);		
			$vyhryPocet=0;
			$prehryPocet=0;
			$nedohrate=0;
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	$vyhryPocet=$row['pocVytaztiev'];
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);

if ($link = prihlas_sql()) {
	$sql = 'create temporary table prehra as 
(
SELECT *
FROM `zapasy`
WHERE (teamB ='.$id.'
AND vytaz =1) OR (teamA ='.$id.'
AND vytaz =2)
);';
   	 	$result = mysql_query($sql, $link);
$sql = '
select count(*) as pocPrehier
from prehra
limit 1;
';   	 	
   	$result = mysql_query($sql, $link);	
   	 while ($row = mysql_fetch_assoc($result)) {
   	 	$prehryPocet=$row['pocPrehier'];
	    }
		 mysql_free_result($result);
		 	 } 
	 mysql_close($link);
			return array(   array('Vyhry',$vyhryPocet ),    
    array('Prehry',$prehryPocet));
			}
			
	function profil_hraca($id) {
	$hrac=getHrac($id);
	echo '<p>Profil hraca: '. $hrac['meno']. ' '.$hrac['priezvisko']. ' vek: '.$hrac['vek']. '</p>';
	vypisTreningyHraca($id);
 
   echo '<br \><br \><br \>';
   ?>
    <script type="text/javascript" src="ploter/jquery.min.js"></script>
<script type="text/javascript" src="ploter/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="ploter/plugins/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript" src="ploter/plugins/jqplot.donutRenderer.min.js"></script>
<link rel="stylesheet" type="text/css" href="ploter/jquery.jqplot.min.css" />
<script type="text/javascript">
$(document).ready(function(){
  var data =<?php echo json_encode( 
				ZdrojeGraf($id)
   ); ?>; 
  
  var plot1 = jQuery.jqplot ('chartdiv', [data],
    {
    	title:'Uspesnost v zapasoch',
      seriesDefaults: {
        // Make this a pie chart.
        renderer: jQuery.jqplot.PieRenderer,
        rendererOptions: {
          // Put data labels on the pie slices.
          // By default, labels show the percentage of the slice.
          showDataLabels: true
        }
      },
      legend: { show:true, location: 'e' }
    }
  );
});

</script>
<div id="chartdiv" style="height:200px;width:300px; "></div>
<?php
		}
?>
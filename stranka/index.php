<?php 
session_start();
include('funkcie.php'); 
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link type="text/css" rel="stylesheet" media="all" href="style.css" />
<link href="style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="./js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="./js/jquery.table.addrow.js"></script> <!-- tables -->
<script type="text/javascript">
$("document").ready(function(){
	$(".addRow").btnAddRow({rowNumColumn:"rowNumber"});
	$(".delRow").btnDelRow();
});
</script>
<script type="text/javascript">
function erase1()
{
document.getElementById('pocKurtov').value="";
}

function erase2()
{
document.getElementById('popis').value="";
}
</script>

<!-- datepick -->
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css"/>
<script src="js/jquery-ui.min.js"></script>
<!-- datepicker -->
<script type="text/javascript">
  $(document).ready(function() {
    $("#datepicker").datepicker();
  });
  
    $(document).ready(function() {
    $("#datepicker2").datepicker();
  });
</script>	
<!-- datepick -->
<script language="javascript" type="text/javascript" src="datetimepicker.js"></script>`

<title>Coach note's - Všetko čo trener potrebuje...</title>
</head>
<body>

<div id="site">
<div id="hlavicka">		
		<a href="#"><img src="images/logo.jpeg" alt="" width="81" height="55" /></a>
		<br /> Coach note's - Všetko čo trener potrebuje... <br /><br />
		</div>
		<div class="clearfix"></div>
		<div class="menu">
			<ul>
				<li class="style1"><a href="?p=system">System</a></li>
				<li class="style1"><a href="?p=home">About</a></li>
			</ul>
		</div>

		<div id="center">
				<?php
		if (( $_GET['p']=='home' ) || (!(isset($_GET['p']))) ) { 
		include('inc/about.html');
		}
		if ( $_GET['p']=='system' ) {
		?>		
		<div class="right">
		
		<?php 	
		//prihlasovanie
			if (isset($_POST['username']) && ($_POST['username'] != "") && isset($_POST['heslo']) && ($_POST['heslo'] != "")) {
if ($link = prihlas_sql()) {
	$sql = "SELECT * FROM `trening_user` WHERE login='" . $_POST['username'] . "' AND heslo=MD5('" . $_POST['heslo'] . "') LIMIT 1;";
	//echo "sql = $sql <br/>"; 
	$result = mysql_query($sql, $link);
	if ($result) {
		if ($row = mysql_fetch_assoc($result)) {
			$_SESSION['id'] = $row['id'];
			$_SESSION['username'] = $row['login']; 
			$_SESSION['prava'] = $row['prava'];
			$_SESSION['token'] = $row['token']; 
			$_SESSION['klub'] = $row['klub'];
		}
		mysql_free_result($result);
	} else {
		echo '<p style="color: #FF0000;">Chyba pri získavaní údajov z databázy - ' . mysql_errno($link) . ': ' . mysql_error($link) . '</p>';
	}
}
else { 
	echo '<p style="color: #FF0000;">Nepodarilo sa spojiť s databázou</p>';
} // end prihlasovanie
//odhlasenie
} elseif (isset($_POST['logout'])) { 
  $_SESSION = array();
  session_destroy();
}
//odhlasenie end
if (!(isset($_SESSION['username']) && ($_SESSION['username'] != ""))) {
	include('inc/login_form.html');
}

// pouzivatel je prihlaseny
else {
   include('inc/logout.html');
   //content  
   if (( $_GET['p']=='system') &&  $_GET['a']=='profil'  &&  isset($_GET['id']) && je_cislo($_GET['id']) ) {
     //ak sa pozera na niekoho profil kazdy ma pristup ku hoci ktoremu proflilu na st. nahlad
     profil_hraca($_GET['id']);
     echo '<a href="'.$_SERVER['PHP_SELF'].'?p=system"' .' >BACK</a>';
   	}
   elseif (( $_GET['p']=='system') &&  $_GET['a']=='vypis'  &&  isset($_GET['id']) && je_cislo($_GET['id']) ) {
     //nekuka na vlastny home ale na vypis treningov kukat moze kadzy vypis
		vypisTRening($_GET['id']);
   	}	
   //ak prezera standardny home
   else { include ('inc/content.php');}
}
?>
<br />	
		

		</div>
     
		<?php
		}
		

		
				
			
				if (( $_GET['p']=='andr' ) && ( isset($_GET['p']) ) ) {			
				
			//este treba pridat token pre okontrolovanie ci to dava ten ktori hrac+pridat do DB treningov:
			// kto viedol trening+ kto trening submitoval+kedy
					//hracska DB
					
				//kontrolny button 
				if ((isset($_POST['token'])) && isset($_POST['login']) && (getToken($_POST['login']) == $_POST['token'] ) && ($_POST['kontrola']=='ano') ){
					echo '<div id="odpoved">kontrola udajov uspesna!</div>';
					}					
					else echo '<div id="odpoved">zle zadany token/login<div>';
					
				//preberanie udajov					
				if ((isset($_POST['token'])) && isset($_POST['login']) && (getToken($_POST['login']) == $_POST['token'] ) && ($_POST['udaje']=='ano')  ){
					
					
			 	if ( isset($_POST['hraci']) ) {
			 		for( $i=0; $i < intval($_POST['pocet_h']); $i+=1  ) {			 			
			 		
			 		$id='id_'.$i;	
					$meno='meno_'.$i;
					$priezvisko='priezvisko_'.$i;

					$vek='vek_'.$i;
					$respekt='respekt_'.$i;					
					
					insertnovyhractemp($_POST[$id],$_POST[$meno],$_POST[$priezvisko],$_POST[$vek],$_POST[$respekt] );
					}
		 		}			
					
			if ( isset($_POST['zapasy']) ) {
				for( $i=0; $i < intval($_POST['pocet_z']); $i+=1  ) {
						$id='id_'.$i;
						$typ='typ_'.$i;
						$teamA='teamA_'.$i;
						$teamB='teamB_'.$i;
						$datum='datum_'.$i;
						$vysledok='vysledok_'.$i;
						$vytaz='vytaz_'.$i;
						
						pridajzapastemp($_POST[$id],$_POST[$typ],$_POST[$teamA],$_POST[$teamB],$_POST[$datum],$_POST[$vysledok],$_POST[$vytaz]);
					}	
				}
			//ok
			 if ( isset($_POST['treningy']) ) {
					//ok	 	
			 	//insertnovyhrac('fcia', 'fcia', $_POST['pocet_t'], 1);
				for( $i=0; $i < intval($_POST['pocet_t']); $i+=1  ) {
					//insertnovyhrac($_POST['zapasy_'.$i], $_POST['hraci_'.$i], 2, 1);
				if (!kotrolaDuplicityTreningu($_POST['datum_'.$i], $_POST['uid_'.$i]))   //ak trening s takimto uid mam v BD tak ho tam nepridam
				{ 
			 pridajTReningtemp($_POST['datum_'.$i], $_POST['poznamka_'.$i],
			   $_POST['pocetKurtov_'.$i], $_POST['hraci_'.$i], $_POST['zapasy_'.$i] , loginToId($_POST['login']), $_POST['uid_'.$i] );
			   }
			   else insertnovyhrac("duplicitar!", $_POST['hraci_'.$i], 2, 1);
			   
			 }
			 
			 //srotenie
			 novyHrac();
			 vyriesKonflikty();
			 nahod();
			 vycistiTempDb();
			 //povipisuj svoj stav..
			 
			
		//	 echo '<p id="odpovedTReningyDb">';
			 
			// vypisujTReningydb();
			echo '<p id="odpovedTReningyDb"> ';
			vypisujTReningydb();
			echo '</p>';
		//	echo '</p>';
			 
			 			 
}			 	
 		
 		}

 	}
			?>
			<!-- android upload veci! -->		
		<?php
		//registracia trenera
		if (( $_GET['p']=='reg1' ) && ( isset($_GET['p']) ) ) { 
		if (isset($_POST["login1"]) && kotrolatextu($_POST["login1"]) && 
			 isset($_POST["meno"]) && kontrolameno($_POST["meno"]) &&
			 isset($_POST["email"]) && kontrolaemail($_POST["email"]) &&
			 isset($_POST["pass"])  && kontrolaheslo($_POST["pass"]) &&
			 isset($_POST["suhlas"]) && isset($_POST["klub"])
				) { 
				if (!duplicitaTrenera($_POST["login1"], $_POST["email"])) 	$_POST["chyba"]='1';	
				else {
					registrujTrenera($_POST["login1"], $_POST["meno"], $_POST["email"], $_POST["pass"], $_POST["klub"]);
					echo "<h2> odoslane! pre spravne pozivanie mobilnej applikacie si prosim skotrolujte email!</h2>";
					token($_POST["email"]);
					}
				}
		//formular pre registraciu
		include('inc/form_reg_trener.php');
		
		}
		
		//registracia hraca
		if (( $_GET['p']=='reg3' ) && ( isset($_GET['p']) )) { 
		if (isset($_POST["login1"]) && kotrolatextu($_POST["login1"]) && 
			 isset($_POST["menosel"]) &&
			 isset($_POST["email"]) && kontrolaemail($_POST["email"]) &&
			 isset($_POST["pass"])  && kontrolaheslo($_POST["pass"]) &&
			 isset($_POST["suhlas"]) && isset($_POST["klub"])
				) { 
				if (!duplicitaTrenera($_POST["login1"])) 	$_POST["chyba"]='1';	
				else {
					registrujHraca($_POST["login1"], $_POST["meno"], $_POST["email"], $_POST["pass"], $_POST["klub"]);
					echo "<h2> odoslane! pre spravne pozivanie mobilnej applikacie si prosim skotrolujte email!</h2>";
					token($_POST["email"]);
					}
				}	
		//formular pre registraciu
		include('inc/form_reg_hrac.php');
		}
		//registracia klubu
		if (( $_GET['p']=='reg2' ) && ( isset($_GET['p']) )) { 
				if (isset($_POST["login1"]) && kotrolatextu($_POST["login1"]) && 
			 isset($_POST["meno"]) && kontrolameno($_POST["meno"]) &&
			 isset($_POST["email"]) && kontrolaemail($_POST["email"]) &&
			 isset($_POST["pass"])  && kontrolaheslo($_POST["pass"]) &&
			 isset($_POST["suhlas"]) && 
			 isset($_POST["menok"]) && kontrolameno($_POST["menok"]) &&
			 isset($_POST["sport"]) && kontrolameno($_POST["sport"])  
				) { 
				if (!duplicitaTrenera($_POST["login1"])) 	$_POST["chyba"]='1';	
				else {
					echo "<h2> odoslane!  pre pozivanie mobilnej applikacie si prosim skotrolujte email! </h2>"; 
					registrujKlub($_POST["menok"],$_POST["sport"]);
					$novyklub=najdiklub($_POST["menok"]);
					$prava=46;
					registrujTrenera($_POST["login1"], $_POST["meno"], $_POST["email"], $_POST["pass"], $novyklub, $prava);
					$superUser=najdiTrenera($_POST["login1"]);
					updateKlub($novyklub,$superUser);
					//mail
					token($_POST["email"]);
					}
				}
				
		//formular pre registraciu
		include('inc/form_reg_klub.php');
		}
		
		if ( isset($_GET['pridajToken']) ) {
			 
		echo 'vas token: '.potvrdenie($_GET['pridajToken']);
		}
		?>		
					

		  <!-- <div id="footer">
				<p>
					coded by <a href="www.st.fmph.uniba.sk/~kuchynar1/">Martin Kuchyňár</a> <br />
					<a href="http://www.gnu.org/licenses/gpl.html">Licence</a>
				</p> 
			</div> -->
		</div>

	</div>

</body>
</html>
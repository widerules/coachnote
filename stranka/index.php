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
<script type="text/javascript" src="java.js"></script>
<script type="text/javascript">
<!--//--><![CDATA[//><!--
jQuery.extend(Drupal.settings, { "basePath": "/", "dhtmlMenu": { "slide": "slide", "children": "children", "clone": "clone", "siblings": 0, "relativity": 0, "doubleclick": 0 } });
//--><!]]>
</script>
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
		?>
			
		  <div class="right">
				
				<p class="text1">
					<strong>Čo to je?</strong>...Ročníkovy projekt <br /><br />
					
					<strong>Cieľ projektu</strong><br />
					Tvorba aplikácie pre android a k nej webovské rozhranie, ktoré budú umožňovať jednoduchú správu, Badmintonových(a iných individuálnych športov) tréningov, ich následné spracovanie pre potreby trénerov, pripadne koordináciu treningov medzi trénermi.<br />			<a href="http://code.google.com/p/coachnote/" target="_blank" class="more"> Viac na code.google</a>					
				</p>
				<div class="dots"><img src="images/spacer.gif" alt="" width="580" height="1" /></div>
				
				<div class="dots2">
				
				
					<div class="left">
					<p class="text2">
						<strong>Android app. :</strong></p>
						<div class="clearfix"></div>
						<br />
						<ul>
							<li>zápis dochádzky treningu</li>
							<li>generovanie zápasov podla aktuálneho rebríčku, poctu ihrísk a počtu hráčov na tréningu</li>
							<li>zadanie výsledkov zápasov, aktulizácia poradia v rebríčku</li>
							<li>vedenie stručných poznámok o vedení tréningu(precvičovaná technika, ťažko namáhané svalstvo)</li>
							<li>automatická/manuálna synchronizácia s webovským rozhraním</li>					
							<li> <a href="klient/Trening.apk" > prva funkcna vezia klienta! </a> </li>
						</ul>
										
					
					</div>
					
					<div class="second">
	
						<p class="text2">
							<strong>webovske rozhranie :</strong> 
						</p>
						<div class="clearfix"></div>
						<br />
						
					
						<ul>
							<li>ako neprihlásený user
							<ul>
							<li>prezeranie štatistiky tj. Aktuálneho rebríčku hráčov, zápasy odohrané na určitom tréningu, alebo určitým hráčom</li>
							<li>možnosť nahlásenia chyby, či podnetu</li>
							</ul>
							</li>
							<li>ako prihlásený user(Tréner) <ul>
							<li>-všetky možnosti ktoré obsahuje aplikácia v telefóne(nutnosť autentifikacie podla mena/hesla)</li>
							<li>oprava chybných zápisov</li>			
							<li>export dát do "tlačiteľných" formátov, hlavne dochádzka hráčov</li>				
							</ul></li>				
						</ul>
						
						
					</div>
				</div>
				
			</div>
			<div class="clearfix"></div>
		<?php
		}
		if ( $_GET['p']=='system' ) {
		?>		
		<div class="right">
		
		<?php 	
			if (isset($_POST['username']) && ($_POST['username'] != "") && isset($_POST['heslo']) && ($_POST['heslo'] != "")) {
if ($link = prihlas_sql()) {
	$sql = "SELECT * FROM `trening_user` WHERE login='" . $_POST['username'] . "' AND heslo=MD5('" . $_POST['heslo'] . "') LIMIT 1;";
	//echo "sql = $sql <br/>"; 
	$result = mysql_query($sql, $link);
	if ($result) {
		if ($row = mysql_fetch_assoc($result)) {
			$_SESSION['username'] = $row['login']; 
			$_SESSION['prava'] = $row['prava'];
			$_SESSION['token'] = $row['token']; 
		}
		mysql_free_result($result);
	} else {
		echo '<p style="color: #FF0000;">Chyba pri získavaní údajov z databázy - ' . mysql_errno($link) . ': ' . mysql_error($link) . '</p>';
	}
}
else { 
	echo '<p style="color: #FF0000;">Nepodarilo sa spojiť s databázou</p>';
} // end spojenie s db

} elseif (isset($_POST['submit2'])) { // IF isset username, heslo
  $_SESSION = array();
  session_destroy();
}
if (!(isset($_SESSION['username']) && ($_SESSION['username'] != ""))) {
?>
<div>Pre pouzivanie/prezeranie sa musite najprv prihlasit</div>
<form action="<?php echo $_SERVER['REQUEST_URI']; ?>" method="post" name="form1">
<table border="0" cellspacing="0" cellpadding="0">
<tr><th>username:</th>
<td><input name="username" type="text" value="<?php if (isset($_POST['username'])) echo $_POST['username']; ?>" size="20" maxlength="20" /></td></tr>
<tr><th>heslo:</th>
<td><input name="heslo" type="password" size="20" maxlength="20" /></td></tr>
<tr><td>&nbsp;</td><td><input name="submit" type="submit" id="submit" value="Prihlás" /></td></tr>
</table>
</form>
<?php
}
else {
// pouzivatel je prihlaseny
  echo '<div>Vitaj v systéme!<b> ' . $_SESSION['username'] . ' ' . '</b></div>';
?>

<form action="<?php echo $_SERVER['REQUEST_URI']; ?>" method="post" name="form1">
<input name="submit2" type="submit" id="submit2" value="Odhlás" />
</form>  
<?php  
    }
?>
<br />	
		
		<!-- android upload veci! -->
			<?php
			 if ( isset($_POST['ntrening']) ) {
			 	//insertDatum($_POST['datum']);//nenakodena ta fcia a tokodovanie je dajake zle
//			 	$_POST['datum'] $_POST['pocetKurtov'] $_POST['poznamka']
				$dochadzka="";
				$zapasy="";
				echo 'pocet:'.$_POST['pocet_h'];
				for( $i=0; $i <intval($_POST['pocet_h']); $i++  ) {
					$meno='meno_'.$i;
					$priezvisko='priezvisko_'.$i;
					
					$id=dajIdHraca($_POST[$meno],	$_POST[$priezvisko]);
					
					
					if ( $id>0 ) {
						$dochadzka = $id.'/'.$dochadzka;
						}
					}
				
				for( $i=0; $i <intval($_POST['pocet_z']); $i++  ) {
					$ameno='ameno_'.$i;
					$apriezvisko='apriezvisko_'.$i;
					$bmeno='bmeno_'.$i;
					$bpriezvisko='bpriezvisko_'.$i;
					$vysledok='vysledok_'.$i;
					
					$aid=dajIdHraca($_POST[$ameno],	$_POST[$apriezvisko]);
					$bid=dajIdHraca($_POST[$bmeno],	$_POST[$bpriezvisko]);
					
					echo 'meno: '.$_POST[$ameno].'   priez: '.$_POST[$apriezvisko];
					echo '    id:'.$aid;
																				
					if (($aid>0) && ($bid>0)) {
					 $zapasy = $aid.' '. $bid.' '. $_POST[$v].' '. $_POST[$vysledok]. '/'. $zapasy;
					 }
									
					}
			 pridajTRening($_POST['datum'], $_POST['poznamka'],  $_POST['pocetKurtov'], $dochadzka, $zapasy);
			 	}
			 	if ( isset($_POST['nhraci']) ) {
			 		//echo 'pocet: '.intval($_POST['pocet_h']);
			 		
			 		for( $i=0; $i < intval($_POST['pocet_h']); $i+=1  ) {
			 			
					$meno='meno_'.$i;
					$priezvisko='priezvisko_'.$i;

					$vek='vek_'.$i;
					$respekt='respekt_'.$i;					
					
					insertnovyhrac($_POST[$meno],$_POST[$priezvisko],$_POST[$vek],$_POST[$respekt] );
					echo $i;
					}
			 		}
			?>
			<!-- android upload veci! -->
		</div>
     
		<?php
		}
		?>	
		  <div id="footer">
				<p>
					coded by <a href="www.st.fmph.uniba.sk/~kuchynar1/">Martin Kuchyňár</a> <br />
					<a href="http://www.gnu.org/licenses/gpl.html">Licence</a>
				</p> 
			</div>
		</div>

	</div>

</body>
</html>

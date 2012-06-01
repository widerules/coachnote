
<div id="welcome">
<?php
  if ($_SESSION['prava']=='1') {
  	echo  '<p>Vitaj v systéme klubu '.$_SESSION['klub'] .' !<b> '.'Hrac ' . $_SESSION['username'] . '</p>';
  	  
//  	 <!-- profil page -->
	profil_hraca($_SESSION['id']);
//  	 <!-- profil page end -->
  vypisTreningyKlubu($_SESSION['klub']);
  }
  
  if ($_SESSION['prava']=='2') {
  	echo '<p>Vitaj v systéme klubu '.$_SESSION['klub'] .' !<b> '.'Trener ' . $_SESSION['username'] . '</p>';
  	?>
  	<!-- panel na pridavanie/editovanie treningov -->
<div id="novyT">
<div id="login">
<a href="<?php echo $_SERVER['PHP_SELF']; ?>?p=system&amp;p2=pridaj" > pridaj novy Trening </a> <br />
<a href="<?php echo $_SERVER['PHP_SELF']; ?>?p=system&amp;p2=edituj" > edituj existujuci Trening </a><br />
<a href="<?php echo $_SERVER['PHP_SELF']; ?>?p=system&amp;p2=prezeraj" > stary trening k nahladu </a><br />
 <a href="<?php echo $_SERVER['PHP_SELF']; ?>?p=system&amp;p2=hraci" > nasi hraci </a><br /> 
</div>  
</div>
  	<?php
  	// trenerske formulare 
	if ($_GET['p2']=='pridaj') {
		if (isset($_POST['dochadzka']) 
		&& isset($_POST['zapasyA']) && isset($_POST['zapasyB']) 
		&& isset($_POST['popis']) && isset($_POST['datum']) 
		&& isset($_POST['pocetKurtov']) 
		) { 
		
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!		
		//nieje osetrene dummy spravanie ze pocet kurtov: rew atd..
			
			foreach ($_POST['dochadzka'] as &$hrac) {
    			$dochadzka.=$hrac.'/';
			}
			//echo $dochadzka.'<br/>';
			
			$i=0;
			$zapasyB=$_POST['zapasyB'];
			foreach ($_POST['zapasyA'] as &$zapas) {
				//echo 'zapas:'.$zapas. '  vs.  ' . $zapasyB[$i];
				$zapasy.=pridajzapas('1',$zapas,$zapasyB[$i],$_POST['datum'],$_POST['skore'],$_POST['vytaz']).'/';
				$i++;
			}			
			
			//echo $zapasy;
			
			$datum = new DateTime($_POST['datum']); 
			$uid= uniqid();
			echo '/////////////////////'.$uid;
			pridajTRening($datum->format('Y/m/d H:i:s'), $_POST['popis'], $_POST['pocetKurtov'], $dochadzka, $zapasy,$_SESSION['id'] , $uid);
			}
		?>
		

  
 <form action='<?php echo $_SERVER['REQUEST_URI']; ?>' id="novyTrening" method='post' name='form' class='order_form'>
 
           <h2>Zadaj zakladne info o TReningu</h2>		 <br />		           
           <p>Datum Treningu: <input id="datepicker" type="text" name="datum" value="pre zadanie klikni..."></p>
			  <p><textarea rows="2" name="popis" id="popis" onclick="erase2();" cols="30">napis poznaku k treningu</textarea> </p>
				<p>Pocet kurtov: <input type="text" onclick="erase1();" id="pocKurtov" name="pocetKurtov" value="napis pocet kurtov" /></p>           
           
                <p><table cellpadding='0' cellspacing='0'>
                  <tr>
                    <th></th>
                    <th><label for='dochadzka'>na treningu sa zucastnili:</label></th>
                    <th></th>
                  </tr>
                  <tr>
                    <td><span class="rowNumber">1</span></td>
                    <td><?php napisdalsiehodochdzka();?></td>
                    <td><input type="button" class="delRow delete" id="del" value=""/></td>
                  </tr>
                  <tr>
                    <td colspan='10'>
                      <div class='left'><input type="button" class="addRow submit" value="addHrac" /></div>
                    </td>
                  </tr>
                </table></p> 
		 <p><table cellpadding='0' cellspacing='0'>
                  <tr>
                    <th></th>
                    <th><label for='zapas'>na treningu sa hrali tieto zapasy:</label></th>
                    <th><label for='vytaz'>vytaz</label>  </th>
                    <th><label for='skore'>skore</label> </th>
                    <th></th>
                  </tr>
                  <tr>
                    <td><span class="rowNumber">1</span></td>
                    <td><?php napisdalsiehozapasy();?></td>
                    <td> <select name="vytaz" >
								 <option value="1">Team A</option>
  								 <option value="2">Team B</option>
  								 </select>  
  						  </td>
                    <td><input type="text" class="mini" id="skore" name="skore" value="" /></td>
                    <td><input type="button" class="delRow delete" id="del" value=""/></td>
                  </tr>
                  <tr>
                    <td colspan='10'>
                      <div class='left'><input type="button" class="addRow submit" value="addZapas" /></div>
                    </td>
                  </tr>
                </table><p>                 
                <p><input type='submit' name='odoslat' value='Continue' class='submit' /></p>
        </form>
		<?php 
		}
		
	elseif ($_GET['p2']=='edituj' && isset($_GET['id'])) {
			if (isset($_POST['dochadzka']) 
		&& isset($_POST['zapasyA']) && isset($_POST['zapasyB']) 
		&& isset($_POST['popis']) && isset($_POST['datum']) 
		&& isset($_POST['pocetKurtov']) 
		) { 
		
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!		
		//nieje osetrene dummy spravanie ze pocet kurtov: rew atd..
		//!!tuto nema byt pridavanie ale update!!
			
			foreach ($_POST['dochadzka'] as &$hrac) {
				if ($hrac>0)
    			$dochadzka.=$hrac.'/';
			}
			//echo $dochadzka.'<br/>';
			
			vymazZapasy($_GET['id']);//treningu s timto id vymaze vsetky treningy a nahradi ich novymy zaznammi aj ked to mozno
			//mozno nieje nutne, dochadzka tetto			
			
			$i=0;
			$zapasyB=$_POST['zapasyB'];
			foreach ($_POST['zapasyA'] as &$zapas) {
				//echo 'zapas:'.$zapas. '  vs.  ' . $zapasyB[$i];
				if ($zapas>0 && $zapasyB[$i]>0)
				$zapasy.=pridajzapas('1',$zapas,$zapasyB[$i],$_POST['datum'],$_POST['skore'],$_POST['vytaz']).'/';
				$i++;
			}			
			
			//echo $zapasy;
			
			$datum = new DateTime($_POST['datum']); 
			updateTRening($_GET['id'],$datum->format('Y/m/d H:i:s'), $_POST['popis'], $_POST['pocetKurtov'], $dochadzka, $zapasy);
			}
			?>
			<form action='<?php echo $_SERVER['REQUEST_URI']; ?>' id="updateTrening" method='post' name='form' class='order_form'>
 
           <h2>Zadaj zakladne info o TReningu</h2>		 <br />		        
  
           <?php updatetreninguvypis($_GET['id']) ?>
			<a href="<?php echo $_SERVER['PHP_SELF'];?>?p=system&amp;p2=edituj " > back </a>
			</form>
			<?php
			}
		elseif ($_GET['p2']=='edituj' && !isset($_GET['id']))  {
		?>
		<h2> Vyber ktory trening chces editovat </h2>
		<?php
		vypislinky();
		}
		
		elseif ($_GET['p2']=='prezeraj' && !isset($_GET['id']))  {
			vypisTreningyKlubu($_SESSION['klub']);
			}

		elseif ($_GET['p2']=='hraci' )  {
			vypisujhracskudb();
			}
//<!-- trenerske formulare end -->
  
  	}
  	
  	
if ($_SESSION['prava']=='3') {
  	  	?>
  	<!-- panel na pridavanie/editovanie treningov -->
<div id="novyT">
<div id="login">
<a href="<?php echo $_SERVER['PHP_SELF']; ?>?p=system&amp;p2=pridaj" > pridaj novy Trening </a> <br />
<a href="<?php echo $_SERVER['PHP_SELF']; ?>?p=system&amp;p2=edituj" > edituj existujuci Trening </a><br />
<a href="<?php echo $_SERVER['PHP_SELF']; ?>?p=system&amp;p2=prezeraj" > stary trening k nahladu </a><br />
<!-- <a href="<?php echo $_SERVER['PHP_SELF']; ?>?p=system&amp;p2=edituj" > nasi hraci </a><br /> -->
</div>  
</div>
  	<?php
  	// trenerske formulare 
	if ($_GET['p2']=='pridaj') {
		if (isset($_POST['dochadzka']) 
		&& isset($_POST['zapasyA']) && isset($_POST['zapasyB']) 
		&& isset($_POST['popis']) && isset($_POST['datum']) 
		&& isset($_POST['pocetKurtov']) 
		) { 
		
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!		
		//nieje osetrene dummy spravanie ze pocet kurtov: rew atd..
			
			foreach ($_POST['dochadzka'] as &$hrac) {
    			$dochadzka.=$hrac.'/';
			}
			//echo $dochadzka.'<br/>';
			
			$i=0;
			$zapasyB=$_POST['zapasyB'];
			foreach ($_POST['zapasyA'] as &$zapas) {
				//echo 'zapas:'.$zapas. '  vs.  ' . $zapasyB[$i];
				$zapasy.=pridajzapas('1',$zapas,$zapasyB[$i],$_POST['datum'],$_POST['skore'],$_POST['vytaz']).'/';
				$i++;
			}			
			
			//echo $zapasy;
			
			$datum = new DateTime($_POST['datum']); 
			pridajTRening($datum->format('Y/m/d H:i:s'), $_POST['popis'], $_POST['pocetKurtov'], $dochadzka, $zapasy);
			}
		?>
		

  
 <form action='<?php echo $_SERVER['REQUEST_URI']; ?>' id="novyTrening" method='post' name='form' class='order_form'>
 
           <h2>Zadaj zakladne info o TReningu</h2>		 <br />		           
           <p>Datum Treningu: <input id="datepicker" type="text" name="datum" value="pre zadanie klikni..."></p>
			  <p><textarea rows="2" name="popis" id="popis" onclick="erase2();" cols="30">napis poznaku k treningu</textarea> </p>
				<p>Pocet kurtov: <input type="text" onclick="erase1();" id="pocKurtov" name="pocetKurtov" value="napis pocet kurtov" /></p>           
           
                <p><table cellpadding='0' cellspacing='0'>
                  <tr>
                    <th></th>
                    <th><label for='dochadzka'>na treningu sa zucastnili:</label></th>
                    <th></th>
                  </tr>
                  <tr>
                    <td><span class="rowNumber">1</span></td>
                    <td><?php napisdalsiehodochdzka();?></td>
                    <td><input type="button" class="delRow delete" id="del" value=""/></td>
                  </tr>
                  <tr>
                    <td colspan='10'>
                      <div class='left'><input type="button" class="addRow submit" value="addHrac" /></div>
                    </td>
                  </tr>
                </table></p> 
		 <p><table cellpadding='0' cellspacing='0'>
                  <tr>
                    <th></th>
                    <th><label for='zapas'>na treningu sa hrali tieto zapasy:</label></th>
                    <th><label for='vytaz'>vytaz</label>  </th>
                    <th><label for='skore'>skore</label> </th>
                    <th></th>
                  </tr>
                  <tr>
                    <td><span class="rowNumber">1</span></td>
                    <td><?php napisdalsiehozapasy();?></td>
                    <td> <select name="vytaz" >
								 <option value="1">Team A</option>
  								 <option value="2">Team B</option>
  								 </select>  
  						  </td>
                    <td><input type="text" class="mini" id="skore" name="skore" value="" /></td>
                    <td><input type="button" class="delRow delete" id="del" value=""/></td>
                  </tr>
                  <tr>
                    <td colspan='10'>
                      <div class='left'><input type="button" class="addRow submit" value="addZapas" /></div>
                    </td>
                  </tr>
                </table><p>                 
                <p><input type='submit' name='odoslat' value='Continue' class='submit' /></p>
        </form>
		<?php 
		}
		
	elseif ($_GET['p2']=='edituj' && isset($_GET['id'])) {
			if (isset($_POST['dochadzka']) 
		&& isset($_POST['zapasyA']) && isset($_POST['zapasyB']) 
		&& isset($_POST['popis']) && isset($_POST['datum']) 
		&& isset($_POST['pocetKurtov']) 
		) { 
		
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!		
		//nieje osetrene dummy spravanie ze pocet kurtov: rew atd..
		//!!tuto nema byt pridavanie ale update!!
			
			foreach ($_POST['dochadzka'] as &$hrac) {
				if ($hrac>0)
    			$dochadzka.=$hrac.'/';
			}
			//echo $dochadzka.'<br/>';
			
			vymazZapasy($_GET['id']);//treningu s timto id vymaze vsetky treningy a nahradi ich novymy zaznammi aj ked to mozno
			//mozno nieje nutne, dochadzka tetto			
			
			$i=0;
			$zapasyB=$_POST['zapasyB'];
			foreach ($_POST['zapasyA'] as &$zapas) {
				//echo 'zapas:'.$zapas. '  vs.  ' . $zapasyB[$i];
				if ($zapas>0 && $zapasyB[$i]>0)
				$zapasy.=pridajzapas('1',$zapas,$zapasyB[$i],$_POST['datum'],$_POST['skore'],$_POST['vytaz']).'/';
				$i++;
			}			
			
			//echo $zapasy;
			
			$datum = new DateTime($_POST['datum']); 
			updateTRening($_GET['id'],$datum->format('Y/m/d H:i:s'), $_POST['popis'], $_POST['pocetKurtov'], $dochadzka, $zapasy);
			}
			?>
			<form action='<?php echo $_SERVER['REQUEST_URI']; ?>' id="updateTrening" method='post' name='form' class='order_form'>
 
           <h2>Zadaj zakladne info o TReningu</h2>		 <br />		        
  
           <?php updatetreninguvypis($_GET['id']) ?>
			<a href="<?php echo $_SERVER['PHP_SELF'];?>?p=system&amp;p2=edituj " > back </a>
			</form>
			<?php
			}
		elseif ($_GET['p2']=='edituj' && !isset($_GET['id']))  {
		?>
		<h2> Vyber ktory trening chces editovat </h2>
		<?php
		vypislinky();
		}
		
		elseif ($_GET['p2']=='prezeraj' && !isset($_GET['id']))  {
			vypisTreningyKlubu($_SESSION['klub']);
			}
			
		elseif ($_GET['p2']=='hraci' )  {
			vypisujhracskudb();
			}
//<!-- trenerske formulare end -->
   
  	 }
  if ($_SESSION['prava']=='47') { echo '<p> Welcome owner! <p>';  }
?>
</div>

<div id="content">
<?php
if ($_SESSION['prava']=='47') {
	//include('profil.php');
	$id=1;
	$hrac=getHrac($id);
	echo '<p>Profil hraca: '. $hrac['meno']. ' '.$hrac['priezvisko']. ' vek: '.$hrac['vek']. '</p>';
	vypisTreningyHraca($id);
   echo '<br \><br \><br \>';
	getTreningyKlubu("Spoje BA");
}
?>


</div>
  <div class="right">
			<div id="regklub1">	
				
								 <h2>Registrácia Klubu</h2>
						 <div class="desc">  Administrativne udaje o klube </div>
		 		 <form name="nk" id="nk" action="<?php echo $_SERVER['REQUEST_URI']; ?>" method="post">
		<fieldset>
			<legend>Registračné údaje</legend>
				<label for="menok"><strong>Meno klubu:</strong> <br /></label> 
		   	<input name="menok" type="text" size="25" maxlength="30" id="menok" value="<?php if (isset($_POST["menok"])) echo $_POST["menok"]; ?>" /><br />		   	  
		   	  <?php if (isset($_POST["menok"]) && !kotrolatextu($_POST["menok"])) echo "Váš login je zle zadany <br />"; ?>	  
		       		       
		     <label for="sport"> <strong>Sport:</strong></label> <br />
		     <input name="sport" type="text" size="25" maxlength="30" id="email" value="<?php if (isset($_POST["sport"])) echo $_POST["sport"]; ?>" /><br />
		       <?php if (isset($_POST["sport"]) && !kotrolatextu($_POST["sport"])) echo "Sport musi mat nazov aspon 3 znaky<br />"; ?> 
		     <!-- najradcej by som tam dal select kde sa vybere z preddefinovanych sportov -->		     
  		</fieldset>
		</div>
		
		<div id="regklub2">
			
			 <h2>Registrácia Administratora Klubu</h2>
						 <div class="desc">  User ktori bude automaticky trenerom, ale bude jedniny ktori moze   </div>
						       
  		 
		<fieldset>
			<legend>Registračné údaje</legend>
				<label for="login1"><strong>Prihlasovacie meno:</strong> <br /></label> 
		   	<input name="login1" type="text" size="25" maxlength="30" id="login1" value="<?php if (isset($_POST["login1"])) echo $_POST["login1"]; ?>" /><br />
		   	 <?php if (isset($_POST["chyba"]) ) {
		   	 	echo "Duplicitny login!<br />"; 
		   	 	unset($_POST["chyba"]);
		   	 	}?> 
		   	  <?php if (isset($_POST["login1"]) && !kotrolatextu($_POST["login1"])) echo "Váš login je zle zadany <br />"; ?>			
  			  <label for="meno"><strong> Vaše meno a  priezvisko:</strong></label><br /> 
		     <input name="meno" type="text" size="25" maxlength="30" id="meno" value="<?php if (isset($_POST["meno"])) echo $_POST["meno"]; ?>" /><br />
		       <?php if (isset($_POST["meno"]) && !kontrolameno($_POST["meno"])) echo "Vaše meno nemá aspoň 3 znaky alebo obsahuje číslice<br />"; ?>  
	       		       
		     <label for="email"> <strong>Email:</strong></label> <br />
		     <input name="email" type="text" size="25" maxlength="30" id="email" value="<?php if (isset($_POST["email"])) echo $_POST["email"]; ?>" /><br />
		       <?php if (isset($_POST["email"]) && !kontrolaemail($_POST["email"])) echo "Váš email je zle zadany<br />"; ?> 
		     
			<label for="pass"><strong>Heslo:</strong></label> <br />
		   <input name="pass" type="password" size="25" maxlength="30" id="pass" value="" /><br />
		   <?php if ( isset($_POST["pass"])  && !kontrolaheslo($_POST["pass"]) ) echo "heslo nezadane! / Musi mat minimalne6 pismen! <br />"; ?>
		   
		   <label for="pass2"><strong>znova zadajte heslo:</strong></label> <br />
		   <input name="pass2" type="password" size="25" maxlength="30" id="pass2" value="" /> 	<br />
		   <?php if ( isset($_POST["pass2"])  && !kontrolaheslo($_POST["pass2"]) ) echo "heslo nezadane! / Musi mat minimalne6 pismen! <br />"; ?>
		   
		   <?php if (($_POST["pass"]!=$_POST["pass2"]) && isset($_POST["pass"])) echo "hesla sa nezhoduju! <br />"; ?>	     
		     
  				<input type="radio" name="suhlas" id="suhlas" value="1" />
			 <label for="suhlas"><strong>suhlasim s pravidlami portalu</strong></label> <br />
		     <?php  if (isset($_POST["sreg"])) echo "Musíte pristúpiť podmienkam webu"; 
		     //toto sa neuklada a zaroven sa zobrazuje varovanie vzdy aby si pekne vzdy zadal ze ano suhlasi ?> <br />
		  </fieldset>
		   <p><input name="odosliNK" type="submit" id="odosliNK" value="Registruj ma!" /></p>
  		</form>		
		</div>
		
			</div>
			<div class=".right"></div>
			<div class="clearfix"></div>
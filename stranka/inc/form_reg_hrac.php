<script type="text/javascript">
function showHint(str)
{
   
var xmlhttp;
if ( parseInt(str)<0)
  { 
  
   var myList = window.document.reg.menosel;
   
for(var count = myList.options.length - 1; count >= 0; count--)
    {
        myList.options[count] = null;
    }//do nothing
  return;
  }
if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
    	
   var myList = window.document.reg.menosel;
   
for(var count = myList.options.length - 1; count >= 0; count--)
    {
        myList.options[count] = null;
    }    	
    	
    	var odpoved=xmlhttp.responseText;
var pole=new Array();
pole= odpoved.split('~');
var select=document.getElementById("menosel");
for(var i=0; i<pole.length-1; i++) {
	var tadada= pole[i];
	var texty=new Array();
  	   	texty=tadada.split(",");
  	   var option = document.createElement('option');
     option.appendChild(document.createTextNode(texty[1]));
     option.setAttribute('value', texty[0]);
     select.appendChild(option);
    }
    }
  }
xmlhttp.open("GET","inc/vypismien.php?q="+str,true);
xmlhttp.send();
}
</script>
		  <div class="right">
				
				
						 <h2>Registrácia Hráča</h2>
						 <div class="desc">  info  </div>
						       
  		 <form name="reg" id="reg" action="<?php echo $_SERVER['REQUEST_URI']; ?>" method="post">
		<fieldset>
			<legend>Registračné údaje</legend>
				<label for="login1"><strong>Prihlasovacie meno:</strong> <br /></label> 
		   	<input name="login1" type="text" size="25" maxlength="30" id="login1" value="<?php if (isset($_POST["login1"])) echo $_POST["login1"]; ?>" /><br />
		   	 <?php if (isset($_POST["chyba"]) ) {
		   	 	echo "Duplicitny login alebo email alebo oboje!<br />"; 
		   	 	unset($_POST["chyba"]);
		   	 	}?> 
		   	  <?php if (isset($_POST["login1"]) && !kotrolatextu($_POST["login1"])) echo "Váš login je zle zadany <br />"; ?>			
  			  <!-- <label for="meno"><strong> Vaše meno a  priezvisko:</strong></label><br /> 
		     <input name="meno" type="text" size="25" maxlength="30" id="meno" value="<?php if (isset($_POST["meno"])) echo $_POST["meno"]; ?>" /><br />
		       <?php if (isset($_POST["meno"]) && !kontrolameno($_POST["meno"])) echo "Vaše meno nemá aspoň 3 znaky alebo obsahuje číslice<br />"; ?> -->  
	       		       
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

			 <label for="klub"><strong>Zvolte klub Posobenia</strong></label> <br />		     
		     <select name="klub" id="klub" onchange="showHint(this.value);">
		     <option value='-1' > --- </option>
		     	<?php vypiskluby(); ?> 
		     	</select><br />
		     	
		    <label for="menosel"><strong>Zvolte meno a priezvisko</strong></label> <br />		     
		     <select name="menosel" id="menosel">
					<option value='-1' > --- </option>    
		     	</select><br />
		     <div id="txtHint"></div>
  				<input type="radio" name="suhlas" id="suhlas" value="1" />
			 <label for="suhlas"><strong>suhlasim s pravidlami portalu, spristupnenim osobnych udajov trenerom a spoluhracom</strong></label> <br />
		     <?php  if (isset($_POST["sreg"])) echo "Musíte pristúpiť podmienkam serveru"; 
		     //toto sa neuklada a zaroven sa zobrazuje varovanie vzdy aby si pekne vzdy zadal ze ano suhlasi ?> <br />
		     
		     <p><input name="sreg" type="submit" id="submit" value="Registruj ma!" /></p>
  		</fieldset>
  		</form>
		
		
			</div>
			<div class=".right"></div>
			<div class="clearfix"></div>
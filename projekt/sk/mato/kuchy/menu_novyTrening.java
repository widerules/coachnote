package sk.mato.kuchy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

public class menu_novyTrening extends TabActivity {

	//private InputStream dbhracov;
	private sqlPomoc dbhracov= new sqlPomoc(this, "hraci", null, 1);
	private sqlPomoc dbtreningy= new sqlPomoc(this, "treningy", null, 1);
	private sqlPomoc dbzapasy= new sqlPomoc(this, "zapasy", null, 1);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novytrening);
	
		//prve pustenie/pripad vycistenia DB
		if (!sqlPomoc.checkDataBase(getDatabasePath("hraci").getAbsolutePath()))
		{
			//inicializacia 
			dbhracov.inicializujHracskuDB();
			
			//nacitanie assets
			ArrayList<Hrac> hraci= new ArrayList<Hrac>();
			try {
				InputStream a = getAssets().open("hraci.xml");
				hraci= OXml.nacitajHracov(a);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//pridanie do sql
			for (Hrac h : hraci) {
				dbhracov.pridajHraca(h);
			}
			
			Toast
			.makeText(
					getBaseContext(),
					" Automaticky načítaná databaza, odporúčame aktualizáciu!",
					Toast.LENGTH_SHORT).show();
		}
		if (!sqlPomoc.checkDataBase(getDatabasePath("treningy").getAbsolutePath())){
			dbtreningy.inicializujTreningoovuDB();
		}
		
		if (!sqlPomoc.checkDataBase(getDatabasePath("zapasy").getAbsolutePath())){
			dbzapasy.inicializujZapasovuDB();
		}
		
		
		/*File db = new File("/data/data/" + getPackageName()
				+ "/files/hraci.xml");
		if (!db.exists()) {
			try {
				dbhracov = getAssets().open("hraci.xml");
				db.createNewFile();
				OutputStream myOutput = new FileOutputStream(db);

				byte[] buffer = new byte[1024];
				int length;
				while ((length = dbhracov.read(buffer)) > 0) {
					myOutput.write(buffer, 0, length);
				}
				myOutput.flush();
				myOutput.close();
				dbhracov.close();
				Toast
						.makeText(
								getBaseContext(),
								" Automaticky načítaná databaza, odporúčame aktualizáciu!",
								Toast.LENGTH_SHORT).show();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		try {
			File trening = new File("/data/data/" + getPackageName()
					+ "/files/trening.xml");
			trening.createNewFile();
			OutputStream myOutput = new FileOutputStream(trening);
			myOutput.flush();
			myOutput.close();
			Trening cisty = new Trening(new ArrayList<Hrac>(),
					new ArrayList<Zapas>(), -1, "napis popis treningu",
					new Date());
			OXml.vytvorNovyTrening(cisty);
			Toast.makeText(getBaseContext(), "Vytvorený nový tréning!",
					Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// praca s zalozkami

		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, tab_hraci.class);

		spec = tabHost.newTabSpec("ludia").setIndicator("",
				res.getDrawable(R.drawable.ludia)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, tab_zapasy.class);
		spec = tabHost.newTabSpec("zapasy").setIndicator("",
				res.getDrawable(R.drawable.zapasy)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, tab_formular.class);
		spec = tabHost.newTabSpec("poznamky").setIndicator("",
				res.getDrawable(R.drawable.form)).setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(2); // nech sa prva zobrazuje ta sveobecna cast
	}

	public static void zapissubor(InputStream target, String name)
			throws IOException {
		File db = new File("/data/data/sk.mato.kuchy/files/" + name);
		db.createNewFile();
		OutputStream myOutput = new FileOutputStream(db);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = target.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		target.close();

	}
}

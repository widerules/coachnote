package sk.mato.kuchy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CoachnotesActivity extends Activity {
	/** Called when the activity is first created. */
	private sqlPomoc dbhracov = new sqlPomoc(this, "hraci", null, 1);
	private sqlPomoc dbtreningy = new sqlPomoc(this, "treningy", null, 1);
	private sqlPomoc dbzapasy = new sqlPomoc(this, "zapasy", null, 1);
	private sqlPomoc dbtoken = new sqlPomoc(this, "token", null, 1);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// prve pustenie/pripad vycistenia DB
		if (!sqlPomoc.checkDataBase(getDatabasePath("hraci").getAbsolutePath())) {
			// inicializacia
			dbhracov.inicializujHracskuDB();

			// nacitanie assets
			ArrayList<Hrac> hraci = new ArrayList<Hrac>();
			try {
				InputStream a = getAssets().open("hraci.xml");
				hraci = OXml.nacitajHracov(a);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// pridanie do sql
			for (Hrac h : hraci) {
				dbhracov.pridajHraca(h);
			}

			Toast.makeText(getBaseContext(),
					" Automaticky načítaná databaza, odporúčame aktualizáciu!",
					Toast.LENGTH_SHORT).show();
		}
		if (!sqlPomoc.checkDataBase(getDatabasePath("treningy")
				.getAbsolutePath())) {
			dbtreningy.inicializujTreningoovuDB();
			Log.i(Constants._ID, "inicializujem");
		}

		if (!sqlPomoc
				.checkDataBase(getDatabasePath("zapasy").getAbsolutePath())) {
			dbzapasy.inicializujZapasovuDB();
		}

		if (!sqlPomoc.checkDataBase(getDatabasePath("token").getAbsolutePath())) {
			dbtoken.inicializujToken();
		}

		dbhracov.close();
		dbtreningy.close();
		dbzapasy.close();
		dbtoken.close();

		// novy trening
		Button menu_novyTrening = (Button) findViewById(R.id.menu_novyTrening);
		menu_novyTrening.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),
						menu_novyTrening.class);
				startActivityForResult(intent, 0);
			}
		});

		// menu_otvortStare
		Button menu_otvortStare = (Button) findViewById(R.id.menu_otvortStare);
		menu_otvortStare.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),
						menu_otvorStare.class);
				startActivityForResult(intent, 0);
			}
		});

		// upload na server
		Button menu_upload = (Button) findViewById(R.id.menu_upload);
		menu_upload.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(), menu_upload.class);
				startActivityForResult(intent, 0);
			}
		});

		// profil
		Button profil = (Button) findViewById(R.id.profil);
		profil.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(), profil.class);
				startActivityForResult(intent, 0);
			}
		});

		// koniec
		Button koniec = (Button) findViewById(R.id.Koniec2);
		koniec.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				CoachnotesActivity.this.finish();
			}
		});
		
	}
	
	
	
}

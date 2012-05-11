package sk.mato.kuchy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/*
 * treba pridat do treningovejDb a ZapasovejDb parameter ci uz to je zosynchronizovane 
 * Tj. budu sa odosielat iba tie nezosynchonizovane a vsetko co si stiahne z netu bude zosynchonizovane
 * 
 * potom bude problem s tym ak niekto prida nieco na server pomocou webstranky!...este neviem ako*/

public class menu_upload extends Activity {

	private sqlPomoc dbhracov = new sqlPomoc(this, "hraci", null, 1);
	private sqlPomoc dbtreningy = new sqlPomoc(this, "treningy", null, 1);
	private sqlPomoc dbzapasy = new sqlPomoc(this, "zapasy", null, 1);
	private sqlPomoc dbtoken = new sqlPomoc(this, "zapasy", null, 1);

	private ArrayList<Hrac> DB;
	private String token= new String();
	private LinearLayout linlay;
	private ScrollView pohlad;
	private ProgressDialog pd;                   

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		pohlad = new ScrollView(this);
		linlay = new LinearLayout(this);
		pohlad.addView(linlay);

		Button uploadHraci = new Button(this);
		uploadHraci.setText("Synchronizuj");

		uploadHraci.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				pd = ProgressDialog.show(menu_upload.this, "Upload",
						"prebieha Synchronizacia...", true);

				new Thread() {
					public void run() {
						try {
							upload();
						} catch (Exception e) {
						}
						handler.sendEmptyMessage(0);
						pd.dismiss();

					}
				}.start();
			}

			private Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {

				}
			};
		});

		Button uploadTreningov = new Button(this);
		uploadTreningov.setText("Upload Treningov //iniic");
		uploadTreningov.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				pd = ProgressDialog.show(menu_upload.this, "Upload",
						"prebieha upload vsetkych hracov...", true);

				new Thread() {
					public void run() {
						try {
							// uploadDBTreningov();
						} catch (Exception e) {
						}
						handler.sendEmptyMessage(0);
						pd.dismiss();

					}
				}.start();
			}

			private Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
				}
			};
		});

		linlay.addView(uploadHraci);
		linlay.addView(uploadTreningov);
		setContentView(pohlad);
	}

	private void upload() {
		DB = dbhracov.dajCeluDb();
		//token = dbtoken.getToken();

		List<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("token", ""));
		//pridat token meno a token token...

		// DB hracov
		data.add(new BasicNameValuePair("hraci", "ano"));

		data.add(new BasicNameValuePair("pocet_h", "" + DB.size()));

		for (int i = 0; i < DB.size(); i++) {
			data.add(new BasicNameValuePair("id_" + i, "" + DB.get(i).getId()
					+ ""));
			data.add(new BasicNameValuePair("meno_" + i, ""
					+ DB.get(i).getMeno()));
		
			data.add(new BasicNameValuePair("priezvisko_" + i, ""
					+ DB.get(i).getPriezvisko()));
			data
					.add(new BasicNameValuePair("vek_" + i, ""
							+ DB.get(i).getVek()));
			data.add(new BasicNameValuePair("respekt_" + i, ""
					+ DB.get(i).getRespekt()));
		}
		// DB zapasov
		Cursor cursorzapasy = dbzapasy.getReadableDatabase().rawQuery(
				"SELECT * FROM `zapasy` ", new String[] {});

		data.add(new BasicNameValuePair("zapasy", "ano"));

		int i = 0;
		for (cursorzapasy.moveToFirst(); !cursorzapasy.isAfterLast(); cursorzapasy
				.moveToNext()) {
			data.add(new BasicNameValuePair("id_" + i, cursorzapasy
					.getString(0)));
			data.add(new BasicNameValuePair("typ_" + i, cursorzapasy
					.getString(1)));
			data.add(new BasicNameValuePair("teamA_" + i, cursorzapasy
					.getString(3)));
			data.add(new BasicNameValuePair("teamB_" + i, cursorzapasy
					.getString(2)));
			data.add(new BasicNameValuePair("datum_" + i, cursorzapasy
					.getString(4)));
			data.add(new BasicNameValuePair("vysledok_" + i, cursorzapasy
					.getString(5)));
			data.add(new BasicNameValuePair("vytaz_" + i, cursorzapasy
					.getString(6)));
			i++;
		}
		cursorzapasy.close();

		data.add(new BasicNameValuePair("pocet_z", i + ""));

		// DB treningov
		Cursor cursor = dbtreningy.getReadableDatabase().rawQuery(
				"SELECT * FROM `treningy` ", new String[] {});

		data.add(new BasicNameValuePair("treningy", "ano"));
		i = 0;
		Log.d("post", cursor.getCount()+"");
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Log.d("post", cursor.getString(1));
			data.add(new BasicNameValuePair("datum_" + i, cursor.getString(1)));
			data.add(new BasicNameValuePair("pocetKurtov_" + i, cursor
					.getString(3)));
			data.add(new BasicNameValuePair("poznamka_" + i, cursor
					.getString(2)));
			data.add(new BasicNameValuePair("hraci_" + i, cursor.getString(4)));
			data
					.add(new BasicNameValuePair("zapasy_" + i, cursor
							.getString(5)));
			i++;
		}
		cursor.close();
		data.add(new BasicNameValuePair("pocet_t", i + ""));
		Log.d("postaaaaaaaa", i+"");
		
		try {
			WebUtilities
					.post(
							"http://www.st.fmph.uniba.sk/~kuchynar1/rp/?p=system",
							data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

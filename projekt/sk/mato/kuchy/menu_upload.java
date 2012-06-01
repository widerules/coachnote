package sk.mato.kuchy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

/*
 * 
 * FIXME jediny problem je v primani zapasov proste tu niesu...
 * 
 * */

public class menu_upload extends Activity {

	private sqlPomoc dbhracov = new sqlPomoc(this, "hraci", null, 1);
	private sqlPomoc dbtreningy = new sqlPomoc(this, "treningy", null, 1);
	private sqlPomoc dbzapasy = new sqlPomoc(this, "zapasy", null, 1);
	private sqlPomoc dbtoken = new sqlPomoc(this, "token", null, 1);
	private NotificationManager nm;

	private ArrayList<Hrac> DB;
	private String token = new String();
	private LinearLayout linlay;
	private ScrollView pohlad;
	private ProgressDialog pd;
	private TextView res;
	private String odpoved;
	private String responze;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		res = new TextView(this);
		pohlad = new ScrollView(this);
		linlay = new LinearLayout(this);
		linlay.setOrientation(LinearLayout.VERTICAL);
		pohlad.addView(linlay);

		Button uploadHraci = new Button(this);
		uploadHraci.setText("Synchronizuj");

		uploadHraci.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	pd = ProgressDialog.show(menu_upload.this, "Upload",
			//			"prebieha Synchronizacia...", true);
				new Thread() {
					public void run() {
						try {
							ukazNotifikaciu();
							upload();
							schovajNotifikaciu();
						} catch (Exception e) {
						}
				//		handler.sendEmptyMessage(0);
				//		pd.dismiss();

					}
				}.start();
			}
/*
			private Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {

				}
			};*/
		});
		

		Button uploadTreningov = new Button(this);
		uploadTreningov.setText("skontroluj platnost");
		uploadTreningov.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				pd = ProgressDialog.show(menu_upload.this, "pracuje..",
						"kontrolujem token...", true, false);
				new Thread() {
					public void run() {
						try {
							if (chcekHesla())
								odpoved = "kontrola udajov uspesna!";
							else
								odpoved = "kontrola udajov NEuspesna!";
							// pd.setTitle(odpoved);
							// this.wait(100);
						} catch (Exception e) {

						}
						handler.sendEmptyMessage(0);

					}

				}.start();
			}

			private Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					pd.dismiss();
					res.setText(odpoved + " ");
				}
			};
		});

		linlay.addView(uploadHraci);
		linlay.addView(uploadTreningov);
		linlay.addView(res);
		setContentView(pohlad);
	}

	private void upload() {
		DB = dbhracov.dajCeluDb();
		token = dbtoken.getToken();
		// String ret = dbtoken.getToken();
		String udaje[] = null;
		if (token != null) {
			udaje = token.split("/");
		} else {
			Intent mojIntent = new Intent(menu_upload.this, profil.class);
			menu_upload.this.startActivity(mojIntent);
			finish();
		}

		List<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("udaje", "ano"));
		data.add(new BasicNameValuePair("token", udaje[0]));
		Log.d("token", udaje[0]);
		data.add(new BasicNameValuePair("login", udaje[1]));
		Log.d("login", udaje[1]);
		// pridat token meno a token token...

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
			data.add(new BasicNameValuePair("vek_" + i, "" + DB.get(i).getVek()));
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
		// Log.d("post", cursor.getCount() + "");
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			// Log.d("post", cursor.getString(1));
			data.add(new BasicNameValuePair("uid_" + i, cursor.getString(6)));
			data.add(new BasicNameValuePair("datum_" + i, cursor.getString(1)));
			data.add(new BasicNameValuePair("pocetKurtov_" + i, cursor
					.getString(3)));
			data.add(new BasicNameValuePair("poznamka_" + i, cursor
					.getString(2)));
			data.add(new BasicNameValuePair("hraci_" + i, cursor.getString(4)));
			data.add(new BasicNameValuePair("zapasy_" + i, cursor.getString(5)));
			Log.d("zapasy string ",  cursor.getString(5)+ "nazov:"+"zapasy_" + i);
			i++;
		}
		cursor.close();
		data.add(new BasicNameValuePair("pocet_t", i + ""));

		dbhracov.close();
		dbtreningy.close();
		dbzapasy.close();
		// Log.d("postaaaaaaaa", data.toString());

		try {
			responze = new String(WebUtilities.post(
					"http://www.st.fmph.uniba.sk/~kuchynar1/rp/?p=andr", data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.clear();
		Log.d("odpoved", responze);
		String pouzitelne = responze.substring(responze
				.indexOf("<p id=\"odpovedTReningyDb\">"));
		pouzitelne = pouzitelne.substring(0, pouzitelne.indexOf("</p>"));
		
		preberData(pouzitelne);

	}

	private void preberData(String info) {
		// TODO Auto-generated method stub
		dbhracov = new sqlPomoc(this, "hraci", null, 1);
		dbtreningy = new sqlPomoc(this, "treningy", null, 1);
		dbzapasy = new sqlPomoc(this, "zapasy", null, 1);
		dbtoken = new sqlPomoc(this, "token", null, 1);

		dbtreningy.vycistiTreningovuDB();
		dbhracov.vycistiHracskuDB();
		dbzapasy.vycistiZapasovuDB();

		info = info.substring(45); // tam je ze <p id=\"odpovedTReningyDb\">

		String pole[] = info.substring(0, info.indexOf("<div id=\"zapasy\">"))
				.split("<br><br>");
		for (String string : pole) {
			Log.d("cele treningy", string);
			String posledne[] = string.split("~");
			if (posledne.length == 7) {
				dbtreningy.pridajTreningRaw(posledne[0], posledne[1],
						posledne[2], posledne[3], posledne[4], posledne[6]);
				Log.d("pridavam", posledne[0] + posledne[1] + posledne[2]
						+ posledne[3] + posledne[4] + posledne[6]);
			}
		}

		String pole2[] = info.substring(
				info.indexOf("<div id=\"zapasy\">") + 17,
				info.indexOf("<div id=\"hraci\">")).split("<br><br>");
		for (String string : pole2) {
			Log.d("cele zapasy", string);
			String posledne[] = string.split("~");
			if (posledne.length == 7) {
				dbzapasy.pridajZapasRaw(posledne[0], posledne[1], posledne[2],
						posledne[3], posledne[4], posledne[5], posledne[6]) ;
				Log.d("pridavam", posledne[0] + posledne[1] + posledne[2]
						+ posledne[3] + posledne[4] + posledne[6]);
			}
		}

		String pole3[] = info
				.substring(info.indexOf("<div id=\"hraci\">") + 16).split(
						"<br><br>");
		for (String string : pole3) {
			Log.d("cele hraci", string);
			String posledne[] = string.split("~");
			if (posledne.length == 5) {
				dbhracov.pridajHracaRaw(posledne[0], posledne[1], posledne[2],
						posledne[3], posledne[4]);
				Log.d("pridavam", posledne[0] + posledne[1] + posledne[2]
						+ posledne[3] + posledne[4]);
			}
		}
		dbhracov.close();
		dbtreningy.close();
		dbzapasy.close();
	}

	/*
	 * /*Log.d("cele", string); Log.d("datum:", posledne[0]); //datum treningu
	 * Log.d("Popis:", posledne[1]); Log.d("pocet kurtov:", posledne[2]);
	 * Log.d("ludia:", posledne[3]); Log.d("zapasy:", posledne[4]);
	 * Log.d("trener:", posledne[5]); Log.d("uid:", posledne[6]); Log.d("id",
	 * i+""); Log.d("duplicita",
	 * ""+dbtreningy.kontrolaDuplicityTreningu(posledne[6]));
	 */
	// if (dbtreningy.kontrolaDuplicityTreningu(posledne[6])) {

	// Log.d("pridavam", "trening!");
	// }*/

	private boolean chcekHesla() {
		// TODO Auto-generated method stub
		token = dbtoken.getToken();
		// String ret = dbtoken.getToken();
		String udaje[] = null;
		if (token != null) {
			udaje = token.split("/");
		} else {
			Intent mojIntent = new Intent(menu_upload.this, profil.class);
			menu_upload.this.startActivity(mojIntent);
			finish();
		}
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("kontrola", "ano"));
		data.add(new BasicNameValuePair("token", udaje[0]));
		data.add(new BasicNameValuePair("login", udaje[1]));

		try {
			String odpoved = new String(WebUtilities.post(
					"http://www.st.fmph.uniba.sk/~kuchynar1/rp/?p=andr", data));
			 Log.d("odpovede", odpoved+"");
			if (odpoved.contains(new String("kontrola udajov uspesna!")))
				return true;
			else
				return false;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.clear();
		return false;
	}
	
	private void ukazNotifikaciu(){
		
		 nm= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		int icon = R.drawable.sync;
		
		//CharSequence tickerText = "Hello";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, "synchronizacion in progress..", when);
		
		Context context = getApplicationContext();
		CharSequence contentTitle = "";
		CharSequence contentText = "";
		
		Intent notificationIntent = new Intent(this, menu_upload.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		nm.notify(1, notification);
	}
	
	private void schovajNotifikaciu() {
		nm= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(1);
	}
	
	
}

package sk.mato.kuchy;
//Log.i(Constants._ID, novy.getTeamA().getPriezvisko());
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class menu_zobrazStare extends Activity {

	private sqlPomoc dbhracov = new sqlPomoc(this, "hraci", null, 1);
	private sqlPomoc dbtreningy = new sqlPomoc(this, "treningy", null, 1);
	private sqlPomoc dbzapasy = new sqlPomoc(this, "zapasy", null, 1);
	private int idTreningu;

	private Trening trening;
	private ScrollView pohlad;
	private LinearLayout linlay;
	private TextView statystiky;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		idTreningu = Integer.parseInt(getIntent().getExtras().getString("id"));

		pohlad = new ScrollView(this);
		linlay = new LinearLayout(this);
		statystiky = new TextView(this);

		// otvor trening
		Cursor cursor = dbtreningy.getReadableDatabase().rawQuery(
				"SELECT * FROM `treningy` WHERE `id` = '" + idTreningu + "' ",
				new String[] {});

		Date novyDatumTreningu = new Date();
		String novyPopisTreningu = new String();
		int novyPocetKurtov = 0;
		ArrayList<Zapas> noveZapasy = new ArrayList<Zapas>();
		ArrayList<Hrac> novyHraci = new ArrayList<Hrac>();
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		// aj to bude iba jeden treining...
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			
			try {
				novyDatumTreningu = df.parse(cursor.getString(1));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			novyPopisTreningu = cursor.getString(2);
			novyPocetKurtov = Integer.parseInt(cursor.getString(3));

			String[] hraciString = cursor.getString(4).split("/");
			for (int i = 0; i < hraciString.length; i++) {
				try{
					novyHraci.add(dbhracov.getHraca(Integer.parseInt(hraciString[i])));
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}

			String[] zapasyString = cursor.getString(5).split("/");
			
			Log.i(Constants._ID, zapasyString.length+"");
			
			for (int i = 0; i < zapasyString.length; i++) {
				try{
					noveZapasy.add(dbzapasy.getZapas(Integer
							.parseInt(zapasyString[i]), novyHraci));
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// musel byt na tom treningu aby hral dajaky zapas...ak nie
				// nenajde to a samo sa to v vymaze
			}
		}

		trening = new Trening(novyHraci, noveZapasy, novyPocetKurtov,
				novyPopisTreningu, novyDatumTreningu);

		statystiky.setText("statistyky z treningy z:"
				+ trening.getDatumTreningu().toString() + "\n");

		statystiky.append("popis treningu:\n" + trening.getPopisTreningu()
				+ "\n\n");

		statystiky.append("pocetkurtov: " + trening.getPocetKurtov() + "\n\n");

		statystiky.append("dochadzka:\n" + trening.vypisHracov() + "\n\n");

		statystiky.append("zapasy:\n" + trening.vypisZapasy(-1) + "\n\n");

		trening.vytvorRebricek();

		linlay.addView(statystiky);
		pohlad.addView(linlay);
		setContentView(pohlad);
		
	}

}

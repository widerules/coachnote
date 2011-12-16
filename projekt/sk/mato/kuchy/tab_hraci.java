package sk.mato.kuchy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class tab_hraci extends Activity {

	private int GET_CODE;
	private Trening trening;
	private TextView vypis;
	private ScrollView pohlad;
	private LinearLayout linLay;
	private InputStream akt, dbhracov;
	private ArrayList<Hrac> hracilist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vypis = new TextView(tab_hraci.this);
		// nacitanie databaz
		try {
			dbhracov = openFileInput("hraci.xml");
			akt = openFileInput("trening.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		}

		trening = OXml.nacitajTrening(akt, OXml.nacitajHracov(dbhracov));
		// hlavicka
		vypis.append("Na treningu sa zucastnuli: \n");
		vypis.append(trening.vypisHracov());

		pohlad = new ScrollView(this);
		linLay = new LinearLayout(this);
		linLay.setOrientation(LinearLayout.VERTICAL);
		pohlad.addView(linLay);

		linLay.addView(vypis);
		setContentView(pohlad);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			switch (resultCode) {
			case RESULT_CANCELED:
				finish();
				break;
			case  1:
				try {
					hracilist = OXml.nacitajHracov(openFileInput("hraci.xml"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					finish();
				}
				trening.pridajHracovText(new StringBuffer(data.getAction()), hracilist);
				break;
			case  -1:
				trening.vymazHracovText(new StringBuffer(data.getAction()), trening.getHracov());
				break;
			
			
			}
			try {
				OXml.vytvorNovyTrening(trening);
				Toast.makeText(getBaseContext(),
						"hrac uspesne pridany/odobrany!", Toast.LENGTH_LONG)
						.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getBaseContext(),
						"hrac NEuspesne pridany/odobrany!", Toast.LENGTH_LONG)
						.show();
			}
			vypis.setText("na treningu sa zucastnili: \n"
					+ trening.vypisHracov() + "\n");
			
		}	
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		new MenuInflater(this).inflate(R.menu.option, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.add) {
			// startActivity(new Intent(menu_novyTrening.this, pridajhraca.class
			// ) );
			// return true;
			Intent intent = new Intent(tab_hraci.this, pridajhraca.class);
			startActivityForResult(intent, GET_CODE);
		}
		if (item.getItemId() == R.id.del) {

			Intent intent = new Intent(tab_hraci.this, vymazhraca.class);
			startActivityForResult(intent, GET_CODE);
		}
		return super.onOptionsItemSelected(item);
	}
}

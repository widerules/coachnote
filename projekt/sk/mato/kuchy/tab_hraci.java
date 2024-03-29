package sk.mato.kuchy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
	private InputStream akt;
	
	//private InputStream dbhracov;
	private ArrayList<Hrac> hracilist;
	
	private sqlPomoc dbhraci= new sqlPomoc(this, "hraci", null, 1);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vypis = new TextView(tab_hraci.this);
	/*	// nacitanie databaz
		try {
			dbhracov = openFileInput("hraci.xml");
			akt = openFileInput("trening.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		}

		trening = OXml.nacitajTrening(akt, OXml.nacitajHracov(dbhracov));
		*/
		
		ArrayList<Hrac> dbHracov= dbhraci.dajCeluDb();
		
		
		trening= OXml.nacitajTrening(akt, dbHracov);
		
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
				/*try {
					hracilist = OXml.nacitajHracov(openFileInput("hraci.xml"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					finish();
				}
				*/
				hracilist=dbhraci.dajCeluDb();
				trening.setHraci(new ArrayList<Hrac>());
				trening.zmenHracovText(new StringBuffer(data.getAction()), hracilist);
				break;
			/*case  -1:
				trening.vymazHracovText(new StringBuffer(data.getAction()), trening.getHracov());
				break;
			*/
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
			Intent intent = new Intent(tab_hraci.this, pridajhraca.class);
			startActivityForResult(intent, GET_CODE);
		}
		
		/*if (item.getItemId() == R.id.del) {
			Intent intent = new Intent(tab_hraci.this, pridajhraca.class);
			startActivityForResult(intent, GET_CODE);
		}*/
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			final AlertDialog alertDialog = new AlertDialog.Builder(
					tab_hraci.this).create();
			alertDialog.setTitle("POZOR");
			alertDialog.setMessage("tento trening nebude ulozeny!");
			alertDialog.setButton("exit",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							tab_hraci.this.finish();
						}
					});
			alertDialog.setButton2("cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			alertDialog.show();
			return true;
		}
		else {
			return super.onKeyDown(keyCode, event);
		}

	}
}

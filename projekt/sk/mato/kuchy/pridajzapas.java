package sk.mato.kuchy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class pridajzapas extends Activity {
	private Trening trening;
	private InputStream akt;
	private sqlPomoc dbhraci = new sqlPomoc(this, "hraci", null, 1);
	private ArrayList<Hrac> hraci = new ArrayList<Hrac>();
	private String A = "nic", B = "nic";
	private ArrayList<Hrac> dochadzka;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// nacitaj
		try {
			akt = openFileInput("trening.xml");
			hraci = dbhraci.dajCeluDb();
			trening = OXml.nacitajTrening(akt, hraci);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setContentView(R.layout.pridajzapas);

		dochadzka = trening.getHracov();
		final ArrayList<String> dochadzkaString = new ArrayList<String>();
		//dochadzkaString.add("----");
		for (Hrac hrac : dochadzka) {
			dochadzkaString.add(hrac.getMeno() + " " + hrac.getPriezvisko());
		}

		if (dochadzkaString.size() < 2) {
			// na toto musi mat aspon 2 hracov!
			finish();
		}

		final Spinner teamA = (Spinner) findViewById(R.id.teamB);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dochadzkaString);
		
		teamA.setAdapter(adapter);
		teamA.setSelection(Adapter.NO_SELECTION);
		
		

		final Spinner teamB = (Spinner) findViewById(R.id.teamA);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dochadzkaString);
		teamB.setAdapter(adapter2);
		teamA.setSelection(Adapter.NO_SELECTION);
		teamB.setOnItemSelectedListener( new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				B = parent.getItemAtPosition(pos).toString(); 
				/*if (A==B && !A.equals("----")) {
					AlertDialog alertDialog = new AlertDialog.Builder(
							pridajzapas.this).create();
					alertDialog.setTitle("POZOR");
					alertDialog.setMessage("nemoze hrat sam so sebou!");
					alertDialog.setButton("exit",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									B="nic";
									teamB.setSelection(0);
								}
							});
					alertDialog.show();
				}			*/
				if (A==B) {
					if (pos+1 <= dochadzkaString.size()-1 ) teamA.setSelection(pos+1);
					else 
						if (pos!=0) teamA.setSelection(pos-1);
					}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				B = "nic";
			}
		} );
		
		teamA.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				A = parent.getItemAtPosition(pos).toString();			
				if (A==B) {
					if (pos+1 <= dochadzkaString.size()-1 ) teamB.setSelection(pos+1);
					else 
						if (pos!=0) teamB.setSelection(pos-1);
					}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				A = "nic";
			}

		});

		Button odosli = (Button) findViewById(R.id.odosliZapas);

		odosli.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (A.equals("nic") ||  B.equals("nic") || A.equals("----") || B.equals("----") ) {
					AlertDialog alertDialog = new AlertDialog.Builder(
							pridajzapas.this).create();
					alertDialog.setTitle("POZOR");
					alertDialog.setMessage("nezadany aspon jeden z hracov!");
					alertDialog.setButton("exit",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
								}
							});
					alertDialog.show();
					return;
				}
				
				Hrac a = null,b = null;
				for (Hrac s : dochadzka) {
					if (A.equals( s.getMeno() + " " + s.getPriezvisko() )) a=s;
					if (B.equals( s.getMeno() + " " + s.getPriezvisko() )) b=s;
				}
				
				if (a==null || b==null) return;
				
				Dvojhra novy = new Dvojhra(a, b);
				
				trening.addZapasy(novy);
				
				//uloz trening
				try {
					OXml.vytvorNovyTrening(trening);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				finish();
			}
		});

	}
}

package sk.mato.kuchy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class tab_zapasy extends Activity {

	
	private Trening trening;
	private InputStream akt;
	
	//private InputStream dbhracov;
	private sqlPomoc dbhraci= new sqlPomoc(this, "hraci", null, 1);
	private ArrayList<Hrac> hraci= new ArrayList<Hrac>();
	
	private ScrollView zoznam;
	private RelativeLayout pohlad;
	private LinearLayout linLay;
	private ArrayList<TextView> nadpisy;
	private ArrayList<RadioButton> vytazy;
	private ArrayList<Spinner> spinnery;
	private int pozicia=0;
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		uloz();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		nacitaj();
		vypisuj();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		zoznam = new ScrollView(this);
		linLay = new LinearLayout(this);
		linLay.setOrientation(LinearLayout.VERTICAL);
		zoznam.addView(linLay);
		
		// nacitanie databaz
		nacitaj();
		//vykreslenie
		vypisuj();
	}

	private void vymaz( int index) {
		ArrayList<Zapas> a= trening.getZapasy();
		a.remove(0);
		trening.setZapasy(a);
		
		uloz();
		vypisuj();
	}
	
	private boolean vypisuj() {
		zoznam = new ScrollView(this);
		linLay = new LinearLayout(this);
		linLay.setOrientation(LinearLayout.VERTICAL);
		zoznam.addView(linLay);

		// pozor mozna chyba pri deletovani hraca co uz ma zapas to urcite
		// spadne!

		if (trening.getZapasy().size() < 1)
			trening.generujZapasy();
		//natotok by osm pridal tlacitko

		TextView rebricek = new TextView(this);
		rebricek.setText("Akutalny rebricek:\n" + trening.vypisRebricek());
		linLay.addView(rebricek);

		TextView nadpis = new TextView(this);
		nadpis.setText("Odporucane zapasy: \n");
		linLay.addView(nadpis);

		
		nadpisy = new ArrayList<TextView>();
		vytazy = new ArrayList<RadioButton>();
		spinnery = new ArrayList<Spinner>();
		int pocetPreIDcka = 0;
		for (int i = 0; i < trening.getZapasy().size() ; i++) {
			Zapas zapas= trening.getZapasy().get(i);
			pozicia=i;
			
			//vymazavaci button
			Button vymazZapas = new Button(this);
			vymazZapas.setText("vymaz tento Zapas");
			
			vymazZapas.setOnClickListener( new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					vymaz(pozicia);
				}
			}); 
	
			linLay.addView(vymazZapas);
			
			// vypise dvojicu...
			TextView tv = new TextView(this);
			tv.setText(zapas.tostring());
			linLay.addView(tv);
			nadpisy.add(tv);

			// 2 radio button ze ci vyhral jeden alebo druhy
			RadioGroup vytaz = new RadioGroup(this);
			vytaz.setOrientation(RadioGroup.HORIZONTAL);

			RadioButton Ahrac = new RadioButton(this);
			Ahrac.setText("Vyhral: Team A" );
			RadioButton Bhrac = new RadioButton(this);
			Bhrac.setText("Vyhral: Team B" );

			vytaz.addView(Ahrac);
			vytaz.addView(Bhrac);
			// 2*i~!
			vytazy.add(Ahrac);
			vytazy.add(Bhrac);

			linLay.addView(vytaz);

			// spinner na to ze na kolko vyber mozsnosti by mal byt od 0-30
			Spinner naKolko = new Spinner(this);
			naKolko.setPrompt("na kolko skoncil zapas?");
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this, R.array.zapas_array,
							android.R.layout.simple_spinner_item);
			adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			naKolko.setAdapter(adapter);
			naKolko.setId(pocetPreIDcka);
			pocetPreIDcka++;
			spinnery.add(naKolko);
			linLay.addView(naKolko);

			TextView delimeter = new TextView(this);
			delimeter.setText("\n\n");
			linLay.addView(delimeter);
			nadpisy.add(tv);
		}

		// zberdat
		LinearLayout butony = new LinearLayout(this);
		Button odosli = new Button(this);
		odosli.setText("Odosli vysledky!");

		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		butony.setLayoutParams(param);

		odosli.setLayoutParams(param);

		Button generujZnova = new Button(this);
		generujZnova.setText("generuj dalsie kolo!");

		butony.addView(generujZnova);

		generujZnova.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				trening.generujZapasy();
				uloz();
				vypisuj();

			}
		});

		odosli.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						ArrayList<Zapas> zapisuj = new ArrayList<Zapas>();
						zapisuj = trening.getZapasy();
						int i = 0;
						for (Zapas zapas : zapisuj) {

							if (vytazy.get(2 * i).isChecked())
								zapas.setVytaz(1);
							if (vytazy.get(2 * i + 1).isChecked())
								zapas.setVytaz(2);
							
							int vysledok = -1;
							vysledok = spinnery.get(i)
									.getSelectedItemPosition() - 1;
							zapas.setVysledok(vysledok);
							i++;

						}

						trening.setZapasy(zapisuj);
						try {
							OXml.vytvorNovyTrening(trening);
							Toast.makeText(getBaseContext(),
									"Zapasy, vysledky ulozene!",
									Toast.LENGTH_LONG).show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(getBaseContext(),
									"Zapasy, vysledky NEulozene!",
									Toast.LENGTH_LONG).show();
						}
					}
				});
		butony.addView(odosli);

		pohlad = new RelativeLayout(this);
		pohlad.addView(zoznam);
		pohlad.addView(butony);
		setContentView(pohlad);
		return true;
	}

	private void nacitaj() {
		/*try {
			dbhracov = openFileInput("hraci.xml");
			akt = openFileInput("trening.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		}

		
		trening = OXml.nacitajTrening(akt, OXml.nacitajHracov(dbhracov));
		*/
		try {
			akt = openFileInput("trening.xml");
			hraci=dbhraci.dajCeluDb();
			trening = OXml.nacitajTrening(akt, hraci);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void uloz() {
		try {
			OXml.vytvorNovyTrening(trening);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			final AlertDialog alertDialog = new AlertDialog.Builder(
					tab_zapasy.this).create();
			alertDialog.setTitle("POZOR");
			alertDialog.setMessage("tento trening nebude ulozeny!");
			alertDialog.setButton("exit",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							tab_zapasy.this.finish();
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

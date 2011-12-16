package sk.mato.kuchy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
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
	private InputStream akt, dbhracov;
	private ScrollView zoznam;
	private RelativeLayout pohlad;
	private LinearLayout linLay;
	private ArrayList<TextView> nadpisy;
	private ArrayList<RadioButton> vytazy;
	private ArrayList<Spinner> spinnery;

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

		TextView rebricek = new TextView(this);
		rebricek.setText("Akutalny rebricek:\n" + trening.vypisRebricek());
		linLay.addView(rebricek);

		TextView nadpis = new TextView(this);
		nadpis.setText("Odporucane zapasy: \n");
		linLay.addView(nadpis);

		ArrayList<Zapas> zapasy = new ArrayList<Zapas>();
		zapasy = trening.getZapasy();
		nadpisy = new ArrayList<TextView>();
		vytazy = new ArrayList<RadioButton>();
		spinnery = new ArrayList<Spinner>();
		int pocetPreIDcka = 0;
		for (Zapas zapas : zapasy) {
			// vypise dvojicu...
			TextView tv = new TextView(this);
			tv.setText(zapas.getA().getMeno() + " "
					+ zapas.getA().getPriezvisko() + " vs. "
					+ zapas.getB().getMeno() + " "
					+ zapas.getB().getPriezvisko());
			linLay.addView(tv);
			nadpisy.add(tv);

			// 2 radio button ze ci vyhral jeden alebo druhy
			RadioGroup vytaz = new RadioGroup(this);
			vytaz.setOrientation(RadioGroup.HORIZONTAL);

			RadioButton Ahrac = new RadioButton(this);
			Ahrac.setText("Vyhral:" + zapas.getA().getPriezvisko());
			RadioButton Bhrac = new RadioButton(this);
			Bhrac.setText("Vyhral:" + zapas.getB().getPriezvisko());

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

		odosli.setOnClickListener(new OnClickListener() {// totok nefunguje!

					public void onClick(View v) {
						// TODO Auto-generated method stub
						ArrayList<Zapas> zapisuj = new ArrayList<Zapas>();
						zapisuj = trening.getZapasy();
						int i = 0;
						for (Zapas zapas : zapisuj) {

							if (vytazy.get(2 * i).isChecked())
								zapas.setVytaz(zapas.getA());
							if (vytazy.get(2 * i + 1).isChecked())
								zapas.setVytaz(zapas.getB());
							
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
		try {
			dbhracov = openFileInput("hraci.xml");
			akt = openFileInput("trening.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		}

		trening = OXml.nacitajTrening(akt, OXml.nacitajHracov(dbhracov));
	}

	private void uloz() {
		try {
			OXml.vytvorNovyTrening(trening);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

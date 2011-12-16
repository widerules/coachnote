package sk.mato.kuchy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class pridajhraca extends ListActivity {
	// musim uplne prekodit ten GUI
	private ArrayList<Hrac> hracilist;
	private ArrayList<String> hraciMena;
	private ArrayList<String> result;
	private OnItemClickListener piker;
	private String vysledok;
	private int GET_CODE = 0;
	private int RESULT_ADD = 1; // 1 znaci ze pridavam

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vysledok = new String();
		result = new ArrayList<String>();
		piker = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(
						getApplicationContext(),
						((TextView) view).getText()
								+ " bol/a pridany na tento trening",
						Toast.LENGTH_SHORT).show();
				String item = new String((String) ((TextView) view).getText());
				if (item.equals("Pridaj noveho hraca co nieje v DB")) {
					Intent intent = new Intent(pridajhraca.this, novyhrac.class);
					startActivityForResult(intent, GET_CODE);

				}

				if (item.equals("Koniec")) {
					setResult(RESULT_ADD, (new Intent()).setAction(vysledok));
					finish();
				} else {

					result.add(item);
					vysledok += item;
				}

			}
		};

		// nacitanie hracov

		try {
			InputStream dbhracov = openFileInput("hraci.xml");
			hracilist = OXml.nacitajHracov(dbhracov);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// vybranie mien
		hraciMena = new ArrayList<String>();
		hraciMena.add("Koniec");

		hraciMena.add("Pridaj noveho hraca co nieje v DB");
		for (Hrac s : hracilist) {
			hraciMena.add(s.getMeno() + " " + s.getPriezvisko() + "\n");
		}

		setListAdapter(new ArrayAdapter<String>(this, R.layout.add_hrac,
				hraciMena));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(piker);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		vysledok = new String();
		vysledok = "add"; // asi by bolo vhodne totok nahradit GET_CODE-dom
		result = new ArrayList<String>();
		piker = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(
						getApplicationContext(),
						((TextView) view).getText()
								+ " bol/a pridany na tento trening",
						Toast.LENGTH_SHORT).show();
				String item = new String((String) ((TextView) view).getText());

				if (item.equals("Novy hrac!")) {
					Intent intent = new Intent(pridajhraca.this, novyhrac.class);
					startActivityForResult(intent, GET_CODE);
				}

				if (item.equals("Koniec")) {
					setResult(RESULT_OK, (new Intent()).setAction(vysledok));
					finish();
				} else {

					result.add(item);
					vysledok += item;
				}

			}
		};

		// nacitanie hracov

		try {
			InputStream dbhracov = openFileInput("hraci.xml");
			hracilist = OXml.nacitajHracov(dbhracov);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// vybranie mien
		hraciMena = new ArrayList<String>();
		hraciMena.add("Koniec");
		hraciMena.add("Pridaj noveho hraca co nieje v DB");
		for (Hrac s : hracilist) {
			hraciMena.add(s.getMeno() + " " + s.getPriezvisko() + "\n");
		}

		setListAdapter(new ArrayAdapter<String>(this, R.layout.add_hrac,
				hraciMena));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(piker);
	}

}

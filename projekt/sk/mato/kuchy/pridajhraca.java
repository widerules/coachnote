package sk.mato.kuchy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class pridajhraca extends Activity {

	private sqlPomoc dbHracov=new sqlPomoc(this, "hraci", null, 1);
	private ArrayList<Hrac> hracilist= new ArrayList<Hrac>();
	private OnItemClickListener piker;
	private String vysledok= new String();
	private int GET_CODE = 0; // pre vytvorenie hraca co nieje v DB
	private int RESULT_ADD = 1; // 1 znaci ze pridavam pre

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.pridajhraca);
		
		hracilist=dbHracov.dajCeluDb();
		
		super.onCreate(savedInstanceState);
		vysledok = new String();
		piker = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(
						getApplicationContext(),
						hracilist.get(position).getMeno() + " "
								+ hracilist.get(position).getPriezvisko()
								+ " bol/a pridany na tento trening",
						Toast.LENGTH_SHORT).show();
				vysledok += hracilist.get(position).getMeno() + " "
						+ hracilist.get(position).getPriezvisko() + "\n";
			}
		};

		// nacitanie hracov
/*
		try {
			InputStream dbhracov = openFileInput("hraci.xml");
			hracilist = OXml.nacitajHracov(dbhracov);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		// list View
		ListView lv = (ListView) findViewById(R.id.listview);

		String[] from = new String[] { "id", "meno", "priez", "vek", "res" };
		int[] to = new int[] { R.id.item1, R.id.item2, R.id.item3, R.id.item4,
				R.id.item5 };

		List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
		for (int j = 0; j < hracilist.size(); j++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", "" + j);
			map.put("meno", hracilist.get(j).getMeno());
			map.put("priez", hracilist.get(j).getPriezvisko());
			map.put("vek", hracilist.get(j).getVek() + "");
			map.put("res", hracilist.get(j).getRespekt() + "");
			fillMaps.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
				R.layout.add_hrac, from, to);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(piker);

		// koniec Button
		Button koniec = (Button) findViewById(R.id.koniec);
		koniec.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (vysledok.equals(new String())) vysledok="47 47\n";
				setResult(RESULT_ADD, (new Intent()).setAction(vysledok));
				finish();
			}
		});

		// pridaj noveho hraca Button
		Button novyH = (Button) findViewById(R.id.novyH);
		novyH.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(pridajhraca.this, novyhrac.class);
				startActivityForResult(intent, GET_CODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		vysledok = new String();
		piker = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(
						getApplicationContext(),
						hracilist.get(position).getMeno() + " "
								+ hracilist.get(position).getPriezvisko()
								+ " bol/a pridany na tento trening",
						Toast.LENGTH_SHORT).show();
				vysledok += hracilist.get(position).getMeno() + " "
						+ hracilist.get(position).getPriezvisko() + "\n";
			}
		};
	}
}

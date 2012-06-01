package sk.mato.kuchy;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

//TODO spravit aby bolo oznacene co uz bolo vybraete ked pridem do tohoto
public class pridajhraca extends Activity {

	private sqlPomoc dbHracov = new sqlPomoc(this, "hraci", null, 1);
	private ArrayList<Hrac> hracilist = new ArrayList<Hrac>();
	private OnItemClickListener piker;
	private ArrayList<Hrac> tempHracilist = new ArrayList<Hrac>();
	private String vysledok = new String();
	private int GET_CODE = 0; // pre vytvorenie hraca co nieje v DB
	private int RESULT_ADD = 1;
	private InputStream akt;
	private Trening trening;
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.pridajhraca);

		hracilist = dbHracov.dajCeluDb();

		try {
			akt = openFileInput("trening.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		trening = OXml.nacitajTrening(akt, hracilist);

		if (trening.getHracov().size() > 0) {
			tempHracilist = trening.getHracov();
			
		}
		
		super.onCreate(savedInstanceState);
		lv = (ListView) findViewById(R.id.listview);
		piker = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!tempHracilist.contains(hracilist.get(position))) {
					tempHracilist.add(hracilist.get(position));
					view.setBackgroundColor(Color.rgb(19, 52, 22));
					//parent.getChildAt(0).setBackgroundColor(Color.rgb(19, 52, 22));
					Log.d("ako?", lv.indexOfChild(view)+"");
				} else {
					tempHracilist.remove(hracilist.get(position));
					view.setBackgroundColor(Color.TRANSPARENT);
				}
			}
		};
		
		// list View vytvorenie + danie do adapteru
		

		String[] from = new String[] { "id", "meno", "priez", "vek", "res" };
		int[] to = new int[] { R.id.item1, R.id.item2, R.id.item3, R.id.item4,
				R.id.item5 };

		List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();

		for (int j = 0; j < hracilist.size(); j++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", "" + hracilist.get(j).getId());
			map.put("meno", hracilist.get(j).getMeno());
			map.put("priez", hracilist.get(j).getPriezvisko());
			map.put("vek", hracilist.get(j).getVek() + "");
			map.put("res", hracilist.get(j).getRespekt() + "");
			fillMaps.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
				R.layout.add_hrac, from, to);
		
		
		Log.d("adapter count",adapter.getCount()+"");
		
		
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(piker);
		
		// koniec Button
		Button koniec = (Button) findViewById(R.id.koniec);
		koniec.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				vysledok = new String();
				for (Hrac hrac : tempHracilist) {
					vytvorString(hrac);
				}

				Log.i(Constants._ID, "vysledok" + vysledok);
				
				if (vysledok.equals(new String()))
					vysledok = "47 47\n";
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
				vytvorString(hracilist.get(position));
			}
		};
		// v podstate refresh akctivity
		startActivity(getIntent());
		finish();
	}

	protected void vytvorString(Hrac h) {
		vysledok += h.getMeno() + " " + h.getPriezvisko() + "\n";
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			final AlertDialog alertDialog = new AlertDialog.Builder(
					pridajhraca.this).create();
			alertDialog.setTitle("POZOR");
			alertDialog.setMessage("Ulozit Zoznam hracov pred navratom?");
			alertDialog.setButton("ano", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					vysledok = new String();
					for (Hrac hrac : tempHracilist) {
						vytvorString(hrac);
					}

					Log.i(Constants._ID, "vysledok" + vysledok);

					if (vysledok.equals(new String()))
						vysledok = "47 47\n";
					pridajhraca.this.setResult(RESULT_ADD,
							(new Intent()).setAction(vysledok));
					pridajhraca.this.finish();
				}
			});
			alertDialog.setButton2("nie",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							vysledok = new String();
							vysledok = "47 47\n";
							pridajhraca.this.setResult(RESULT_ADD,
									(new Intent()).setAction(vysledok));
							pridajhraca.this.finish();
						}
					});
			alertDialog.show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}

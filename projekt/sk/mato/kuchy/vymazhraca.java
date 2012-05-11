package sk.mato.kuchy;

/*private int RESULT_REMOVE=-1;
 * 
 * 	 InputStream dbhracov=openFileInput("hraci.xml");
		InputStream  akt=openFileInput("trening.xml");
		trening= OXml.nacitajTrening(akt, OXml.nacitajHracov(dbhracov));
		hracilist= trening.getHracov();
 */

//FIXME!!

import java.io.IOException;
import java.io.InputStream;
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

public class vymazhraca extends Activity {
	
	private ArrayList<Hrac> hracilist;
	private OnItemClickListener piker;
	private String vysledok;
	private int RESULT_REMOVE=-1; // -1 znaci ze vymazavam pre nad-activity

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.vymazhraca);

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
						+ hracilist.get(position).getPriezvisko()+"\n";
			}
		};

		// nacitanie hracov

		try {
			InputStream dbhracov=openFileInput("hraci.xml");
			InputStream  akt=openFileInput("trening.xml");
			Trening trening= OXml.nacitajTrening(akt, OXml.nacitajHracov(dbhracov));
			hracilist= trening.getHracov();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				setResult(RESULT_REMOVE, (new Intent()).setAction(vysledok));
				finish();
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
						+ hracilist.get(position).getPriezvisko()+"\n";
			}
		};
	}
}
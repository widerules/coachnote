package sk.mato.kuchy;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class menu_otvorStare extends ListActivity {

	private OnItemClickListener piker;
	private ArrayList<String> mena = new ArrayList<String>();
	private ArrayList<Integer> id = new ArrayList<Integer>();
	private sqlPomoc dbtreningy = new sqlPomoc(this, "treningy", null, 1);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Cursor cursor = dbtreningy.getReadableDatabase().rawQuery(
				"SELECT id, datum FROM `treningy` ", new String[] {});

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			mena.add(cursor.getString(1));
			id.add(Integer.parseInt(cursor.getString(0)));
		}

		piker = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id2) {

				Intent intent = new Intent(getBaseContext(),
						menu_zobrazStare.class);
				intent.putExtra("id", id.get(position) + "");
				// do premennej extra supnem id treningu na otvorenie
				startActivityForResult(intent, 0);
			}
		};

		setListAdapter(new ArrayAdapter<String>(this, R.layout.staretreningy,
				mena));
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(piker);
	}
}

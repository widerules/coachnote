package sk.mato.kuchy;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class menu_otvorStare extends ListActivity {

	private OnItemClickListener piker;
	private OnItemLongClickListener deleter;
	private ArrayList<String> mena = new ArrayList<String>();
	private ArrayList<Integer> id = new ArrayList<Integer>();
	private sqlPomoc dbtreningy = new sqlPomoc(this, "treningy", null, 1);
	private boolean prazdne= true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!sqlPomoc.checkDataBase(getDatabasePath("treningy").getAbsolutePath())){
			dbtreningy.inicializujTreningoovuDB();
			prazdne=true;
		}
		
		nacitajList();
		
		//Log.d("prazdna premenna", ""+prazdne);
		
		if (prazdne) {
			AlertDialog alertDialog = new AlertDialog.Builder(menu_otvorStare.this).create();
			alertDialog.setTitle("POZOR");
			alertDialog.setMessage("niesu evidovanie ziadne treningy!");
			alertDialog.setButton("exit",new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			    	  finish();
			    } }); 
			alertDialog.show();
		}
		
		deleter= new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				final int idTreningu= id.get(arg2); //aktualne stlacene id treningu v DB
				final String datumTreningu= mena.get(arg2); 
				
				AlertDialog alertDialog = new AlertDialog.Builder(menu_otvorStare.this).create();
				alertDialog.setTitle("Vymazat trening?");
				alertDialog.setMessage("naozaj vymazat trening?(datum: "+datumTreningu+")");
				alertDialog.setButton("ano", new DialogInterface.OnClickListener() {
				      public void onClick(DialogInterface dialog, int which) {
				    	  dbtreningy.vymazTrening(idTreningu);
				    	  //v podstate refresh akctivity
				    	  startActivity(getIntent());
				    	  finish();
				       //here you can add functions
				    } }); 
				
				alertDialog.setButton2("nie", new DialogInterface.OnClickListener() {
				      public void onClick(DialogInterface dialog, int which) {
				       //tak teda nic
				    } });
				
				alertDialog.setIcon(R.drawable.icon);
				alertDialog.show();
				
				

				
				return true;
			}
		};
		
		
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
		lv.setOnItemLongClickListener(deleter);
	}

	private void nacitajList() {
		Cursor cursor = dbtreningy.getReadableDatabase().rawQuery(
				"SELECT id, datum FROM `treningy` ", new String[] {});
		mena= new ArrayList<String>();
		id= new ArrayList<Integer>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			mena.add(cursor.getString(1));
			id.add(Integer.parseInt(cursor.getString(0)));
			prazdne=false;
		}
	}
	
	
}

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

public class vymazhraca extends ListActivity {	
	
	private ArrayList<Hrac> hracilist;
	private ArrayList<String> hraciMena;
	private ArrayList<String> result;
	private OnItemClickListener piker;
	private String vysledok;
	private Trening trening;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  vysledok= new String();
	  vysledok="del";
	  result = new ArrayList<String>();
	  piker = new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
			    
			      // When clicked, show a toast with the TextView text
			      Toast.makeText(getApplicationContext(), ((TextView) view).getText()+" bol/a vyhodeny z tohoto treningu",
			          Toast.LENGTH_SHORT).show();
			      String item= new String((String) ((TextView) view).getText());
			      if (item.equals("Koniec")) {
			    	  setResult(RESULT_OK, (new Intent()).setAction(vysledok));
			          finish();  
			      }
			      else {
			    	 
			      result.add(item);
			      vysledok+= item;  
			      }
			      
			      
			    }
			  };
	  
	  //nacitanie hracov 
	  
	  try {
		InputStream dbhracov=openFileInput("hraci.xml");
		InputStream  akt=openFileInput("trening.xml");
		trening= OXml.nacitajTrening(akt, OXml.nacitajHracov(dbhracov));
		hracilist= trening.getHracov();
		//hracilist= xml.nacitajHracov(dbhracov);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		finish();
	}
	 //vybranie mien
	hraciMena = new ArrayList<String>();
	hraciMena.add("Koniec");
	for (Hrac s : hracilist) {
		hraciMena.add(s.getMeno()+" "+ s.getPriezvisko()+"\n");
	}	

	
	setListAdapter(new ArrayAdapter<String>(this, R.layout.del_hrac, hraciMena));

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(piker);
	  
	}

}

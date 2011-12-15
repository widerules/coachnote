package sk.mato.kuchy;

import java.io.File;
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


public class menu_otvorStare extends ListActivity {
	
private File[] fili;
private OnItemClickListener piker;
private ArrayList<String> mena= new ArrayList<String>();

@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            
        File hlavnaDir = new File("/data/data/sk.mato.kuchy/files/");
    
        fili=hlavnaDir.listFiles();
        if (fili==null) {
        	Toast.makeText(getApplicationContext(), "Ziadne Zaznamy!",
			          Toast.LENGTH_SHORT).show();
        	finish();
        }
        for (File i : fili) {
        	mena.add(i.getName());
		}
        //toto su blbosti co tam zavadzaju
        mena.remove("trening.xml");
        mena.remove("hraci.xml");
        mena.remove(".sudo_as_admin_successful");
        
       
        
        piker = new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
			      Toast.makeText(getApplicationContext(), "Oznaceny:"+((TextView) view).getText(),
			          Toast.LENGTH_SHORT).show();
			      /*tutok bude inicializacia dalsej aktivyty
			       *  ktora bude spustat zobrazovanie statystyky
			      */
			      Intent intent= new Intent( getBaseContext(), menu_zobrazStare.class);
			      intent.putExtra("Otvor", ((TextView) view).getText() ); 
			      //do premennej extra supnem nazov suboru na otvorenie
			      
				  startActivityForResult(intent, 0);
			    }
			  };
			  
	   setListAdapter(new ArrayAdapter<String>(this, R.layout.staretreningy, mena));
	   ListView lv = getListView();
		  lv.setTextFilterEnabled(true);

		  lv.setOnItemClickListener(piker);
	}
}


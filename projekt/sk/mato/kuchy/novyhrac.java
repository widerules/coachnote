package sk.mato.kuchy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class novyhrac extends Activity {
	
private InputStream hraci;
private ArrayList<Hrac> hracilist;
private String noveMeno, novePriezvisko;
private int novyVek;
private Double novyRespekt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novyhrac);
		
		//meno
		
		 final EditText edittext = (EditText) findViewById(R.id.meno);
		edittext.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		            (keyCode == KeyEvent.KEYCODE_ENTER)) {
		          // Perform action on key press
		          Toast.makeText(getBaseContext(), edittext.getText(), Toast.LENGTH_SHORT).show();
		          noveMeno= edittext.getText().toString();
		          return true;
		        }
		        return false;
		    }
		});	
		
		final EditText edittext2 = (EditText) findViewById(R.id.priezvisko);
		edittext2.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		            (keyCode == KeyEvent.KEYCODE_ENTER)) {
		          // Perform action on key press
		          Toast.makeText(getBaseContext(), edittext.getText(), Toast.LENGTH_SHORT).show();
		          novePriezvisko= edittext2.getText().toString();
		          return true;
		        }
		        return false;
		    }
		});	

		final EditText edittext3 = (EditText) findViewById(R.id.respekt);
		edittext3.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		            (keyCode == KeyEvent.KEYCODE_ENTER)) {
		          // Perform action on key press
		          Toast.makeText(getBaseContext(), edittext.getText(), Toast.LENGTH_SHORT).show();
		          novyRespekt= Double.parseDouble(edittext3.getText().toString());
		          return true;
		        }
		        return false;
		    }
		});	
		
		//vek
		novyVek=-1;
		Spinner spinner = (Spinner) findViewById(R.id.spinnerVek);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	    this, R.array.vek_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter); 
	    spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	            novyVek= Integer.parseInt(parent.getItemAtPosition(pos).toString());
	        }
	        public void onNothingSelected(AdapterView<?> parent) {
	        }
	    });
		
	    //odosielacie tlacitko
        Button button = (Button) findViewById(R.id.ulozHraca);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //zapis do suboru...
            	noveMeno= edittext.getText().toString();
            	novePriezvisko= edittext2.getText().toString();
            	novyRespekt= Double.parseDouble(edittext3.getText().toString());
            	try {
        			hraci=openFileInput("hraci.xml");
        			hracilist= OXml.nacitajHracov(hraci);
        		} catch (FileNotFoundException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        			finish();
        		}
        		
        		Hrac novy= new Hrac(noveMeno, novePriezvisko, novyVek, novyRespekt);
        		hracilist.add(novy);
        		try {
					OXml.zapisHracomNovyrespekt(hracilist);
					Toast.makeText(getBaseContext(), "Novy hráč úspešne pridany! ", Toast.LENGTH_SHORT).show();
					finish();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        	
	}
}

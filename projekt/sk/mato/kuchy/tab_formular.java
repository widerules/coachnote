package sk.mato.kuchy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class tab_formular extends Activity  {

private TextView mDateDisplay;
private Date aktualneZadanydatum;
private Button mPickDate;
private int aktualneZadanyPocetKurtov;
private int mYear;
private int mMonth;
private int mDay;

static final int DATE_DIALOG_ID = 0;
private InputStream akt,dbhracov;
private Trening trening;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)  {  
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	     setContentView(R.layout.tab_formular);
	   //nacitanie databaz
	        try {
				dbhracov=openFileInput("hraci.xml");
				akt=openFileInput("trening.xml");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				finish();
			}
			trening= OXml.nacitajTrening(akt, OXml.nacitajHracov(dbhracov));
			
			
	//inicializacia editTextu
			final EditText edittext = (EditText) findViewById(R.id.popistreningu);
			if  (trening != null) edittext.setText(trening.getPopisTreningu());
			edittext.setOnKeyListener(new OnKeyListener() {
			    public boolean onKey(View v, int keyCode, KeyEvent event) {
			        // If the event is a key-down event on the "enter" button
			        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
			            (keyCode == KeyEvent.KEYCODE_ENTER)) {
			          // Perform action on key press
			          Toast.makeText(getBaseContext(), edittext.getText(), Toast.LENGTH_SHORT).show();
			          trening.setPopisTreningu(edittext.getText().toString());
			          return true;
			        }
			        return false;
			    }
			});	
			
	//inicializacia spinnera
			aktualneZadanyPocetKurtov=-1;
			Spinner spinner = (Spinner) findViewById(R.id.spinner);
		    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		    this, R.array.kurty_array, android.R.layout.simple_spinner_item);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    spinner.setAdapter(adapter); 
		    spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
		        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		            aktualneZadanyPocetKurtov= Integer.parseInt(parent.getItemAtPosition(pos).toString());
		        }
		        public void onNothingSelected(AdapterView<?> parent) {
		        }
		    });
	//inicializacia datepickera
		  //if (trening != null) aktualneZadanydatum= trening.getDatumTreningu();
		    aktualneZadanydatum= null;
		 // capture our View elements
	        mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
	        mPickDate = (Button) findViewById(R.id.pickDate);

	        // add a click listener to the button
	        mPickDate.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                showDialog(DATE_DIALOG_ID);
	            }
	        });

	        // get the current date
	        final Calendar c = Calendar.getInstance();
	        mYear = c.get(Calendar.YEAR);
	        mMonth = c.get(Calendar.MONTH);
	        mDay = c.get(Calendar.DAY_OF_MONTH);

	        // display the current date (this method is below)
	        try {
				updateDisplay();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	  //inicializacia potvrzovacieho buttona
	        Button button = (Button) findViewById(R.id.odosliform);
	        button.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                //zapis do suboru...
	            	trening.setPopisTreningu(edittext.getText().toString());
	            	trening.setPocetKurtov(aktualneZadanyPocetKurtov);
	            	try {
	            		OXml.vytvorNovyTrening(trening);
						Toast.makeText(getBaseContext(), "Udaje o treningu uspesne ulozene!"
								, Toast.LENGTH_LONG).show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Toast.makeText(getBaseContext(), "chyba pri zapisovani suboru!"
								, Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
	            }
	        });
//inicializacia ukoncovacieho buttona
    Button ukonci = (Button) findViewById(R.id.koniec);
    ukonci.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
        	//if (aktualneZadanydatum!= null) trening.setDatumTreningu(aktualneZadanydatum);
			trening.setPopisTreningu(edittext.getText().toString());
        	trening.setPocetKurtov(aktualneZadanyPocetKurtov);
        	try {
        		OXml.vytvorNovyTrening(trening);
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				String nazov= formatter.format(trening.getDatumTreningu());
				File premenuj = new File("/data/data/sk.mato.kuchy/files/trening.xml");
				File novy = new File("/data/data/sk.mato.kuchy/files/"+nazov+".xml");
				premenuj.renameTo(novy);
				
				Toast.makeText(getBaseContext(), "Udaje o treningu uspesne ulozene!"
						, Toast.LENGTH_LONG).show();
				finish();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getBaseContext(), "chyba pri zapisovani suboru!"
						, Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			
        }
    });
}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
    	trening.setPocetKurtov(aktualneZadanyPocetKurtov);
    	try {
    		OXml.vytvorNovyTrening(trening);
			Toast.makeText(getBaseContext(), "Udaje o treningu uspesne ulozene!"
					, Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getBaseContext(), "chyba pri zapisovani suboru!"
					, Toast.LENGTH_LONG).show();
			//e.printStackTrace();
		}
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			dbhracov=openFileInput("hraci.xml");
			akt=openFileInput("trening.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		}
		trening= OXml.nacitajTrening(akt, OXml.nacitajHracov(dbhracov));
	}


	private void updateDisplay() throws ParseException {
		// TODO Auto-generated method stub
		mDateDisplay.setText(
                new StringBuilder()
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(" "));
		//yyyy-mm-dd format
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		aktualneZadanydatum=  formatter.parse(mDay+"/"+mMonth+"/"+mYear);
		trening.setDatumTreningu(aktualneZadanydatum);
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, 
                                  int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                try {
					updateDisplay();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        };

	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
     return null;
    }
}

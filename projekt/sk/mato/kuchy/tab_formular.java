package sk.mato.kuchy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

public class tab_formular extends Activity {

	private TextView mDateDisplay;
	private Button mPickDate;
	private int aktualneZadanyPocetKurtov;
	private int mYear;
	private int mMonth;
	private int mDay;
	

	static final int DATE_DIALOG_ID = 0;
	private final int TIME_DIALOG_ID=1;
	private InputStream akt;
	
	private sqlPomoc dbhracov= new sqlPomoc(this, "hraci", null, 1);
	private sqlPomoc dbtreningy= new sqlPomoc(this, "treningy", null, 1);
	private sqlPomoc dbzapasy= new sqlPomoc(this, "zapasy", null, 1);
	
	private ArrayList<Hrac> hraci= new ArrayList<Hrac>();
	private Trening trening;
	private TextView mTimeDisplay;
	private Button mPickTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_formular);
		// nacitanie databaz
		try {
			akt = openFileInput("trening.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hraci= dbhracov.dajCeluDb();
		trening = OXml.nacitajTrening(akt, hraci);

		// inicializacia editTextu
		final EditText edittext = (EditText) findViewById(R.id.popistreningu);
		if (trening != null)
			edittext.setText(trening.getPopisTreningu());
		edittext.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					Toast.makeText(getBaseContext(), edittext.getText(),
							Toast.LENGTH_SHORT).show();
					trening.setPopisTreningu(edittext.getText().toString());
					return true;
				}
				return false;
			}
		});

		// inicializacia spinnera
		aktualneZadanyPocetKurtov = -1;
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(this, R.array.kurty_array,
						android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						aktualneZadanyPocetKurtov = Integer.parseInt(parent
								.getItemAtPosition(pos).toString());
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
		// inicializacia datepickera
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
		mPickDate = (Button) findViewById(R.id.pickDate);

		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

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
			
		//inicializacia time pickera
		mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
	    mPickTime = (Button) findViewById(R.id.pickTime);
	    
	    mPickTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
	            showDialog(TIME_DIALOG_ID);
	        }
	    });

	    updateDisplayTime();

		
		
		// inicializacia potvrzovacieho buttona
		Button button = (Button) findViewById(R.id.odosliform);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				trening.setPopisTreningu(edittext.getText().toString());

				trening.setPocetKurtov(aktualneZadanyPocetKurtov);
				try {
					OXml.vytvorNovyTrening(trening);
					Toast.makeText(getBaseContext(),
							"Udaje o treningu uspesne ulozene!",
							Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getBaseContext(),
							"chyba pri zapisovani suboru!", Toast.LENGTH_LONG)
							.show();
					e.printStackTrace();
				}
			}
		});
		// inicializacia ukoncovacieho buttona
		Button ukonci = (Button) findViewById(R.id.koniec);
		ukonci.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				trening.setPopisTreningu(edittext.getText().toString());
				trening.setPocetKurtov(aktualneZadanyPocetKurtov);
				
				for (Zapas z : trening.getZapasy()) {
					z.setDatum(trening.getDatumTreningu());
					dbzapasy.pridajZapas(z);
				}
				
				StringBuffer zapasyString=new StringBuffer();
				for (Zapas z : trening.getZapasy()) {
					zapasyString.append(dbzapasy.getIdZapasu(z)+"/");
				}
				
				dbtreningy.pridajTrening(trening, zapasyString);
				
				Toast.makeText(getBaseContext(),
						"Udaje o treningu uspesne ulozene!",
						Toast.LENGTH_LONG).show();
				finish();
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
			Toast.makeText(getBaseContext(),
					"Udaje o treningu uspesne ulozene!", Toast.LENGTH_LONG)
					.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getBaseContext(), "chyba pri zapisovani suboru!",
					Toast.LENGTH_LONG).show();
			 e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			akt = openFileInput("trening.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hraci= dbhracov.dajCeluDb();
		trening = OXml.nacitajTrening(akt, hraci);
	}

	private void updateDisplay() throws ParseException {
		// TODO Auto-generated method stub
		mDateDisplay.setText(new StringBuilder().append(mMonth + 1).append("-")
				.append(mDay).append("-").append(mYear).append(" "));
		trening.setDatumTreningu(new Date(mYear, mMonth, mDay, mHour, mMinute));
	}
	
	private void updateDisplayTime() {
	    mTimeDisplay.setText(
	        new StringBuilder()
	                .append(pad(mHour)).append(":")
	                .append(pad(mMinute)));
		trening.setDatumTreningu(new Date(mYear, mMonth, mDay, mHour, mMinute));
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
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
	private int mHour;
	private int mMinute;

	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
	    new TimePickerDialog.OnTimeSetListener() {
	        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	            mHour = hourOfDay;
	            mMinute = minute;
	            updateDisplayTime();
	        }
	    };
	
	    private static String pad(int c) {
	        if (c >= 10)
	            return String.valueOf(c);
	        else
	            return "0" + String.valueOf(c);
	    }

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		 case TIME_DIALOG_ID:
		        return new TimePickerDialog(this,
		                mTimeSetListener, mHour, mMinute, false);
		}
		return null;
	}

	
}

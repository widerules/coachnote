package sk.mato.kuchy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class profil extends Activity {
	private String novyToken;
	private String novyLogin;
	private sqlPomoc dbtoken = new sqlPomoc(this, "token", null, 1);
	private boolean prazdna = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profil);

		if (!sqlPomoc.checkDataBase(getDatabasePath("token").getAbsolutePath())) {
			dbtoken.inicializujToken();
			prazdna = true;
			// Log.d("sql", "inicializujem token!");
		} else {
			TextView stare = (TextView) findViewById(R.id.starytoken);
			String ret = dbtoken.getToken();
			String udaje[] = null;
			if (ret == null) {
				prazdna = true;
			} else
				udaje = ret.split("/");

			//Log.d("prazdna", "" + prazdna);
			if (prazdna)
				stare.setText("NEZADANE ZIADNE UDAJE!");
			else
				stare.setText("Aktualny Login: " + udaje[0]
						+ "\nAktualny Token: " + udaje[1]);
		}

		final EditText editToken = (EditText) findViewById(R.id.editToken);
		editToken.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					Toast.makeText(getBaseContext(), editToken.getText(),
							Toast.LENGTH_SHORT).show();
					novyToken = editToken.getText().toString();
					return true;
				}
				return false;
			}
		});

		final EditText editlogin = (EditText) findViewById(R.id.editlogin);
		editlogin.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					Toast.makeText(getBaseContext(), editToken.getText(),
							Toast.LENGTH_SHORT).show();
					novyLogin = editlogin.getText().toString();
					return true;
				}
				return false;
			}
		});

		// odosielacie tlacitko
		Button odosli = (Button) findViewById(R.id.button1);
		odosli.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				novyLogin = editlogin.getText().toString();
				novyToken = editToken.getText().toString();
				dbtoken.vycistitokenDB();
				dbtoken.pridajToken(novyToken, novyLogin);
				// refresh
				startActivity(getIntent());
				finish();
			}
		});
	}

}

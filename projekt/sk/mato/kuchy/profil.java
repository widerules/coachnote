package sk.mato.kuchy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	private String token = new String();
	private ProgressDialog pd;
	private String odpoved;
	private TextView res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profil);
		res = (TextView) findViewById(R.id.platnost);
		// dbtoken.inicializujToken();

		if (!sqlPomoc.checkDataBase(getDatabasePath("token").getAbsolutePath())) {
			dbtoken.inicializujToken();
			prazdna = true;
			Log.d("sql", "inicializujem token!");
		} else {
			TextView stare = (TextView) findViewById(R.id.starytoken);
			String ret = dbtoken.getToken();
			String udaje[] = null;
			if (ret == null) {
				prazdna = true;
			} else
				udaje = ret.split("/");

			// Log.d("prazdna", "" + prazdna);
			if (prazdna)
				stare.setText("NEZADANE ZIADNE UDAJE!");
			else
				stare.setText("Aktualny Token: " + udaje[0]
						+ "\nAktualny login: " + udaje[1]);
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

		// kontrolovacie tlacitko
		Button kontrola = (Button) findViewById(R.id.kontrola);
		kontrola.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				pd = ProgressDialog.show(profil.this, "pracuje..",
						"kontrolujem token...", true, false);
				new Thread() {
					public void run() {
						try {
							if (chcekHesla())
								odpoved = "kontrola udajov uspesna!";
							else
								odpoved = "kontrola udajov NEuspesna!";
							// pd.setTitle(odpoved);
							// this.wait(100);
						} catch (Exception e) {

						}
						handler.sendEmptyMessage(0);

					}

				}.start();
			}

			private Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					pd.dismiss();
					res.setText(odpoved + " ");
				}
			};
		});

	}

	private boolean chcekHesla() {
		// TODO Auto-generated method stub
		token = dbtoken.getToken();
		// String ret = dbtoken.getToken();
		String udaje[] = null;
		if (token == null) {
			return false;
		} else {
			udaje = token.split("/");
		}
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("kontrola", "ano"));
		data.add(new BasicNameValuePair("token", udaje[0]));
		data.add(new BasicNameValuePair("login", udaje[1]));

		try {
			String odpoved = new String(WebUtilities.post(
					"http://www.st.fmph.uniba.sk/~kuchynar1/rp/?p=andr", data));
			// Log.d("odpovede", odpoved+"");
			if (odpoved.contains(new String("kontrola udajov uspesna!")))
				return true;
			else
				return false;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.clear();
		return false;
	}
}

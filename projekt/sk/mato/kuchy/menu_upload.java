package sk.mato.kuchy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class menu_upload extends Activity {

	private ArrayList<Hrac> DB;
	private LinearLayout linlay;
	private ScrollView pohlad;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		pohlad = new ScrollView(this);
		linlay = new LinearLayout(this);
		pohlad.addView(linlay);

		Button uploadHraci = new Button(this);
		uploadHraci.setText("Upload hracov");

		uploadHraci.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				pd = ProgressDialog.show(menu_upload.this, "Upload",
						"prebieha upload vsetkych hracov...", true);

				new Thread() {
					public void run() {
						try {
							uploadDBHracov();
						} catch (Exception e) {
						}
						handler.sendEmptyMessage(0);
						pd.dismiss();

					}
				}.start();
			}

			private Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {

				}
			};
		});

		Button uploadTreningov = new Button(this);
		uploadTreningov.setText("Upload Treningov");
		uploadTreningov.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				pd = ProgressDialog.show(menu_upload.this, "Upload",
						"prebieha upload vsetkych hracov...", true);

				new Thread() {
					public void run() {
						try {
							uploadDBTreningov();
						} catch (Exception e) {
						}
						handler.sendEmptyMessage(0);
						pd.dismiss();

					}
				}.start();
			}

			private Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
				}
			};
		});

		try {
			DB = OXml.nacitajHracov(openFileInput("hraci.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		linlay.addView(uploadHraci);
		linlay.addView(uploadTreningov);
		setContentView(pohlad);
	}

	private void uploadDBHracov() {
		try {
			DB = OXml.nacitajHracov(openFileInput("hraci.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("nhraci", "ano"));
		data.add(new BasicNameValuePair("pocet_h", "" + DB.size()));

		for (int i = 0; i < DB.size(); i++) {
			data.add(new BasicNameValuePair("meno_" + i, ""
					+ DB.get(i).getMeno()));
			data.add(new BasicNameValuePair("priezvisko_" + i, ""
					+ DB.get(i).getPriezvisko()));
			data
					.add(new BasicNameValuePair("vek_" + i, ""
							+ DB.get(i).getVek()));
			data.add(new BasicNameValuePair("respekt_" + i, ""
					+ DB.get(i).getRespekt()));
		}
		try {
			WebUtilities
					.post(
							"http://www.st.fmph.uniba.sk/~kuchynar1/rp/?p=system",
							data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void uploadDBTreningov() {
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		File hlavnaDir = new File("/data/data/sk.mato.kuchy/files/");

		for (File f : hlavnaDir.listFiles()) {

			if (!f.getName().equalsIgnoreCase("hraci.xml")) {
				try {

					Trening uploadovany;
					uploadovany = OXml.nacitajTrening(
							openFileInput(f.getName()), DB);

					data = new ArrayList<NameValuePair>();

					data.add(new BasicNameValuePair("datum", uploadovany
							.vypisDatumTreningu()));
					data.add(new BasicNameValuePair("pocetKurtov", ""
							+ uploadovany.getPocetKurtov()));
					data.add(new BasicNameValuePair("poznamka", uploadovany
							.getPopisTreningu()));
					data.add(new BasicNameValuePair("pocet_h", ""
							+ uploadovany.getPocetHracov()));
					data.add(new BasicNameValuePair("ntrening", "ano"));

					for (int i = 0; i < uploadovany.getPocetHracov(); i++) {
						data.add(new BasicNameValuePair("meno_" + i,
								uploadovany.getHracov().get(i).getMeno()));
						data
								.add(new BasicNameValuePair("priezvisko_" + i,
										uploadovany.getHracov().get(i)
												.getPriezvisko()));
					}

					data.add(new BasicNameValuePair("pocet_z", ""
							+ uploadovany.getZapasy().size()));

					for (int i = 0; i < uploadovany.getZapasy().size(); i++) {
						data
								.add(new BasicNameValuePair("ameno_" + i,
										uploadovany.getZapasy().get(i).getA()
												.getMeno()));
						data.add(new BasicNameValuePair("apriezvisko_" + i,
								uploadovany.getZapasy().get(i).getA()
										.getPriezvisko()));

						data
								.add(new BasicNameValuePair("bmeno_" + i,
										uploadovany.getZapasy().get(i).getB()
												.getMeno()));
						data.add(new BasicNameValuePair("bpriezvisko_" + i,
								uploadovany.getZapasy().get(i).getB()
										.getPriezvisko()));

						String prvy;
						if (uploadovany.getZapasy().get(i).getVytaz() == uploadovany
								.getZapasy().get(i).getA())
							prvy = "1";
						else
							prvy = "2";

						data.add(new BasicNameValuePair("v_" + i, prvy));

						data
								.add(new BasicNameValuePair("vysledok_" + i, ""
										+ uploadovany.getZapasy().get(i)
												.getVysledok()));

						WebUtilities
								.post(
										"http://www.st.fmph.uniba.sk/~kuchynar1/rp/?p=system",
										data);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

}

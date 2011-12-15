package sk.mato.kuchy;

import java.io.FileNotFoundException;
import java.io.InputStream;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class menu_zobrazStare extends Activity{

private Trening trening;
private InputStream akt,dbhracov;
private ScrollView pohlad;
private LinearLayout linlay;
private TextView statystiky;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String nazovSuboru= getIntent().getExtras().getString("Otvor");
		pohlad= new ScrollView(this);
		linlay= new LinearLayout(this);
		statystiky= new TextView(this);
		
		//nacitanie urciteho treningu
		try {
			dbhracov=openFileInput("hraci.xml");
			akt= openFileInput(nazovSuboru);
			trening= OXml.nacitajTrening(akt, OXml.nacitajHracov(dbhracov));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("moje","nenacitany subor!");
			finish();
		}	
		statystiky.setText("statistyky z treningy z:" + trening.getDatumTreningu().toString()+"\n");
		
		statystiky.append("popis treningu:\n" + trening.getPopisTreningu()+ "\n\n");
		
		statystiky.append("pocetkurtov: "+trening.getPocetKurtov()+"\n\n");
		
		statystiky.append("dochadzka:\n"+trening.vypisHracov()+"\n\n");
		
		statystiky.append("zapasy:\n"+ trening.vypisZapasy(-1)+"\n\n");
		
		trening.vytvorRebricek();
		statystiky.append("aktualny rebricek:\n"+ trening.vypisRebricek()+"\n\n");
		linlay.addView(statystiky);
		
		pohlad.addView(linlay);
		
		setContentView(pohlad);
	}

}

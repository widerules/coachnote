package sk.mato.kuchy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class menu_upload extends Activity {
	
private ArrayList<Hrac> DB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ScrollView pohlad= new ScrollView(this);
        LinearLayout linlay= new LinearLayout(this);
        pohlad.addView(linlay);
        
		 File hlavnaDir = new File("/data/data/sk.mato.kuchy/files/");
		 
		 try {
			DB=OXml.nacitajHracov(openFileInput("hraci.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 List<NameValuePair> data= new ArrayList<NameValuePair>();
		
		
		//upload seckeho
		 for ( File f : hlavnaDir.listFiles()) {
			 if (!f.getName().equalsIgnoreCase("hraci.xml")) {
			 try {
				 Trening uploadovany;
				uploadovany= OXml.nacitajTrening(openFileInput(f.getName()), DB);
				
				data= new ArrayList<NameValuePair>();
				
				data.add(new BasicNameValuePair("datum", uploadovany.vypisDatumTreningu()));
				data.add(new BasicNameValuePair("pocetKurtov", ""+uploadovany.getPocetKurtov()));
				data.add(new BasicNameValuePair("poznamka", uploadovany.getPopisTreningu()));
				data.add(new BasicNameValuePair("pocet_h", ""+uploadovany.getPocetHracov()));
				data.add(new BasicNameValuePair("ntrening", "ano"));
				
				for (int i = 0; i < uploadovany.getPocetHracov(); i++) {
					data.add(new BasicNameValuePair("meno_"+i, uploadovany.getHracov().get(i).getMeno()));
					data.add(new BasicNameValuePair("priezvisko_"+i, uploadovany.getHracov().get(i).getPriezvisko()));
				}
				
				data.add(new BasicNameValuePair("pocet_z", ""+ uploadovany.getZapasy().size()));
				
				for (int i = 0; i < uploadovany.getZapasy().size(); i++) {
					data.add(new BasicNameValuePair("ameno_"+i, uploadovany.getZapasy().get(i).getA().getMeno()));
					data.add(new BasicNameValuePair("apriezvisko_"+i, uploadovany.getZapasy().get(i).getA().getPriezvisko()));
					
					data.add(new BasicNameValuePair("bmeno_"+i, uploadovany.getZapasy().get(i).getB().getMeno()));
					data.add(new BasicNameValuePair("bpriezvisko_"+i, uploadovany.getZapasy().get(i).getB().getPriezvisko()));
					
					String prvy;
					if ( uploadovany.getZapasy().get(i).getVytaz()==uploadovany.getZapasy().get(i).getA() )prvy="1";
					else prvy="2";
						
					data.add(new BasicNameValuePair("v_"+i, prvy ));
					
					data.add(new BasicNameValuePair("vysledok_"+i, ""+uploadovany.getZapasy().get(i).getVysledok()));
				
				
				HttpEntity a= WebUtilities.post("http://www.st.fmph.uniba.sk/~kuchynar1/rp/?p=applet", data);
				
				String pokus=null;
		        TextView tv= new TextView(this);
		        
		        
		        pokus=EntityUtils.toString(a);
		        tv.append(pokus);
		        linlay.addView(tv);
				setContentView(pohlad);
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
		 
		 
	//finish();	
	}
}

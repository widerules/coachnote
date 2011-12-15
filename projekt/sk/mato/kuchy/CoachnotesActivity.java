package sk.mato.kuchy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CoachnotesActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button menu_novyTrening= (Button) findViewById(R.id.menu_novyTrening); 
        menu_novyTrening.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent( v.getContext(), menu_novyTrening.class);
				startActivityForResult(intent, 0);
			}
		});
        
        //menu_otvortStare
        
        Button menu_otvortStare= (Button) findViewById(R.id.menu_otvortStare); 
        menu_otvortStare.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent( v.getContext(), menu_otvorStare.class);
				startActivityForResult(intent, 0);
			}
		});
        
        Button menu_upload= (Button) findViewById(R.id.menu_upload); 
        menu_upload.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent( v.getContext(), menu_upload.class);
				startActivityForResult(intent, 0);
			}
		}); 
    }
}

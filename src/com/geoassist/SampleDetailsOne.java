package com.geoassist;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.geoassist.data.Project;
import com.geoassist.data.RockType;

public class SampleDetailsOne extends BaseActivity  implements OnItemSelectedListener{
	final String[] rockTypes = { "Select", "Igneous", "Metamorphic" ,"Sedimentary" };
    final String[] strctTypes = {"Select", "Fault", "Fold" ,"Joint" };
    private Spinner rockTypeSpn;
    private Spinner strctTypeSpn;
    private static final int IGNEOUS_MINERAL_ACTIVITY = 100;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("## Sample Details", "Samples started");
		setContentView(R.layout.activity_sample_details_one);
		currProject =  (Project) getIntent().getSerializableExtra("Project");
		Log.e("Project " , String.valueOf(currProject));

		ArrayAdapter<String> rockAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line,rockTypes);
		ArrayAdapter<String> strctAdapter = new ArrayAdapter<String>(this, 
						android.R.layout.simple_dropdown_item_1line,strctTypes);
		rockTypeSpn  = (Spinner)  findViewById(R.id.dtls_rockTypeSpn);
		rockTypeSpn.setAdapter(rockAdapter);
		rockTypeSpn.setOnItemSelectedListener(this);
		strctTypeSpn = (Spinner) findViewById(R.id.dtls_strcTypeSpn);
		strctTypeSpn.setAdapter(strctAdapter);
		strctTypeSpn.setOnItemSelectedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_details, menu);
		return true;
	}
	public void startNextActivity(Class<?> activityClass, int activityCode) {
		invokeNextActivity(activityClass, activityCode);
		return;
	}

	@Override
	public void onItemSelected(AdapterView<?> spinner, View view, int pos,
			long arg3) {
		Class<?> activityClass = null;
		int 	 activityCode = 0;
		switch (spinner.getId()) {
			case R.id.dtls_rockTypeSpn:
				Log.e("Rock" , "Selected Id : " + String.valueOf(view.getId() + " Value : " + String.valueOf(pos)));
				if (rockTypes[pos].equals("Igneous")) {
					activityClass = IgneousMineral.class;
					activityCode  = IGNEOUS_MINERAL_ACTIVITY;
					currProject.rockType = RockType.IGNEOUS;
				}
				else if (rockTypes[pos].equals("Sedimentary")) {
					activityClass = IgneousMineral.class;
					activityCode  = IGNEOUS_MINERAL_ACTIVITY;
					currProject.rockType = RockType.SEDIMENTARY;
				}

				break;
			case R.id.dtls_strcTypeSpn:
//				Log.e("Struct" , "Selected Id : " + String.valueOf(view.getId() + " Value : " + String.valueOf(pos)));
				break;
		}
		if (activityCode != 0) {
			startNextActivity(activityClass, activityCode);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}


}

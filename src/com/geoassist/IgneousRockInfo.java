package com.geoassist;

import com.geoassist.R;
import com.geoassist.data.RockType;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class IgneousRockInfo extends BaseActivity implements OnClickListener, OnItemSelectedListener {

	Spinner    rockTypeSpn = null;
	static final int START_ROCK_DETAILS = 100;
	static final int START_GRAIN_DETAILS = 100;
	
	final String[] igneousRrockTypes  = { "Select", "QAP Diagram", "Rock Name" ,"Dike" ,"Piroclastic Diagram"};
	final String[] sedRockTypes  = { "Select", "Siliciclastic", "Chemical" ,"Bio Chemical"};
	private String[] rockTypes = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_igneous_rock_info);
		rockTypeSpn = (Spinner) findViewById(R.id.ignRockType);
		switch (currProject.rockType ) {
			case RockType.IGNEOUS:
				rockTypes = igneousRrockTypes;
				break;
			case RockType.SEDIMENTARY:
				rockTypes = sedRockTypes;
				break;
		}
		ArrayAdapter<String> rockTypeAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line, rockTypes);
        rockTypeSpn.setAdapter(rockTypeAdapter);
        rockTypeSpn.setOnItemSelectedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.igneous_rock_info, menu);
		return true;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}

	public void startNextActivity(String  type) {
		Intent intnt = new Intent(this, IgneousRockDetails.class);
		intnt.putExtra("Type", type);
		Log.e("Intent ", "Started");
		startActivityForResult(intnt, START_ROCK_DETAILS);
		return;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		if (parent.getId() == R.id.ignRockType) {
			if (rockTypes[pos].equals("Rock Name" )){
				Log.e("Type ", "Rock Name");
				startNextActivity("Rock Name");
			}
			else if (rockTypes[pos].equals("Dike" )){
				Log.e("Type ", "Dike");
				startNextActivity("Dike");
			}
			else if (currProject.rockType == RockType.SEDIMENTARY) {
				invokeNextActivity(GrainDetails.class, START_GRAIN_DETAILS  );
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}

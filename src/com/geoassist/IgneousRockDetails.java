package com.geoassist;

import com.geoassist.R;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class IgneousRockDetails extends BaseActivity implements
		OnItemSelectedListener, OnClickListener {
	ImageButton doneBtn;
	Spinner rockNameSpn;
	final String[] rockNames = { "Select", "Granite", "Granodiorite",
			"Diorite", "Gabbro", "Rhyolite", "Andersite", "Basalt", "Other" };
	Spinner dikeCmpSpn;
	final String[] compositions = { "Select", "Quartz", "Aplite", "Diabase",
			"Pegmatite", "Alaskite", "Other" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intnt = getIntent();
		if (intnt != null) {
			String data = intnt.getStringExtra("Type");
			if (data.equals("Rock Name")) {
				setContentView(R.layout.activity_igneous_rock_details);
				rockNameSpn = (Spinner) findViewById(R.id.ignRockNameSpn);
				ArrayAdapter<String> rockTypeAdapter = new ArrayAdapter<String>(
						this, android.R.layout.simple_dropdown_item_1line,
						rockNames);
				rockNameSpn.setAdapter(rockTypeAdapter);
				rockNameSpn.setOnItemSelectedListener(this);
			} else if (data.equals("Dike")) {
				setContentView(R.layout.activity_dike_rock_details);
				Log.e("Dike ", "Spinner");

				dikeCmpSpn = (Spinner) findViewById(R.id.dikeCompositionSpn);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						this, android.R.layout.simple_dropdown_item_1line,
						compositions);
				dikeCmpSpn.setAdapter(adapter);
				dikeCmpSpn.setOnItemSelectedListener(this);
			}
		}
		doneBtn = (ImageButton) findViewById(R.id.btnDone);
		doneBtn.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.igneous_rock_details, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDone:
			finish();
			break;
		}
	}
}

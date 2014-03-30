package com.geoassist;
import com.geoassist.R;
import com.geoassist.data.Project;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class UserDetails extends BaseActivity implements OnClickListener{

	EditText etLocation = null;
	EditText etUsrName  = null;
	EditText etProjName  = null;

	boolean clickedLocBtn = false;
	ImageButton nextBtn;
	static final int LOCATION_ACTIVITY = 100;
	static final int START_GATHER_DETAILS = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_details);
		etLocation = (EditText) findViewById(R.id.lcnEt);
		etUsrName  = (EditText) findViewById(R.id.userNameEt);
		etProjName = (EditText) findViewById(R.id.projNameEt);
		currProject =  (Project) getIntent().getSerializableExtra("Project");

		Log.e("Project " , String.valueOf(currProject));
		nextBtn = (ImageButton) findViewById(R.id.nextBtn);
		nextBtn .setOnClickListener(this);
	}

	public void setupLocationTouchListener() {
		etLocation.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(!clickedLocBtn) {
					clickedLocBtn = true;
//					Intent i = new Intent(UserDetails.this, LocationFindActivity.class);
					Intent i = new Intent(UserDetails.this, GisMap.class);
					startActivityForResult(i, LOCATION_ACTIVITY);
				}
				return false;
			}
		});
	}

	public void startNextActivity() {
		Intent intnt = new Intent(this, SampleDetailsOne.class);
		intnt.putExtra("Project", currProject);  
		startActivityForResult(intnt, START_GATHER_DETAILS);
		return;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.nextBtn:
				// Collect the details that user has entered.
				collectDetails();
				startNextActivity();
				break;
		}
	}
	
	public void collectDetails() {
		Log.e(" CurrProject", String.valueOf(currProject));
		currProject.scientistName = etUsrName.getText().toString();  
		currProject.name		  = etProjName.getText().toString();
		currProject.location	  = etLocation.getText().toString();  
		Log.e("USER Details" , "USR  Name : " + currProject.scientistName +
							   "Proj Name : " + currProject.name +
							   "Location  : " + currProject.location);
	}
}

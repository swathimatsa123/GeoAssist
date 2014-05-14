package com.geoassist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.geoassist.data.MetDtls;
import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;

public class MetmorphicAddition extends BaseActionBarActivity implements OnClickListener{
	ImageButton doneBtn;
	ImageButton cancelBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.met_dtls_add);
	    ActionBar actionBar = getSupportActionBar();

	    actionBar.setCustomView(R.layout.mineral_add_menu);
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	    doneBtn  = (ImageButton)actionBar.getCustomView().findViewById(R.id.done);
	    doneBtn.setOnClickListener(this);
	    doneBtn.getRootView().setBackgroundColor(0xFFF0FFF0);

	    cancelBtn  = (ImageButton)actionBar.getCustomView().findViewById(R.id.cancel);
	    cancelBtn.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.done:
				WorkingProject proj = WorkingProject.getInstance();
				SaveDetails(proj);
				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				break;
			case R.id.cancel:
				finish();
				break;
		}
	}
	public void SaveDetails(WorkingProject proj ) {
		MetDtls metDtl = new MetDtls();
		EditText strke = (EditText) findViewById(R.id.strikeEt);
		EditText dip   = (EditText) findViewById(R.id.dipEt);
		EditText lineation   = (EditText) findViewById(R.id.lineationEt);
		EditText plunge = (EditText) findViewById(R.id.plungeEt);
		metDtl.strike = strke.getText().toString();
		metDtl.dip    = dip.getText().toString();
		metDtl.lineation = lineation.getText().toString();
		metDtl.plunge = plunge.getText().toString();
		
		Site site = proj.sites.get(proj.sites.size()-1);
		site.metDtls.add(metDtl);
	}
	
}
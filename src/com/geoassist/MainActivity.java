package com.geoassist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.geoassist.data.Project;

public class MainActivity extends BaseActionBarActivity implements OnClickListener{
	
	ImageButton  addBtn;
	ImageButton  opnBtn;
	static final int START_NEW_PROJECT = 100;
	static final int START_MAP_ACTIVITY= 200;
	SharedPreferences preferences;
	ImageButton		settingsBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    ActionBar actionBar = getSupportActionBar();
	    // add the custom view to the action bar
	    actionBar.setCustomView(R.layout.main_activity_menu);
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		addBtn = (ImageButton) findViewById(R.id.add_project);
		opnBtn = (ImageButton) findViewById(R.id.open_project);
		addBtn.setOnClickListener(this);
		opnBtn.setOnClickListener(this);
	    settingsBtn  = (ImageButton)actionBar.getCustomView().findViewById(R.id.settings);
	    settingsBtn.setOnClickListener(this);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String usrName  = preferences.getString("username", "");
		if (usrName.equals("")) {
			startSettings();
		}
	}
	
	@Override
	public View onCreateView(View parent, String name, Context context,
			AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(parent, name, context, attrs);
	}
	
	public void startNewProject() {
		Intent intnt = new Intent(this, MainMap.class);
		currProject = new Project();
		intnt.putExtra("Project", currProject); 
		startActivityForResult(intnt, START_NEW_PROJECT);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		return;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.add_project:
				startNewProject();
				break;
			case R.id.settings:
				Log.e("Settings", "Called");
				startSettings();
				break;
		}
	}

}

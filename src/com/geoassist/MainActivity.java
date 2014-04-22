package com.geoassist;

import com.geoassist.R;
import com.geoassist.data.Project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends BaseActivity implements OnClickListener{
	
	ImageButton  addBtn;
	ImageButton  opnBtn;
	static final int START_NEW_PROJECT = 100;
	static final int START_MAP_ACTIVITY= 200;
	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addBtn = (ImageButton) findViewById(R.id.add_project);
		opnBtn = (ImageButton) findViewById(R.id.open_project);
		addBtn.setOnClickListener(this);
		opnBtn.setOnClickListener(this);
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
		Log.e("Intent ", "Started");
		currProject = new Project();
		Log.e("## In  New", String.valueOf(currProject));
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
		}
	}

}

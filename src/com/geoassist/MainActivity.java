package com.geoassist;

import java.util.List;

import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.geoassist.data.GeoDb;
import com.geoassist.data.Project;

public class MainActivity extends BaseActionBarActivity implements OnClickListener{
	
	ImageButton  addBtn;
	ImageButton  opnBtn;
	static final int START_NEW_PROJECT = 100;
	static final int START_MAP_ACTIVITY= 200;
	SharedPreferences preferences;
	ImageButton		settingsBtn;
	public GeoDb 	db ;
	public Dialog   dlg;
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
		db = new GeoDb(this);
//		db.addProject("Test1");
//		db.addSite("Test1", "S21N34");
//		db.addProject("Test2");
//		db.addSite("Test2", "S33N33");
//		db.getAllProjects();
//		db.getSitesForProject("Test1");
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

	public void pickProject() {
		final Dialog dialog = new Dialog(this);
		this.dlg = dialog;
		dialog.setContentView(R.layout.project_select);
		dialog.setTitle("Select Project");
		List <String> projects  = this.db.getAllProjects();
		for (int i =0; i< projects.size(); i++) {
			Log.e("In Pick project", "### Project Name " + projects.get(i));
		}
		String[] projectNames = new String[projects.size()];
		projectNames = projects.toArray(projectNames);
		Spinner projSpn = (Spinner) dialog.findViewById(R.id.projNameSpn);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
									android.R.layout.simple_dropdown_item_1line,
									projectNames);

		projSpn.setAdapter(adapter);			
		
		ImageButton okButton = (ImageButton) dialog.findViewById(R.id.doneBtn);
		okButton.setOnClickListener(this);

		// if button is clicked, close the custom dialog
//		okButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				secondOrderFold[(int) secondOrderSpn.getSelectedItemId()];
//				dialog.dismiss();
//			}
//		});

		ImageButton cnclButton = (ImageButton) dialog.findViewById(R.id.cancelBtn);
		// if button is clicked, close the custom dialog
		cnclButton.setOnClickListener(this);
		dialog.show();
	    return;
	}	
	public void projectSelectionDone() {
		Spinner projSpn = (Spinner) dlg.findViewById(R.id.projNameSpn);
//		secondOrderFold[(int) secondOrderSpn.getSelectedItemId()];
//		projSpn.getSelectedItem().toString();
		Log.e("Spinner Selected is ", projSpn.getSelectedItem().toString());
		this.db.constructProjectFromDb(projSpn.getSelectedItem().toString());
		dlg.dismiss();
		Log.e("Starting project ", projSpn.getSelectedItem().toString());
		startNewProject();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.add_project:
				startNewProject();
				break;
			case R.id.open_project:
				pickProject();
				break;
			case R.id.settings:
				Log.e("Settings", "Called");
				startSettings();
				break;
			case R.id.doneBtn:
				Log.e("Done", "Called");
				projectSelectionDone();
				break;				
			case R.id.cancelBtn:
				this.dlg.dismiss();
		}
	}

}

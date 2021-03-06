package com.geoassist;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import com.geoassist.data.Project;
import com.geoassist.data.ProjectReport;
import com.geoassist.data.WorkingProject;

public class BaseActivity extends Activity {
	static final int START_MAP_ACTIVITY= 200;
	static final int START_SETTINGS_ACTIVITY = 300;
	Project 	currProject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currProject =  (Project) getIntent().getSerializableExtra("Project");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.simple_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean retVal = true;
	    switch (item.getItemId()) {
//		    case R.id.ic_map:
//		    	Log.e("OPTION MAP" , "Selected");
//		    	break;
////		    case R.id.ic_notes:
////		    	Log.e("OPTION Notes" , "Selected");
////		    	break;
//		    	
//		    case R.id.ic_email:
//		    	Log.e("OPTION Email" , "Selected");
////		    	saveProject(currProject);
//		    	break;
//		    case R.id.ic_save:
//		    	this.finish();
//		    	break;
//		    case R.id.ic_action_settings:
//		    	Log.e("OPTION Settings" , "Selected");
//		    	startSettings();
//		    	break;
		    	
		    default:
		      retVal = super.onOptionsItemSelected(item);
		      break;
	    }
	    return retVal;
	}


	public void startSettings( ){
		Intent intnt = new Intent(this, SettingsActivity.class);
		startActivityForResult(intnt, START_SETTINGS_ACTIVITY);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		return;
	}
	
	public void saveProject ( WorkingProject project){
		ProjectReport rp = new ProjectReport();
		rp.save(project);
		sendReport(rp.getFileName(), project);
		return;
	}
	
	public void sendReport(String fileName, WorkingProject proj) {
	    Uri attachment = Uri.fromFile(new File(fileName));
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("application/pdf");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"matsa.swathi@gmail.com"});
		intent.putExtra(Intent.EXTRA_SUBJECT, "Report from field exploration @" +proj.location);
		intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Please find the attached report"));
		intent.putExtra(Intent.EXTRA_STREAM, attachment);
		startActivity(intent);
	}	

	public void invokeNextActivity(Class<?> activityClass, int activityCode) {
		Intent intnt = new Intent(this, activityClass);
		intnt.putExtra("Project", currProject);  
		startActivityForResult(intnt, activityCode);
		return;
	}

}


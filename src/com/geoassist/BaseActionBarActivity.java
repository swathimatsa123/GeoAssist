package com.geoassist;
import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.geoassist.data.GeoDb;
import com.geoassist.data.Project;
import com.geoassist.data.ProjectReport;
import com.geoassist.data.User;
import com.geoassist.data.WorkingProject;


public class BaseActionBarActivity extends ActionBarActivity {

		static final int START_MAP_ACTIVITY= 200;
		static final int START_SETTINGS_ACTIVITY = 300;
		Project 	currProject;
		public GeoDb		db = null;
		public void startSettings( ){
			Intent intnt = new Intent(this, SettingsActivity.class);
			startActivityForResult(intnt, START_SETTINGS_ACTIVITY);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
			return;
		}
		
		public boolean isNumber(String str) {
			try { 
		        Integer.parseInt(str); 
		    } catch(NumberFormatException e) { 
		        return false; 
		    }
		    return true;
		}

		public Spinner setSpinner (int id,  final String[] types) {
			Spinner spn = (Spinner)  findViewById(id);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
										android.R.layout.simple_dropdown_item_1line,
										types);
			spn.setAdapter(adapter);
			return spn;
		}


		public void saveProject ( WorkingProject project){
			ProjectReport rp = new ProjectReport();
			rp.save(project);
			sendReport(rp.getFileName(), project);
			return;
		}
		
		public void sendReport(String fileName, WorkingProject  proj) {
		    Uri attachment = Uri.fromFile(new File(fileName));
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("application/pdf");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"matsa.swathi@gmail.com"});
			User usr = User.getInstance();
			intent.putExtra(Intent.EXTRA_SUBJECT, "Report from field exploration by " +usr.userName);
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

		public void exitAlert() {
			 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BaseActionBarActivity .this);
		     alertDialogBuilder.setTitle(this.getTitle()+ " decision");
			 alertDialogBuilder.setMessage("Project not Saved. \n Do you want to save your work?");
			 // set positive button: Yes message
			 alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						saveDialog();
					}
				  });
			 // set negative button: No message
			 alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// cancel the alert box and put a Toast to the user
						dialog.cancel();
						BaseActionBarActivity.this.finish();
					}
				});
//			 // set neutral button: Exit the app message
//			 alertDialogBuilder.setNeutralButton("Exit the app",new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int id) {
//						// exit the app and go to the HOME
////						MainActivity.this.finish();
//					}
//				});
			 
			 AlertDialog alertDialog = alertDialogBuilder.create();
			 // show alert
			 alertDialog.show();
		}

		public void saveDialog() {
			 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BaseActionBarActivity .this);
		     alertDialogBuilder.setTitle("Save Project as");
		     LayoutInflater layoutInflater = LayoutInflater.from(BaseActionBarActivity .this);
		     View promptView = layoutInflater.inflate(R.layout.file_save, null);
		     alertDialogBuilder.setView(promptView);
		     final EditText projNameEt = (EditText) promptView.findViewById(R.id.projNameEt);
		      
			 alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						WorkingProject proj = WorkingProject.getInstance();
						proj.name = projNameEt.getText().toString();
						BaseActionBarActivity.this.db = GeoDb.getInstance(BaseActionBarActivity.this);
						Log.e("DB Instance is ", String.valueOf(BaseActionBarActivity.this.db));
						if (BaseActionBarActivity.this.db != null) {
							BaseActionBarActivity.this.db.saveWorkingProject(proj);
						}
						BaseActionBarActivity.this.finish();
					}
				  });
			 alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						BaseActionBarActivity.this.finish();
					}
			 });
			 
			 AlertDialog alertDialog = alertDialogBuilder.create();
			 // show alert
			 alertDialog.show();
		}

		public String getTextFromEt(int etId) {
			EditText et = (EditText)findViewById(etId);
			String ret = "";
			if (et != null) {
				ret = et.getText().toString();
			}
			return ret;
		}

		public String getTextFromSpinner(int spnId) {
			Spinner spn= (Spinner)findViewById(spnId);
			String ret = "";
			if (spn != null) {
				ret = spn.getSelectedItem().toString();
			}
			return ret;
		}
}


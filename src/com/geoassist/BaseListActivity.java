package com.geoassist;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.geoassist.data.ProjectReport;
import com.geoassist.data.WorkingProject;

public class BaseListActivity extends ExpandableListActivity implements OnClickListener{
	static final int START_MAP_ACTIVITY= 200;
	static final int START_SETTINGS_ACTIVITY = 300;
	static final int START_CAMERA_ACTIVITY = 400;
    private File mediaFile=null;

	WorkingProject workingProj = WorkingProject.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    ActionBar actionBar = getActionBar();
	    actionBar.setCustomView(R.layout.sample_menu);
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	    
	    ImageButton doneBtn = (ImageButton)actionBar.getCustomView().findViewById(R.id.done);
	    doneBtn.setOnClickListener(this);
	    ImageButton cancelBtn = (ImageButton)actionBar.getCustomView().findViewById(R.id.cancel);
	    cancelBtn.setOnClickListener(this);
	    ImageButton cameraBtn = (ImageButton)actionBar.getCustomView().findViewById(R.id.newSite);
	    cameraBtn.setOnClickListener(this);
	    ImageButton compassBtn = (ImageButton)actionBar.getCustomView().findViewById(R.id.report);
	    compassBtn.setOnClickListener(this);
	    ImageButton notesBtn = (ImageButton)actionBar.getCustomView().findViewById(R.id.perSampleNotes);
	    notesBtn.setOnClickListener(this);
	    ImageButton settingsBtn = (ImageButton)actionBar.getCustomView().findViewById(R.id.settings);
	    settingsBtn.setOnClickListener(this);
	    
	}


//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		boolean retVal = true;
//	    switch (item.getItemId()) {
//		    case R.id.perSampleNotes:
//		    	Log.e("OPTION Notes" , "Selected");
//		    	break;
//
//		    case R.id.report:
//		    	Log.e("OPTION Compass" , "Selected");
//		    	break;
//		    
//		    case R.id.newSite:
//		    	invokeCamera();
//		    	break;
//
//		    case R.id.cancel:
//		    	Log.e("OPTION Cancel" , "Selected");
////		    	this.finish();
//		    	break;
//		    case R.id.done:
//		    	Log.e("OPTION Save" , "Selected");
//		    	this.finish();
//		    	break;
//		    case R.id.settings:
//		    	Log.e("OPTION Settings" , "Selected");
//		    	startSettings();
//		    	break;
//		    	
//		    default:
//		      retVal = super.onOptionsItemSelected(item);
//		      break;
//	    }
//	    return retVal;
//	}

	public boolean isNumber(String str) {
		try { 
	        Integer.parseInt(str); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}

	public void startSettings( ){
		Intent intnt = new Intent(this, SettingsActivity.class);
		startActivityForResult(intnt, START_SETTINGS_ACTIVITY);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		return;
	}
	
	public void invokeActivity(Class<?> activityClass, int activityCode) {
		Intent intnt = new Intent(this, activityClass);
		startActivityForResult(intnt, activityCode);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		return;
	}
	
	
	public void saveProject ( WorkingProject workingproject){
		ProjectReport rp = new ProjectReport();
//		rp.save(workingproject);
		sendReport(rp.getFileName(), workingproject);
		return;
	}
	
	public void sendReport(String fileName, WorkingProject  proj) {
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
//		intnt.putExtra("Project", currProject);  
		startActivityForResult(intnt, activityCode);
		return;
	}
	
	public void invokeCamera()
	{
    	Intent mIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	mIntent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri()); // set the image file name
    	startActivityForResult(mIntent, START_CAMERA_ACTIVITY);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode == Activity.RESULT_OK) {
	        	galleryAddPic();
	        	if (data != null){ 
	            	Toast.makeText(this, "Image saved to:" +
	                     data.getData()+"---", Toast.LENGTH_LONG).show();
	            }
	        } else if (resultCode == Activity.RESULT_CANCELED) {
	        } else {
//	        	Toast.makeText(myCxt, "Failed Activity:" , Toast.LENGTH_LONG).show();
	        }
	}

	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    Uri contentUri = Uri.fromFile(mediaFile);
	    mediaScanIntent.setData(contentUri);
	    sendBroadcast(mediaScanIntent);
	    Intent editIntent = new Intent(Intent.ACTION_EDIT);
	    editIntent.setDataAndType(contentUri, "image/*");
	    editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	    startActivity(Intent.createChooser(editIntent, null));
	}

		
	private Uri getOutputMediaFileUri(){
	      return Uri.fromFile(getOutputMediaFile());
	}
	/** Create a File for saving an image or video */
	private File getOutputMediaFile(){

		   File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
		              Environment.DIRECTORY_PICTURES), "GeoAssist");
		    if (! mediaStorageDir.exists()){
		        if (! mediaStorageDir.mkdirs()){
		            Log.d("GeoAssist", "failed to create directory");
		            return null;
		        }
		    }

		    // Create a media file name
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		    mediaFile = new File(mediaStorageDir.getPath() + File.separator +"geoAssist_"+ timeStamp + ".jpg");
//		    Log.e("FileName" , "Return File:" + mediaStorageDir.getPath()+ File.separator  + "geoAssist_" + timeStamp + ".jpg");
		    return mediaFile;
	}

	@Override
	public void onClick(View v) {
	    switch (v.getId()) {
		    case R.id.perSampleNotes:
		    	Log.e("OPTION Notes" , "Selected");
		    	break;
	
		    case R.id.report:
		    	Log.e("OPTION Compass" , "Selected");
		    	break;
		    
		    case R.id.newSite:
		    	invokeCamera();
		    	break;

			case R.id.done:
				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				break;
			case R.id.cancel:
				finish();
				break;

			case R.id.settings:
		    	Log.e("OPTION Settings" , "Selected");
		    	startSettings();
		    	break;
		    default:
		      break;
	    }
	    return ;
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

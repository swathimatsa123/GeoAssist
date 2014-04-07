package com.geoassist;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.geoassist.data.ProjectReport;
import com.geoassist.data.WorkingProject;

public class BaseListActivity extends ExpandableListActivity {
	static final int START_MAP_ACTIVITY= 200;
	static final int START_SETTINGS_ACTIVITY = 300;
	static final int START_CAMERA_ACTIVITY = 400;
    private File mediaFile=null;

	WorkingProject workingProj = WorkingProject.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		workingProj =  (WorkingProject) getIntent().getSerializableExtra("Project");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean retVal = true;
	    switch (item.getItemId()) {
		    case R.id.ic_map:
		    	Log.e("OPTION MAP" , "Selected");
		    	startMap();
		    	break;
//		    case R.id.ic_notes:
//		    	Log.e("OPTION Notes" , "Selected");
//		    	break;
//		    	
		    case R.id.ic_email:
		    	Log.e("OPTION Email" , "Selected");
		    	saveProject(workingProj);
		    	break;
		    case R.id.ic_camera:
		    	invokeCamera();
		    	break;
		    case R.id.ic_done:
		    	Log.e("OPTION Email" , "Selected");
//		    	saveProject(workingProj);
		    	this.finish();
		    	break;
		    case R.id.ic_cancel:
		    	Log.e("OPTION Cancel" , "Selected");
		    	this.finish();
		    	break;
		    case R.id.ic_save:
		    	Log.e("OPTION Save" , "Selected");
		    	break;
		    case R.id.ic_action_settings:
		    	Log.e("OPTION Settings" , "Selected");
		    	startSettings();
		    	break;
		    	
		    default:
		      retVal = super.onOptionsItemSelected(item);
		      break;
	    }
	    return retVal;
	}

	public void startMap( ){
		Intent intnt = new Intent(this, LocationFindActivity.class);
		Log.e("Intent ", "Started");
		startActivityForResult(intnt, START_MAP_ACTIVITY);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		return;
	}

	public void startSettings( ){
		Intent intnt = new Intent(this, SettingsActivity.class);
		Log.e("Intent ", "Started");
		startActivityForResult(intnt, START_SETTINGS_ACTIVITY);
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
//		   Toast.makeText(myCxt, "Activity completed", Toast.LENGTH_LONG).show();
	        if (resultCode == Activity.RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	        	galleryAddPic();
	        	if (data != null){ 
	            	Toast.makeText(this, "Image saved to:" +
	                     data.getData()+"---", Toast.LENGTH_LONG).show();
	            }
	        } else if (resultCode == Activity.RESULT_CANCELED) {
//	        	Toast.makeText(myCxt, "Cancelled Activity:" , Toast.LENGTH_LONG).show();
	        } else {
//	        	Toast.makeText(myCxt, "Failed Activity:" , Toast.LENGTH_LONG).show();
	        }
	}

	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    Uri contentUri = Uri.fromFile(mediaFile);
	    mediaScanIntent.setData(contentUri);
	    sendBroadcast(mediaScanIntent);
	    Log.e(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString(),"URI1");
	    Log.e(contentUri.toString(),"URI2");
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

}

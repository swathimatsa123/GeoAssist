package com.geoassist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.geoassist.data.WorkingProject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

public class MainMap extends BaseActionBarActivity  implements OnClickListener, 
														   OnFocusChangeListener,
														   OnEditorActionListener,
														   OnItemSelectedListener{
	static final int START_NEW_PROJECT = 100;
	EditText  		projNameEt;
	ImageButton		saveBtn;
	ImageButton		reportBtn;
	ImageButton		cancelBtn;
	ImageButton		sampleBtn;
	ImageButton		settingsBtn;

	Boolean			projNamed = false;
	ArrayList<String> mapFiles = new ArrayList<String>();
	ArrayList<String> mapsWithPath = new ArrayList<String>();
	PlaceholderFragment mapFrag;
	double lat;
	double lng;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_map);
	    ActionBar actionBar = getSupportActionBar();
	    // add the custom view to the action bar
	    actionBar.setCustomView(R.layout.main_map_menu);
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	    mapFrag = new PlaceholderFragment();
	    if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, mapFrag).commit();
		}
//	    projNameEt = (EditText) actionBar.getCustomView().findViewById(R.id.mainProjName);
//	    projNameEt.setText("Untitled Project");
//	    projNameEt.clearFocus();
//	    projNameEt.setInputType(InputType.TYPE_NULL);
//	    projNameEt.setOnFocusChangeListener(this);
//	    projNameEt.setOnEditorActionListener(this);
	    saveBtn    = (ImageButton)actionBar.getCustomView().findViewById(R.id.done);
	    saveBtn.setOnClickListener(this);
	    reportBtn    = (ImageButton)actionBar.getCustomView().findViewById(R.id.compass);
	    reportBtn.setOnClickListener(this);

	    cancelBtn    = (ImageButton)actionBar.getCustomView().findViewById(R.id.cancel);
	    cancelBtn.setOnClickListener(this);

	    sampleBtn  = (ImageButton)actionBar.getCustomView().findViewById(R.id.camera);
	    sampleBtn.setOnClickListener(this);

	    settingsBtn  = (ImageButton)actionBar.getCustomView().findViewById(R.id.settings);
	    settingsBtn.setOnClickListener(this);
	    
	    mapFiles.add("Select Map Source");
		this.walkdir(Environment.getExternalStorageDirectory());
		ArrayAdapter<String> mapAdapter = new ArrayAdapter<String>(this, 
					android.R.layout.simple_dropdown_item_1line, mapFiles);
		Spinner mapFilesSpn  = (Spinner)  findViewById(R.id.mainMapSrc);
		Log.e("MapFileSpn", String.valueOf(mapFilesSpn));
		mapFilesSpn.setAdapter(mapAdapter);
		mapFilesSpn.setOnItemSelectedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void walkdir(File dir) {
	    String pattern = ".mbtiles";

	    File[] listFile = dir.listFiles();
	    if (listFile != null) {
	        for (int i = 0; i < listFile.length; i++) {
	            if (listFile[i].isDirectory()) {
	                walkdir(listFile[i]);
	            } else if (listFile[i].getName().endsWith(pattern)){
//	            	Log.e("Adding" , listFile[i].getName());
	            	mapFiles.add(listFile[i].getName());
	            	mapsWithPath.add(Environment.getExternalStorageDirectory()+"/" +  dir.getName()+"/" +
	            					listFile[i].getName());
//	            	Log.e("Path", Environment.getExternalStorageDirectory()+"/" + 
//	            					dir.getName()+"/" +
//	            					listFile[i].getName());
	            }
	        }
	    }    
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
		Log.e("Spinner Select ", "Value is  "+ String.valueOf(R.id.mainMapSrc)+ " " + view.getId());
		Log.e("Parent Select ", "Value  is  "+ parent.getId());

		if (parent.getId() == R.id.mainMapSrc) {
			Log.e("Inner Spinner Select ", "Value is "+ String.valueOf(pos));
			WorkingProject proj = WorkingProject.getInstance();
			if (pos != 0) {
				proj.mapFile = mapsWithPath.get(pos-1);
				Log.e("MAP File", proj.mapFile);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		Log.e("Nothing Selecte", "Why");
		
	}

	public void startNewSite() {
		Intent intnt = new Intent(this, UserDetails.class);
		Bundle dataBundle = new Bundle();
		dataBundle.putDouble("LatValue", this.mapFrag.currentLat);
		dataBundle.putDouble("LongValue",this.mapFrag.currentLng);
		intnt.putExtras(dataBundle);
		startActivityForResult(intnt, START_NEW_PROJECT);
		return;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment 				
								implements GooglePlayServicesClient.ConnectionCallbacks,
								GooglePlayServicesClient.OnConnectionFailedListener,
								LocationListener,
								OnClickListener,
								OnEditorActionListener{
		LocationRequest 			mLocationRequest;
		private Marker 				myPosMarker;
		private GoogleMap 			map;
		private LocationClient 		locationclient;
		public static final int 	MINUTE_IN_SECONDS = 60;
		public static final int 	UPDATE_INTERVAL_IN_SECONDS = 60 *MINUTE_IN_SECONDS;
		public static final int 	UPDATE_INTERVAL_FASTEST_IN_SECONDS =  MINUTE_IN_SECONDS;
		public double 				currentLat = 0;
		public double 				currentLng = 0;
		public LatLng 				myPos;    
		List<Marker> markerList = new ArrayList<Marker>();
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_map,
					container, false);
			int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
			if(resp == ConnectionResult.SUCCESS){
				locationclient = new LocationClient(getActivity(),this, this);
				locationclient.connect();
			}
			else{
				Toast.makeText(getActivity(), "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
			}				
			 mLocationRequest = LocationRequest.create();
		     mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		     mLocationRequest.setInterval(UPDATE_INTERVAL_IN_SECONDS);
		     mLocationRequest.setFastestInterval(UPDATE_INTERVAL_IN_SECONDS);	
		     return rootView;
		}

		@Override
		public void onLocationChanged(Location location) {
		    currentLat = location.getLatitude();
		    currentLng = location.getLongitude();
			myPos      = new LatLng(currentLat,currentLng);
			WorkingProject proj = WorkingProject.getInstance();
			if (map == null) {
				SupportMapFragment supportMap = (SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
				map = supportMap.getMap();
	
				if (map != null) {
						//user marker stuff
						map.setMapType(GoogleMap.MAP_TYPE_NONE);
						if (proj.mapFile != null) {
							map.addTileOverlay(new TileOverlayOptions().tileProvider(new MBTileAdapter(proj.mapFile )));
						}
						myPosMarker = map.addMarker(new MarkerOptions()
				    									.position(myPos)
				    									.draggable(false));
				    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
				}
			}
			else {
				if (proj.mapFile != null) {
					map.addTileOverlay(new TileOverlayOptions().tileProvider(new MBTileAdapter(proj.mapFile )));
				}
				myPosMarker.setPosition(myPos);
			}
			/* Add Samples here */
			Log.e("Sites" , String.valueOf(proj.sites.size()) + "Markers "+ String.valueOf(markerList.size()));
			
			for (int i=0; i < proj.sites.size(); i++) {
					LatLng 	sitePos = new LatLng(proj.sites.get(i).lat, proj.sites.get(i).lng);
					if (proj.sites.get(i).lat != 0) {
						Log.e("Adding", "@ "+ String.valueOf(proj.sites.get(i).lat)+ " "+ String.valueOf(proj.sites.get(i).lng));
						Log.e("Marker" , "I: "+ String.valueOf(i) + "Size "+ String.valueOf(markerList.size()));
						myPosMarker = map.addMarker(new MarkerOptions()
														.position(sitePos)
														.title("Sample "+ String.valueOf(i))
														.draggable(false));
						markerList.add(myPosMarker);
					}
			}
		}

		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onConnected(Bundle connectionHint) {
			locationclient.requestLocationUpdates(mLocationRequest, 
												 (com.google.android.gms.location.LocationListener) this);
		}

		@Override
		public void onDisconnected() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.done:
			Log.e("Save", "Called");
			WorkingProject workingProj = WorkingProject.getInstance();
//			saveProject(workingProj);
			this.saveDialog();
//			this.finish();
			break;
		case R.id.compass:
			Log.e("Report", "Called");
			this.finish();
			break;
		case R.id.settings:
			Log.e("Settings", "Called");
			startSettings();
			break;
		case R.id.cancel:
			Log.e("Cancel", "Called");
			this.exitAlert();
			break;
		case R.id.camera:
			Log.e("AddSampe", "Called");
			startNewSite();
			break;
			
		}
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if ((hasFocus) && (projNamed != true)) {
			projNameEt.setText("");
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(projNameEt, InputMethodManager.SHOW_FORCED);
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//		if (v.getId() == R.id.mainProjName) {
//			if (actionId == EditorInfo.IME_ACTION_DONE) {
//				projNamed = true;
//				WorkingProject proj = WorkingProject.getInstance();
//				proj.name = projNameEt.getText().toString();
//			}
//		}
		return false;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case START_NEW_PROJECT:
				
				break;
			}
		}
	}
}

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

import com.geoassist.data.Site;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

public class MainMap extends BaseActionBarActivity  implements OnClickListener, 
														   OnFocusChangeListener,
														   OnEditorActionListener,
														   OnItemSelectedListener{
	static final int NEW_SITE_ACTIVITY = 100;
	EditText  		projNameEt;
	ImageButton		saveBtn;
	ImageButton		reportBtn;
	ImageButton		cancelBtn;
	ImageButton		sampleBtn;
	ImageButton		settingsBtn;
	Spinner         mapLyrSpn;
	Boolean			projNamed = false;
	ArrayList<String> mapFiles = new ArrayList<String>();
	ArrayList<String> mapsWithPath = new ArrayList<String>();
	final String[] rockTypes = {"All", "Igneous", "Metamorphic","Sedimentary" };
	String			lyrFltr = rockTypes[0];
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
	    saveBtn    = (ImageButton)actionBar.getCustomView().findViewById(R.id.done);
	    saveBtn.setOnClickListener(this);
	    reportBtn    = (ImageButton)actionBar.getCustomView().findViewById(R.id.report);
	    reportBtn.setOnClickListener(this);

	    cancelBtn    = (ImageButton)actionBar.getCustomView().findViewById(R.id.cancel);
	    cancelBtn.setOnClickListener(this);

	    sampleBtn  = (ImageButton)actionBar.getCustomView().findViewById(R.id.newSite);
	    sampleBtn.setOnClickListener(this);

	    settingsBtn  = (ImageButton)actionBar.getCustomView().findViewById(R.id.settings);
	    settingsBtn.setOnClickListener(this);
	    
	    mapFiles.add("Select Map Source");
		this.walkdir(Environment.getExternalStorageDirectory());
		ArrayAdapter<String> mapAdapter = new ArrayAdapter<String>(this, 
					android.R.layout.simple_dropdown_item_1line, mapFiles);
		Spinner mapFilesSpn  = (Spinner)  findViewById(R.id.mainMapSrc);
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
	protected void onResume() {
		mapLyrSpn  = (Spinner) findViewById(R.id.mapLyrSpn);
		ArrayAdapter<String> mapLyrAdapter = new ArrayAdapter<String>(this, 
					android.R.layout.simple_dropdown_item_1line,rockTypes);
		mapLyrSpn.setAdapter(mapLyrAdapter );
		mapLyrSpn.setOnItemSelectedListener(this);
		super.onResume();
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
		if (parent.getId() == R.id.mainMapSrc) {
			WorkingProject proj = WorkingProject.getInstance();
			if (pos != 0) {
				proj.mapFile = mapsWithPath.get(pos-1);
				this.mapFrag.refreshMapView();
			}
		}
		else if (parent.getId() == R.id.mapLyrSpn) {
			if (pos >= 0 ) {
				this.lyrFltr = rockTypes[pos];
				this.mapFrag.refreshMapView();
			}
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	public void startNewSite() {
		Intent intnt = new Intent(this, AddSiteActivity.class);
		Bundle dataBundle = new Bundle();
		dataBundle.putDouble("LatValue", this.mapFrag.currentLat);
		dataBundle.putDouble("LongValue",this.mapFrag.currentLng);
		intnt.putExtras(dataBundle);
		startActivityForResult(intnt, NEW_SITE_ACTIVITY);
		return;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment 				
								implements GooglePlayServicesClient.ConnectionCallbacks,
								GooglePlayServicesClient.OnConnectionFailedListener,
								LocationListener{
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
		    refreshMapView();
		}

		public void refreshMapView() {
		    if ((currentLat == 0) && (currentLng == 0)){
		    	return;
		    }
		    	
			myPos      = new LatLng(currentLat,currentLng);
			for (Marker marker: markerList) {
				marker.remove();
			}
			markerList.clear();
			WorkingProject proj = WorkingProject.getInstance();
			if (map == null) {
				SupportMapFragment supportMap = (SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
				map = supportMap.getMap();
	
				if (map != null) {
//						map.setMapType(GoogleMap.MAP_TYPE_NONE);
						if (proj.mapFile != null) {
							map.addTileOverlay(new TileOverlayOptions().tileProvider(new MBTileAdapter(proj.mapFile )));
						}
				    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
				}
			}
			else {
				if (proj.mapFile != null) {
					map.addTileOverlay(new TileOverlayOptions().tileProvider(new MBTileAdapter(proj.mapFile )));
				}
			}
			BitmapDescriptor usrIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker);
			myPosMarker = map.addMarker(new MarkerOptions()
	    									.position(myPos)
	    									.draggable(false)
	    									.title("You")
	    									.icon(usrIcon));
			myPosMarker.showInfoWindow();
			markerList.add(myPosMarker);
			for (int i=0; i < proj.sites.size(); i++) {
				Site site = proj.sites.get(i);
				LatLng 	sitePos = new LatLng(site.lat, site.lng);
				if (getActivity() == null) {
					return;
				}
					
				if (((MainMap)getActivity()).lyrFltr.equals("All")||
					(site.rockType	== null)||
				    ((MainMap)getActivity()).lyrFltr.equals(site.rockType)) {
					if (site.lat != 0) {
						int bmpSrc = R.drawable.red_pin;
						if (site.rockType!= null) {
							if (site.rockType.equals(((MainMap) getActivity()).rockTypes[1])) {
								bmpSrc = R.drawable.red_pin;
							}
							else if (site.rockType.equals(((MainMap) getActivity()).rockTypes[2])) {
								bmpSrc = R.drawable.blue_pin;
							}
							else if (site.rockType.equals(((MainMap) getActivity()).rockTypes[3])) {
								bmpSrc = R.drawable.green_pin;
							}
						}
						BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(bmpSrc);
						myPosMarker = map.addMarker(new MarkerOptions()
														.position(sitePos)
														.title("Site  "+ String.valueOf(i+1))
														.icon(icon)
														.draggable(false));
						myPosMarker.showInfoWindow();
						markerList.add(myPosMarker);
					}
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
	}

	@Override
	public void onClick(View v) {
		WorkingProject workingProj = WorkingProject.getInstance();
		switch(v.getId()) {
		case R.id.done:
			Log.e("Save", "Called");
			this.saveDialog();
			break;
		case R.id.report:
			Log.e("Report", "Called");
			saveProject(workingProj);
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
		case R.id.newSite:
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
			case NEW_SITE_ACTIVITY:
				this.mapFrag.refreshMapView();
				break;
			}
		}
	}
}

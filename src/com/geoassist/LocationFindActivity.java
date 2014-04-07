package com.geoassist;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.geoassist.R;
import com.geoassist.data.WorkingProject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

public class LocationFindActivity extends FragmentActivity  implements GooglePlayServicesClient.ConnectionCallbacks,
																   GooglePlayServicesClient.OnConnectionFailedListener,
																   LocationListener, 
																   OnMapClickListener,
																   OnMarkerClickListener,
																   OnMarkerDragListener{
	private GoogleMap 			map;
    private LocationClient 		locationclient;
    private double 				currentLat = 0;
    private double 				currentLng = 0;
    private Marker				evtMarker;
    private Marker 				myPosMarker;
    private LatLng 				myPos;
    private LatLng 				evtPos =new LatLng(currentLat,currentLng);
    
    LocationRequest 			mLocationRequest;
    private boolean             mapUpdateRcvd = false;
    private boolean             chosenByDrag  = false;
    public static final int 	MINUTE_IN_SECONDS = 60;
    public static final int 	UPDATE_INTERVAL_IN_SECONDS = 60 *MINUTE_IN_SECONDS;
    public static final int 	UPDATE_INTERVAL_FASTEST_IN_SECONDS =  MINUTE_IN_SECONDS;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_location);
		int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(resp == ConnectionResult.SUCCESS){
			locationclient = new LocationClient(this,this, this);
			locationclient.connect();
		}
		else{
			Toast.makeText(this, "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
		}				
		 mLocationRequest = LocationRequest.create();
	     mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	     mLocationRequest.setInterval(UPDATE_INTERVAL_IN_SECONDS);
	     mLocationRequest.setFastestInterval(UPDATE_INTERVAL_IN_SECONDS);	
	}

    //public void sendDetailsBack(LatLng pos) {
	public void onClickSave(View v) {
		String [] address = null;
		Log.e("Chosen by Drag", String.valueOf(chosenByDrag));
		
		if(evtMarker == null) {
			Log.d("DEBUG", "No location set, so defaulting to cancel");
			onClickCancel(v);
			return;
		}
		Intent data = new Intent();
		data.putExtra("Location", "Nothing");
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish();				
	}
	
	public void onClickCancel(View v) {
		Intent data = new Intent();
		//data.putExtra("Location", null);
		setResult(RESULT_CANCELED, data);
		finish();
	}

	@Override
	public void onLocationChanged(Location location) {
	    currentLat = location.getLatitude();
	    currentLng = location.getLongitude();
		
		//myPos      = new LatLng(currentLat,currentLng);
		setMarkerCurrentUserLocation();
		if (map == null) {
			Log.d("DEBUG", "Map creation is null");

			SupportMapFragment supportMap = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
			map = supportMap.getMap();

			if (map != null) {
					//user marker stuff
					WorkingProject proj = WorkingProject.getInstance();
					map.setMapType(GoogleMap.MAP_TYPE_NONE);
					map.addTileOverlay(new TileOverlayOptions().tileProvider(new MBTileAdapter(proj.mapFile)));


					map.setOnMapClickListener(this);
			    	map.setOnMarkerClickListener(this);
			    	myPosMarker = map.addMarker(new MarkerOptions()
			    									.position(myPos)
			    									.draggable(false)
			    									.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker)));
			        map.setOnMarkerDragListener(this);
			    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
			}
		}
		else {
			Log.d("DEBUG", "Map creation is not null");
			myPosMarker.setPosition(myPos);
		}
		mapUpdateRcvd = true;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
	}
	@Override
    protected void onPause() {
        super.onPause();
        locationclient.removeLocationUpdates((com.google.android.gms.location.LocationListener) this);	
    }
	
	@Override
	public void onConnected(Bundle connectionHint) {
        locationclient.requestLocationUpdates(mLocationRequest, 
        		(com.google.android.gms.location.LocationListener) this);
	}

	@Override
	public void onDisconnected() {
        locationclient.removeLocationUpdates((com.google.android.gms.location.LocationListener) this);	
	}
	
	
	public void setMarkerCurrentUserLocation() {
		Location mCurrentLocation;
		mCurrentLocation = locationclient.getLastLocation();
        Log.d("DEBUG", "Current user location is: lat["+
        mCurrentLocation.getLatitude() + "] long is [" + mCurrentLocation.getLongitude()+"]");
        myPos = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
	}

	@Override
	public void onMapClick(LatLng latLng) {
		// TODO Auto-generated method stub
		if (evtMarker != null) {
			evtMarker.remove();
		}
		evtMarker = map.addMarker(new MarkerOptions()
						.position(latLng)
						.draggable(true)
						.visible(true)
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
		);
		evtPos = latLng;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		Log.d("DEBUG", "Marker clicked at lat["+marker.getPosition().latitude+
				"], long["+marker.getPosition().longitude+"]");
		myPos = marker.getPosition();
		return true;
	}

	@Override
	public void onMarkerDrag(Marker marker) {
    	//myPosMarker.setTitle("Drop Me");
    	evtMarker.showInfoWindow();
		
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		evtPos = marker.getPosition();

		chosenByDrag = true;
		evtMarker.setTitle("Test");
    	evtMarker.showInfoWindow();
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		chosenByDrag = false;
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

}

package com.geoassist;
import java.io.File;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationService;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.gdb.GdbFeatureTable;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.geoassist.R;

public class GisMap extends Activity {
	MapView mapView;

	private GdbFeatureTable geodatabaseFeatureTable;
	private FeatureLayer 	localFeatureLayer;
	public MapView getMapView() {
		return mapView;
	}

	private final String TOPO_URL = "http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer";
	Point point = null;
	private String lacalMapFileName = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gis_map);
		readLocalMap ();
		// Retrieve the map and initial extent from XML layout
		mapView = (MapView)findViewById(R.id.map);
//		mapView.addLayer(localFeatureLayer);
		mapView.addLayer(new ArcGISLocalTiledLayer(lacalMapFileName), 0);
			
		// Add dynamic layer to MapView
		mapView.addLayer(new ArcGISTiledMapServiceLayer("" +
		"http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
		mapView.addLayer(new ArcGISTiledMapServiceLayer("" +TOPO_URL));
	    mapView.enableWrapAround(true);
	    
		mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
			public void onStatusChanged(Object source, STATUS status) {
				if (source == mapView && status == STATUS.INITIALIZED) {
					LocationService ls = mapView.getLocationService();
					ls.setAutoPan(false);
					ls.start();
					point = ls.getPoint();
					
					
					Log.e("## Got Point", String.valueOf(point));
					Log.e("## Got Location", String.valueOf(ls.getLocation()));
					if ((mapView != null) && (point != null)) {
						  double locy = 37.774719;
					      double locx = -122.473470;
					      Point wgspoint = new Point(locx, locy);
					      Point mapPoint = (Point) GeometryEngine.project(wgspoint,  
					    		  SpatialReference.create(4326),
					    		  mapView.getSpatialReference());

					      {
					    	  SimpleMarkerSymbol  pms = new SimpleMarkerSymbol(Color.RED, 20, SimpleMarkerSymbol.STYLE.DIAMOND);
					    	  if (pms != null) {
					    		  pms.setAngle(0f);
					    		  pms.setOffsetX(0f);
					    		  pms.setOffsetY(12f);
					    		  GraphicsLayer graphicsLayer = new GraphicsLayer();
					    		  mapView.addLayer(graphicsLayer);
					    		  graphicsLayer.addGraphic(new Graphic(mapPoint, pms));
					    	  }
						}		
						mapView.zoomTo(mapPoint, 10);
					}
				}
			}
		});
		
		/////////////////
		/////////////////
		if (point != null) {
			Log.e("## 222 Got Point", String.valueOf(point));
			mapView.zoomTo(point, 10);
		}
	}
	public void readLocalMap () {
		
		File rootsd = Environment.getExternalStorageDirectory();
		String mapFile = rootsd.getAbsolutePath() + "/DCIM/SanFrancisco.tpk";
		lacalMapFileName = Environment.getExternalStorageDirectory().getPath() 
										+ "/DCIM/SanFrancisco.tpk";
//		Geodatabase geodatabase = new Geodatabase(mapFile);
//
//		//get the geodatabase feature table
//		geodatabaseFeatureTable = geodatabase.getGdbFeatureTableByLayerId(0);
//
//		//create a feature layer
//		localFeatureLayer = new FeatureLayer(geodatabaseFeatureTable);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gis_map, menu);
		return true;
	}

	protected void onPause() {
		super.onPause();
		mapView.pause();
	}

	protected void onResume() {
		super.onResume();
		mapView.unpause();
	}
}


package com.geoassist;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;


public class StrikeDip  implements SensorEventListener {
	private SensorManager mSensorManager;
	Activity  act;
	StrikeDipFragment frg;
	private Float azimut; 
	float[] mGravity;
	float[] mGeomagnetic;
	float declanation = 0;
	Sensor accelerometer = null;
	Sensor magnetometer = null;
	compassView cmpsView = null; 
	EditText strikeTxt = null;
	EditText dipTxt = null;
	EditText allInfoTxt = null;
	private static long   sensorCount = 0;
	private static final int COMPASS_CORRECTION = 300;
	    
	public StrikeDip (Activity act, StrikeDipFragment frg) {
		this.act = act;
		this.frg = frg;
	}

	public void start() {
		mSensorManager = (SensorManager) this.act.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
			accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		}
		else {
			Toast.makeText(this.act, "Accelero Meter is not available", Toast.LENGTH_LONG).show();
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
			magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		    mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
		}
		else {
			Toast.makeText(this.act, "Magnetic Sensor is not available", Toast.LENGTH_LONG).show();
		}
	}
	
	public void stop(){
		mSensorManager.unregisterListener(this);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		String dispStr = "";
		switch (accuracy) {
		case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
			dispStr = "High";
			break;
		case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
			dispStr = "Low";
			break;
		case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
			dispStr = "Medium";
			break;
		case SensorManager.SENSOR_STATUS_UNRELIABLE:	
			dispStr = "Unreliable";
			break;
		}
		Toast.makeText(this.act, "Sensor Acuuracy is" + dispStr, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
//		Log.e("### Sensor Changed", "Called");
		if (sensorCount == 50) sensorCount = 0;
		else {
			if (sensorCount != 0)
			{
				sensorCount = sensorCount+1;
				return;
			}
		}
		int mGeomagneticLen = 0;
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
	        mGravity = event.values;
	    if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
	        mGeomagnetic = event.values;
	        mGeomagneticLen = event.values.length;
	    }
	    if (mGravity != null && mGeomagnetic != null) {
	        float R[] = new float[9];
	        float I[] = new float[9];
	        boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
	        if (success) {
	          float orientation[] = new float[3];
	          SensorManager.getOrientation(R, orientation);
	          azimut = orientation[0]; // orientation contains: azimut, pitch and roll
	          double xd = 0; 
	          double yd = 0; 
	          double zd = 0;
	          zd = Math.toDegrees(orientation[0]);
	          zd  = zd - this.declanation -11;
	          xd = Math.toDegrees(orientation[1]);
	          yd = Math.toDegrees(orientation[2]);
	          yd = (double) Math.round(yd * 100) / 100;
	          zd = (double) Math.round(zd * 100) / 100;
	          xd = (double) Math.round(xd * 100) / 100;
	          
	          String direction = "";
	          if ( (0 < yd) && (yd <90) ){
	        	  direction = "South";
	          }
	          else if ((90 < yd) && (yd <180) ){
	        	  direction = "East";	        	  
	          }
	          else if ((-180 < yd) && (yd < -90) ){
	        	  direction = "North";	        	  
	          }
	          else {
	        	  direction = "West";	        	  
	          }
	          this.frg.updateReadings(String.valueOf(Math.abs(zd)), 
	        		  				  String.valueOf(Math.abs(xd)) + "  " + direction);
	        }
	    }
	}
}
package com.geoassist.data;

import java.util.ArrayList;

import android.util.Log;

public class Site {
	public double lat;
	public double lng;
	public ArrayList<Mineral>  minerals;
	public 	int 			rockType;
	public  int             rockName;
	public 	StructureType	structType;
	public Site() {
		this.minerals = new ArrayList<Mineral>();
		Log.e("Minerals Constructor", String.valueOf(this.minerals));
	}
}

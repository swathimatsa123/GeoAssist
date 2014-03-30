package com.geoassist.data;

import java.io.Serializable;

public class Mineral implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String 	mineralName;
	public float 	minGrainSize;
	public float	maxGrainSize;
	public float	composition;
	public String   mineralCleavege;
	public  int     grainForm;
	public  int     grainShape;
}

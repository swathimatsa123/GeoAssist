package com.geoassist.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  String 			scientistName;
	public  String 			name;
	public  String			location;
	public  String []		notes;
	public 	int 		rockType;
	public 	StructureType	structType;
	public ArrayList<Mineral>  minerals;
}

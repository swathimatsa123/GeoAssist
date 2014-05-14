package com.geoassist.data;

import java.util.ArrayList;

public class WorkingProject {
    private static WorkingProject   _instance;
	public  String 			scientistName;
	public  String 			name;
	public  String			location;
	public  String			mapFile;
	public  String []		notes;
//	public 	String          rockType;
//	public  String          rockName;
//	public  String          rockUnit;
	public 	StructureType	structType;
	public ArrayList<Site>  sites;
	
    private WorkingProject(){
    	sites = new ArrayList<Site>();
    }

    public static WorkingProject getInstance(){
        if (_instance == null){
            _instance = new WorkingProject();
        }
        return _instance;
    }
}
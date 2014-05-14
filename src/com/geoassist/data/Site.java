package com.geoassist.data;

import java.util.ArrayList;

public class Site {
	public double lat;
	public double lng;
	public ArrayList<Mineral>  minerals;
	public ArrayList<Contact>  contacts;
	public ArrayList<MetDtls>  metDtls;
	
	public 	String 			rockType;
	public 	String 			rockId;
	public  String          rockUnit;
	public  String          genStrike;
	public  String          genDip;
	public  String          dikeThickNess;
	public  String          minGrainSize;
	public  String          maxGrainSize;
	public  String          dikeDescription;
	public  String          rockInfoNotes;

	public  String          sedGrainDia;
	public  String          sedGrainPhi;
	public  String          sedGrainName;
	public  String          sedGrainMinerals;
	public  String          sedGrainLithics;
	public  String          grainSorting;
	public  String          grainRounding;
	public  String          sedBedding;
	public  String          sedCrossBedding;
	public  String          sedRipples;
	public  String          sedSoleMarks;
	public  String          sedSSD;	
	public  String          sedOtherStr;
	public  String          sedFossils;
	public  String          sedDepEnv;
	
	public  String          metStructure;
	public 	StructureType	structType;
	
	public String 			foldType;
	public String 			foldTighness;
	public String 			foldHinge;
	public String 			foldLimbs;
	public String 			foldLambda;
	public String 			foldAmplitude;
	public String           fold2nOrder;
	public String 			foldSymmetry;
	public String           foldRamsaysCls;
	public String           foldLambdaUnits;
	public String           foldAmplitudeUnits;
	
	public String           faultMovement;
	public String           faultTrend;
	public String           faultZoneWidth;
	public String           faultSeparation;
	public String           faultOffset;
	public String           faultPiercingPt;
	public String           faultNetSlip;
	public String           faultMineralization;
	
	public String           jointStrike;
	public String           jointDip;
	public String           jointSpacing;
	public String           jointBedding;
	public String           jointFoldType;
	
	public String           veinStrike;
	public String           veinDip;
	public String           veinZoneWidth;
	public String           veinComposition;
	public Site() {
		this.minerals = new ArrayList<Mineral>();
		this.contacts = new ArrayList<Contact>();
		this.metDtls  = new ArrayList<MetDtls>();
	}
}

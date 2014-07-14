package com.geoassist.data;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GeoDb extends SQLiteOpenHelper {
    private static GeoDb   _instance;
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "geoAssistDb";
	private static final String PROJECTS_TABLE = "projects";
	private static final String PROJECTS_TABLE_KEY = "name";
	private static final String PROJECTS_LOCATION = "location";
	private static final String PROJECTS_SITE_COUNT= "siteCount";


	private static final String SITES_TABLE = "sites";
	private static final String SITES_TABLE_KEY = "location";
	private static final String SITE_PROJECT = "projectName";
	private static final String ROCKTYPE =  "rockType";
	private static final String ROCKID =  "rockId";
	private static final String ROCKUNIT =  "rockUnit";
	private static final String GENSTRIKE =  "genStrike";
	private static final String GENDIP = "genDip";
	private static final String DIKETHICKNESS =  "dikeThickNess";
	private static final String MINGRAINSIZE =  "minGrainSize";
	private static final String  MAXGRAINSIZE =  "maxGrainSize";
	private static final String  DIKEDESCRIPTION =  "dikeDescription";
	private static final String  ROCKINFONOTES =  "rockInfoNotes";

	private static final String  SEDGRAINDIA =  "sedGrainDia";
	private static final String  SEDGRAINPHI =  "sedGrainPhi";
	private static final String  SEDGRAINNAME =  "sedGrainName";
	private static final String  SEDGRAINMINERALS =  "sedGrainMinerals";
	private static final String  SEDGRAINLITHICS =  "sedGrainLithics";
	private static final String  GRAINSORTING =  "grainSorting";
	private static final String  GRAINROUNDING =  "grainRounding";
	private static final String  SEDBEDDING =  "sedBedding";
	private static final String  SEDCROSSBEDDING =  "sedCrossBedding";
	private static final String  SEDRIPPLES =  "sedRipples";
	private static final String  SEDSOLEMARKS =  "sedSoleMarks";
	private static final String  SEDSSD =  "sedSSD";	
	private static final String  SEDOTHERSTR =  "sedOtherStr";
	private static final String  SEDFOSSILS =  "sedFossils";
	private static final String  SEDDEPENV =  "sedDepEnv";
	private static final String  FOLDTYPE = 	"foldType";
	private static final String  FOLDTIGHNESS = 	"foldTighness";
	private static final String  FOLDHINGE = 	"foldHinge";
	private static final String  FOLDLIMBS = 	"foldLimbs";
	private static final String  FOLDLAMBDA = 	"foldLambda";
	private static final String  FOLDAMPLITUDE = 	"foldAmplitude";
	private static final String  FOLD2NORDER =  "fold2nOrder";
	private static final String  FOLDSYMMETRY = 	"foldSymmetry";
	private static final String  FOLDRAMSAYSCLS =  "foldRamsaysCls";
	private static final String  FOLDLAMBDAUNITS =  "foldLambdaUnits";
	private static final String  FOLDAMPLITUDEUNITS =  "foldAmplitudeUnits";

	private static final String CREATE_PROJECTS_TABLE = "CREATE TABLE "
			+ " " + PROJECTS_TABLE + " (" + PROJECTS_TABLE_KEY + " TEXT PRIMARY KEY NOT NULL, " + PROJECTS_LOCATION
			+ " TEXT," + PROJECTS_SITE_COUNT + " INTEGER" + ")";

	private static final String CREATE_SITES_TABLE = "CREATE TABLE "
			+ SITES_TABLE + " (" + SITES_TABLE_KEY + " TEXT PRIMARY KEY NOT NULL, " + 
			SITE_PROJECT + " TEXT, " +
			ROCKTYPE + " TEXT, " +
			ROCKID + " TEXT, " +
			ROCKUNIT + " TEXT, " +
			GENSTRIKE + " TEXT, " +
			GENDIP + " TEXT, " +
			DIKETHICKNESS + " TEXT, " +
			MINGRAINSIZE + " TEXT, " +
			MAXGRAINSIZE + " TEXT, " +
			DIKEDESCRIPTION + " TEXT, " +
			ROCKINFONOTES + " TEXT, " +
			SEDGRAINDIA + " TEXT, " +
			SEDGRAINPHI + " TEXT, " +
			SEDGRAINNAME + " TEXT, " +
			SEDGRAINMINERALS + " TEXT, " +
			SEDGRAINLITHICS + " TEXT, " +
			GRAINSORTING + " TEXT, " +
			GRAINROUNDING + " TEXT, " +
			SEDBEDDING + " TEXT, " +
			SEDCROSSBEDDING + " TEXT, " +
			SEDRIPPLES + " TEXT, " +
			SEDSOLEMARKS + " TEXT, " +
			SEDSSD + " TEXT, " +
			SEDOTHERSTR + " TEXT, " +
			SEDFOSSILS + " TEXT, " +
			SEDDEPENV + " TEXT, " +
			FOLDTYPE + " TEXT, " +
			FOLDTIGHNESS + " TEXT, " +
			FOLDHINGE + " TEXT, " +
			FOLDLIMBS + " TEXT, " +
			FOLDLAMBDA + " TEXT, " +
			FOLDAMPLITUDE + " TEXT, " +
			FOLD2NORDER + " TEXT, " +
			FOLDSYMMETRY + " TEXT, " +
			FOLDRAMSAYSCLS + " TEXT, " +
			FOLDLAMBDAUNITS + " TEXT, " +
			FOLDAMPLITUDEUNITS + " TEXT " +
			")";

	public GeoDb (Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

    public static GeoDb getInstance(Context context){
        if (_instance == null){
            _instance = new GeoDb(context);
        }
        return _instance;
    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e("##### onCreate", CREATE_PROJECTS_TABLE);
		db.execSQL(CREATE_PROJECTS_TABLE);
		Log.e("##### onCreate", CREATE_SITES_TABLE);
		db.execSQL(CREATE_SITES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + PROJECTS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SITES_TABLE);
		onCreate(db);
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}
	public void saveWorkingProject(WorkingProject proj) {
		Log.e("Saving Project", String.valueOf(proj.name));
		this.addProject(proj);
	    for (int i= 0; i< proj.sites.size(); i++) {
	    	Log.e("Adding Site", String.valueOf(i));
	    	addSite(proj.name, proj.sites.get(i));
	    }
	    return;
	}
	

	public void addProject(WorkingProject proj) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PROJECTS_TABLE_KEY, proj.name); 
		values.put(PROJECTS_LOCATION, proj.location); 
		values.put(PROJECTS_SITE_COUNT, proj.sites.size()); 
		db.insert(PROJECTS_TABLE, null, values);
	    db.close(); 
	}
	
	public void addProject(String projName) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PROJECTS_TABLE_KEY, projName); 
		values.put(PROJECTS_LOCATION, "SantaRosa"); 
		values.put(PROJECTS_SITE_COUNT, 10); 
		db.insert(PROJECTS_TABLE, null, values);
	    db.close(); 
	}
	
	public void deleteProject(String projName) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(PROJECTS_TABLE, PROJECTS_TABLE_KEY+ " = ?",
	            new String[] { String.valueOf(projName) });
	    db.close();
	}
	
	public List <String>  getAllProjects() {
		List <String> projects = new ArrayList<String>() ;
		String selectQuery = "SELECT  * FROM " + PROJECTS_TABLE;
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	    	do {
	    		Log.e("Entry  " , String.valueOf(cursor.getString(0)));
	    		projects.add (cursor.getString(0));
	        } while (cursor.moveToNext());
	    }
	    return projects;
	}
	public void addSite(String projName, String siteLocation) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SITES_TABLE_KEY, siteLocation); 
		values.put(SITE_PROJECT, projName); 
		db.insert(SITES_TABLE, null, values);
	    db.close(); 
	}
	public void addSite(String projName, Site site) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SITES_TABLE_KEY, "Lat:"+ String.valueOf(site.lat)+ ":Lng:" + String.valueOf(site.lng)); 
		values.put(SITE_PROJECT, projName); 
		values.put( ROCKTYPE ,  site.rockType);
		values.put( ROCKID ,  site.rockId);
		values.put( ROCKUNIT ,  site.rockUnit);
		values.put( GENSTRIKE ,  site.genStrike);
		values.put( GENDIP , site.genDip);
		values.put( DIKETHICKNESS ,  site.dikeThickNess);
		values.put( MINGRAINSIZE ,  site.minGrainSize);
		values.put( MAXGRAINSIZE ,  site.maxGrainSize);
		values.put( DIKEDESCRIPTION ,  site.dikeDescription);
		values.put( ROCKINFONOTES ,  site.rockInfoNotes);

		values.put( SEDGRAINDIA ,  site.sedGrainDia);
		values.put( SEDGRAINPHI ,  site.sedGrainPhi);
		values.put( SEDGRAINNAME ,  site.sedGrainName);
		values.put( SEDGRAINMINERALS ,  site.sedGrainMinerals);
		values.put( SEDGRAINLITHICS ,  site.sedGrainLithics);
		values.put( GRAINSORTING ,  site.grainSorting);
		values.put( GRAINROUNDING ,  site.grainRounding);
		values.put( SEDBEDDING ,  site.sedBedding);
		values.put( SEDCROSSBEDDING ,  site.sedCrossBedding);
		values.put( SEDRIPPLES ,  site.sedRipples);
		values.put( SEDSOLEMARKS ,  site.sedSoleMarks);
		values.put( SEDSSD ,  site.sedSSD);	
		values.put( SEDOTHERSTR ,  site.sedOtherStr);
		values.put( SEDFOSSILS ,  site.sedFossils);
		values.put( SEDDEPENV ,  site.sedDepEnv);
		values.put( FOLDTYPE , 	site.foldType);
		values.put( FOLDTIGHNESS , 	site.foldTighness);
		values.put( FOLDHINGE , 	site.foldHinge);
		values.put( FOLDLIMBS , 	site.foldLimbs);
		values.put( FOLDLAMBDA , 	site.foldLambda);
		values.put( FOLDAMPLITUDE , 	site.foldAmplitude);
		values.put( FOLD2NORDER ,  site.fold2nOrder);
		values.put( FOLDSYMMETRY , 	site.foldSymmetry);
		values.put( FOLDRAMSAYSCLS ,  site.foldRamsaysCls);
		values.put( FOLDLAMBDAUNITS ,  site.foldLambdaUnits);
		values.put( FOLDAMPLITUDEUNITS ,  site.foldAmplitudeUnits);
		
		db.insert(SITES_TABLE, null, values);
	    db.close(); 
	}
	
	public void getSitesForProject(String projName) {
	    String selectQuery = "SELECT  * FROM " + SITES_TABLE + " WHERE "+ SITE_PROJECT + "=" + "'"+projName+"'";
	    Log.e("### SQL SELECT", selectQuery);
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	    	do {
	    		Log.e("Entry  " , String.valueOf(cursor.getString(0)));
	        } while (cursor.moveToNext());
	    }
	}
	
	public void constructProjectFromDb (String projName) {
	    String selectQuery = "SELECT  * FROM " + SITES_TABLE + " WHERE "+ SITE_PROJECT + "=" + "'"+projName+"'";
	    WorkingProject proj = WorkingProject.getInstance();
	    Log.e("### SQL SELECT", selectQuery);
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	    	do {
	    		Site site = new Site();
	    		site.rockType = cursor.getString( cursor.getColumnIndex(ROCKTYPE));
	    		site.rockId   = cursor.getString( cursor.getColumnIndex(ROCKID));
	    		site.rockUnit = cursor.getString( cursor.getColumnIndex(ROCKUNIT));
	    		site.genStrike= cursor.getString( cursor.getColumnIndex(GENSTRIKE));
	    		site.genDip = cursor.getString( cursor.getColumnIndex(GENDIP));
	    		site.dikeThickNess = cursor.getString( cursor.getColumnIndex(DIKETHICKNESS));
	    		site.minGrainSize = cursor.getString( cursor.getColumnIndex(MINGRAINSIZE));
	    		site.maxGrainSize = cursor.getString( cursor.getColumnIndex(MAXGRAINSIZE));
	    		site.dikeDescription = cursor.getString( cursor.getColumnIndex(DIKEDESCRIPTION));
	    		site.rockInfoNotes = cursor.getString( cursor.getColumnIndex(ROCKINFONOTES));
	    		site.sedGrainDia = cursor.getString( cursor.getColumnIndex(SEDGRAINDIA));
	    		site.sedGrainPhi = cursor.getString( cursor.getColumnIndex(SEDGRAINPHI));
	    		site.sedGrainName = cursor.getString( cursor.getColumnIndex(SEDGRAINNAME));
	    		site.sedGrainMinerals = cursor.getString( cursor.getColumnIndex(SEDGRAINMINERALS));
	    		site.sedGrainLithics = cursor.getString( cursor.getColumnIndex(SEDGRAINLITHICS));
	    		site.grainSorting = cursor.getString( cursor.getColumnIndex(GRAINSORTING));
	    		site.grainRounding = cursor.getString( cursor.getColumnIndex(GRAINROUNDING));
	    		site.sedBedding = cursor.getString( cursor.getColumnIndex(SEDBEDDING));
	    		site.sedCrossBedding = cursor.getString( cursor.getColumnIndex(SEDCROSSBEDDING));
	    		site.sedRipples = cursor.getString( cursor.getColumnIndex(SEDRIPPLES));
	    		site.sedSoleMarks = cursor.getString( cursor.getColumnIndex(SEDSOLEMARKS));
	    		site.sedSSD = cursor.getString( cursor.getColumnIndex(SEDSSD));
	    		site.sedOtherStr = cursor.getString( cursor.getColumnIndex(SEDOTHERSTR));
	    		site.sedFossils = cursor.getString( cursor.getColumnIndex(SEDFOSSILS));
	    		site.sedDepEnv = cursor.getString( cursor.getColumnIndex(SEDDEPENV));
	    		site.foldType  = cursor.getString( cursor.getColumnIndex(FOLDTYPE));
	    		site.foldTighness = cursor.getString( cursor.getColumnIndex(FOLDTIGHNESS));
	    		site.foldHinge = cursor.getString( cursor.getColumnIndex(FOLDHINGE));
	    		site.foldLimbs = cursor.getString( cursor.getColumnIndex(FOLDLIMBS));
	    		site.foldLambda = cursor.getString( cursor.getColumnIndex(FOLDLAMBDA));
	    		site.foldAmplitudeUnits = cursor.getString( cursor.getColumnIndex(FOLDAMPLITUDEUNITS));
	    		site.fold2nOrder = cursor.getString( cursor.getColumnIndex(FOLD2NORDER));
	    		site.foldSymmetry = cursor.getString( cursor.getColumnIndex(FOLDSYMMETRY));
	    		site.foldRamsaysCls = cursor.getString( cursor.getColumnIndex(FOLDRAMSAYSCLS));
	    		site.foldLambdaUnits = cursor.getString( cursor.getColumnIndex(FOLDLAMBDAUNITS));
	    		site.foldAmplitudeUnits = cursor.getString( cursor.getColumnIndex(FOLDAMPLITUDEUNITS));
	    		String  locKey = cursor.getString( cursor.getColumnIndex(SITES_TABLE_KEY));
	    		if (true == locKey.contains(":")) {
	    			String [] parts = locKey.split(":");
	    			site.lat = Double.parseDouble(parts[1]);
	    			site.lng = Double.parseDouble(parts[3]);
	    		}
	    		proj.sites.add(site);
	        } while (cursor.moveToNext());
	    }		
	}
	
}

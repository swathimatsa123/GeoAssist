package com.geoassist;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;


//@SuppressWarnings("unchecked")
public class MainExpdList  extends BaseExpandableListAdapter implements OnItemSelectedListener, OnClickListener{

	public ArrayList<String> groupItem, tempChild;
	public ArrayList<Object> Childtem = new ArrayList<Object>();
	ArrayList<String> mapFiles = new ArrayList<String>();
	ArrayList<String> mapsWithPath = new ArrayList<String>();
	WorkingProject proj = WorkingProject.getInstance();
	public enum Order{
		 LOCATION (0),
		 ROCK (1),
		 MINERALS (2),
		 CONTACT (3),
		 FOLD (4),
		 FAULT (5),
		 JOINT (6),
		 VEIN (7),
		 SAMPLE (8),
		 PICTURE (9),
		 NOTES (10),
		 TRACK (11);
		 public int order;
		 private Order(int o) {
			 order=o;
		 }
		 public int getOrder() {
			 return order;
		 }
	}
	public LayoutInflater minflater;
	public Activity activity;
	EditText projLatEt;
	EditText projLngEt;
	EditText siteNumEt;
	double  lat = 0;
	double  lng = 0;
	final String[] rockTypes = { "Select", "Igneous", "Foliation/Lineation" ,"Sedimentary" };
	final String[] contactTypes = { "Select", "Sharp", "Gradational", "Interbedding",
									"Transition", "Undulating", "Non-conformable"};
	
	final String[] boundaryTypes = { "Select", "Stringer","Float","Fault" ,"Unconformity","Scour",
									 "Dike", "Alluvium", "Lense"};

	final String[] faultMovements = { "Select", "Normal","Reverse", "Dextral (R-lat)",	"Sinistral (L-lat)"};
	final String[] units = {"Select", "mm", "cm", "m", "ft", "inch"};
	final String[] beddingTypes  = {"Select", "Dip Joints", "Strike Joints","Oblique Joints"};
	final String[] foldTypes   =   {"Select", "Longitudinal Joints(a-b)","Crossjoints/Transverse (a-c)",
									"Diagonal Joint","Radial Joint"};
	final String[] compositionTypes   =   {"Select", "Quartz","Calcite","Chlorite","Other"};
	private Spinner rockTypeSpn;
	
	public MainExpdList(ArrayList<String> grList, ArrayList<Object> childItem) {
		groupItem = grList;
		this.Childtem = childItem;
	}

	public void setInflater(LayoutInflater mInflater, Activity act) {
		this.minflater = mInflater;
		this.activity = act;
	}
	public void setLocation(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ImageButton doneBtn;
		int viewSrc = 0;
		Log.e("GetChildView in Main", "Called");
//		StackTraceElement[]  trace = Thread.currentThread().getStackTrace();
//		for (int i=0;i< trace.length;i++){
//			Log.e("Trace "+String.valueOf(i), trace[i].toString());
//		}
//		
		boolean donotExpand = false;
		switch (groupPosition) {
		case 0:
			viewSrc = R.layout.site_info;
			convertView = minflater.inflate(viewSrc, null);
			doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
			projLatEt = (EditText) convertView.findViewById(R.id.siteLatEt);
			projLatEt.setText(String.valueOf(this.lat));
			projLngEt = (EditText) convertView.findViewById(R.id.siteLngEt);
			projLngEt.setText(String.valueOf(this.lng));
			siteNumEt = (EditText) convertView.findViewById(R.id.siteNumEt);
			siteNumEt.setText(String.valueOf(proj.sites.size()));
			Log.e("Done Btn", String.valueOf(doneBtn));
			if (doneBtn!= null) { 
			doneBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AddSiteActivity parent = (AddSiteActivity)MainExpdList.this.activity;
					parent.collectDetails(groupPosition);
//					TextView textView = (TextView) v.findViewById(R.id.groupId);
//					textView.setTextColor(0xFF00CC00);
				}
			});
			}
			break;
		case 1:
			viewSrc = R.layout.rock_unit;
			convertView = minflater.inflate(viewSrc, null);
			doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
			if (doneBtn!= null) { 
				doneBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AddSiteActivity parent = (AddSiteActivity)MainExpdList.this.activity;
					parent.collectDetails(groupPosition);
//					TextView textView = (TextView) v.findViewById(R.id.groupId);
//					textView.setTextColor(0xFF00CC00);
				}
			});
			}
			break;
		case 2:
			convertView = setupRockInfoView (groupPosition);
			break;

		case 3:
			viewSrc = R.layout.info_detail;
			convertView = minflater.inflate(viewSrc, null);
			((AddSiteActivity) this.activity).invokeMineralListView();
			donotExpand = true;
			break;
			
		case 4:
			convertView = setupContactInfoView(groupPosition);
			break;

//		case 4:
//			convertView = setupFoldInfoView(groupPosition);
//			break;

		case 6:
			convertView = setupFaultInfoView(groupPosition);
			break;

		case 7:
			convertView = setupJointInfoView(groupPosition);
			break;

		case 8:
			convertView = setupVeinInfoView(groupPosition);
			break;
												
		default:
			viewSrc = R.layout.info_detail;
			convertView = minflater.inflate(viewSrc, null);
			break;
		}
		if (donotExpand == false) {
			convertView.setBackgroundColor(0xFFF0FFF0);
		}
		return convertView;
	}

	private Object Order(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	public  View  setupRockInfoView (final int groupPosition) {
		View convertView = minflater.inflate(R.layout.rock_info, null);
		ImageButton dtlsBtn = (ImageButton) convertView.findViewById(R.id.addDtls);
		ArrayAdapter<String> rockAdapter = new ArrayAdapter<String>(this.activity, 
												android.R.layout.simple_spinner_dropdown_item,rockTypes);
		rockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rockTypeSpn  = (Spinner)  convertView.findViewById(R.id.rockTypeSpinner);
		rockTypeSpn.setAdapter(rockAdapter);
		rockTypeSpn.setOnItemSelectedListener(this);
		dtlsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int selection = MainExpdList.this.rockTypeSpn.getSelectedItemPosition();
				Site site = proj.sites.get(proj.sites.size()-1);
				site.rockType = rockTypes[selection];
				AddSiteActivity parent = (AddSiteActivity)MainExpdList.this.activity;
				parent.startRockInfoActivity();
			}
		});

		ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddSiteActivity parent = (AddSiteActivity)MainExpdList.this.activity;
				parent.collectDetails(groupPosition);
//				TextView textView = (TextView) v.findViewById(R.id.groupId);
//				textView.setTextColor(0xFF00CC00);
			}
		});
		return convertView;
	}

	public  View  setupContactInfoView (final int groupPosition) {
		View convertView = minflater.inflate(R.layout.contact_add, null);
		ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddSiteActivity parent = (AddSiteActivity)MainExpdList.this.activity;
				parent.collectDetails(groupPosition);
//				TextView textView = (TextView) v.findViewById(R.id.groupId);
//				textView.setTextColor(0xFF00CC00);
			}
		});
		ArrayAdapter<String> contactAdapter = new ArrayAdapter<String>(this.activity, 
				android.R.layout.simple_dropdown_item_1line,contactTypes);
		Spinner contactTypeSpn = (Spinner)  convertView.findViewById(R.id.contactTypeSpn);
		contactTypeSpn.setAdapter(contactAdapter);
		setSpinner(R.id.boundarySpn, convertView, boundaryTypes);
		return convertView;
	}
	
	public void setSpinner (int id, View v, final String[] types) {
		Spinner spn = (Spinner)  v.findViewById(id);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.activity, 
									android.R.layout.simple_dropdown_item_1line,
									types);

		spn.setAdapter(adapter);			
	}

	public  View  setupFoldInfoView (final int groupPosition) {
		View convertView = minflater.inflate(R.layout.fold_second_view, null);
		ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddSiteActivity parent = (AddSiteActivity)MainExpdList.this.activity;
				parent.collectDetails(groupPosition);
//				TextView textView = (TextView) v.findViewById(R.id.groupId);
//				textView.setTextColor(0xFF00CC00);
			}
		});
		return convertView;
	}

	public  View  setupFaultInfoView (final int groupPosition) {
		View convertView = minflater.inflate(R.layout.fault_view, null);
		ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddSiteActivity parent = (AddSiteActivity)MainExpdList.this.activity;
				parent.collectDetails(groupPosition);
//				TextView textView = (TextView) v.findViewById(R.id.groupId);
//				textView.setTextColor(0xFF00CC00);
			}
		});
		
		setSpinner(R.id.offsetUnitsSpn, convertView, units);
		setSpinner(R.id.movementSpn, convertView, faultMovements);
		setSpinner(R.id.netSlipUnits,convertView, units);
		return convertView;
	}

	public  View  setupJointInfoView (final int groupPosition) {
		View convertView = minflater.inflate(R.layout.joint_view, null);
		ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddSiteActivity parent = (AddSiteActivity)MainExpdList.this.activity;
				parent.collectDetails(groupPosition);
//				TextView textView = (TextView) v.findViewById(R.id.groupId);
//				textView.setTextColor(0xFF00CC00);
			}
		});
		setSpinner(R.id.beddingSpn, convertView, beddingTypes);
		setSpinner(R.id.foldTypeSpn, convertView, foldTypes);
		return convertView;
	}

	public  View  setupVeinInfoView (final int groupPosition) {
		View convertView = minflater.inflate(R.layout.vein_info, null);
		ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddSiteActivity parent = (AddSiteActivity)MainExpdList.this.activity;
				parent.collectDetails(groupPosition);
//				TextView textView = (TextView) v.findViewById(R.id.groupId);
//				textView.setTextColor(0xFF00CC00);
			}
		});
		setSpinner(R.id.compositionSpn, convertView, compositionTypes);
		setSpinner(R.id.units, convertView, units);
		return convertView;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<String>) Childtem.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return groupItem.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.info_group, null);
		}
		((CheckedTextView) convertView).setText(groupItem.get(groupPosition));
		((CheckedTextView) convertView).setChecked(isExpanded);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		if (parent.getId() == R.id.rockTypeSpinner) {
			Site site = proj.sites.get(proj.sites.size()-1);
			if (pos != 0 ) {
				Log.e("Assigning RockType",rockTypes[pos]);
				site.rockType = rockTypes[pos];
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
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
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onClick(View v) {
		Log.e("ONClick" , String.valueOf(v.getClass()));
		if (v.getId() == R.id.doneBtn) {
		}
	}

}

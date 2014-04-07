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

import com.geoassist.data.RockType;
import com.geoassist.data.WorkingProject;


//@SuppressWarnings("unchecked")
public class ExpListAdapter  extends BaseExpandableListAdapter implements OnItemSelectedListener{

	public ArrayList<String> groupItem, tempChild;
	public ArrayList<Object> Childtem = new ArrayList<Object>();
	ArrayList<String> mapFiles = new ArrayList<String>();
	ArrayList<String> mapsWithPath = new ArrayList<String>();
	
	public LayoutInflater minflater;
	public Activity activity;
	EditText projNameEt;
	EditText projLcnEt;
	final String[] rockTypes = { "Select", "Igneous", "Metamorphic" ,"Sedimentary" };
    private Spinner rockTypeSpn;
	
	public ExpListAdapter(ArrayList<String> grList, ArrayList<Object> childItem) {
		groupItem = grList;
		this.Childtem = childItem;
	}

	public void setInflater(LayoutInflater mInflater, Activity act) {
		this.minflater = mInflater;
		this.activity = act;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		Log.e("Get ChildView", "Group :" + String.valueOf(groupPosition) + 
							   "Child :"  + String.valueOf(childPosition)
				);
		tempChild = (ArrayList<String>) Childtem.get(groupPosition);
	
		TextView text = null;
//		ssif (convertView == null) {
		if (true) {
			int viewSrc = 0;
			switch (groupPosition) {
			case 0:
				viewSrc = R.layout.project_info;
				convertView = minflater.inflate(viewSrc, null);
				ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
				projNameEt = (EditText) convertView.findViewById(R.id.projNameEt);
				projLcnEt = (EditText) convertView.findViewById(R.id.lcnEt);
				doneBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.e("Done", "ProjBtn " + String.valueOf(ExpListAdapter.this.projNameEt));
						Log.e("Done", "ProjName " + ExpListAdapter.this.projNameEt.getText().toString());
						UserDetails parent = (UserDetails)ExpListAdapter.this.activity;
						parent.collectDetails(groupPosition);
					}
				});
            	mapFiles.add("Select Map Source");
				this.walkdir(Environment.getExternalStorageDirectory());
				ArrayAdapter<String> mapAdapter = new ArrayAdapter<String>(this.activity, 
						android.R.layout.simple_dropdown_item_1line, mapFiles);
				Spinner mapFilesSpn  = (Spinner)  convertView.findViewById(R.id.mapFileSpn);
				mapFilesSpn.setAdapter(mapAdapter);
				mapFilesSpn.setOnItemSelectedListener(this);

				Log.e(" ProjNameEt " , String.valueOf(projNameEt));
				break;
			case 1:
				convertView = setupRockInfoView (groupPosition);
				break;
				
			default:
				viewSrc = R.layout.info_detail;
				convertView = minflater.inflate(viewSrc, null);
				break;
			}
		}
		convertView.setBackgroundColor(Color.LTGRAY);
		return convertView;
	}

	public  View  setupRockInfoView (final int groupPosition) {
		View convertView = minflater.inflate(R.layout.rock_info, null);
		ImageButton dtlsBtn = (ImageButton) convertView.findViewById(R.id.addDtls);
		ArrayAdapter<String> rockAdapter = new ArrayAdapter<String>(this.activity, 
												android.R.layout.simple_dropdown_item_1line,rockTypes);
		rockTypeSpn  = (Spinner)  convertView.findViewById(R.id.rockTypeSpinner);
		rockTypeSpn.setAdapter(rockAdapter);
		rockTypeSpn.setOnItemSelectedListener(this);
		dtlsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("DetailsBtn", "Got it");
				WorkingProject proj = WorkingProject.getInstance();
				int selection = ExpListAdapter.this.rockTypeSpn.getSelectedItemPosition();
				Log.e("Seletion " , "Text @" + String.valueOf(selection) + " is" +rockTypes[selection]);
				if (rockTypes[selection].equals("Igneous")) {
					proj.rockType = RockType.IGNEOUS;
				}
				Log.e("RockType "+ String.valueOf(proj.rockType), " Value" + String.valueOf(RockType.IGNEOUS));
				UserDetails parent = (UserDetails)ExpListAdapter.this.activity;
				parent.startRockInfoActivity();
			}
		});

		ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("DONE#### ", "Called### "+String.valueOf(groupPosition));
				UserDetails parent = (UserDetails)ExpListAdapter.this.activity;
				parent.collectDetails(groupPosition);
			}
		});
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
		Log.e("Spinner Select ", "Value is "+ String.valueOf(pos));
		WorkingProject proj = WorkingProject.getInstance();
		proj.rockType = pos;
		if (parent.getId() == R.id.ignRockType) {
			if (rockTypes[pos].equals("Rock Name" )){
				proj.rockType = RockType.IGNEOUS;
				Log.e("Type ", "Rock Name");
			}
			else if (rockTypes[pos].equals("Dike" )){
				Log.e("Type ", "Dike");
				proj.rockType = RockType.SEDIMENTARY;
			}
		}
		else if (parent.getId() == R.id.mapFileSpn) {
			if (pos != 0) {
				proj.mapFile = mapsWithPath.get(pos-1);
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
	            	Log.e("Adding" , listFile[i].getName());
	            	mapFiles.add(listFile[i].getName());
	            	mapsWithPath.add(Environment.getExternalStorageDirectory()+"/" +  dir.getName()+"/" +
	            					listFile[i].getName());
	            	Log.e("Path", Environment.getExternalStorageDirectory()+"/" + 
	            					dir.getName()+"/" +
	            					listFile[i].getName());
	            }
	        }
	    }    
	}

//	@Override
//	public void onClick(View v) {
//		Log.e("ONClick" , String.valueOf(v.getClass()));
//		if (v.getId() == R.id.doneBtn) {
//			Log.e("Done", "ProjBtn " + String.valueOf(ExpListAdapter.this.projNameEt));
//			Log.e("Done", "ProjName " + ExpListAdapter.this.projNameEt.getText().toString());
//			UserDetails parent = (UserDetails)this.activity;
//			parent.collectDetails();
//		}
//	}

}

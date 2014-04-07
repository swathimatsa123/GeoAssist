package com.geoassist;

import java.util.ArrayList;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.geoassist.data.RockNames;
import com.geoassist.data.RockType;
import com.geoassist.data.WorkingProject;


public class RockExpList extends GeoBaseListAdapter implements OnItemSelectedListener{


	Spinner rockNameSpn;
	final String[] rockNames = { "Select", "Granite", "Granodiorite",
			"Diorite", "Gabbro", "Rhyolite", "Andersite", "Basalt", "Other" };
	
	public RockExpList(ArrayList<String> grList, ArrayList<Object> childItem) {
		super(grList, childItem);
		this.groupItem = grList;
		this.Childtem = childItem;
	}

	@Override
	public View getChildView(final int 		groupPosition, 
							final int 	childPosition,
							boolean 	isLastChild, 
							View 		convertView, 
							ViewGroup 	parent) {
		Log.e("Get ChildView", "Group :" + String.valueOf(groupPosition) + 
							   "Child :"  + String.valueOf(childPosition));
		int viewSrc = 0;
		switch (groupPosition) {
		case 0:
			viewSrc = R.layout.rock_l1_detail;
			convertView = minflater.inflate(viewSrc, null);
			ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
			ArrayAdapter<String> rockAdapter = new ArrayAdapter<String>(this.activity, 
					android.R.layout.simple_dropdown_item_1line,rockNames);
			rockNameSpn  = (Spinner)  convertView.findViewById(R.id.rockTypeSpinner);
			rockNameSpn.setAdapter(rockAdapter);
			rockNameSpn.setOnItemSelectedListener(this);
			doneBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					RockDetails parent = (RockDetails)RockExpList.this.activity;
					Log.e("Done Button", "Called");
					parent.collectDetails(groupPosition);
				}
			});
			break;
		case 1:
			convertView = setupDikeInfoView (groupPosition);
			break;
		case 2:
			convertView = setupNotesView(groupPosition);
			break;
			
		default:
			viewSrc = R.layout.info_detail;
			convertView = minflater.inflate(viewSrc, null);
			break;
		}
		return convertView;
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
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		Log.e("Spinner Select ", "Value is "+ String.valueOf(pos));
		if (view.getId() ==  R.id.rockTypeSpinner){
			WorkingProject proj = WorkingProject.getInstance();
			proj.rockName= pos;
		}
	}

	public View setupDikeInfoView ( final int groupPosition) {
		View convertView = minflater.inflate(R.layout.dike_info, null);
		ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RockDetails parent = (RockDetails)RockExpList.this.activity;
				Log.e("Done Button", "Called");
				parent.collectDetails(groupPosition);
			}
		});
		return convertView;
	}

	public View setupNotesView ( final int groupPosition) {
		View convertView = minflater.inflate(R.layout.notes, null);
		ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
		EditText notesTxt = (EditText) convertView.findViewById(R.id.notes);
		notesTxt.setBackgroundColor(Color.LTGRAY);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RockDetails parent = (RockDetails)RockExpList.this.activity;
				Log.e("Done Button", "Called");
				parent.collectDetails(groupPosition);
			}
		});
		return convertView;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}

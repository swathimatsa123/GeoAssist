package com.geoassist;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.geoassist.data.Mineral;
import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;

public class UserDetails extends BaseListActivity implements OnClickListener {
	boolean clickedLocBtn = false;
	ImageButton nextBtn;
	static final int LOCATION_ACTIVITY = 100;
	static final int START_GATHER_DETAILS = 100;
	static final int ROCK_INFO_ACTIVITY = 200;
	private ExpListAdapter expListAdapter = null;
	private ExpandableListView exList = null;
	ArrayList<String> groupItem = new ArrayList<String>();
	ArrayList<Object> childItem = new ArrayList<Object>();
	public double latValue = 0;
	public double longValue = 0;
	WorkingProject  proj = null;
	Site			site = new Site();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_user_details);
		//
		// setContentView(R.layout.info_group);
		exList = getExpandableListView();
		exList.setDividerHeight(2);
		exList.setGroupIndicator(null);
		exList.setClickable(true);
		groupItem.add("Location");
		groupItem.add("Rock");
		groupItem.add("Minerals ");
		groupItem.add("Contact");
		groupItem.add("Fold");
		groupItem.add("Fault");
		groupItem.add("Joint");
		groupItem.add("Vein");
		groupItem.add("Sample");
		groupItem.add("Picture");
		groupItem.add("Notes");
		groupItem.add("Track");
		
		for (int i = 0; i<12; i++) {
			ArrayList<String> child = new ArrayList<String>();
			child = new ArrayList<String>();
			child.add("Dummy");
			childItem.add(child);
		}

		expListAdapter = new ExpListAdapter(groupItem, childItem);
		expListAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),this);
		exList.setAdapter(expListAdapter);
		exList.setOnChildClickListener(this);
		exList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		exList.setBackgroundColor(Color.WHITE);
		Bundle extras = getIntent().getExtras();
		latValue = extras.getDouble("LatValue");
		longValue = extras.getDouble("LongValue");
		expListAdapter.setLocation(latValue, longValue);
		WorkingProject proj = WorkingProject.getInstance();
		proj.sites.add(site); 
		// ImageButton doneBtn = (ImageButton) findViewById(R.id.viewDoneBtn);
		// doneBtn.setOnClickListener(this);
	}


	public void startRockInfoActivity() {
		Intent intnt = new Intent(this, RockDetails.class);
		startActivityForResult(intnt, ROCK_INFO_ACTIVITY);
		return;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	    case R.id.done:
			collectDetails(0);
	    	break;

		default:
			break;
		}
		super.onClick(v);
	}

	public void collectDetails(int groupPosition) {
		WorkingProject proj = WorkingProject.getInstance();
		switch (groupPosition) {
		case 0:
			EditText etLat = (EditText) findViewById(R.id.siteLatEt);
			EditText etLng  = (EditText) findViewById(R.id.siteLngEt);
			if ((etLat != null) && (etLng != null)) {
				site.lat = Double.parseDouble(etLat.getText().toString());
				site.lng = Double.parseDouble(etLng.getText().toString());
			}
			break;
		case 1:
			break;
		case 2:
			collectMineralsInfo(proj);
			break;
		}
		exList.collapseGroup(groupPosition);
		TextView textView = (TextView) exList.getChildAt(groupPosition)
				.findViewById(R.id.groupId);
		if (textView != null) {
			textView.setTextColor(0xFF00CC00);
		}
	}

	public boolean isNumber(String str) {
		try { 
	        Integer.parseInt(str); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	public void collectMineralsInfo(WorkingProject proj ) {
		Mineral mineral = new Mineral();
		EditText mineralNameEt;
		EditText minGrainSizeEt;
		EditText maxGrainSizeEt;
		EditText compositionEt;
		EditText cleavegeEt;
		Spinner grainFormSpn;
		Spinner grainShapeSpn;
		mineralNameEt  = (EditText) findViewById(R.id.mineralNameEt);
		minGrainSizeEt = (EditText) findViewById(R.id.minGrainSizeEt);
		maxGrainSizeEt = (EditText) findViewById(R.id.maxGrainSizeEt);
		compositionEt  = (EditText) findViewById(R.id.compositionEt);
		cleavegeEt     = (EditText) findViewById(R.id.mineralCleavegeEt);
		grainFormSpn = (Spinner) findViewById(R.id.grainFormSpn);
		grainShapeSpn  = (Spinner) findViewById(R.id.grainShapeSpn);
		
		if (isNumber(compositionEt.getText().toString()) == true) {
			mineral.composition = Float.parseFloat(compositionEt.getText().toString());
		}
		mineral.grainForm =  (int) grainFormSpn.getSelectedItemId();
		mineral.grainShape = (int) grainShapeSpn.getSelectedItemId();
		if (isNumber(maxGrainSizeEt.getText().toString()) == true) {
			mineral.maxGrainSize = Float.parseFloat(maxGrainSizeEt.getText().toString());
		}
		if (isNumber(minGrainSizeEt.getText().toString()) == true) {
			mineral.minGrainSize = Float.parseFloat(minGrainSizeEt.getText().toString());
		}
		mineral.mineralName  = mineralNameEt.getText().toString();
		mineral.mineralCleavege = cleavegeEt.getText().toString();
		Site site = proj.sites.get(proj.sites.size()-1);
		site.minerals.add(mineral);
	}
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Toast.makeText(this, "Clicked On Child", Toast.LENGTH_SHORT).show();
		return true;
	}
}

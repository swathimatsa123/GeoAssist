package com.geoassist;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;

public class AddSiteActivity extends BaseListActivity implements OnClickListener, OnGroupClickListener {
	boolean clickedLocBtn = false;
	ImageButton nextBtn;
	static final int MINERAL_POSITION = 3;
	static final int CONTACT_POSITION = 4;
	static final int FOLD_POSITION = 5;
	static final int LOCATION_ACTIVITY = 100;
	static final int ROCK_INFO_ACTIVITY = 900;
	static final int MINERAL_VIEW_ACTIVITY = 300;
	static final int CONTACT_VIEW_ACTIVITY = 400;
	static final int FOLD_VIEW_ACTIVITY = 500;
	static final int PICTURE_POSITION = 9;
	static final int PICTURE_ACTIVITY = 900;
	
	private MainExpdList expListAdapter = null;
	private ExpandableListView exList = null;
	ArrayList<String> groupItem = new ArrayList<String>();
	ArrayList<Object> childItem = new ArrayList<Object>();
	public double latValue = 0;
	public double longValue = 0;
//	StrikeDip strdip = null; 
	WorkingProject  proj = null;
	Site			site = new Site();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		strdip = new StrikeDip(this) ;
		
		exList = getExpandableListView();
		exList.setDividerHeight(2);
		exList.setGroupIndicator(null);
		exList.setClickable(true);
		groupItem.add("Location");
		groupItem.add("Rock Unit");
		groupItem.add("Rock ");
		groupItem.add("Minerals");
		groupItem.add("Contact");
		groupItem.add("Fold");
		groupItem.add("Fault");
		groupItem.add("Joint");
		groupItem.add("Vein");
//		groupItem.add("Sample");
		groupItem.add("Picture");
		groupItem.add("Notes");
//		groupItem.add("Track");
		
		for (int i = 0; i<groupItem.size(); i++) {
			Log.e("Group ", String.valueOf(i)+ " is being init");
			ArrayList<String> child = new ArrayList<String>();
			child = new ArrayList<String>();
			child.add("Dummy");
			childItem.add(child);
		}

		expListAdapter = new MainExpdList(groupItem, childItem);
		expListAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),this);
		exList.setAdapter(expListAdapter);
		exList.setOnChildClickListener(this);
		exList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		exList.setBackgroundColor(Color.WHITE);
		Bundle extras = getIntent().getExtras();
		latValue = extras.getDouble("LatValue");
		longValue = extras.getDouble("LongValue");
		expListAdapter.setLocation(latValue, longValue);
		proj = WorkingProject.getInstance();
		SharedPreferences preferences;
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String usrName  = preferences.getString("username", "");
		Log.e("USER Name In New", usrName);
		proj.scientistName = usrName;
		proj.sites.add(site); 
		exList.setOnGroupClickListener(this);
	}


	@Override
	protected void onResume() {
//		strdip.start();
		super.onResume();
	}


	public void startRockInfoActivity() {
		Intent intnt = new Intent(this, RockDetails.class);
		startActivityForResult(intnt, ROCK_INFO_ACTIVITY);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		return;
	}
	
	public void invokeMineralListView () {
		Intent intnt = new Intent(this, MineralMain.class);
		startActivityForResult(intnt, MINERAL_VIEW_ACTIVITY );
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		return;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if ((requestCode == MINERAL_VIEW_ACTIVITY) ||
	    	(requestCode == CONTACT_VIEW_ACTIVITY)	||
	    	(requestCode == FOLD_VIEW_ACTIVITY)) {
	        if (resultCode == RESULT_OK) {
	        	int groupPos = requestCode/100;
	    		TextView textView = (TextView) exList.getChildAt(groupPos).findViewById(R.id.groupId);
	    		if (textView != null) {
	    			textView.setTextColor(0xFF00CC00);
	    		}
	        }
	    }
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	    case R.id.done:
			collectDetails(0);
	    	break;
		case R.id.cancel:
			if (proj.sites != null) {
				proj.sites.remove(site);
			}
			break;
		default:
			break;
		}
		super.onClick(v);
	}

	public void collectDetails(int groupPosition) {
		WorkingProject proj = WorkingProject.getInstance();
		Log.e("CollectDetails called0", "Group "+ String.valueOf(groupPosition));
		Site site = proj.sites.get(proj.sites.size()-1);

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
			EditText rockUnitEt = (EditText) findViewById(R.id.rockUnitEt);
			site.rockUnit = rockUnitEt.getText().toString();
			break;
		case 2:
//			collectMineralsInfo(proj);
			TextView textView = (TextView) exList.getChildAt(groupPosition).findViewById(R.id.groupId);
			if (textView != null) {
				textView.setTextColor(0xFF00CC00);
			}
			break;
		case 6:
			site.faultMovement =  getTextFromSpinner(R.id.movementSpn);
			site.faultZoneWidth =  getTextFromEt(R.id.zoneWidthEt);
			site.faultSeparation  =  getTextFromEt(R.id.sepTxt);
			site.faultOffset =  getTextFromEt(R.id.offset) +  getTextFromSpinner(R.id.offsetUnitsSpn);
			site.faultPiercingPt =  getTextFromEt(R.id.piercingPtEt);
			site.faultNetSlip   =  getTextFromEt(R.id.netSlipEt)+ getTextFromSpinner(R.id.netSlipUnits);
			site.faultMineralization =  getTextFromEt( R.id.mineralizationEt);
			break;
		case 7:
			site.jointStrike = getTextFromEt( R.id.strikeEt);
			site.jointDip = getTextFromEt( R.id.dipEt);
			site.jointSpacing = getTextFromEt( R.id.spacingEt);
			site.jointBedding =  getTextFromSpinner( R.id.beddingSpn);
			site.jointFoldType = getTextFromSpinner( R.id.foldTypeSpn);
			break;
		case 8:
			site.veinStrike = getTextFromEt(R.id.strikeEt) ;
			site.veinDip = getTextFromEt(R.id.dipEt) ;
			site.veinZoneWidth = getTextFromEt(R.id.zoneWidthEt) + getTextFromSpinner( R.id.units) ;
			site.veinComposition = getTextFromEt(R.id.compositionEt)+ getTextFromSpinner( R.id.compositionSpn)  ;
			break;
		}
		exList.collapseGroup(groupPosition);
		Log.e("First " + String.valueOf(exList.getFirstVisiblePosition()),
			  " Last " + String.valueOf(exList.getLastVisiblePosition()));
		Log.e("Child@" , String.valueOf(exList.getChildAt(groupPosition)));
		Log.e("NewView @" , String.valueOf(exList.getItemAtPosition(groupPosition)));
		View v = (View) exList.getChildAt(groupPosition);
		if (v != null) {
			TextView textView = (TextView) exList.getChildAt(groupPosition).findViewById(R.id.groupId);
			if (textView != null) {
				textView.setTextColor(0xFF00CC00);
			}
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Toast.makeText(this, "Clicked On Child", Toast.LENGTH_SHORT).show();
		return true;
	}


	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
		boolean retVal = false;
		switch (groupPosition) {
		case MINERAL_POSITION:
			invokeMineralListView();
			retVal = true;
			break;
		case CONTACT_POSITION:
//			invokeActivity(ContactMain.class, CONTACT_VIEW_ACTIVITY);
			invokeActivity(AddContactActivity.class, CONTACT_VIEW_ACTIVITY);
			retVal = true;
			break;
		case FOLD_POSITION:
			invokeActivity(FoldMain.class, FOLD_VIEW_ACTIVITY);
			retVal = true;
			break;

		case PICTURE_POSITION:
			invokeActivity(FoldMain.class, FOLD_VIEW_ACTIVITY);
			retVal = true;
			break;

		default:
			retVal = false;
		}
		return retVal; 
	}
	public void collectRockDetails (WorkingProject proj) {
		
		return;
	}
}

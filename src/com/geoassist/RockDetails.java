package com.geoassist;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;

public class RockDetails extends BaseListActivity implements OnClickListener, OnGroupClickListener {
	static final int MET_POSITION = 1;
	static final int MET_ACTIVITY = 100;

	ImageButton doneBtn;
	Spinner rockNameSpn;
	final String[] rockNames = { "Select", "Granite", "Granodiorite",
			"Diorite", "Gabbro", "Rhyolite", "Andersite", "Basalt", "Other" };
	Spinner dikeCmpSpn;
	final String[] compositions = { "Select", "Quartz", "Aplite", "Diabase",
			"Pegmatite", "Alaskite", "Other" };
	private RockExpList expListAdapter = null;
	private ExpandableListView exList  = null;
	ArrayList<String> groupItem = new ArrayList<String>();
	ArrayList<Object> childItem = new ArrayList<Object>();
	WorkingProject proj = WorkingProject.getInstance();
	Site site;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		proj = WorkingProject.getInstance();
		exList = getExpandableListView();
		exList.setDividerHeight(2);
		exList.setGroupIndicator(null);
		exList.setClickable(true);
		groupItem.add("Rock Details");
		site = proj.sites.get(proj.sites.size()-1);
		if (site.rockType.equals("Igneous")) {
			groupItem.add("Dike ");
		}
		else if (site.rockType.equals("Sedimentary")) {
			groupItem.add("Sedimentary");
		}
//		else if (site.rockType.equals("Metamorphic")) {
//			groupItem.add("Metamorphic");
//		}
		else if (site.rockType.equals("Foliation/Lineation")) {
			groupItem.add("Foliation/Lineation");
		}		
		groupItem.add("Notes");
		for (int i = 0; i<groupItem.size(); i++) {
			ArrayList<String> child = new ArrayList<String>();
			child = new ArrayList<String>();
			child.add("Dummy");
			childItem.add(child);
		}

		expListAdapter = new RockExpList (groupItem, childItem);
		expListAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),this);
		exList.setAdapter(expListAdapter);
		exList.setOnChildClickListener(this);
		exList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		exList.setOnGroupClickListener(this);
	}

	public void collectDetails(int groupId) {
		exList.collapseGroup(groupId);
        TextView textView=(TextView) exList.getChildAt(groupId).findViewById(R.id.groupId);
        Log.e("TextView", String.valueOf(textView));
        if (textView != null) {
                textView.setTextColor(0xFF00CC00);
        }
		Site site = proj.sites.get(proj.sites.size()-1);

		switch (groupId) {
		case 0:
			site.rockId  = getTextFromSpinner(R.id.rockTypeSpinner) ;
			Log.e("## RockDetails", site.rockId);
			break;
		case 1:
			if (site.rockType.equals("Igneous")) {
				site.genStrike = getTextFromEt(R.id.strikeEt);
				site.genDip    = getTextFromEt(R.id.dipEt);
				site.dikeThickNess = getTextFromEt(R.id.thicknessEt);
				site.minGrainSize  = getTextFromEt(R.id.grainSizeMin);
				site.maxGrainSize  = getTextFromEt(R.id.grainSizeMax);
				site.dikeDescription = getTextFromEt(R.id.descriptionEt);
			}
			else if (site.rockType.equals("Sedimentary")) {
				site.sedGrainDia = getTextFromSpinner(R.id.grainDia);
				site.sedGrainPhi = getTextFromSpinner(R.id.grainPhi);
				site.sedGrainName = getTextFromSpinner(R.id.grainNameSpn);
				site.sedGrainMinerals = getTextFromEt(R.id.grianMineralsEt);
				site.sedGrainLithics = getTextFromEt(R.id.grainLithicsEt);
				site.grainSorting = getTextFromSpinner(R.id.grainSortSpn);
				site.grainRounding = getTextFromSpinner(R.id.grainRoundSpn);
				site.sedBedding = getTextFromSpinner(R.id.sedBeddingSpn);
				site.sedCrossBedding = getTextFromSpinner(R.id.crossBeddingSpn);
				site.genStrike = getTextFromEt(R.id.beddingStrikeEt);
				site.genDip = getTextFromEt(R.id.beddingDipEt);
				site.sedRipples = getTextFromSpinner(R.id.rippleSpn);
				site.sedSoleMarks  = getTextFromSpinner(R.id.soleMarksSpn);
				site.sedSSD = getTextFromSpinner(R.id.sedSsdSpn);	
				site.sedOtherStr = getTextFromEt(R.id.sedStructEt);	
				site.sedFossils = getTextFromSpinner(R.id.sedFossilsSpn);	
				site.sedDepEnv= getTextFromSpinner(R.id.sedDepSpn);							
			}
			else if (site.rockType.equals("Foliation/Lineation")) {
			}
			break;
		case 2:
			site.rockInfoNotes   = getTextFromEt(R.id.notes);
			break;
		}
		

	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		boolean retVal = false;
		switch (groupPosition) {
		case MET_POSITION:
			if (site.rockType.equals("Metamorphic")) {
				invokeActivity(MetamorphicActivity.class, MET_ACTIVITY);
				retVal = true;
			}
			break;
		default:
			retVal = false;
		}
		return retVal; 
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if ((requestCode == MET_ACTIVITY) ) {
	        if (resultCode == RESULT_OK) {
	        	int groupPos = requestCode/100;
	    		TextView textView = (TextView) exList.getChildAt(groupPos).findViewById(R.id.groupId);
	    		if (textView != null) {
	    			textView.setTextColor(0xFF00CC00);
	    		}
	        }
	    }
	}

	public int getViewFileForRock() {
		Site site = proj.sites.get(proj.sites.size()-1);
		if (site.rockType.equals("Igneous")) {
			return R.layout.dike_info;
		}
		else if (site.rockType.equals("Sedimentary")) {
			return R.layout.sed_info;
		}
		else if (site.rockType.equals("Foliation/Lineation")) {
			return R.layout.met_dtls_add;		
		}
		return R.layout.dike_info;		
	}
}
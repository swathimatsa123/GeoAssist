package com.geoassist;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;

public class MetamorphicActivity extends BaseListActivity implements OnClickListener {
	static final int ADD_CONTACT_ACTIVITY = 100;
	private MetamorphicAdaptor expListAdapter = null;
	private ExpandableListView exList = null;
	ArrayList<String> groupItem = new ArrayList<String>();
	ArrayList<Object> childItem = new ArrayList<Object>();
	ImageButton doneBtn;
	ImageButton cancelBtn;
	ImageButton addBtn;
	WorkingProject proj = WorkingProject.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(R.layout.mineral_menu);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		doneBtn = (ImageButton) actionBar.getCustomView().findViewById(R.id.done);
		doneBtn.setOnClickListener(this);
		cancelBtn = (ImageButton) actionBar.getCustomView().findViewById(R.id.cancel);
		cancelBtn.setOnClickListener(this);
		addBtn = (ImageButton) actionBar.getCustomView().findViewById(R.id.add);
		addBtn.setOnClickListener(this);

		exList = getExpandableListView();
		exList.setDividerHeight(2);
		exList.setGroupIndicator(null);
		exList.setClickable(true);
		WorkingProject proj = WorkingProject.getInstance();
		if (proj.sites.size( )>= 0) {
			Site site = proj.sites.get(proj.sites.size()-1);
			for (int i =0; i< site.metDtls.size();i++) {
				groupItem.add("Foliation "+String.valueOf(i+1));
				ArrayList<String> child = new ArrayList<String>();
				child.add("Dummy");
				childItem.add(child);
			}
		}
		expListAdapter = new MetamorphicAdaptor(groupItem, childItem);
		expListAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),this);
		exList.setAdapter(expListAdapter);
		exList.setOnChildClickListener(this);
		exList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		exList.setBackgroundColor(Color.WHITE);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			invokeActivity(MetmorphicAddition.class, ADD_CONTACT_ACTIVITY);
			break;
		default:
			break;
		}
		super.onClick(v);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == ADD_CONTACT_ACTIVITY) {
	        if (resultCode == RESULT_OK) {
	        	refreshContactList();
	        }
	    }
	}
	private void refreshContactList() {
		groupItem.clear();
		if (proj.sites.size( )>= 0) {
			Site site = proj.sites.get(proj.sites.size()-1);
			for (int i =0; i< site.metDtls.size();i++) {
				groupItem.add("Foliation "+String.valueOf(i+1));
				ArrayList<String> child = new ArrayList<String>();
				child.add("Dummy");
				childItem.add(child);
			}
		}
    	expListAdapter.notifyDataSetChanged();
	}
	
	public void deleteContact(int index) {
		WorkingProject proj = WorkingProject.getInstance();
		Site site = proj.sites.get(proj.sites.size()-1);
		site.metDtls.remove(index);
		refreshContactList();
    }
}

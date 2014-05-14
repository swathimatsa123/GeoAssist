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
import android.widget.Toast;

import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;

public class MineralMain extends BaseListActivity implements OnClickListener {
	static final int ADD_MINERAL_ACTIVITY = 100;
	ImageButton nextBtn;
	private MineralListAdaptor expListAdapter = null;
	private ExpandableListView exList = null;
	ArrayList<String> groupItem = new ArrayList<String>();
	ArrayList<Object> childItem = new ArrayList<Object>();
	ImageButton doneBtn;
	ImageButton cancelBtn;
	ImageButton addBtn;

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
			for (int i =0; i< site.minerals.size();i++) {
				groupItem.add("Mineral "+site.minerals.get(i).mineralName);
			}
		}
//		groupItem.add("Mineral 1");
//		groupItem.add("Mineral 2");
//		groupItem.add("Mineral 3");
//		groupItem.add("Mineral 4");

		for (int i = 0; i < 12; i++) {
			ArrayList<String> child = new ArrayList<String>();
			child = new ArrayList<String>();
			child.add("Dummy");
			childItem.add(child);
		}

		expListAdapter = new MineralListAdaptor(groupItem, childItem);
		expListAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),this);
		exList.setAdapter(expListAdapter);
		exList.setOnChildClickListener(this);
		exList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		exList.setBackgroundColor(Color.WHITE);

	}
	public void deleteMineral(int index) {
		WorkingProject proj = WorkingProject.getInstance();
		Site site = proj.sites.get(proj.sites.size()-1);
		site.minerals.remove(index);
    	refreshMineralList();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.done:
//			Intent resultIntent = new Intent();
//			setResult(Activity.RESULT_OK, resultIntent);
//			finish();
//			break;
//		case R.id.cancel:
//			finish();
//			break;
		case R.id.add:
			invokeActivity(AddMineralActivity.class, ADD_MINERAL_ACTIVITY);
			break;
		default:
			break;
		}
		super.onClick(v);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Toast.makeText(this, "Clicked On Child", Toast.LENGTH_SHORT).show();
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == ADD_MINERAL_ACTIVITY) {
	        if (resultCode == RESULT_OK) {
	        	refreshMineralList();
	        }
	    }
	}
	private void refreshMineralList() {
		WorkingProject proj = WorkingProject.getInstance();
		groupItem.clear();
		if (proj.sites.size( )>= 0) {
			Site site = proj.sites.get(proj.sites.size()-1);
			for (int i =0; i< site.minerals.size();i++) {
				groupItem.add("Mineral "+site.minerals.get(i).mineralName);
			}
		}
    	expListAdapter.notifyDataSetChanged();
	}
}

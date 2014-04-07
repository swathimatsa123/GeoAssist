package com.geoassist;
import java.io.File;
import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.geoassist.data.Project;
import com.geoassist.data.WorkingProject;

public class UserDetails extends BaseListActivity implements OnClickListener{
	Project 	currProject;
	boolean clickedLocBtn = false;
	ImageButton nextBtn;
	static final int LOCATION_ACTIVITY = 100;
	static final int START_GATHER_DETAILS = 100;
	static final int ROCK_INFO_ACTIVITY   = 200;
	private ExpListAdapter expListAdapter = null;
	private ExpandableListView exList  = null;
	ArrayList<String> groupItem = new ArrayList<String>();
	ArrayList<Object> childItem = new ArrayList<Object>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_user_details);
//
//		setContentView(R.layout.info_group);
		currProject =  (Project) getIntent().getSerializableExtra("Project");
		exList = getExpandableListView();
		exList.setDividerHeight(2);
		exList.setGroupIndicator(null);
		exList.setClickable(true);
		groupItem.add("Project Information");
		groupItem.add("Rock");
		groupItem.add("Minerals ");
		groupItem.add("Contact");        
		
		ArrayList<String> child = new ArrayList<String>();
		child = new ArrayList<String>();
		child.add("Dummy");
		childItem.add(child);

		child = new ArrayList<String>();
		child.add("Dummy");
		childItem.add(child);

		child = new ArrayList<String>();
		child.add("Dummy");
		childItem.add(child);

		child = new ArrayList<String>();
		child.add("Dummy");
		childItem.add(child);

		expListAdapter = new ExpListAdapter(groupItem, childItem);
		expListAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),this);
		exList.setAdapter(expListAdapter);
		exList.setOnChildClickListener(this);
		exList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		exList.setBackgroundColor(Color.WHITE);
//		ImageButton doneBtn =  (ImageButton) findViewById(R.id.viewDoneBtn);
//		doneBtn.setOnClickListener(this);
	}


	public void startNextActivity() {
		Intent intnt = new Intent(this, SampleDetailsOne.class);
		intnt.putExtra("Project", currProject);  
		startActivityForResult(intnt, START_GATHER_DETAILS);
		return;
	}
	
	public void startRockInfoActivity () {
		Intent intnt = new Intent(this, RockDetails.class);
		startActivityForResult(intnt, ROCK_INFO_ACTIVITY);
		return;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.nextBtn:
				// Collect the details that user has entered.
				collectDetails(0);
				startNextActivity();
				break;
		}
	}
	
	public void collectDetails(int groupPosition) {
		Log.e(" CurrProject", String.valueOf(currProject));
		WorkingProject proj = WorkingProject.getInstance();
		switch (groupPosition) {
			case 0:
				EditText etProjName = (EditText) findViewById(R.id.projNameEt);
				EditText etProjLcn  = (EditText) findViewById(R.id.lcnEt);
				if (etProjName != null) {
					Log.e("Proj Details" , etProjName.getText().toString());
					proj.name = etProjName.getText().toString();
					proj.location = etProjLcn.getText().toString();
				}
				break;
			case 1:
				break;
		}
		exList.collapseGroup(groupPosition);
        TextView textView=(TextView) exList.getChildAt(groupPosition).findViewById(R.id.groupId);
        Log.e("TExtView", String.valueOf(textView));
        if (textView != null) {
            textView.setTextColor(0xFF00CC00);
        }
	}
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Log.e("Activity", "OnChildClick");
		Toast.makeText(this, "Clicked On Child", Toast.LENGTH_SHORT).show();
		return true;
	}
}

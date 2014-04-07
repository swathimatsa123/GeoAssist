package com.geoassist;

import java.util.ArrayList;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.geoassist.data.WorkingProject;

public class RockDetails extends BaseListActivity {
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_user_details);
		exList = getExpandableListView();
		exList.setDividerHeight(2);
		exList.setGroupIndicator(null);
		exList.setClickable(true);
		groupItem.add("Rock Details");
		groupItem.add("Dike ?");
		groupItem.add("Notes");
		
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

		expListAdapter = new RockExpList (groupItem, childItem);
		expListAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),this);
		exList.setAdapter(expListAdapter);
		exList.setOnChildClickListener(this);
		exList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
	}

	public void collectDetails(int groupId) {
		WorkingProject proj = WorkingProject.getInstance();
		exList.collapseGroup(groupId);
        TextView textView=(TextView) exList.getChildAt(groupId).findViewById(R.id.groupId);
        Log.e("TExtView", String.valueOf(textView));
        if (textView != null) {
                textView.setTextColor(0xFF00CC00);
        }
	}
}

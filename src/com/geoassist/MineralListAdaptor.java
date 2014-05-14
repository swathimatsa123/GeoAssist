package com.geoassist;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.geoassist.data.Mineral;
import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;

public class MineralListAdaptor extends GeoBaseListAdapter {
	ImageButton  delBtn = null;
	public MineralListAdaptor(ArrayList<String> grList,
			ArrayList<Object> childItem) {
		super(grList, childItem);
		this.groupItem = grList;
		this.Childtem = childItem;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		convertView = minflater.inflate(R.layout.mineral_view, null);
		delBtn = (ImageButton)convertView.findViewById(R.id.delete);
		WorkingProject proj = WorkingProject.getInstance();
		convertView.setBackgroundColor(Color.LTGRAY);
		if (proj.sites.size( )>= groupPosition) {
			Site site = proj.sites.get(proj.sites.size()-1);
			Mineral mineral = site.minerals.get(groupPosition);
			TextView  tv1 = (TextView) convertView.findViewById(R.id.mineralInfo1);
			tv1.setText("Grain Size:    Min - " + String.valueOf(mineral.minGrainSize) +
					    "    Max - " + String.valueOf(mineral.maxGrainSize));
			TextView  tv2 = (TextView) convertView.findViewById(R.id.mineralInfo2);
			tv2.setText("Composition : " + String.valueOf(mineral.composition) +
				        "    Cleavege    : " + String.valueOf(mineral.mineralCleavege));
			TextView  tv3 = (TextView) convertView.findViewById(R.id.mineralInfo3);
			tv3.setText("Grain:    Form - " + mineral.grainForm+  "    Shape - " + mineral.grainShape);
			delBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((MineralMain) MineralListAdaptor.this.activity).deleteMineral(groupPosition);
				}
			});

		}
		convertView.setBackgroundColor(0xFFF0FFF0);
		return convertView;
	}

}

package com.geoassist;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.geoassist.data.MetDtls;
import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;

public class MetamorphicAdaptor  extends GeoBaseListAdapter {
	ImageButton  delBtn = null;
	
	public MetamorphicAdaptor(ArrayList<String> grList,
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
			MetDtls metDtl = site.metDtls.get(groupPosition);
			TextView  tv1 = (TextView) convertView.findViewById(R.id.mineralInfo1);
			tv1.setText("Foliation -  Strike : " + String.valueOf(metDtl.strike) +
									  "  Dip : " + String.valueOf(metDtl.dip));
			
			TextView  tv2 = (TextView) convertView.findViewById(R.id.mineralInfo2);
			tv2.setText("Foliation Trend:       " + String.valueOf(metDtl.lineation));
			TextView  tv3 = (TextView) convertView.findViewById(R.id.mineralInfo3);
			tv3.setText("Foliation Plunge:      " + String.valueOf(metDtl.plunge));
			delBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((MetamorphicActivity) MetamorphicAdaptor.this.activity).deleteContact(groupPosition);
				}
			});

		}
		return convertView;
	}
}

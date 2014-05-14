package com.geoassist;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.geoassist.data.Contact;
import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;

public class ContactListAdaptor extends GeoBaseListAdapter {
	ImageButton  delBtn = null;
	
	public ContactListAdaptor(ArrayList<String> grList,
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
			Contact contact = site.contacts.get(groupPosition);
			TextView  tv1 = (TextView) convertView.findViewById(R.id.mineralInfo1);
			tv1.setText("Contact Unit:       " + String.valueOf(contact.contact1Name));
			TextView  tv2 = (TextView) convertView.findViewById(R.id.mineralInfo2);
			tv2.setText("Contact Type:       " + String.valueOf(contact.contactType));
			TextView  tv3 = (TextView) convertView.findViewById(R.id.mineralInfo3);
			tv3.setText("Contact Boundary:   " + String.valueOf(contact.boundary));
			delBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((ContactMain) ContactListAdaptor.this.activity).deleteContact(groupPosition);
				}
			});

		}
		return convertView;
	}
}

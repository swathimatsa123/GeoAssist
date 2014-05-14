package com.geoassist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.geoassist.data.Contact;
import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;

public class AddContactActivity extends BaseActionBarActivity implements OnClickListener{
	ImageButton doneBtn;
	ImageButton cancelBtn;
	Spinner     contactTypeSpn;
	Spinner     boundarySpn;
	final String[] contactTypes = {"Select Type", "Sharp", "Gradational","Interbedding Transition", 
									"Undulating","Nonconformable"};
	final String[] boundaryTypes = {"Select Type", "Stringer", "Float", "Fault","Unconformity",
									"Scour","Dike","Alluvium","Lense"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_add);

		ActionBar actionBar = getSupportActionBar();

	    actionBar.setCustomView(R.layout.mineral_add_menu);
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	    doneBtn  = (ImageButton)actionBar.getCustomView().findViewById(R.id.done);
	    doneBtn.setOnClickListener(this);
	    doneBtn.getRootView().setBackgroundColor(0xFFF0FFF0);
	    cancelBtn  = (ImageButton)actionBar.getCustomView().findViewById(R.id.cancel);
	    cancelBtn.setOnClickListener(this);
	    contactTypeSpn = (Spinner) findViewById(R.id.contactTypeSpn);
		ArrayAdapter<String> contactTypeAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line,contactTypes);
		contactTypeSpn.setAdapter(contactTypeAdapter);
	    
	    boundarySpn = (Spinner) findViewById(R.id.boundarySpn);
	    ArrayAdapter<String> boundarySpnAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line,boundaryTypes);
	    boundarySpn.setAdapter(boundarySpnAdapter);

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.done:
				WorkingProject proj = WorkingProject.getInstance();
				SaveContactDetails(proj);
				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				break;
			case R.id.cancel:
				finish();
				break;
		}
	}
	public void SaveContactDetails(WorkingProject proj ) {
		Contact contact = new Contact();
		EditText contact1Name = (EditText) findViewById(R.id.contact1Et);
		EditText contact2Name = (EditText) findViewById(R.id.contact2Et);
		EditText contactTrend = (EditText) findViewById(R.id.trendEt);
		
		EditText contactNotes= (EditText) findViewById(R.id.notes);
		Spinner  contactTypeSpn  = (Spinner) findViewById(R.id.contactTypeSpn);
		Spinner  boundarySpn  = (Spinner) findViewById(R.id.boundarySpn);
		contact.contact1Name = contact1Name.getText().toString();
		contact.contact2Name = contact2Name.getText().toString();
		contact.boundary    =   boundaryTypes[(int) boundarySpn.getSelectedItemId()];
		contact.contactType =   contactTypes[(int) contactTypeSpn.getSelectedItemId()];
		contact.contactTrend = contactTrend.getText().toString();  
		contact.notes  = contactNotes.getText().toString();
		Site site = proj.sites.get(proj.sites.size()-1);
		site.contacts.add(contact);
	}
	
}

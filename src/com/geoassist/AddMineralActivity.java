package com.geoassist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.geoassist.data.Mineral;
import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;

public class AddMineralActivity extends BaseActionBarActivity implements OnClickListener{
	
	EditText  mineralName;
	EditText  minGrainSize;
	EditText  maxGrainSize;
	EditText  composition;
	EditText  cleavege;
	Spinner   grainForm;
	Spinner   grainShape;
	ImageButton doneBtn;
	ImageButton cancelBtn;
	final String[] grainForms = {"Select Type", "Anhedral", "Subhedral","Euhedral" };
	final String[] grainShapes = {"Select Type","Equant", "Interstitial", 
								  "Irregular" ,"Platy","Prismatic" ,"Tabular",
								  "Acicular", "Other" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_mineral);
		ActionBar actionBar = getSupportActionBar();

		mineralName = (EditText)findViewById(R.id.mineralNameEt);
		minGrainSize = (EditText)findViewById(R.id.minGrainSizeEt);
		maxGrainSize = (EditText)findViewById(R.id.maxGrainSizeEt);
		composition = (EditText)findViewById(R.id.compositionEt);
		cleavege   =    (EditText)findViewById(R.id.mineralCleavegeEt);
	    // add the custom view to the action bar
	    actionBar.setCustomView(R.layout.mineral_add_menu);
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	    doneBtn  = (ImageButton)actionBar.getCustomView().findViewById(R.id.done);
	    doneBtn.setOnClickListener(this);
	    doneBtn.getRootView().setBackgroundColor(0xFFF0FFF0);
	    cancelBtn  = (ImageButton)actionBar.getCustomView().findViewById(R.id.cancel);
	    cancelBtn.setOnClickListener(this);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.done:
				WorkingProject proj = WorkingProject.getInstance();
				SaveMineralDetails(proj);
				Intent resultIntent = new Intent(this, MineralMain.class);
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				break;
			case R.id.cancel:
				finish();
				break;
		}
	}
	
	@Override
	protected void onResume() {
		grainForm  = (Spinner) findViewById(R.id.grainFormSpn);
		grainShape = (Spinner) findViewById(R.id.grainShapeSpn);
		ArrayAdapter<String> formAdapter = new ArrayAdapter<String>(this, 
					android.R.layout.simple_dropdown_item_1line,grainForms);
		grainForm.setAdapter(formAdapter);

		ArrayAdapter<String> shapesAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line,grainShapes);
		grainShape.setAdapter(shapesAdapter);
		super.onResume();
	}

	public void SaveMineralDetails(WorkingProject proj ) {
		Mineral mineral = new Mineral();
		EditText mineralNameEt;
		EditText minGrainSizeEt;
		EditText maxGrainSizeEt;
		EditText compositionEt;
		EditText cleavegeEt;
		Spinner grainFormSpn;
		Spinner grainShapeSpn;
		mineralNameEt  = (EditText) findViewById(R.id.mineralNameEt);
		minGrainSizeEt = (EditText) findViewById(R.id.minGrainSizeEt);
		maxGrainSizeEt = (EditText) findViewById(R.id.maxGrainSizeEt);
		compositionEt  = (EditText) findViewById(R.id.compositionEt);
		cleavegeEt     = (EditText) findViewById(R.id.mineralCleavegeEt);
		grainFormSpn = (Spinner) findViewById(R.id.grainFormSpn);
		grainShapeSpn  = (Spinner) findViewById(R.id.grainShapeSpn);
		
		if (isNumber(compositionEt.getText().toString()) == true) {
			mineral.composition = Float.parseFloat(compositionEt.getText().toString());
		}
		mineral.grainForm =   grainForms[(int) grainFormSpn.getSelectedItemId()];
		mineral.grainShape =  grainShapes[(int)grainShapeSpn.getSelectedItemId()];
		if (isNumber(maxGrainSizeEt.getText().toString()) == true) {
			mineral.maxGrainSize = Float.parseFloat(maxGrainSizeEt.getText().toString());
		}
		if (isNumber(minGrainSizeEt.getText().toString()) == true) {
			mineral.minGrainSize = Float.parseFloat(minGrainSizeEt.getText().toString());
		}
		mineral.mineralName  = mineralNameEt.getText().toString();
		mineral.mineralCleavege = cleavegeEt.getText().toString();
		Site site = proj.sites.get(proj.sites.size()-1);
		site.minerals.add(mineral);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add_mineral,
					container, false);
			rootView.setBackgroundColor(0xFFF0FFF0);
			return rootView;
		}
	}

}

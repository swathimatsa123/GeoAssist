package com.geoassist;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.geoassist.data.GrainForm;
import com.geoassist.data.GrainShape;
import com.geoassist.data.Project;
import com.geoassist.data.Mineral;
import com.geoassist.data.RockType;
public class IgneousMineral extends BaseActivity implements OnClickListener {
	EditText mineralNameEt = null;
	EditText minGrainSizeEt = null;
	EditText maxGrainSizeEt = null;
	EditText compositionEt = null;
	EditText cleavegeEt    = null;
	Spinner  rockNameSpn   = null;
	Spinner grainFormSpn = null;
	Spinner grainShapeSpn = null;

	ImageButton nextBtn;
	Button  addMineralsBtn;
	static final int START_IGNEOUS_INFO_DETAILS  = 100;
	static final int START_ADD_MINERALS_ACTIVITY = 100;
	final String[] grainForms  = { "Select", "Anhedral", "Subhedral" ,"Euhedral" };
    final String[] grainShapes = { "Select", "Equant", "Interstital" ,"Irregular" };
//    final String[] rockName    = { "Select", "QAP Diagram", "Interstital" ,"Irregular" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_igneous_mineral);
		currProject =  (Project) getIntent().getSerializableExtra("Project");
		setupUi();
	}

	public void setupUi() {
		mineralNameEt  = (EditText) findViewById(R.id.mineralNameEt);
		minGrainSizeEt = (EditText) findViewById(R.id.minGrainSizeEt);
		maxGrainSizeEt = (EditText) findViewById(R.id.maxGrainSizeEt);
		compositionEt  = (EditText) findViewById(R.id.compositionEt);
		cleavegeEt     = (EditText) findViewById(R.id.mineralCleavegeEt);
		grainFormSpn = (Spinner) findViewById(R.id.grainFormSpn);
		grainShapeSpn = (Spinner) findViewById(R.id.grainShapeSpn);
        ArrayAdapter<String> grainFormsAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line, grainForms);
        ArrayAdapter<String> grainShapesAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line, grainShapes);
        grainFormSpn.setAdapter(grainFormsAdapter);
        grainShapeSpn.setAdapter(grainShapesAdapter);
    	mineralNameEt = (EditText) findViewById(R.id.mineralNameEt);
    	
        nextBtn = (ImageButton) findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(this);
		
		addMineralsBtn = (Button) findViewById(R.id.addMineralsBtn);
		addMineralsBtn.setOnClickListener(this);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.igneous_mineral, menu);
		return true;
	}
	
	public void collectMineralDetails () {
		Mineral  mineral = (Mineral) new Mineral();
		if (currProject.minerals == null) {
			currProject.minerals = new ArrayList<Mineral>();
		}
		mineral.mineralName   = mineralNameEt.getText().toString();  
		mineral.minGrainSize  = Float.valueOf(minGrainSizeEt.getText().toString());  
		mineral.maxGrainSize  = Float.valueOf(maxGrainSizeEt.getText().toString());
		mineral.composition   = Float.valueOf(compositionEt.getText().toString());
		mineral.mineralCleavege = cleavegeEt.getText().toString();

		if (grainForms[grainFormSpn.getSelectedItemPosition()].equalsIgnoreCase("Anhedral")) {
			mineral.grainForm = GrainForm.ANHEDRAL; 
		}
		else if (grainForms[grainFormSpn.getSelectedItemPosition()].equalsIgnoreCase("Subhedral")) { 
			mineral.grainForm = GrainForm.SUBHEDRAL; 
		}
		else if (grainForms[grainFormSpn.getSelectedItemPosition()].equalsIgnoreCase("Euhedral")) { 
			mineral.grainForm = GrainForm.EUHEDRAL; 
		}
		
		if (grainShapes[grainShapeSpn.getSelectedItemPosition()].equalsIgnoreCase("Equant")) {
			mineral.grainShape = GrainShape.EQUANT;
		}
		else if (grainShapes[grainShapeSpn.getSelectedItemPosition()].equalsIgnoreCase("Interstital")) {
			mineral.grainShape = GrainShape.INTER_TERRESTRIAL; 
		}
		else if (grainShapes[grainShapeSpn.getSelectedItemPosition()].equalsIgnoreCase("Irregular")) {
			mineral.grainShape = GrainShape.INTER_TERRESTRIAL; 
		}
		currProject.minerals.add(mineral);
		Log.e("Add Minerals", "Done");
		for (int i = 0; i < currProject.minerals.size(); i++) {
			Mineral m = currProject.minerals.get(i);
			Log.e("Mineral Details ", " " + m.mineralName + 
					                  " " + String.valueOf(m.minGrainSize) +
					                  " " + String.valueOf(m.maxGrainSize) +
					                  " " + String.valueOf(m.composition) +
					                  " " + m.mineralCleavege +
					                  " " + String.valueOf(m.grainForm) +
					                  " " + String.valueOf(m.grainShape));			
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.nextBtn:
				collectMineralDetails ();
				invokeNextActivity(IgneousRockInfo.class, START_IGNEOUS_INFO_DETAILS);
				break;
			case R.id.addMineralsBtn:
				collectMineralDetails ();
				invokeNextActivity(IgneousMineral.class, START_ADD_MINERALS_ACTIVITY);
				break;
		}
	}
}

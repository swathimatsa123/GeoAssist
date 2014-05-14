package com.geoassist;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;

public class FoldMain extends BaseActionBarActivity implements OnClickListener{
	ImageButton doneBtn;
	ImageButton cancelBtn;
	final String[] foldTypes    = {"Select Type",  "Anticline","Syncline","Neutral"};
	final String[] foldTighness = {"Select Type",  "isoclinal (<10)", "tight (10-30)", "closed (30-70)",
									"gentle (> 120)", "open   (70-120)"};

	final String[]  foldHinge = {"Select Type", "Rounded", "Angular", "Very angular", "Chevron", "Kink", "Box"};
	final String[]  foldLimbs = {"Select Type", "Planar", "Curved"};
	final String[]  secondOrderFold = {"Select Type", "Z-Fold", "M-Fold","S-Fold"};
	final String[]  foldSymmetry = {"Select Type", "Symmetric", "Non-Symmetric"};
	final String[]  foldClass = {"Select Type", "Class-1A", "Class 1B", "Class 1C", "Class 2","Class 3"};
	final String[]  ampUnits  = {"Select Type", "mm", "cm"};
	Spinner 	foldTypeSpn;
	Spinner 	tightNessSpn;
	Spinner 	hingeSpn;
	Spinner 	limbsSpn;
	Spinner 	secondOrderSpn;
	Spinner 	symmetrySpn;
	Spinner 	lambdaUnitsSpn;
	Spinner 	ampUnitsSpn;
	Spinner 	foldClassSpn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fold_fist_view);
		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(R.layout.fold_menu);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		doneBtn = (ImageButton) actionBar.getCustomView().findViewById(R.id.done);
		doneBtn.setOnClickListener(this);
	    doneBtn.getRootView().setBackgroundColor(0xFFF0FFF0);
		cancelBtn = (ImageButton) actionBar.getCustomView().findViewById(R.id.cancel);
		cancelBtn.setOnClickListener(this);
		doneBtn.getRootView().setBackgroundColor(0xFFF0FFF0);
		
		foldTypeSpn    = setSpinner(R.id.foldTypeSpn, foldTypes);
		tightNessSpn   = setSpinner(R.id.tightNessSpn, foldTighness);
		hingeSpn       = setSpinner(R.id.hingeSpn, foldHinge);
		limbsSpn       = setSpinner(R.id.limbsSpn, foldLimbs);
		secondOrderSpn = setSpinner(R.id.secondOrderSpn, secondOrderFold);
		symmetrySpn    = setSpinner(R.id.symmetrySpn, foldSymmetry);
		lambdaUnitsSpn = setSpinner(R.id.lambdaUnits, ampUnits);
		ampUnitsSpn    = setSpinner(R.id.ampUnits, ampUnits);
		foldClassSpn    = setSpinner(R.id.ramsayClassSpn, foldClass);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.done:
				WorkingProject proj = WorkingProject.getInstance();
				SaveFoldDetails(proj);
				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				break;
			case R.id.cancel:
				finish();
				break;
		}
	}
	public void SaveFoldDetails(WorkingProject proj ) {
		Site site = proj.sites.get(proj.sites.size()-1);
		site.foldType =   foldTypes[(int) foldTypeSpn.getSelectedItemId()];
		site.foldTighness =  foldTighness[(int) tightNessSpn.getSelectedItemId()];
		site.foldHinge =  foldHinge[(int) hingeSpn.getSelectedItemId()];
		site.foldLimbs =  foldLimbs[(int) limbsSpn.getSelectedItemId()];
		site.foldSymmetry =  foldSymmetry[(int) symmetrySpn.getSelectedItemId()];
		site.fold2nOrder=  secondOrderFold[(int) secondOrderSpn.getSelectedItemId()];
		site.foldRamsaysCls = getTextFromSpinner(R.id.ramsayClassSpn);
		site.foldLambda = getTextFromEt(R.id.lambdaEt) + getTextFromSpinner(R.id.lambdaUnits);
		site.foldAmplitude = getTextFromEt(R.id.amplitudeEt) + getTextFromSpinner(R.id.ampUnits);
	}

}

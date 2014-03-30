package com.geoassist;
import com.geoassist.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class GrainDetails extends BaseActivity implements OnClickListener{
	static final int SEDIMENT_STRUCTURES_ACTIVITY = 100;
	ImageButton nextBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grain_details);
		nextBtn = (ImageButton) findViewById(R.id.nextBtn);
		nextBtn .setOnClickListener(this);

	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.nextBtn:
				// Collect the details that user has entered.
				collectDetails();
				invokeNextActivity(SedimentStrucutres.class, SEDIMENT_STRUCTURES_ACTIVITY);
				break;
		}
	}
	
	public void collectDetails() {
	}
}

package com.geoassist;
import com.geoassist.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class NotesActivity extends BaseActivity implements OnClickListener{

	ImageButton nextBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);
		nextBtn = (ImageButton) findViewById(R.id.mineralDone);
		nextBtn .setOnClickListener(this);
		nextBtn.getRootView().setBackgroundColor(0xFFF0FFF0);

	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.mineralDone:
				// Collect the details that user has entered.
				collectDetails();
				break;
		}
	}
	
	public void collectDetails() {
	}
}


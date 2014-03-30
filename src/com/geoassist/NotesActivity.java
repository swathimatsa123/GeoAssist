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
		nextBtn = (ImageButton) findViewById(R.id.nextBtn);
		nextBtn .setOnClickListener(this);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.nextBtn:
				// Collect the details that user has entered.
				collectDetails();
				break;
		}
	}
	
	public void collectDetails() {
	}
}


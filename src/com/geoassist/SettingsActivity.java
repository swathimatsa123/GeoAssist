package com.geoassist;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.geoassist.data.User;
public class SettingsActivity extends BaseActionBarActivity implements OnClickListener{
	EditText   usrNameEt;
	EditText   emailToEt;
	EditText   emailCcEt;
	SharedPreferences preferences;
	ImageButton		doneBtn;
	ImageButton		cancelBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	    ActionBar actionBar = getSupportActionBar();
	    // add the custom view to the action bar
	    actionBar.setCustomView(R.layout.settings_menu);
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

	    doneBtn  = (ImageButton)actionBar.getCustomView().findViewById(R.id.done);
	    doneBtn.setOnClickListener(this);
	    doneBtn.getRootView().setBackgroundColor(0xFFF0FFF0);

	    cancelBtn  = (ImageButton)actionBar.getCustomView().findViewById(R.id.cancel);
	    cancelBtn.setOnClickListener(this);

		usrNameEt = (EditText) findViewById(R.id.settingUsrNameEt);
		emailToEt = (EditText) findViewById(R.id.emailToEt);
		emailCcEt = (EditText) findViewById(R.id.emailccEt);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String usrName  = preferences.getString("username", "");
		usrNameEt.setText(usrName);
		String emailTo  = preferences.getString("emailTo", "");
		emailToEt.setText(emailTo);
		String emailCc  = preferences.getString("emailCc", "");
		emailCcEt.setText(emailCc);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.done:
				Editor edit = preferences.edit();
				edit.putString("username", usrNameEt.getText().toString());
				edit.putString("emailTo", emailToEt.getText().toString());
				edit.putString("emailCc", emailCcEt.getText().toString());
				edit.commit();
				User usr = User.getInstance();
				usr.setDetails( usrNameEt.getText().toString(), 
								emailToEt.getText().toString(), 
								emailCcEt.getText().toString());
				
				finish();
				break;
			case R.id.cancel:
				finish();
				break;
		}
	}

}

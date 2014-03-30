package com.geoassist;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
public class SettingsActivity extends Activity {
	EditText   usrNameEt;
	EditText   emailToEt;
	EditText   emailCcEt;
	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		usrNameEt = (EditText) findViewById(R.id.settingUsrNameEt);
		emailToEt = (EditText) findViewById(R.id.emailToEt);
		emailCcEt = (EditText) findViewById(R.id.emailccEt);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.ic_done) {
			Editor edit = preferences.edit();
			edit.putString("username", usrNameEt.getText().toString());
			edit.putString("emailTo", emailToEt.getText().toString());
			edit.putString("emailCc", emailCcEt.getText().toString());
			edit.commit(); 
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

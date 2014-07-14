package com.geoassist;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class StrikeDipFragment  extends Fragment implements OnClickListener{
	public StrikeDip strdip = null; 
	public EditText strike = null;
	public EditText dip = null;
	public ImageButton dipDone = null;
	public ImageButton strikeDone = null;		
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.strike_dip_frg,
        container, false);
	strike = (EditText)view.findViewById(R.id.strikeEt);
	dip = (EditText)view.findViewById(R.id.dipEt);
	strikeDone = (ImageButton)view.findViewById(R.id.strRecordBtn);
	dipDone = (ImageButton)view.findViewById(R.id.dipRecordBtn);
	if (strikeDone != null) {
		strikeDone.setOnClickListener(this);
	}
	if (dipDone != null) {
		dipDone.setOnClickListener(this);
	}

	return view;
  }
  
  public void updateReadings (String strikeVal, String dipVal) {
	  if (this.strike !=null) this.strike.setText(strikeVal);
	  if (this.dip !=null) this.dip.setText(dipVal);
  }
  

  @Override
public void onActivityCreated(Bundle savedInstanceState) {
	  super.onActivityCreated(savedInstanceState);
	  if (strike !=null) strike.setText("Fragment Strike");
	  if (dip !=null) dip.setText("Fragment Dip");
	  strdip = new StrikeDip(getActivity(), this);
	  strdip.start();
  }



//@Override
//public void onResume() {
//	// TODO Auto-generated method stub
//	super.onResume();
//	EditText strike = (EditText)getActivity().findViewById(R.id.strikeEt);
//	if (strike !=null) strike.setText("Fragment Strike");
//	EditText dip = (EditText)getActivity().findViewById(R.id.dipEt);
//	if (dip !=null) dip.setText("Fragment Dip");
//}

@Override
public void onClick(View v) {
	switch( v.getId()) {
	case R.id.dipRecordBtn:
	case R.id.strRecordBtn:
		strdip.stop();
		break;
	}
}
} 
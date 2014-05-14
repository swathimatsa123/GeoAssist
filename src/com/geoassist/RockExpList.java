package com.geoassist;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.geoassist.data.Site;
import com.geoassist.data.WorkingProject;


public class RockExpList extends BaseExpandableListAdapter implements OnItemSelectedListener{


	Spinner rockNameSpn;
	final String[] rockNames = { "Select", "Granite", "Granodiorite",
			"Diorite", "Gabbro", "Rhyolite", "Andersite", "Basalt", "Other" };

	final String[] sedRockTypes= { "Select",
			"Breccia",
			"Conglomerate",
			"Quartz sandstone",
			"Arkose sandstone",
			"Lithic sandstone",
			"Graywacke",
			"Siltstone",
			"Shale",
			"Claystone",
			"Chert",
			"Dolostone",
			"Travertine",
			"Oolitic limestone",
			"Limestone",
			"Micrite",
			"Chalk",
			"Fossiliferous limestone",
			"Coquina",
			"Bituminous coal",
			"Lignite",
			"Coal"};

	final String [] sedDia = {"Select",
			"> 0.004 mm",
			"0.004 to 0.062 mm",
			"0.062 to 0.125 mm",
			"0.125 to 0.250 mm",
			"0.250 to 0.50 mm",
			"0.50 to 1.0 mm",
			"1.0 to 2.0 mm",
			"2.0 to 4.0 mm",
			"4.0 to 8.0 mm",
			"8.0 to 16.0 mm",
			"16.0 to 32.0 mm",
			"32.0 to 64.0 mm",
			"64.0 to 128 mm",
			"128 to 256 mm",
			"256 to 512 mm",
			"512 to 1024 mm",
			"1024 to 2048 mm",
			"2048 to 4096 mm"};
	final String [] grainsPhi = {"Select",
			"8 to 12",
			"4 to 8",
			"3 to 4",
			"2 to 3",
			"1 to 2",
			"0 to 1",
			"-1 to 0",
			"-2 to -1",
			"-3 to -2",
			"-4 to -3",
			"-5 to -4",
			"-6 to -5",
			"-7 to -6",
			"-8 to -7",
			"-9 to -8",
			"-10 tp -9",
			"-11 to -10",
			"-12 to -11"};
	final String [] grainGrade ={"Select" , 
			"Clay",
			"Silt",
			"Very fine sand",
			"Fine sand",
			"Medium sand",
			"Coarse sand",
			"Very coarse sand",
			"Very fine pebbles",
			"Fine pebbles",
			"Medium pebbles",
			"Coarse pebbles",
			"Very coarse pebbles",
			"Small cobbles",
			"Large cobbles",
			"Small boulders",
			"Medium boulders",
			"Large boulders",
			"Very large boulders"};
	final String[] grainSortings = {"Select",
			"Very well sorted",
			"Well sorted",
			"Moderately sorted",
			"Poorly sorted",
			"Very poorly sorted"};
			
	final String[] grainRounding = {"Select",
			"Very angular",
			"Angular",
			"Sub-angular",
			"Sub-rounded",
			"Rounded",
			"Well-rounded"};
	final String[] sedBedding = {	"Select",
				"Parallel lamination",
				"Graded bedding",
				"Reverse graded bedding",
				"Imbrication",
				"Lenticular",
				"Trough",
				"Non-parallel planar"};

	final String[] sedCrossBedding = {	
			"Select",
			"Angular cross-lamination",
			"Parablolic cross-lamination",
			"Sinusoidal cross-lamination",
			"Wedge sets",
			"Trough (festoon) crossbeds",
			"Hummocky crossbeds",
			"Herringbone crossbeds"
	};
	final String[] sedRipples = {	"Select",
			"Linguoid current ripples",
			"Transverse current ripples",
			"Oscillation (wave) ripples"};
	final String[] sedSoleMarks = {"Select",
									"Flute casts",
									"Groove casts "};
	final String[] sedSSD= {
			"Select",
			"Convolute lamination",
			"Flame structures",
			"Slump structures",
			"Load casts",};

	final String[] sedFossils= {"Select",
			"Algal mats",
			"Ammonites",
			"Belemnites",
			"Brachiopods",
			"Bryozoans",
			"Corals, solitary",
			"Corals, colonial",
			"Crinoids",
			"Echinoderms",
			"Echinoids",
			"Fish bones",
			"Fish scales",
			"Foraminifers",
			"Gastropods",
			"Graptolites",
			"Leaves",
			"Ostracodes",
			"Pelecypods",
			"Roots",
			"Stromatolites",
			"Tree trunk",
			"Trilobites",
			"Vertebrate",
			"Wood",
			"(other)"};
	final String[] sedDepEnv= {"Select",
			"Alluvial",
			"Basin plain",
			"Beach",
			"Continental slope",
			"Deltaic",
			"Deep-sea fan",
			"Dysaerobic",
			"Eolian",
			"Estuarine",
			"Glacial",
			"Hillslope",
			"Intertidal",
			"Lacustrine",
			"Periglacial",
			"Reefal",
			"Supratidal",
			"Shelf",
			"Tidal",
			"(other)"};

	final String[] metRocks= {"Select",
			"Slate",
			"Phyllite",
			"Schist",
			"Gneiss",
			"Migmatite",
			"Amphibolite",
			"Eclogite",
			"Hornfels",
			"Serpentinite",
			"Quartzite",
			"Marble",
			"Granofels",
			"Scarn",
			"Mylonite",
			"Fault breccia",
			"Fault gauge",
			"Meta-conglomerate",
			"Blueschist"};
	public LayoutInflater minflater;
	public Activity activity;
	public ArrayList<String> groupItem, tempChild;
	public ArrayList<Object> Childtem = new ArrayList<Object>();


	public void setInflater(LayoutInflater mInflater, Activity act) {
		this.minflater = mInflater;
		this.activity = act;
	}

	public RockExpList(ArrayList<String> grList, ArrayList<Object> childItem) {
		this.groupItem = grList;
		this.Childtem = childItem;
	}

	@Override
	public View getChildView(final int 		groupPosition, 
							final int 	childPosition,
							boolean 	isLastChild, 
							View 		convertView, 
							ViewGroup 	parent) {
		int viewSrc = 0;
		switch (groupPosition) {
		case 0:
			int rockType = ((RockDetails)this.activity).getViewFileForRock();
			ArrayAdapter<String> rockAdapter = null;
			switch (rockType) {
			case R.layout.sed_info:
				rockAdapter = new ArrayAdapter<String>(this.activity, 
								android.R.layout.simple_dropdown_item_1line,
								sedRockTypes);
				
				break;
			case R.layout.dike_info:
				rockAdapter = new ArrayAdapter<String>(this.activity, 
								android.R.layout.simple_dropdown_item_1line,
								rockNames);
				break;
			case R.layout.met_dtls_add:
				rockAdapter = new ArrayAdapter<String>(this.activity, 
						android.R.layout.simple_dropdown_item_1line,
						metRocks);
				break;

			default:
				rockAdapter = new ArrayAdapter<String>(this.activity, 
						android.R.layout.simple_dropdown_item_1line,
						metRocks);
				break;
			}
			viewSrc = R.layout.rock_l1_detail;
			convertView = minflater.inflate(viewSrc, null);
			ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
			rockNameSpn  = (Spinner)  convertView.findViewById(R.id.rockTypeSpinner);
			rockNameSpn.setAdapter(rockAdapter);
			rockNameSpn.setOnItemSelectedListener(this);
			doneBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					RockDetails parent = (RockDetails)RockExpList.this.activity;
					parent.collectDetails(groupPosition);
				}
			});
			break;
		case 1:
			viewSrc = ((RockDetails)this.activity).getViewFileForRock();
			convertView = setupDetailsInfoView(viewSrc, groupPosition);
			break;
		case 2:
			convertView = setupNotesView(groupPosition);
			break;
			
		default:
			viewSrc = R.layout.info_detail;
			convertView = minflater.inflate(viewSrc, null);
			break;
		}
		convertView.setBackgroundColor(0xFFF0FFF0);
		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.info_group, null);
		}
		((CheckedTextView) convertView).setText(groupItem.get(groupPosition));
		((CheckedTextView) convertView).setChecked(isExpanded);
		return convertView;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		Log.e("Spinner Select ", "Value is "+ String.valueOf(pos));
		if (view.getId() ==  R.id.rockTypeSpinner){
			WorkingProject proj = WorkingProject.getInstance();
			Site site = proj.sites.get(proj.sites.size()-1);
			site.rockType= rockNames[pos];
		}
	}

	public View setupDikeInfoView ( final int groupPosition) {
		View convertView = minflater.inflate(R.layout.dike_info, null);
		ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RockDetails parent = (RockDetails)RockExpList.this.activity;
				Log.e("Done Button", "Called");
				parent.collectDetails(groupPosition);
			}
		});
		return convertView;
	}

	public View setupDetailsInfoView ( int viewSrc, final int groupPosition) {
		View convertView = minflater.inflate(viewSrc, null);
		setSpinner (R.id.grainDia, convertView, sedDia);
		setSpinner (R.id.grainPhi, convertView, grainsPhi);
		setSpinner (R.id.grainNameSpn, convertView, grainGrade);
		setSpinner (R.id.grainSortSpn, convertView,  grainSortings);
		setSpinner (R.id.sedBeddingSpn, convertView,  sedBedding);
		setSpinner (R.id.grainRoundSpn, convertView, grainRounding);
		setSpinner (R.id.crossBeddingSpn, convertView, sedCrossBedding);
		setSpinner (R.id.rippleSpn, convertView, sedRipples);
		setSpinner (R.id.soleMarksSpn, convertView, sedSoleMarks);
		setSpinner (R.id.sedSsdSpn, convertView, sedSSD);
		setSpinner (R.id.sedFossilsSpn, convertView, sedFossils);
		setSpinner (R.id.sedDepSpn, convertView, sedDepEnv);		
		ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
		if (doneBtn!= null) {
			doneBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					RockDetails parent = (RockDetails)RockExpList.this.activity;
					Log.e("Done Button", "Called");
					parent.collectDetails(groupPosition);
				}
			});
		}
		return convertView;
	}
	public void setSpinner (int id, View v, final String[] types) {
		Spinner spn = (Spinner)  v.findViewById(id);
		if (spn != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.activity, 
										android.R.layout.simple_dropdown_item_1line,
										types);
	
			spn.setAdapter(adapter);			
		}
	}
	
	public View setupNotesView ( final int groupPosition) {
		View convertView = minflater.inflate(R.layout.notes, null);
		ImageButton doneBtn = (ImageButton) convertView.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RockDetails parent = (RockDetails)RockExpList.this.activity;
				Log.e("Done Button", "Called");
				parent.collectDetails(groupPosition);
			}
		});
		return convertView;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getGroupCount() {
		return groupItem.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<String>) Childtem.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
}

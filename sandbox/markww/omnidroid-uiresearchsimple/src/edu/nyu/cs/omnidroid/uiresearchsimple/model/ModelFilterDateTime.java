package edu.nyu.cs.omnidroid.uiresearchsimple.model;

import edu.nyu.cs.omnidroid.uiresearchsimple.R;

/**
 * Dummy representation of a date/time filter. We'll modify
 * this to match the database model.
 */
public class ModelFilterDateTime extends ModelFilter
{
	private String mDateTimeRange;
	
	
	public ModelFilterDateTime(String dateTimeRange) {
		super("Date/Time");
		mDateTimeRange = dateTimeRange;
	}
	
	public ModelDisplayInfo getDisplayInfo() {
		return new ModelDisplayInfo(R.drawable.icon_filter_datetime, getTypeName() + ": " + mDateTimeRange, "Date/time filtering desc...");
	}	
	
	public String getDlgClassName() {
		return "edu.nyu.cs.omnidroid.uiresearchsimple.dlg.DlgFilterDateTime";
	}
	
	public String getDateTimeRange() {
		return mDateTimeRange;
	}
}
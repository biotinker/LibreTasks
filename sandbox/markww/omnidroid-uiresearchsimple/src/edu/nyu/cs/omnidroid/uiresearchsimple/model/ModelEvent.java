package edu.nyu.cs.omnidroid.uiresearchsimple.model;

/**
 * Dummy representation of an Event. We'll modify
 * this to match the database model.
 */
public class ModelEvent extends ModelItem
{
	private int mIconResId;
	private String mDescription;
	
	
	public ModelEvent(String typeName, int iconResId, String description) {
		super(typeName);
		mIconResId = iconResId;
		mDescription = description;
	}
	
	public ModelDisplayInfo getDisplayInfo() {
		return new ModelDisplayInfo(mIconResId, getTypeName(), mDescription);
	}
	
	public String getDlgClassName() {
		return "undefined (for now)";
	}
}
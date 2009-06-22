package edu.nyu.cs.omnidroid.uiresearchsimple.model;


/**
 * All model items derived from <code>ModelItem</code> should
 * have a method to produce an instance of this class so the UI
 * system knows how to render it to screen.
 */
public class ModelDisplayInfo
{
	private int mIconResId;
	private String mTitle;
	private String mDescription;
	
	public ModelDisplayInfo(int iconResId, String title, String description)
	{
		mIconResId = iconResId;
		mTitle = title;
		mDescription = description;
	}
	
	public int getIconResId() {
		return mIconResId;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public String getDescription() {
		return mDescription;
	}
}
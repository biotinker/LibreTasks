package edu.nyu.cs.omnidroid.uiresearchsimple.model;

import edu.nyu.cs.omnidroid.uiresearchsimple.R;


/**
 * Dummy representation of a contact filter. We'll modify
 * this to match the database model.
 */
public class ModelFilterContact extends ModelFilter
{
	private String mContactName;
	private String mContactId;
	// etc.
	
	public ModelFilterContact(String contactName)
	{
		super("Contact");
		mContactName = contactName;
	}
	
	public ModelDisplayInfo getDisplayInfo() {
		return new ModelDisplayInfo(R.drawable.icon_filter_contact, getTypeName() + ": " + mContactName, "Contact filter description...");
	}
	
	public String getDlgClassName() {
		return "edu.nyu.cs.omnidroid.uiresearchsimple.dlg.DlgFilterContact";
	}
	
	public String getContactName() {
		return mContactName;
	}
	
	public String getContactId() {
		return mContactId;
	}
}
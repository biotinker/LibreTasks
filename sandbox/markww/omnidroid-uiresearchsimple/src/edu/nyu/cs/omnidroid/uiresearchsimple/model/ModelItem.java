package edu.nyu.cs.omnidroid.uiresearchsimple.model;


/**
 * Dummy representation of a base class for Event/Filter/Task. 
 * We'll modify this to match the database model.
 */
public abstract class ModelItem
{
	protected String mTypeName;
	
	
	public ModelItem(String typeName) {	
		mTypeName = typeName;
	}
	
	public String getTypeName() {
		return mTypeName;
	}
	
	/**
	 * Items need to be able to generate some representation of
	 * themselves when in the UI. They should be able to return
	 * a description for this purpose.
	 * @return  Representation used for rendering in the UI.
	 */
	public abstract ModelDisplayInfo getDisplayInfo();
	
	/**
	 * Return the full class name for the dialog that should be
	 * used to capture information about us. For instance, a
	 * <code>FilterContact</code> class would return:
	 *    "edu.nyu.cs.omnidroid.uiresearchsimple.dlg.DlgFilterContact"
	 * @return Full name of dialog class used to capture information about us.
	 */
	public abstract String getDlgClassName();
}
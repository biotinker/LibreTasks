package edu.nyu.cs.omnidroid.ui.simple;

import android.content.Context;
import edu.nyu.cs.omnidroid.model.UIDbHelper;

/**
 * This singleton wraps <code>UIDbHelper</code> to provide a single 
 * static instance of the class. This can be done directly in the
 * UIDbHelper class if necessary.
 */
public class DbInterfaceUI {
	
	private static DbInterfaceUI mInstance;
	private UIDbHelper mHelper;
	
	/** We need to know all the filter IDs as they'll appear in
	 *  the database for FactoryFilterToUI to work.
	 */
	public static final int FILTER_ID_TEXT_EQUALS        = 0;
	public static final int FILTER_ID_TEXT_CONTAINS      = 1;
	public static final int FILTER_ID_PHONENUMBER_EQUALS = 2;
	public static final int FILTER_ID_DATE_BEFORE        = 3;
	public static final int FILTER_ID_DATE_AFTER         = 4;
	
	
	private DbInterfaceUI(Context context) {
		mHelper = new UIDbHelper(context);
	}
	
	public static void init(Context context) {
		if (mInstance == null) {
			mInstance = new DbInterfaceUI(context);
		}
	}
	
	public static DbInterfaceUI instance() {
		return mInstance;
	}
	
	public UIDbHelper db() {
		return mHelper;
	}
}
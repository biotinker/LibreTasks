package edu.nyu.cs.omnidroid.ui.simple;

import android.content.Context;
import edu.nyu.cs.omnidroid.model.FilterIDLookup;
import edu.nyu.cs.omnidroid.model.UIDbHelper;

/**
 * This singleton wraps <code>UIDbHelper</code> to provide a single 
 * static instance of the class. This can be done directly in the
 * UIDbHelper class if necessary.
 */
public class DbInterfaceUI {
	
	private static DbInterfaceUI mInstance;
	private UIDbHelper mHelper;
	private FilterIDLookup mFilterLookup;
	
	
	private DbInterfaceUI(Context context) {
		mHelper = new UIDbHelper(context);
		mFilterLookup = new FilterIDLookup(context);
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
	
	public FilterIDLookup getFilterLookup() {
		return mFilterLookup;
	}
}
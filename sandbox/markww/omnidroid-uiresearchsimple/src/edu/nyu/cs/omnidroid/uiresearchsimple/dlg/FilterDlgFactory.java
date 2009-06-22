package edu.nyu.cs.omnidroid.uiresearchsimple.dlg;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelItem;


/**
 * Allows us to create dialogs linked to their specific <code>ModelFilter</code>
 * type just by name (which will probably be stored in the database), or from
 * an existing instance of a <code>ModelFilter</code> to signify an editing job.
 */
public class FilterDlgFactory
{
	private FilterDlgFactory()
	{
	}
	
	public static Dialog getDialogForFilter(Context context, 
											String className)
	{
		try {
			Class<?> c = Class.forName(className);
			return (Dialog)c.getConstructor(Context.class).newInstance(context);
		}
		catch (Exception ex) {
			Log.e("FilterDlgFactory", "Error creating dialog for filter input by name [" + className + "]:", ex);
			throw new RuntimeException("Error creating dialog for filter by name: [" + className + "] in FilterDlgFactor.java.");
		}
	}
	
	public static Dialog getDialogForFilter(Context context, 
			                                ModelItem item)
	{
		try {
			Class<?> c = Class.forName(item.getDlgClassName());
			return (Dialog)c.getConstructor(Context.class, ModelItem.class).newInstance(context, item);
		}
		catch (Exception ex) {
			Log.e("FilterDlgFactory", "Error creating dialog for filter input by name [" + item.getDlgClassName() + "]:", ex);
			throw new RuntimeException("Error creating dialog for filter by name: [" + item.getClass().getName() + "] in FilterDlgFactor.java.");
		}
	}
}
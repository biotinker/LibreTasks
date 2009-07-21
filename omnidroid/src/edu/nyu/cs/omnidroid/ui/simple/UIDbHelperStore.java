package edu.nyu.cs.omnidroid.ui.simple;

import android.content.Context;
import edu.nyu.cs.omnidroid.model.DataFilterIDLookup;
import edu.nyu.cs.omnidroid.model.UIDbHelper;

/**
 * This singleton was created to hold an instance of the <code>UIDbHelper</code> class which
 * provides the UI package with representations of the core data. It needs to be initialized with a
 * <code>Context</code> instance, thus it is not a collection of static methods. The functionality
 * of this class could be merged directly into UIDbHelper, it would simply act as the singleton
 * instance itself. TODO: (markww) Discuss merging this directly into UIDbHelper.
 */
public class UIDbHelperStore {

  /** The one and only DbInterfaceUI instance. */
  private static UIDbHelperStore instance;

  /** Points to our internal db helper instance. */
  private UIDbHelper dbInstance;

  /** Looks up filters by IDs. */
  private DataFilterIDLookup filterLookup;

  private UIDbHelperStore(Context context) {
    dbInstance = new UIDbHelper(context);
    filterLookup = new DataFilterIDLookup(context);
  }

  public static void init(Context context) {
    if (instance == null) {
      instance = new UIDbHelperStore(context);
    }
  }

  public static UIDbHelperStore instance() {
    // I could add Context as a parameter here, but then every caller needs to pass it in (or null
    // if they don't have a context which will happen like in the case of the UI factory class).
    if (instance == null) {
      throw new IllegalStateException(
          "UIDbHelperStore singleton must be initialized via init() before use!");
    }
    return instance;
  }

  public UIDbHelper db() {
    return dbInstance;
  }

  public DataFilterIDLookup getFilterLookup() {
    return filterLookup;
  }
}
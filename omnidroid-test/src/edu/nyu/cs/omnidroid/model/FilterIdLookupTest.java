package edu.nyu.cs.omnidroid.model;

import edu.nyu.cs.omnidroid.model.db.DataFilterDbAdapter;
import edu.nyu.cs.omnidroid.model.db.DataTypeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.DbHelper;
import android.test.AndroidTestCase;

public class FilterIdLookupTest extends AndroidTestCase {

  private FilterIDLookup filterIDLookup;
  private DataTypeDbAdapter dataTypeDbAdapter;
  private DataFilterDbAdapter dataFilterDbAdapter;
  private DbHelper omnidroidDbHelper;

  // Data for testing
  private static String[] dataTypeNames = { "Type1", "Type2" };
  private Long[] dataTypeIDs;
  private static String[] dataFilterNames = { "Filter1", "Filter2", "Filter3" };
  private Long[] dataFilterIDs;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    omnidroidDbHelper = new DbHelper(this.getContext());
    filterIDLookup = new FilterIDLookup(this.getContext());

    dataTypeDbAdapter = new DataTypeDbAdapter(omnidroidDbHelper.getWritableDatabase());
    dataFilterDbAdapter = new DataFilterDbAdapter(omnidroidDbHelper.getWritableDatabase());

    omnidroidDbHelper.backup();

    dataTypeDbAdapter.deleteAll();
    dataFilterDbAdapter.deleteAll();
  }

  @Override
  protected void tearDown() throws Exception {
    dataTypeIDs = null;
    dataFilterIDs = null;
    dataTypeDbAdapter.deleteAll();
    dataFilterDbAdapter.deleteAll();

    // Try to restore the database
    if (omnidroidDbHelper.isBackedUp()) {
      omnidroidDbHelper.restore();
    }

    omnidroidDbHelper.close();
    super.tearDown();
  }

  private void prePopulate1() {
    dataTypeIDs = new Long[2];
    dataFilterIDs = new Long[2 * 3];

    dataTypeIDs[0] = dataTypeDbAdapter.insert(dataTypeNames[0], "");
    dataTypeIDs[1] = dataTypeDbAdapter.insert(dataTypeNames[1], "");

    dataFilterIDs[0] = dataFilterDbAdapter.insert(dataFilterNames[0], dataTypeIDs[0]);
    dataFilterIDs[1] = dataFilterDbAdapter.insert(dataFilterNames[1], dataTypeIDs[0]);
    dataFilterIDs[2] = dataFilterDbAdapter.insert(dataFilterNames[2], dataTypeIDs[0]);

    dataFilterIDs[3] = dataFilterDbAdapter.insert(dataFilterNames[0], dataTypeIDs[1]);
    dataFilterIDs[4] = dataFilterDbAdapter.insert(dataFilterNames[1], dataTypeIDs[1]);
    dataFilterIDs[5] = dataFilterDbAdapter.insert(dataFilterNames[2], dataTypeIDs[1]);
  }

  public void testGetFilterID() {
    prePopulate1();
    assertEquals(dataFilterIDs[0].longValue(), filterIDLookup.getFilterID(dataTypeNames[0],
        dataFilterNames[0]));
    assertEquals(dataFilterIDs[1].longValue(), filterIDLookup.getFilterID(dataTypeNames[0],
        dataFilterNames[1]));
    assertEquals(dataFilterIDs[2].longValue(), filterIDLookup.getFilterID(dataTypeNames[0],
        dataFilterNames[2]));
    
    assertEquals(dataFilterIDs[3].longValue(), filterIDLookup.getFilterID(dataTypeNames[1],
        dataFilterNames[0]));
    assertEquals(dataFilterIDs[4].longValue(), filterIDLookup.getFilterID(dataTypeNames[1],
        dataFilterNames[1]));
    assertEquals(dataFilterIDs[5].longValue(), filterIDLookup.getFilterID(dataTypeNames[1],
        dataFilterNames[2]));
  }
  
  public void testGetFilterID_cache() {
    prePopulate1();
    for (int i=0;i<1000;i++){
      assertEquals(dataFilterIDs[0].longValue(), filterIDLookup.getFilterID(dataTypeNames[0],
          dataFilterNames[0]));
      assertEquals(dataFilterIDs[1].longValue(), filterIDLookup.getFilterID(dataTypeNames[0],
          dataFilterNames[1]));
      assertEquals(dataFilterIDs[2].longValue(), filterIDLookup.getFilterID(dataTypeNames[0],
          dataFilterNames[2]));
      
      assertEquals(dataFilterIDs[3].longValue(), filterIDLookup.getFilterID(dataTypeNames[1],
          dataFilterNames[0]));
      assertEquals(dataFilterIDs[4].longValue(), filterIDLookup.getFilterID(dataTypeNames[1],
          dataFilterNames[1]));
      assertEquals(dataFilterIDs[5].longValue(), filterIDLookup.getFilterID(dataTypeNames[1],
          dataFilterNames[2]));
    }
  }
}
package edu.nyu.cs.omnidroid.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.nyu.cs.omnidroid.model.db.DataFilterDbAdapter;
import edu.nyu.cs.omnidroid.model.db.DataTypeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.DbHelper;
import edu.nyu.cs.omnidroid.util.DualKeyHashMap;
import edu.nyu.cs.omnidroid.util.DualKeyMap;

public class FilterIDLookup {

  private DataTypeDbAdapter dataTypeDbAdapter;
  private DataFilterDbAdapter dataFilterDbAdapter;
  private DbHelper omnidroidDbHelper;
  private DualKeyMap<String, String, Long> filterIDMap;

  public FilterIDLookup(Context context) {
    omnidroidDbHelper = new DbHelper(context);
    SQLiteDatabase database = omnidroidDbHelper.getWritableDatabase();
    dataTypeDbAdapter = new DataTypeDbAdapter(database);
    dataFilterDbAdapter = new DataFilterDbAdapter(database);
    filterIDMap = new DualKeyHashMap<String, String, Long>();
  }

  public void close() {
    omnidroidDbHelper.close();
  }

  public long getFilterID(String dataTypeName, String dataFilterName) {
    if (dataTypeName == null || dataFilterName == null) {
      throw new IllegalArgumentException("Arguments null.");
    }

    if (filterIDMap.get(dataTypeName, dataFilterName) != null) {
      return filterIDMap.get(dataTypeName, dataFilterName);
    }

    // This id is not cached yet.

    // Try to find dataTypeID
    long dataTypeID = -1;
    Cursor cursor1 = dataTypeDbAdapter.fetchAll(dataTypeName, null);
    if (cursor1.getCount() > 0) {
      cursor1.moveToFirst();
      dataTypeID = cursor1.getLong(cursor1.getColumnIndex(DataTypeDbAdapter.KEY_DATATYPEID));
    }
    cursor1.close();

    // Try to find dataFilterID
    long dataFilterID = -1;
    Cursor cursor2 = dataFilterDbAdapter.fetchAll(dataFilterName, dataTypeID);
    if (cursor2.getCount() > 0) {
      cursor2.moveToFirst();
      dataFilterID = cursor2.getLong(cursor2.getColumnIndex(DataFilterDbAdapter.KEY_DATAFILTERID));
    }
    cursor2.close();
    
    // Cache it if the id is valid
    if(dataFilterID > 0) {
      filterIDMap.put(dataTypeName, dataFilterName, dataFilterID);
    }
    
    return dataFilterID;
  }
}
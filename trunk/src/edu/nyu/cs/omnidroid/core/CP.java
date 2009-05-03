/**
 * 
 */
package edu.nyu.cs.omnidroid.core;

import edu.nyu.cs.omnidroid.util.UGParser;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * @author Rajiv
 * 
 */
public class CP extends ContentProvider {
  public static final String CP_Name = "edu.nyu.cs.omnidroid.core.maincp";
  public static final Uri CONTENT_URI = Uri.parse("content://" + CP_Name + "/CP");
  public static final String _ID = "_id";
  public static final String ACTION_DATA = "a_data";
  public static final String INSTANCE_NAME = "i_name";
  
  private static final int DESC = 1;
  private static final int DESC_ID = 2;
  private static final UriMatcher uriMatcher;
  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI(CP_Name, "CP", DESC);
    uriMatcher.addURI(CP_Name, "CP/#", DESC_ID);
  }

  // DATABASE INITIALIZATION
  private SQLiteDatabase OmniMainDB;
  private static final String DATABASE_NAME = "Omnidroid";
  private static final String DATABASE_TABLE = "Main";
  private static final int DATABASE_VERSION = 2;
  private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE
      + " (_id integer primary key autoincrement, " + "i_name text, a_data text);";

  /**
   * 
   * @author 
   *
   */
  private static class DatabaseHelper extends SQLiteOpenHelper {
    DatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
     * (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(DATABASE_CREATE);
    }

    /*
     * (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO: Don't drop the table, instead convert it to new version.
      Log.d("OmniDroid database", "On Upgrade");
      db.execSQL("DROP TABLE IF EXISTS Main");
      onCreate(db);

    }
  }

  /*
   * (non-Javadoc)
   * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
   */
  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    // TODO Auto-generated method stub
    return 0;
  }
  
  /*
   * (non-Javadoc)
   * @see android.content.ContentProvider#getType(android.net.Uri)
   */
  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
    case DESC:
      return "vnd.android.cursor.dir/vnd.nyu.cs.omnidroid.core.maincp ";
    case DESC_ID:
      return "vnd.android.cursor.item/vnd.nyu.cs.omnidroid.core.maincp ";
    default:
      throw new IllegalArgumentException("Incorrect URI " + uri);
    }

  }

  /*
   * (non-Javadoc)
   * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
   */
  @Override
  public Uri insert(Uri uri, ContentValues values) {
    // TODO:: Allow UI to insert a value
    // Link the module with Andrew's code
    long row = OmniMainDB.insert(DATABASE_TABLE, "", values);
    if (row > 0) {
      Uri _uri = ContentUris.withAppendedId(CONTENT_URI, row);
      getContext().getContentResolver().notifyChange(_uri, null);
      return _uri;
    }
    throw new SQLException("Failed to insert row into " + uri);

  }

  /*
   * (non-Javadoc)
   * @see android.content.ContentProvider#onCreate()
   */
  @Override
  public boolean onCreate() {
    // TODO:: Open the connection to the database
    Context context = getContext();
    DatabaseHelper dbHelper = new DatabaseHelper(context);
    OmniMainDB = dbHelper.getWritableDatabase();
    UGParser ug=new UGParser(getContext());
    ug.write(UGParser.KEY_ENABLE_OMNIDROID, "True");
    ug.delete_all();
    return (OmniMainDB == null) ? false : true;

  }

  /*
   * (non-Javadoc)
   * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
   */
  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {
    // TODO:: Allow UI to retrieve the information

    SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
    sqlBuilder.setTables(DATABASE_TABLE);
    if (uriMatcher.match(uri) == DESC_ID)
      sqlBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));

    Cursor c = sqlBuilder.query(OmniMainDB, projection, selection, selectionArgs, null, null, null);

    c.setNotificationUri(getContext().getContentResolver(), uri);
    return c;
  }

  /*
   * (non-Javadoc)
   * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
   */
  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    // TODO :: Allow UI to update information in the CP
    return 0;
  }

}

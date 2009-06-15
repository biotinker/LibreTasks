/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid 
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 *******************************************************************************/
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
 * Class to create a content provider for the OmniDroid application.
 * 
 */
public class CP extends ContentProvider {
  // Content provider name.
  public static final String CP_Name = "edu.nyu.cs.omnidroid.core.maincp";
  // Content provider location
  public static final Uri CONTENT_URI = Uri.parse("content://" + CP_Name + "/CP");
  // Content provider fields
  public static final String _ID = "_id";
  public static final String ACTION_DATA = "a_data";
  public static final String INSTANCE_NAME = "i_name";

  static UGParser ug;
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
  private static final int DATABASE_VERSION = 5;
  private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE
      + " (_id integer primary key autoincrement, " + "i_name text, a_data text);";

  private static class DatabaseHelper extends SQLiteOpenHelper {
    DatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
      // db.setVersion(DATABASE_VERSION);
      db.execSQL(DATABASE_CREATE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
     * int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.d("OmniDroid database", "On Upgrade");
      ug.deleteAll();
      if (oldVersion < newVersion) {
        // FIXME: Don't drop the table, instead convert it to new version.
        db.execSQL("DROP TABLE IF EXISTS Main");
        onCreate(db);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String,
   * java.lang.String[])
   */
  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    // TODO: Allow delete of content
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
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
   * 
   * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
   */
  @Override
  public Uri insert(Uri uri, ContentValues values) {
    // Insert row into the DB
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
   * 
   * @see android.content.ContentProvider#onCreate()
   */
  @Override
  public boolean onCreate() {
    // Get our application context
    Context context = getContext();

    // Set this so the DB can access the UserConfig if necessary
    ug = new UGParser(context);

    // Connect to the DB
    DatabaseHelper dbHelper = new DatabaseHelper(context);
    OmniMainDB = dbHelper.getWritableDatabase();
    return (OmniMainDB == null) ? false : true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[],
   * java.lang.String, java.lang.String[], java.lang.String)
   */
  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {

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
   * 
   * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues,
   * java.lang.String, java.lang.String[])
   */
  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    // TODO: Allow updates to the content
    return 0;
  }

}
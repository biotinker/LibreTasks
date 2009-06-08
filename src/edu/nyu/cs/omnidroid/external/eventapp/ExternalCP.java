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
package edu.nyu.cs.omnidroid.external.eventapp;

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
 * ExternalCP class is used to create a content provider for the external event application.
 * 
 */

public class ExternalCP extends ContentProvider {
  public static final String CP_Name = "edu.nyu.cs.omnidroid.external.eventapp.cp";
  public static final Uri CONTENT_URI = Uri.parse("content://" + CP_Name + "/CP");
  public static final String _ID = "_id";
  public static final String S_NAME = "s_name";
  public static final String S_PH_NO = "s_ph_no";
  public static final String TEXT = "text";

  private static final int DESC = 1;
  private static final int DESC_ID = 2;
  private static final UriMatcher uriMatcher;
  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI(CP_Name, "CP", DESC);
    uriMatcher.addURI(CP_Name, "CP/#", DESC_ID);
  }
  // DATABASE INITIALIZATION
  private SQLiteDatabase OmniDB;
  private static final String DATABASE_NAME = "Omnidroid";
  private static final String DATABASE_TABLE = "Info";
  private static final int DATABASE_VERSION = 4;
  private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE
      + " (_id integer primary key autoincrement, " + "s_name text, s_ph_no text, text text);";

  private static class DatabaseHelper extends SQLiteOpenHelper {
    DatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.d("OmniDroid database", "On Upgrade");
      db.execSQL("DROP TABLE IF EXISTS Info");
      onCreate(db);

    }
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String getType(Uri uri) {
    // TODO Auto-generated method stub

    switch (uriMatcher.match(uri)) {
    case DESC:
      return "vnd.android.cursor.dir/vnd.nyu.cs.omnidroid.external.eventapp.cp";
    case DESC_ID:
      return "vnd.android.cursor.item/vnd.nyu.cs.omnidroid.external.eventapp.cp";
    default:
      throw new IllegalArgumentException("Incorrect URI " + uri);
    }

  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    // TODO:: Allow UI to insert a value
    // Link the module with Andrew's code
    long row = OmniDB.insert(DATABASE_TABLE, "", values);
    if (row > 0) {
      Uri _uri = ContentUris.withAppendedId(CONTENT_URI, row);
      getContext().getContentResolver().notifyChange(_uri, null);
      return _uri;
    }
    throw new SQLException("Failed to insert row into " + uri);

  }

  @Override
  public boolean onCreate() {
    // TODO:: Open the connection to the database
    Context context = getContext();
    DatabaseHelper dbHelper = new DatabaseHelper(context);
    OmniDB = dbHelper.getWritableDatabase();
    return (OmniDB == null) ? false : true;

  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {
    // TODO:: Allow UI to retrieve the information

    SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
    sqlBuilder.setTables(DATABASE_TABLE);
    if (uriMatcher.match(uri) == DESC_ID)
      sqlBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));

    Cursor c = sqlBuilder.query(OmniDB, projection, selection, selectionArgs, null, null, null);

    c.setNotificationUri(getContext().getContentResolver(), uri);
    return c;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    // TODO :: Allow UI to update information in the CP
    return 0;
  }

}

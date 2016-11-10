/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
 /*******************************************************************************
 * Copyright 2009 Omnidroid - http://code.google.com/p/omnidroid
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
package libretasks.app.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Database helper class for the Rules table. Defines basic CRUD methods. 
 * <p>
 * This table contains all rule records defined by the user. Every rule is a combination of records
 * in these four tables: Rules, RuleActions, RuleActionParameters, RuleFilters. 
 * </p>
 * 
 * RuleName is the user defined name of the rule; 
 * RuleDesc is the user defined description of the rule;
 * Enabled is whether this rule is activated. 
 * Created is the time stamp of when this rule is created
 * Updated is the time stamp of when this rule is last updated
 * 
 */
public class RuleDbAdapter extends DbAdapter {

  /* Column names */
  public static final String KEY_RULEID = "RuleID";
  public static final String KEY_EVENTID = "FK_EventID";
  public static final String KEY_RULENAME = "RuleName";
  public static final String KEY_RULEDESC = "RuleDesc";
  public static final String KEY_ENABLED = "Enabled";
  public static final String KEY_CREATED = "Created";
  public static final String KEY_UPDATED = "Updated";
  public static final String KEY_NOTIFICATION = "Notification";
 
  //set this 
  private static boolean notification=true;

  /* An array of all column names */
  public static final String[] KEYS = { KEY_RULEID, KEY_EVENTID, KEY_RULENAME, KEY_RULEDESC,
      KEY_ENABLED, KEY_CREATED, KEY_UPDATED, KEY_NOTIFICATION };

  /* Table name */
  private static final String DATABASE_TABLE = "Rules";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
      + KEY_RULEID + " integer primary key autoincrement, " 
      + KEY_EVENTID + " integer not null, "
      + KEY_RULENAME + " text not null, " 
      + KEY_RULEDESC + " text not null, " 
      + KEY_ENABLED + " integer not null, " 
      + KEY_CREATED + " datetime, " 
      + KEY_UPDATED + " datetime);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;
  
  protected static final String ADD_NOTIFICATION_COLUMN = "ALTER TABLE " + DATABASE_TABLE  
               + " ADD " + KEY_NOTIFICATION + " integer not null DEFAULT 1";

  /**
   * Constructor.
   * 
   * @param database
   *          is the database object to work within.
   */
  public RuleDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new Rule record.
   * 
   * @param eventID
   *          is id of the event which this rule belongs to.
   * @param ruleName
   *          is name of this rule
   * @param ruleDesc
   *          is description of this rule
   * @param enabled
   *          is whether this rule is active
   * @return ruleID if success, or -1 if failed.
   * @throws IllegalArgumentException
   *           if there is null within parameters
   */
  public long insert(Long eventID, String ruleName, String ruleDesc, Boolean enabled) {
    if (eventID == null || ruleName == null || ruleDesc == null || enabled == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }

    // Create time stamp with database format.
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String insertTime = dateFormat.format(new Timestamp(System.currentTimeMillis()));

    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_EVENTID, eventID);
    initialValues.put(KEY_RULENAME, ruleName);
    initialValues.put(KEY_RULEDESC, ruleDesc);
    initialValues.put(KEY_ENABLED, enabled);
    initialValues.put(KEY_CREATED, insertTime);
    initialValues.put(KEY_UPDATED, insertTime);
    initialValues.put(KEY_NOTIFICATION, notification);
    
    // Set null because don't use 'null column hack'.
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * Delete a Rule record.
   * 
   * @param ruleID
   *          is the id of the record.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if ruleID is null
   */
  public boolean delete(Long ruleID) {
    if (ruleID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_RULEID + "=" + ruleID, null) > 0;
  }

  /**
   * Delete all Rule records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the ruleID.
   * 
   * @param ruleID
   *          is the id of the record to be fetched.
   * @return a Cursor pointing to the found record.
   * @throws IllegalArgumentException
   *           if ruleID is null
   */
  public Cursor fetch(Long ruleID) {
    if (ruleID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_RULEID + "=" + ruleID, null,
        null, null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all RegisteredApp records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * Return a Cursor that contains all Rule records which matches the parameters.
   * 
   * @param eventID
   *          is the event id, or null to fetch any eventID
   * @param ruleName
   *          is name of rule, or null to fetch any ruleName
   * @param ruleDesc
   *          is description of rule, or null to fetch any description
   * @param enabled
   *          is whether the rule is activated or null to fetch any enabled status
   * @param orderBy
   *          is the ',' delimited order by columns, or null if no specific order
   * @return a Cursor that contains all Rule records which matches the parameters.
   * @throws SQLiteException
   *           if orderBy is not compiled correctly
   */
  public Cursor fetchAll(Long eventID, String ruleName, String ruleDesc, Boolean enabled,
      String orderBy) {

    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(DATABASE_TABLE);
    qb.appendWhere("1=1");
    if (eventID != null) {
      qb.appendWhere(" AND " + KEY_EVENTID + " = " + eventID);
    }
    if (ruleName != null) {
      qb.appendWhere(" AND " + KEY_RULENAME + " = ");
      qb.appendWhereEscapeString(ruleName);
    }
    if (ruleDesc != null) {
      qb.appendWhere(" AND " + KEY_RULEDESC + " = ");
      qb.appendWhereEscapeString(ruleDesc);
    }
    if (enabled != null) {
      qb.appendWhere(" AND " + KEY_ENABLED + " = " + (enabled ? 1 : 0));
    }

    // Not using additional selections, selectionArgs, groupBy, having, orderBy, set them to null.
    return qb.query(database, KEYS, null, null, null, null, orderBy);
  }

  /**
   * Update a rule record with specific parameters.
   * 
   * @param ruleID
   *          is id of the record to be updated
   * @param eventID
   *          is the event id, or null if not updating it
   * @param ruleName
   *          is the name of the rule, or null if not updating it
   * @param ruleDesc
   *          is the description of rule, or null if not updating it
   * @param enabled
   *          is whether the rule is activated, or null if not updating it
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if ruleID is null
   */
  public boolean update(Long ruleID, Long eventID, String ruleName, String ruleDesc, 
      Boolean enabled, Boolean notification) {

    // Create time stamp with database format.
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String insertTime = dateFormat.format(new Timestamp(System.currentTimeMillis()));

    if (ruleID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    ContentValues args = new ContentValues();
    if (eventID != null) {
      args.put(KEY_EVENTID, eventID);
    }
    if (ruleName != null) {
      args.put(KEY_RULENAME, ruleName);
    }
    if (ruleDesc != null) {
      args.put(KEY_RULEDESC, ruleDesc);
    }
    if (enabled != null) {
      args.put(KEY_ENABLED, enabled);
    }
    if (notification != null) {
      args.put(KEY_NOTIFICATION, notification);
    }

    if (args.size() > 0) {
      // Set update time stamp here
      args.put(KEY_UPDATED, insertTime);
      // Set whereArg to null here
      return database.update(DATABASE_TABLE, args, KEY_RULEID + "=" + ruleID, null) > 0;
    }
    return false;
  }
  
  /** Sets notifications  */
  public static void setDefaultNotificationValue(Boolean notificationValue) {
    notification=notificationValue;
  }
  
  public static String getSqliteCreateStatement() {
    return DATABASE_CREATE;
  }
}

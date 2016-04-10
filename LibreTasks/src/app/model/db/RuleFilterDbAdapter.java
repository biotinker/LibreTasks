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
 * Copyright 2009, 2010 Omnidroid - http://code.google.com/p/omnidroid
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

/**
 * Database helper class for the RuleFilters table. Defines basic CRUD methods. 
 * <p> 
 * This table contains ruleFilers associated with each rule. 
 * FK_RuleID points to the rule it belongs to.
 * FK_EventAttributeID points to the event attribute which it filters on
 * FK_ExternalAttributeID points the external attribute which it filters on
 * </p>
 * <p>
 * Note: Only one of FK_EventAttributeID and FK_ExternalAttributeID is valid foreign key 
 * indicating whether the filter is on an eventAttribute or an external attribute. 
 * The non-valid one should be -1
 * </p>
 * 
 * <p>
 * Every rule has a user defined filter tree, one record in this table is one node of that tree.
 * FK_ParentRuleFilterID points to the ruleFilter in the same table which is parent of this node.
 * (FK_ParentRuleFilterID = -1 if it has no parent.)
 * RuleFilterData is the user defined data associated with this ruleFilterNode
 * </p>
 */
public class RuleFilterDbAdapter extends DbAdapter {

  /* Column names */
  public static final String KEY_RULEFILTERID = "RuleFilterID";
  public static final String KEY_RULEID = "FK_RuleID";
  public static final String KEY_EVENTATTRIBUTEID = "FK_EventAttributeID";
  public static final String KEY_EXTERNALATTRIBUTEID = "FK_ExternalAttributeID";
  public static final String KEY_DATAFILTERID = "FK_DataFilterID";
  public static final String KEY_PARENTRULEFILTERID = "FK_ParentRuleFilterID";
  public static final String KEY_RULEFILTERDATA = "RuleFilterData";

  /* An array of all column names */
  public static final String[] KEYS = { KEY_RULEFILTERID, KEY_RULEID, KEY_EVENTATTRIBUTEID,
      KEY_EXTERNALATTRIBUTEID, KEY_DATAFILTERID, KEY_PARENTRULEFILTERID, KEY_RULEFILTERDATA };

  /* Table name */
  private static final String DATABASE_TABLE = "RuleFilters";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
      + KEY_RULEFILTERID + " integer primary key autoincrement, " 
      + KEY_RULEID + " integer not null, " 
      + KEY_EVENTATTRIBUTEID + " integer not null, "
      + KEY_EXTERNALATTRIBUTEID + " integer not null, " 
      + KEY_DATAFILTERID + " integer not null, "
      + KEY_PARENTRULEFILTERID + " integer not null, " 
      + KEY_RULEFILTERDATA + " text not null);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  /**
   * Constructor.
   * 
   * @param database
   *          is the database object to work within.
   */
  public RuleFilterDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new RuleFilter record
   * 
   * @param ruleID
   *          is id of the rule it belongs to
   * @param eventAttributeID
   *          is its event attribute id or -1 if no event attribute
   * @param externalAttributeID
   *          its external attribute id or -1 if no external attribute
   * @param dataFilterID
   *          is id of its data filter type
   * @param parentRuleFilterID
   *          is id of its parent filter, or -1 if no parent filter
   * @param ruleFilterData
   *          is the data related with this filter
   * @return RuleFilterID or -1 if creation failed.
   * @throws IllegalArgumentException
   *           if there is null within parameters
   */
  public long insert(Long ruleID, Long eventAttributeID, Long externalAttributeID,
      Long dataFilterID, Long parentRuleFilterID, String ruleFilterData) {

    if (ruleID == null || eventAttributeID == null || externalAttributeID == null
        || dataFilterID == null || parentRuleFilterID == null || ruleFilterData == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_RULEID, ruleID);
    initialValues.put(KEY_EVENTATTRIBUTEID, eventAttributeID);
    initialValues.put(KEY_EXTERNALATTRIBUTEID, externalAttributeID);
    initialValues.put(KEY_DATAFILTERID, dataFilterID);
    initialValues.put(KEY_PARENTRULEFILTERID, parentRuleFilterID);
    initialValues.put(KEY_RULEFILTERDATA, ruleFilterData);
    // Set null because don't use 'null column hack'.
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * Delete a RuleFilter record.
   * 
   * @param ruleFilterID
   *          is the id of the record.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if ruleFilterID is null
   */
  public boolean delete(Long ruleFilterID) {
    if (ruleFilterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_RULEFILTERID + "=" + ruleFilterID, null) > 0;
  }

  /**
   * Delete all RuleFilter records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the ruleFilterID.
   * 
   * @param ruleFilterID
   *          is the id of the record to be fetched.
   * @return a Cursor pointing to the found record.
   * @throws IllegalArgumentException
   *           if ruleFilterID is null
   */
  public Cursor fetch(Long ruleFilterID) {
    if (ruleFilterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_RULEFILTERID + "="
        + ruleFilterID, null, null, null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all RuleFilter records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * Return a Cursor that contains all RuleFilter records which matches the parameters.
   * 
   * @param ruleID
   *          is the id of rule the filter belongs to, or null to fetch any.
   * @param eventAttributeID
   *          is id of the event attribute, or null to fetch any.
   * @param externalAttributeID
   *          is id of the external attribute, or null to fetch any.
   * @param dataFilterID
   *          is id of the data filter, or null to fetch any.
   * @param parentRuleFilterID
   *          is id of its parent ruleFiler, or null to fetch any.
   * @param ruleFilterData
   *          is the data associated with this ruleFilter, or null to fetch any.
   * @return a Cursor that contains all RuleFilter records which matches the parameters.
   */
  public Cursor fetchAll(Long ruleID, Long eventAttributeID, Long externalAttributeID,
      Long dataFilterID, Long parentRuleFilterID, String ruleFilterData) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(DATABASE_TABLE);
    qb.appendWhere("1=1");
    if (ruleID != null) {
      qb.appendWhere(" AND " + KEY_RULEID + " = " + ruleID);
    }
    if (eventAttributeID != null) {
      qb.appendWhere(" AND " + KEY_EVENTATTRIBUTEID + " = " + eventAttributeID);
    }
    if (externalAttributeID != null) {
      qb.appendWhere(" AND " + KEY_EXTERNALATTRIBUTEID + " = " + externalAttributeID);
    }
    if (dataFilterID != null) {
      qb.appendWhere(" AND " + KEY_DATAFILTERID + " = " + dataFilterID);
    }
    if (parentRuleFilterID != null) {
      qb.appendWhere(" AND " + KEY_PARENTRULEFILTERID + " = " + parentRuleFilterID);
    }
    if (ruleFilterData != null) {
      qb.appendWhere(" AND " + KEY_RULEFILTERDATA + " = ");
      qb.appendWhereEscapeString(ruleFilterData);
    }
    // Not using additional selections, selectionArgs, groupBy, having, orderBy, set them to null.
    return qb.query(database, KEYS, null, null, null, null, null);
  }

  /**
   * Update a RuleFilter record with specific parameters.
   * 
   * @param ruleFilterID
   *          is id or the record.
   * @param ruleID
   *          is the id of rule the filter belongs to, or null if not updating it.
   * @param eventAttributeID
   *          is id of the event attribute, or null if not updating it.
   * @param externalAttributeID
   *          is id of the external attribute, or null if not updating it.
   * @param dataFilterID
   *          is id of the data filter, or null if not updating it.
   * @param parentRuleFilterID
   *          is id of its parent ruleFiler, or null if not updating it.
   * @param ruleFilterData
   *          is the data associated with this ruleFilter, or null if not updating it.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if ruleFilterID is null
   */
  public boolean update(Long ruleFilterID, Long ruleID, Long eventAttributeID,
      Long externalAttributeID, Long dataFilterID, Long parentRuleFilterID, String ruleFilterData) {
    if (ruleFilterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    ContentValues args = new ContentValues();
    if (ruleID != null) {
      args.put(KEY_RULEID, ruleID);
    }
    if (eventAttributeID != null) {
      args.put(KEY_EVENTATTRIBUTEID, eventAttributeID);
    }
    if (externalAttributeID != null) {
      args.put(KEY_EXTERNALATTRIBUTEID, externalAttributeID);
    }
    if (dataFilterID != null) {
      args.put(KEY_DATAFILTERID, dataFilterID);
    }
    if (parentRuleFilterID != null) {
      args.put(KEY_PARENTRULEFILTERID, parentRuleFilterID);
    }
    if (ruleFilterData != null) {
      args.put(KEY_RULEFILTERDATA, ruleFilterData);
    }

    if (args.size() > 0) {
      // Set whereArg to null here
      return database.update(DATABASE_TABLE, args, KEY_RULEFILTERID + "=" + ruleFilterID, null) > 0;
    }
    return false;
  }
  
  /**
   * Package protected method to perform a sqlite update statement.
   * 
   * @param values
   *          a map from column names to new column values. null is a valid value that will be
   *          translated to NULL.
   * 
   * @param whereClause
   *          the optional WHERE clause to apply when updating. Passing null will update all rows.
   * @see SQLiteDatabase#update(String, ContentValues, String, String[])
   */
  void sqlUpdate(ContentValues values, String whereClause) {
    database.update(DATABASE_TABLE, values, whereClause, null);
  }
  
  public static String getSqliteCreateStatement() {
    return DATABASE_CREATE;
  }
}

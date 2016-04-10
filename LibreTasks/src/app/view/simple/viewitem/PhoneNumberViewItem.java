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
 * Copyright 2010 Omnidroid - http://code.google.com/p/omnidroid 
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
package libretasks.app.view.simple.viewitem;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import libretasks.app.controller.datatypes.DataType;
import libretasks.app.controller.datatypes.OmniPhoneNumber;
import libretasks.app.view.simple.UtilUI;
import libretasks.app.view.simple.model.ModelAttribute;

/**
 * {@link ViewItem} implementation for {@link OmniPhoneNumber}
 */
public class PhoneNumberViewItem extends AbstractViewItem  {
  private final AutoCompleteTextView editText;
  private final Activity activity;
  
  /**
   * Class Constructor.
   * 
   * @param id
   *          the id used to uniquely identify this object.
   * @param dataTypeDbID
   *          the database id for {@link OmniPhoneNumber}
   * @param activity
   *          the activity where this view item is to be built on
   */
  public PhoneNumberViewItem(int id, long dataTypeDbID, Activity activity) {
    super(id, dataTypeDbID);
    
    this.activity = activity;
    editText = new AutoCompleteTextView(activity);
    editText.setId(id);
  }

  /**
   * {@inheritDoc}
   */
  public DataType getData() throws Exception {
    String namePhone = editText.getText().toString();
    String phoneNo = namePhone.substring(1 + namePhone.lastIndexOf(":"));
    return new OmniPhoneNumber(phoneNo);
  }

  /**
   * {@inheritDoc}
   */
  public View buildUI(DataType initData) {
    if (initData != null) {
      editText.setText(initData.getValue());
    }
    
    ContentResolver cr = activity.getContentResolver();
    List<String> contacts = new ArrayList<String>();
    // Form an array specifying which columns to return.
    String[] projection = new String[] {People.NAME, People.NUMBER };
    // Get the base URI for the People table in the Contacts content provider.
    Uri contactsUri = People.CONTENT_URI;
    // Make the query.
    Cursor cursor = cr.query(contactsUri, 
        projection, // Which columns to return
        null, // Which rows to return (all rows)
        null, // Selection arguments (none)
        Contacts.People.DEFAULT_SORT_ORDER);
    if (cursor.moveToFirst()) {
      String name;
      String phoneNumber;
      int nameColumn = cursor.getColumnIndex(People.NAME);
      int phoneColumn = cursor.getColumnIndex(People.NUMBER);
      do {
        // Get the field values of contacts
        name = cursor.getString(nameColumn);
        phoneNumber = cursor.getString(phoneColumn);
        contacts.add(name + ": " + phoneNumber);
      } while (cursor.moveToNext());
    }
    cursor.close();
    
    String[] contactsStr = new String[]{};
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
        android.R.layout.simple_dropdown_item_1line, contacts.toArray(contactsStr));
    
    editText.setAdapter(adapter);
    editText.setThreshold(1);
    return(editText);
  }
  
  /**
   * {@inheritDoc}
   */
  public void insertAttribute(ModelAttribute attribute) {
    UtilUI.replaceEditText(editText, getAttributeInsertName(attribute));  
  }

  /**
   * {@inheritDoc}
   */
  public void loadState(Bundle bundle) {
    String key = String.valueOf(ID);

    if (bundle.containsKey(key)) {
      editText.setText(bundle.getString(key));
    }
  }

  /**
   * {@inheritDoc}
   */
  public void saveState(Bundle bundle) {
    bundle.putString(String.valueOf(ID), editText.getText().toString());
  }
}

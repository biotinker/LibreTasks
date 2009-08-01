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
package edu.nyu.cs.omnidroid.ui.simple;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Spare parts for UI work.
 */
public class UtilUI {

  private UtilUI() {  
  }
  
  /**
   * Wraps call to displaying a simple message box.
   */
  public static void showAlert(Context context, String title, String message) {
    new AlertDialog.Builder(context).setTitle(title).setIcon(0).setMessage(message).setCancelable(
        true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialoginterface, int i) {
      }
    }).show();
  }

  /**
   * Force-inflates a dialog main linear-layout to take max available screen space even though
   * contents might not occupy full screen size.
   */
  public static void inflateDialog(LinearLayout layout) {
    WindowManager wm = (WindowManager) layout.getContext().getSystemService(
      Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    layout.setMinimumWidth(display.getWidth() - 30);
    layout.setMinimumHeight(display.getHeight() - 40);
  }
  
  /**
   * Uncheck any item that is currently selected in a ListView.
   */
  public static void uncheckListViewSingleChoice(ListView listView) {
    if (listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE) {
      int checkedPosition = listView.getCheckedItemPosition();
      if (checkedPosition > -1) {
        listView.setItemChecked(checkedPosition, false);
      }
    }
    else {
      throw new IllegalArgumentException(
        "UtilUI.uncheckListView() only works on lists using choice mode: CHOICE_MODE_SINGLE.");
    }
  }
  
  /**
   * Erases all shared preferences saved for an activity.
   * @param context  Context of caller.
   * @param stateName  State name used for both saving and loading preferences.
   */
  public static void resetSharedPreferences(Context context, String stateName) {
    SharedPreferences state = context.getSharedPreferences(
        stateName, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
      state.edit().clear().commit();
  }
}
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
package libretasks.app.view.simple;

import libretasks.app.R;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Spare parts for UI work.
 */
public class UtilUI {

  /** Alert users about standard info such as actions have been executed etc */
  public static final int NOTIFICATION_ACTION = 0;
  /**Alert users about potential system issues (throttle/etc.) */
  public static final int NOTIFICATION_WARN = 1;
  /**This is used rule with customized send notificaiton action is triggered  */
  public static final int NOTIFICATION_RULE = 2;
   
  private static final String TAG = UtilUI.class.getSimpleName();
  
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
   * Send an Android Noficiation to the system.
   *  
   * @param context - application Context
   * @param nofityType - type of notification see NOTIFICATION_ constants declared above
   * @param title - to display on notification
   * @param message - to display on notification
   */
  //this method could be called by several methods simultaneously with the same notifyType
  //since all instances will be using and pdating values from sharedPreferences and updating same 
  //notification in status bar this method needs to be synchronized.
  public static synchronized void showNotification(Context context, int notifyType, String title, 
      String message) {
    if (message == null) {
      Log.w("showNotification", "No user message provided");
      message = context.getString(R.string.action_default_message);
    }
    if (title == null) {
      Log.i("showNotification", "No title provided");
      title = message;    
    }

    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    int numOfNotifications;
    switch (notifyType) {
    case NOTIFICATION_ACTION :
      numOfNotifications = sharedPreferences.getInt(context.getString(R.string.pref_key_notification_action_count), 0);
      if (++numOfNotifications == 1) {
        editor.putString(context.getString(R.string.pref_key_notification_action_message), message);
      }
      editor.putInt(context.getString
          (R.string.pref_key_notification_action_count), numOfNotifications);
      break;
    case NOTIFICATION_WARN :
      numOfNotifications = sharedPreferences.getInt(context.getString(R.string.pref_key_notification_warn_count), 0);
      if (++numOfNotifications == 1) {
        editor.putString(context.getString(R.string.pref_key_notification_warn_message), message);
      } 
      editor.putInt(context.getString(R.string.pref_key_notification_warn_count), numOfNotifications);
      break;
    case NOTIFICATION_RULE :
      numOfNotifications = sharedPreferences.getInt(context.getString(R.string.pref_key_notification_rule_count), 0);
      if (++numOfNotifications == 1) {
        editor.putString(context.getString(R.string.pref_key_notification_rule_message), message);
        editor.putString(context.getString(R.string.pref_key_notification_rule_title), title);
      }
      editor.putInt(context.getString(R.string.pref_key_notification_rule_count), 
          numOfNotifications);
      break;
    default :
      Log.w(TAG, new IllegalArgumentException());
      return;
    }
    editor.commit();
    
    notify (context, notifyType, numOfNotifications, title, message);
  }
  
  private static void notify(Context context, int notifyType, int numOfNotifications, 
      String title, String message) {
    
    // Start building notification
    NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    Notification notification = new Notification(R.drawable.icon, message, System.currentTimeMillis());
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);      
    
    // Link this notification to the Logs activity
    Intent notificationIntent = new Intent(context, ActivityLogs.class);
    switch (notifyType) {
    case NOTIFICATION_ACTION :
      notificationIntent.putExtra(ActivityLogs.KEY_TAB_TAG, ActivityLogs.TAB_TAG_ACTION_LOG);
      if (numOfNotifications > 1) {
        message = context.getString(R.string.notification_action, numOfNotifications);
        title = context.getString(R.string.notification_action_title, numOfNotifications);
      }
      break;
    case NOTIFICATION_WARN :
      notificationIntent.putExtra(ActivityLogs.KEY_TAB_TAG, ActivityLogs.TAB_TAG_GENERAL_LOG);
      if (numOfNotifications > 1) {
        message = context.getString(R.string.notification_warn, numOfNotifications);
      }
      break;
    case NOTIFICATION_RULE :
      notificationIntent.putExtra(ActivityLogs.KEY_TAB_TAG, ActivityLogs.TAB_TAG_ACTION_LOG);
      if (numOfNotifications > 1) {
        message = context.getString(R.string.notification_rule, numOfNotifications);
      }
      break;
    default :
      Log.w(TAG, new IllegalArgumentException());
      return;
    }
    
    PendingIntent contentIntent = PendingIntent.getActivity(context, notifyType, notificationIntent, 
        PendingIntent.FLAG_UPDATE_CURRENT);    
    
    notification.setLatestEventInfo(context, title, message, contentIntent);
    
    // Set Preferences for notification options (sound/vibrate/lights
    if (prefs.getBoolean(context.getString(R.string.pref_key_sound), false)) {
      notification.defaults |= Notification.DEFAULT_SOUND;      
    }
    if (prefs.getBoolean(context.getString(R.string.pref_key_vibrate), false)) {
      notification.defaults |= Notification.DEFAULT_VIBRATE;
    }
    if (prefs.getBoolean(context.getString(R.string.pref_key_light), false)) {
      notification.defaults |= Notification.DEFAULT_LIGHTS;
    }

    // Send the notification
    nm.notify(notifyType, notification);
  }
  /**
   * loads notifications that haven't been viewed. this method is called after boot is completed.
   * 
   * @param context
   *         application context
   */
  public static void loadNotifications(Context context) {
    int numOfNotifications;
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    numOfNotifications = sharedPreferences.getInt(context.getString(
        R.string.pref_key_notification_warn_count), 0);
    if (numOfNotifications > 0) {
      notify(context, NOTIFICATION_WARN, numOfNotifications, context.getString(R.string.libretasks),
          sharedPreferences.getString(context.getString(R.string
          .pref_key_notification_warn_message), ""));
    }
    numOfNotifications = sharedPreferences.getInt(context.getString(
        R.string.pref_key_notification_action_count), 0);
    if (numOfNotifications > 0) {
      notify(context, NOTIFICATION_ACTION, numOfNotifications, context.getString(
          R.string.libretasks), sharedPreferences.getString(context.getString(
          R.string.pref_key_notification_action_message), ""));
    }
    numOfNotifications = sharedPreferences.getInt(context.getString(
        R.string.pref_key_notification_rule_count), 0);
    if (numOfNotifications > 0) {
      notify(context, NOTIFICATION_RULE, numOfNotifications, sharedPreferences.getString(context
          .getString(R.string.pref_key_notification_rule_title), ""),sharedPreferences.getString(
          context.getString(R.string.pref_key_notification_rule_message), ""));
    }      
  }
  
  /**
   * clears notification from status bar
   * 
   * @param context application context;
   * @param notificationType
   *         id of notification to be canceled
   */
  public static void clearNotification(Context context, int notificationType) {
    
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    
    switch (notificationType) {
    case NOTIFICATION_ACTION:
      editor.putInt(context.getString(R.string.pref_key_notification_action_count), 0);
      break;
    case NOTIFICATION_RULE:
      editor.putInt(context.getString(R.string.pref_key_notification_rule_count), 0);
      break;
    case NOTIFICATION_WARN:
      editor.putInt(context.getString(R.string.pref_key_notification_warn_count), 0);
      break;
    default:
      Log.w(TAG, new IllegalArgumentException());
      return;
    }
    editor.commit();
    
    NotificationManager nm = (NotificationManager)context.getSystemService(
        Context.NOTIFICATION_SERVICE);
    nm.cancel(notificationType);
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
  
  /**
   * Replace text in the passed EditText with a new string. This method takes into account the
   * cursor positions to do partial replacement depending on how much text is currently selected
   * in the field.
   * 
   * @param view The EditText field to do the replacement in.
   * @param newText The new text string to insert in the field.
   */
  public static void replaceEditText(EditText view, String newText) {
    int start = Math.min(view.getSelectionStart(), view.getSelectionEnd());
    int end   = Math.max(view.getSelectionStart(), view.getSelectionEnd());
    int diff  = end - start;
    
    String strContents = view.getText().toString();
    StringBuilder sb = new StringBuilder(strContents.length() + newText.length() - diff);
    sb.append(strContents.substring(0, start));
    sb.append(newText);
    sb.append(strContents.substring(end, strContents.length()));
    view.setText(sb.toString());
  }

}

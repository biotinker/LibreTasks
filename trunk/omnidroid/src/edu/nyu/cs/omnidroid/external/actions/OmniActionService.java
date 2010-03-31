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
package edu.nyu.cs.omnidroid.external.actions;

import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.core.ShowAlertAction;
import edu.nyu.cs.omnidroid.core.ShowNotificationAction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * This service is for Omnidroid to launch simple actions like: show a message, put some message on
 * notification bar, show a web page etc.
 */
public class OmniActionService extends Service {
  
  //operation supported by this service
  public static final String OPERATION_TYPE = "OPERATION_TYPE";
  public static final String SHOW_ALERT_ACTION = "SHOW_ALERT_ACTION";
  public static final String SHOW_NOTIFICATION_ACTION = "SHOW_NOTIFICATION_ACTION";
  
  public static final int NOTIFICATION_ID = 1;
  

  @Override
  public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void onStart(Intent intent, int startId) {
    super.onStart(intent, startId);
    String operationType = intent.getStringExtra(OPERATION_TYPE);
    if (operationType.equals(SHOW_ALERT_ACTION)) {
      showAlert(intent);
    } else if (operationType.equals(SHOW_NOTIFICATION_ACTION)) {
      showNotification(intent);
    } else {
      Log.e("OmniActionSercive", "No such operation supported as: " + operationType);
    }
  }

  private void showNotification(Intent intent) {
    String message = intent.getStringExtra(ShowNotificationAction.PARAM_ALERT_MESSAGE);
    if (message == null) {
      Log.w("showNotification", "No user message provided");
      message = getString(R.string.action_default_message);
    }
    NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    Notification notification = new Notification(R.drawable.icon, message, System.currentTimeMillis());
    Context context = getApplicationContext();
    CharSequence contentTitle = ShowNotificationAction.APP_NAME;
    CharSequence contentText = message;
    Intent notificationIntent = new Intent(this, OmniActionService.class);
    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

    notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
    nm.notify(NOTIFICATION_ID, notification);
  }

  private void showAlert(Intent intent) {
    String message = intent.getStringExtra(ShowAlertAction.PARAM_ALERT_MESSAGE);
    if (message == null) {
      Log.w("showAlert", "No user message provided");
      message = getString(R.string.action_default_message);
    }
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }
  
  

}

/**
 * 
 */
package edu.nyu.cs.omnidroid.external.catcherapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.core.CP;

/**
 * @author Rajiv
 * 
 */

public class SMSListener extends BroadcastReceiver {
  Activity act;

  public SMSListener(Activity act) {
    this.act = act;
  }

  @Override
  public void onReceive(Context context, Intent intent) {

    Toast.makeText(context, intent.getAction(), 5).show();
    if (intent.getAction().equalsIgnoreCase("SMS_SENT")) {
      // Bundle bundle = intent.getExtras();
      Uri DummyURI = Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP/2");
      /* FOR SPLITTING THE URI
      String str_uri = DummyURI.toString();
      String[] temp = null;
      temp = str_uri.split("/");
      String num = temp[temp.length - 1];
      */
      String action_data = displayAction(DummyURI.toString());
      Toast.makeText(context, action_data, 5);
      // if (bundle != null) {
      // Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
  }

  public String displayAction(String uri) {
    
    String str_uri = uri.toString();
    String[] temp = null;
    temp = str_uri.split("/");
    String num = temp[temp.length - 1];
    int new_id = Integer.parseInt(num);
    Cursor cur = act.managedQuery(Uri.parse(uri), null, null, null, null);
    if (cur.moveToPosition(new_id)) {
      Toast.makeText(
          act,
          cur.getString(cur.getColumnIndex(CP._ID)) + ","
          + cur.getString(cur.getColumnIndex(CP.ACTION_DATA)), Toast.LENGTH_LONG).show();
    }
    String action = cur.getColumnName(cur.getColumnIndex(CP.ACTION_DATA));
    return action;
  }

}

package edu.nyu.cs.omnidroid.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.OmLogger;
import edu.nyu.cs.omnidroid.util.UGParser;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class DummyActivity extends Activity {

  private String uri;
  Intent intent;
  // Activity a;
  String filterdata = null;
  String filtertype = null;
  String uridata = null;
  String actionname = null;
  String actionapp = null;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main2);
    this.intent = getIntent();

    this.uri = getURI(intent);
    matchEventName();
    this.finish();

  }

  public String getURI(Intent intent1) {
    /*
     * bundle=intent.getExtras(); Set<String> keys = bundle.keySet();
     */
    Bundle b = intent1.getExtras();
    Object c = b.get("uri");
    String uri = c.toString();
    return uri;

  }

  public void matchEventName() {
    UGParser ug = new UGParser(getApplicationContext());

    ArrayList<HashMap<String, String>> recs = ug.readbyEventName(intent.getAction());
    // ArrayList<HashMap<String, String>> UCRecords = ug.readRecords();
    Iterator<HashMap<String, String>> i = recs.iterator();
    while (i.hasNext()) {
      HashMap<String, String> HM1 = i.next();
      // Configure the Intent Filter with the Events if Instance in enabled
      if (HM1.get("EnableInstance").equalsIgnoreCase("True")) {
        filtertype = HM1.get(ug.KEY_FilterType);
        filterdata = HM1.get(ug.KEY_FilterData);
        uridata = HM1.get("ActionData");
        actionname = HM1.get("ActionName");
        actionapp = HM1.get(ug.KEY_ActionApp);
        // boolean val=checkFilter(uri,filtertype,filterdata);
        getCols(uri, filtertype, filterdata, actionapp);

      }
    }
  }

  public boolean checkFilter(String uri, String filtertype1, String filterdata1) {

    String str_uri = uri;
    String[] temp = null;
    temp = str_uri.split("/");
    String num = temp[temp.length - 1];
    String final_uri = str_uri.substring(0, str_uri.length() - num.length() - 1);
    int new_id = Integer.parseInt(num);
    Cursor cur = managedQuery(Uri.parse(final_uri), null, null, null, null);
    if (cur.moveToPosition(new_id)) {

      if (filterdata1.equalsIgnoreCase(cur.getString(cur.getColumnIndex(filtertype1)))) {
        Toast.makeText(getApplicationContext(), cur.getString(cur.getColumnIndex(filtertype1)),
            Toast.LENGTH_LONG).show();
      }
    }
    String action = cur.getColumnName(cur.getColumnIndex(CP.ACTION_DATA));
    return true;
  }

  public void sendIntent(String actiondata1) {
    Intent send_intent = new Intent();
    send_intent.setAction("SMS_SENT");
    send_intent.putExtra("uri", uridata);
    // sendBroadcast(send_intent);
   // PackageManager pm = this.getPackageManager();
    //try {
     // PackageInfo pi = pm.getPackageInfo(actiondata1, 0);
      AGParser ag=new AGParser(getApplicationContext());
      ComponentName comp=new ComponentName(ag.readPkgName(actiondata1),ag.readListenerClass(actiondata1));
      send_intent.setComponent(comp);
     // send_intent.setClass(this.getApplicationContext(), pi.getClass());
      // startActivity(send_intent);

      sendBroadcast(send_intent);
      Toast.makeText(getApplicationContext(), "Sent!", Toast.LENGTH_SHORT).show();

    //} catch (NameNotFoundException e) {
      // TODO Auto-generated catch block
     // OmLogger.write(getApplicationContext(), actiondata1 + "not installed");
      //e.printStackTrace();
    //}
  }

  public void getCols(String uri, String filtertype1, String filterdata1, String actiondata1) {
    String[] cols = null;
    String str_uri = uri;
    String[] temp = null;
    temp = str_uri.split("/");
    String num = temp[temp.length - 1];
    String final_uri = str_uri.substring(0, str_uri.length() - num.length() - 1);
    int new_id = Integer.parseInt(num);
    int flag = 0;

    Cursor cur = managedQuery(Uri.parse(final_uri), null, null, null, null);
    if (cur.moveToFirst()) {

      do {

        int id = Integer.parseInt(cur.getString(cur.getColumnIndex("_id")));

        if (new_id == id) {
          if (filterdata1.equalsIgnoreCase(cur.getString(cur.getColumnIndex(filtertype1)))) {
            Toast.makeText(
                getApplicationContext(),
                cur.getString(cur.getColumnIndex("s_name")) + ":"
                    + cur.getString(cur.getColumnIndex(filtertype1)), Toast.LENGTH_LONG).show();
            sendIntent(actiondata1);
            flag = 1;
          }

        }

      } while (cur.moveToNext());
      if (flag == 0) {
        Toast.makeText(getApplicationContext(),
            cur.getString(cur.getColumnIndex(filtertype1)) + " does not exist", Toast.LENGTH_LONG)
            .show();
      }

    }

  }
}

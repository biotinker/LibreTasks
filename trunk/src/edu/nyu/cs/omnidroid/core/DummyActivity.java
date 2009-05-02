package edu.nyu.cs.omnidroid.core;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.OmLogger;
import edu.nyu.cs.omnidroid.util.StringMap;
import edu.nyu.cs.omnidroid.util.UGParser;
import android.R.string;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

// TODO: Please add Javadocs and remove string instance constants.
public class DummyActivity extends Activity {

  private String uri;
  Intent intent;
  // Activity a;
  String filterdata = null;
  String filtertype = null;
  String uridata = null;
  String actionname = null;
  String actionapp = null;
  String intentAction = null;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main2);
    this.intent = getIntent();
    if (intent.getAction().contains("SMS_RECEIVED")) {
      String id = null;

      this.uri = "content://sms/inbox";
      intentAction = "SMS_RECEIVED";
      StringBuilder sb = new StringBuilder(uri);
      sb.append("/");
      sb.append(getLastId(uri));
      this.uri = sb.toString();
    } else if (intent.getAction().contains("PHONE_STATE")) {
    } else {
      intentAction = intent.getAction();
      this.uri = getURI(intent);

    }
    matchEventName(intentAction);
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

  public void matchEventName(String intentAction) {
    UGParser ug = new UGParser(getApplicationContext());

    ArrayList<HashMap<String, String>> recs = ug.readbyEventName(intentAction);
    // ArrayList<HashMap<String, String>> UCRecords = ug.readRecords();
    Iterator<HashMap<String, String>> i = recs.iterator();
    while (i.hasNext()) {
      HashMap<String, String> HM1 = i.next();
      // Configure the Intent Filter with the Events if Instance in enabled
      if (HM1.get("EnableInstance").equalsIgnoreCase("True")) {
        filtertype = HM1.get(ug.KEY_FilterType);
        filterdata = HM1.get(ug.KEY_FilterData);
        actionname = HM1.get("ActionName");
        actionapp = HM1.get(ug.KEY_ActionApp);

        // added by Pradeep to populate Omniu CP at runtime
        uridata = HM1.get("ActionData");
        uridata = "CONTACT PHONE NUMBER";
        if (!uridata.contains("content://") && !uridata.equals("")) {
          try{
          uridata = fillURIData(uri, uridata);// Call fillURIData if ActionData contains fields like
          // s_ph_no etc. and not the actual URI.
          }catch(Exception e){OmLogger.write(getApplicationContext(), "Unable to retrieve information from thrower");}
          }

        // boolean val=checkFilter(uri,filtertype,filterdata);
        getCols(uri, filtertype, filterdata, actionapp);

      }
    }
  }

  // Used to populate the action data at Instance time.
  private String fillURIData(String uri2, String uridata2) {
    int cnt = 1;
    String tempstr=null,aData=null;
    Uri uri_ret = null;
    String str_uri = uri2;
    String[] temp = null;
    temp = str_uri.split("/");
    String num = temp[temp.length - 1];
    String final_uri = str_uri.substring(0, str_uri.length() - num.length() - 1);
    int new_id = Integer.parseInt(num);
    if (actionname.equalsIgnoreCase("SMS_RECEIVED"))
      new_id = new_id - 1;
    Cursor cur = managedQuery(Uri.parse(final_uri), null, null, null, null);
    if (cur.moveToFirst()) {
      do {
        int id = Integer.parseInt(cur.getString(cur.getColumnIndex("_id")));
        if (new_id == id) {
          AGParser ag = new AGParser(getApplicationContext());
          ArrayList<StringMap> cm = ag.readContentMap(actionapp);
          Iterator<StringMap> i = cm.iterator();
          StringMap sm = new StringMap();
          while (i.hasNext()) {
            sm = (StringMap) i.next();
            if (sm.get(1).equalsIgnoreCase(uridata2))
              uridata2 = sm.getKey();
          }
          aData = cur.getString(cur.getColumnIndex(uridata2));
          cnt=getBufferCount();
          // Generating buffer name
          tempstr = "temp" + cnt;
          break;
           }
      } while (cur.moveToNext());

    }
    uri_ret=populateInstance(tempstr, aData);
    return uri_ret.toString();
  }

  public int getBufferCount()
  {
    int cnt=1;
    String[] projection = { "i_name", "a_data" };
    ContentValues values = new ContentValues();
    values.put("i_name", "tempcnt");// using tempcnt to store count
    values.put("a_data", "1");
    Cursor cur1 = getContentResolver().query(
        Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"), projection,
        "i_name='tempcnt'", null, null);
    if (cur1.getCount() == 0)
      getContentResolver().insert(
          Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"), values);
    else {
      cur1.moveToFirst();
      // Using buffer of size n to store instance information
      cnt = (Integer.parseInt(cur1.getString(cur1.getColumnIndex("a_data"))) + 1) % 10;
      getContentResolver().delete(Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"),
          "i_name='tempcnt'", null);
      values.clear();
      values.put("i_name", "tempcnt");// using tempcnt to store buffer count
      values.put("a_data", cnt);
      getContentResolver().insert(
          Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"), values);
    }
return cnt;
  }
  
  public Uri populateInstance(String tempstr,String aData)
  {
   Uri uri_ret;
    ContentValues values = new ContentValues();
    values.clear();
    values.put("i_name", tempstr);// using tempstr to store the instance data.
    values.put("a_data", aData);
    String[] projection = { "i_name", "a_data" };
    Cursor cur1 = getContentResolver().query(
        Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"), projection,
        "i_name='" + tempstr + "'", null, null);
    // Checking to see if the temp is populated
    if (cur1.getCount() == 0)
      uri_ret = getContentResolver().insert(
          Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"), values);
    else {
      getContentResolver().delete(Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"),
          "i_name='temp'", null);
      uri_ret = getContentResolver().insert(
          Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"), values);
    }
  return uri_ret;
  }
  
  
  public boolean checkFilter(String uri, String filtertype1, String filterdata1) {

    String str_uri = uri;
    String[] temp = null;
    temp = str_uri.split("/");
    String num = temp[temp.length - 1];
    String final_uri = str_uri.substring(0, str_uri.length() - num.length() - 1);
    int new_id = Integer.parseInt(num);
    if (actionname.equalsIgnoreCase("SMS_RECEIVED"))
      new_id = new_id - 1;
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
    send_intent.setAction(actionname);
    send_intent.putExtra("uri", uridata);
    // sendBroadcast(send_intent);
    // PackageManager pm = this.getPackageManager();
    // try {
    // PackageInfo pi = pm.getPackageInfo(actiondata1, 0);
    AGParser ag = new AGParser(getApplicationContext());
    String pkgname = ag.readPkgName(actiondata1);
    String listener = ag.readListenerClass(actiondata1);
    if (!pkgname.equalsIgnoreCase("")) {
      ComponentName comp = new ComponentName(pkgname, listener);
      send_intent.setComponent(comp);
    }
    // send_intent.setClass(this.getApplicationContext(), pi.getClass());
    // startActivity(send_intent);
    try {
      sendBroadcast(send_intent);
    } catch (Exception e) {
      e.toString();
    }
    Toast.makeText(getApplicationContext(), "Sent!", Toast.LENGTH_SHORT).show();

    // } catch (NameNotFoundException e) {
    // TODO Auto-generated catch block
    // OmLogger.write(getApplicationContext(), actiondata1 + "not installed");
    // e.printStackTrace();
    // }
  }

  public void getCols(String uri, String filtertype1, String filterdata1, String actiondata1) {
    String[] cols = null;
    String str_uri = uri;
    String[] temp = null;
    temp = str_uri.split("/");
    String num = temp[temp.length - 1];
    String final_uri = str_uri.substring(0, str_uri.length() - num.length() - 1);
    int new_id = Integer.parseInt(num);
    if (actionname.equalsIgnoreCase("SMS_RECEIVED"))
      new_id = new_id - 1;

    int flag = 0;

    Cursor cur = managedQuery(Uri.parse(final_uri), null, null, null, null);
    if (cur.moveToFirst()) {

      do {

        int id = Integer.parseInt(cur.getString(cur.getColumnIndex("_id")));
        
        
        String ft = cur.getString(cur.getColumnIndex(filtertype1));

        if (new_id == id) {
          if (filterdata1.equalsIgnoreCase(ft)) {
            {
              if (final_uri.contains("sms")) {
                Toast.makeText(
                    getApplicationContext(),
                    cur.getString(cur.getColumnIndex(filtertype1)) + ":"
                        + cur.getString(cur.getColumnIndex("body")), Toast.LENGTH_LONG).show();
                sendIntent(actiondata1);
              } else {
                try {
                  Toast.makeText(
                      getApplicationContext(),
                      cur.getString(cur.getColumnIndex("s_name")) + ":"
                          + cur.getString(cur.getColumnIndex(filtertype1)), Toast.LENGTH_LONG)
                      .show();
                  sendIntent(actiondata1);
                } catch (Exception e) {
                  OmLogger.write(getApplicationContext(), "Unable to execute action");
                }
              }
            }
            // sendIntent(actiondata1);
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

  public String getLastId(String smsuri) {

    String id = null;
    String lastids = null;
    Cursor c = managedQuery(Uri.parse(smsuri), null, null, null, null);
    if (c.moveToFirst()) {

      id = c.getString(c.getColumnIndex("_id"));
      int lastid = Integer.parseInt(id) - 1;
      lastids = Integer.toString(lastid);
    }

    return id;
  }
}

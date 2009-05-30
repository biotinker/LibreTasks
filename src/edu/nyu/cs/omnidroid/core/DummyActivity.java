package edu.nyu.cs.omnidroid.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.OmLogger;
import edu.nyu.cs.omnidroid.util.StringMap;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * 
 * @author Sucharita
 *
 */
/**
 * This class is the heart of OmniDroid. It picks up the intent received by the Broadcast Receiver
 * and picks up relevant data from the User Config File. It then matches it with the data fetched by
 * querying the content provider of the application that broadcasted the intent, and if there is a
 * match it throws the corresponding intent.
 */
public class DummyActivity extends Activity {

  private String uri;
  Intent intent;
  String filterdata = null;
  String filtertype = null;
  String uridata = null;
  String uridataa2 = null;
  String actionname = null;
  String actionapp = null;
  String intentAction = null;
  String eventapp = null;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
      this.uri = CallLog.Calls.CONTENT_URI.toString();
      intentAction = "PHONE_STATE";
      StringBuilder sb = new StringBuilder(uri);
      sb.append("/");
      sb.append(getLastId(uri));
      this.uri = sb.toString();
    } else {
      intentAction = intent.getAction();
      this.uri = getURI(intent);

    }
    matchEventName(intentAction);
    this.finish();

  }

  public String getURI(Intent intent1) {

    Bundle b = intent1.getExtras();
    Object c = b.get("uri");
    String uri = c.toString();
    return uri;

  }

  public void matchEventName(String intentAction) {
    UGParser ug = new UGParser(getApplicationContext());

    ArrayList<HashMap<String, String>> recs = ug.readbyEventName(intentAction);
    Iterator<HashMap<String, String>> i = recs.iterator();
    while (i.hasNext()) {
      HashMap<String, String> HM1 = i.next();
      // Configure the Intent Filter with the Events if Instance in enabled
      if (HM1.get("EnableInstance").equalsIgnoreCase("True")) {
        filtertype = HM1.get(UGParser.KEY_FILTER_TYPE);
        filterdata = HM1.get(UGParser.KEY_FILTER_DATA);
        actionname = HM1.get(UGParser.KEY_ACTION_TYPE);
        eventapp = HM1.get(UGParser.KEY_EVENT_APP);
        actionapp = HM1.get(UGParser.KEY_ACTION_APP);
        uridata = HM1.get(UGParser.KEY_ACTION_DATA1);
        uridataa2 = HM1.get(UGParser.KEY_ACTION_DATA2);
        if (!uridata.contains("content://") && !uridata.equals("")) {
          try {
            uridata = fillURIData(uri, uridata);// Call fillURIData if ActionData contains fields
                                                // like
            // s_ph_no etc. and not the actual URI.
          } catch (Exception e) {
            OmLogger.write(getApplicationContext(), "Unable to retrieve information from thrower");
          }
        }
        if (!uridataa2.contains("content://") && !uridataa2.equals("")) {
          try {
            uridataa2 = fillURIData(uri, uridataa2);// Call fillURIData if ActionData contains
                                                    // fields like
            // s_ph_no etc. and not the actual URI.
          } catch (Exception e) {
            OmLogger.write(getApplicationContext(), "Unable to retrieve information from thrower");
          }
        }
        checkFilter(uri, filtertype, filterdata, actionapp);

      }
    }
  }

  // Added by Pradeep
  // Used to populate the action data at Instance time.
  private String fillURIData(String uri2, String uridata2) {
    int cnt = 1;
    String tempstr = null, aData = null;
    Uri uri_ret = null;
    String str_uri = uri2;
    String[] temp = null;
    temp = str_uri.split("/");
    String num = temp[temp.length - 1];
    String final_uri = str_uri.substring(0, str_uri.length() - num.length() - 1);
    int new_id = Integer.parseInt(num);
    Cursor cur = managedQuery(Uri.parse(final_uri), null, null, null, null);
    if (cur.moveToFirst()) {
      do {
        int id = Integer.parseInt(cur.getString(cur.getColumnIndex("_id")));
        if (new_id == id) {
          AGParser ag = new AGParser(getApplicationContext());
          ArrayList<StringMap> cm = ag.readContentMap(eventapp);
          Iterator<StringMap> i = cm.iterator();
          StringMap sm = new StringMap();
          while (i.hasNext()) {
            sm = (StringMap) i.next();
            if (sm.get(1).equalsIgnoreCase(uridata2))
              uridata2 = sm.getKey();
          }
          aData = cur.getString(cur.getColumnIndex(uridata2));
          cnt = getBufferCount();
          // Generating buffer name
          tempstr = "temp" + cnt;
          break;
        }
      } while (cur.moveToNext());

    }
    uri_ret = populateInstance(tempstr, aData);
    return uri_ret.toString();
  }

  // Added by Pradeep
  // Retrieves the Buffer Count to generate Buffer for temporarily storing Event Data
  public int getBufferCount() {
    int cnt = 1;
    String[] projection = { "i_name", "a_data" };
    ContentValues values = new ContentValues();
    values.put("i_name", "tempcnt");// using tempcnt to store count
    values.put("a_data", "1");
    Cursor cur1 = getContentResolver().query(
        Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"), projection, "i_name='tempcnt'",
        null, null);
    if (cur1.getCount() == 0)
      getContentResolver().insert(Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"),
          values);
    else {
      cur1.moveToFirst();
      // Using buffer of size n to store instance information
      cnt = (Integer.parseInt(cur1.getString(cur1.getColumnIndex("a_data"))) + 1) % 10;
      getContentResolver().delete(Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"),
          "i_name='tempcnt'", null);
      values.clear();
      values.put("i_name", "tempcnt");// using tempcnt to store buffer count
      values.put("a_data", cnt);
      getContentResolver().insert(Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"),
          values);
    }
    return cnt;
  }

  // Added by Pradeep
  // Used to populate temporary event data(aData) in our temporary buffer(tempstr)
  public Uri populateInstance(String tempstr, String aData) {
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
          "i_name='" + tempstr + "'", null);
      uri_ret = getContentResolver().insert(
          Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"), values);
    }
    return uri_ret;
  }

  // Broadcasts the intent corresponding to the actiondata in the UserConfig file
  public void sendIntent(String actiondata1) {
    Intent send_intent = new Intent();
    send_intent.setAction(actionname);
    send_intent.putExtra("uri", uridata);
    send_intent.putExtra("uri2", uridataa2);
    AGParser ag = new AGParser(getApplicationContext());
    String pkgname = ag.readPkgName(actiondata1);
    String listener = ag.readListenerClass(actiondata1);
    if (!pkgname.equalsIgnoreCase("")) {
      ComponentName comp = new ComponentName(pkgname, listener);
      send_intent.setComponent(comp);
    }
    try {
      sendBroadcast(send_intent);
    } catch (Exception e) {
      e.toString();
    }
    Toast.makeText(getApplicationContext(), "Sent!", Toast.LENGTH_SHORT).show();
  }

  /*
   * matches the filter data from the UserConfig file with the last record from the content provider
   */
  public void checkFilter(String uri, String filtertype1, String filterdata1, String actiondata1) {

    String str_uri = uri;
    String[] temp = null;
    temp = str_uri.split("/");
    String num = temp[temp.length - 1];
    String final_uri = str_uri.substring(0, str_uri.length() - num.length() - 1);
    int new_id = Integer.parseInt(num);

    if (filterdata.equalsIgnoreCase(null) || filterdata.equalsIgnoreCase("")
        || intentAction.contains("PHONE_STATE"))
      sendIntent(actiondata1);

    else {
      try {
        Cursor cur = managedQuery(Uri.parse(final_uri), null, null, null, null);
        cur = managedQuery(Uri.parse(final_uri), null, null, null, null);
        if (cur.moveToFirst()) {
          int id = Integer.parseInt(cur.getString(cur.getColumnIndex("_id")));
          if (new_id == id) {
            String ft = cur.getString(cur.getColumnIndex(filtertype1));

            if (filterdata1.equalsIgnoreCase(ft)) {

              {
                if (final_uri.contains("sms")) {
                  Toast.makeText(
                      getApplicationContext(),
                      cur.getString(cur.getColumnIndex(filtertype1)) + "--->"
                          + cur.getString(cur.getColumnIndex("body")), Toast.LENGTH_LONG).show();

                } else {
                  try {
                    Toast.makeText(
                        getApplicationContext(),
                        cur.getString(cur.getColumnIndex("s_name")) + ":"
                            + cur.getString(cur.getColumnIndex(filtertype1)), Toast.LENGTH_LONG)
                        .show();
                  } catch (Exception e) {
                    OmLogger.write(getApplicationContext(), "Unable to execute action");
                  }
                }
              }
              sendIntent(actiondata1);

            }

          }
        }
      } catch (Exception ex) {
        ex.toString();
      }
    }
  }

  // Retrieve the id of the latest updated row of the content provider
  public String getLastId(String smsuri) {

    String id = null;
    String lastids = null;
    Cursor c = managedQuery(Uri.parse(smsuri), null, null, null, null);
    if (c.moveToFirst()) {
      id = c.getString(c.getColumnIndex("_id"));
      int lastid = Integer.parseInt(id) - 1;
      lastids = Integer.toString(lastid);
    }
    return lastids;

  }
}
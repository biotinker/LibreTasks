package edu.nyu.cs.omnidroid.tests;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.util.*;

/**
 * Test the Application Configuration File
 * 
 * @author acase
 * 
 */
public class TestAppConfig extends Activity {

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    AGParser ag = new AGParser(this.getApplicationContext());
    ag.delete_all();
    ag.write("Application:Test Application");
    ag.write("PkgName:edu.nyu.cs.omnidroid");
    ag.write("ListenerClass:edu.nyu.cs.omnidroid.external.catcherapp.SMSListener");
    ag.write("EventName:Contact_Saved,Contact Saved");
    ag.write("Filters:S_Name,S_Ph_No,Text");    
    ag.write("EventName:Contact_Updated,Contact Updated");
    ag.write("Filters:S_Name,S_Ph_no,Text");
    ag.write("ActionName:Display_Contact,Display Contact");
    ag.write("URIFields:CONTACT PHONE NUMBER,TEXT,ActionData");
    ag.write("ContentMap:");
    ag.write("S_Name,CONTACT NAME,STRING");
    ag.write("S_Ph_No,CONTACT PHONE NUMBER,INT");
    ag.write("Text,TEXT,STRING");
    
    ag.write("Application:SMS");
    ag.write("PkgName:");
    ag.write("ListenerClass:");
    ag.write("EventName:SMS_RECEIVED,SMS RECEIVED");
    ag.write("Filters:address");
    ag.write("EventName:SMS_SENT,SENT SMS");
    ag.write("Filters:address,body");
    ag.write("ActionName: SMS_SEND,SEND SMS");
    ag.write("URIFields:address,body");
    ag.write("ContentMap:");
    //ag.write("S_Name,SENDER NAME,STRING");
   // ag.write("R_Name,RECEIVER NAME,STRING");
    ag.write("address,SENDER PHONE NUMBER,INT");
    ag.write("address,RECEIVER PHONE NUMBER,INT");
    ag.write("body,Text,STRING");
   // ag.write("Location,SMS Number,INT");
    
    ag.write("Application:System");
    ag.write("EventName:android.intent.action.TIME_SET,TIME CHANGED");
    ag.write("EventName:android.intent.action.SCREEN_OFF,SCREEN OFF");
    ag.write("EventName:android.intent.action.SCREEN_ON,SCREEN ON");
    ag.write("EventName:android.intent.action.DATE_CHANGED,DATE CHANGED");
    ag.write("EventName:android.intent.action.NEW_OUTGOING_CALL,NEW OUTGOING CALL");
    ag.write("EventName:android.intent.action.WALLPAPER_CHANGED,WALLPAPER CHANGED");
    ag.write("EventName:android.intent.action.CAMERA_BUTTON,CAMERA CLICK");
    ag.write("EventName:android.intent.action.REBOOT,REBOOT ");
    ag.write("ActionName:android.intent.action.TIME_SET,TIME CHANGED");
    ag.write("ActionName:android.intent.action.SCREEN_OFF,SCREEN OFF");
    ag.write("ActionName:android.intent.action.SCREEN_ON,SCREEN ON");
    ag.write("ActionName:android.intent.action.DATE_CHANGED,DATE CHANGED");
    ag.write("ActionName:android.intent.action.NEW_OUTGOING_CALL,NEW OUTGOING CALL");
    ag.write("ActionName:android.intent.action.WALLPAPER_CHANGED,WALLPAPER CHANGED");
    ag.write("ActionName:android.intent.action.CAMERA_BUTTON,CAMERA CLICK");
    ag.write("ActionName:android.intent.action.REBOOT,REBOOT ");
    
    Toast.makeText(getApplicationContext(), "Population Test App Config File", 5).show();
    this.finish();

  }
}
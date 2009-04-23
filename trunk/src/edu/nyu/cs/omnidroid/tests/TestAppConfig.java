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
    ag.write("Application:Thrower Application");
    ag.write("PkgName:edu.nyu.cs.omnidroid");
    ag.write("ListenerClass:edu.nyu.cs.omnidroid.external.catcherapp.SMSListener");
    ag.write("EventName:SMS_RECEIVED1,SMS RECEIVED1");
    ag.write("Filters:S_Name,S_Ph_No,Text,Location");
    ag.write("EventName:SMS_SENT,SENT SMS");
    ag.write("Filters:R_Name,R_Ph_no,Text");
    ag.write("ActionName:SMS_SENT,SEND SMS");
    ag.write("URIFields:RECEIVER NAME,,Text,ActionData");
    ag.write("ContentMap:");
    ag.write("S_Name,SENDER NAME,STRING");
    ag.write("R_Name,RECEIVER NAME,STRING");
    ag.write("S_Ph_No,SENDER PHONE NUMBER,INT");
    ag.write("R_Ph_No,RECEIVER PHONE NUMBER,INT");
    ag.write("Text,Text,STRING");
    ag.write("Location,SMS Number,INT");
    
    ag.write("Application:SMS");
    ag.write("PkgName:");
    ag.write("ListenerClass:");
    ag.write("EventName:SMS_RECEIVED,SMS RECEIVED");
    ag.write("Filters:S_Name,S_Ph_No,Text,Location");
    ag.write("EventName:SMS_SENT,SENT SMS");
    ag.write("Filters:R_Name,R_Ph_no,Text");
    ag.write("ActionName: SMS_SEND,SEND SMS");
    ag.write("URIFields:R_NAME,R_Ph_No,Text");
    ag.write("ContentMap:");
    ag.write("S_Name,SENDER NAME,STRING");
    ag.write("R_Name,RECEIVER NAME,STRING");
    ag.write("S_Ph_No,SENDER PHONE NUMBER,INT");
    ag.write("R_Ph_No,RECEIVER PHONE NUMBER,INT");
    ag.write("Text,Text,STRING");
    ag.write("Location,SMS Number,INT");
    
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
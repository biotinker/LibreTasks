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
  
  /* (non-Javadoc)
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    AGParser ag=new AGParser(this.getApplicationContext());
    ag.delete_all();
    ag.write("Application:SMS"); 

    ag.write("EventName:SMS_RECEIVED,SMS_RECEIVED");

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

    Toast.makeText(getApplicationContext(),"Population Test App Config File",5).show();
    this.finish();
    
  }
}
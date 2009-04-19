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
//TODO: Write this
  
  /* (non-Javadoc)
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    AGParser ag=new AGParser(this.getApplicationContext());
    ag.delete_all();
    ag.agwrite("Application:SMS"); 

    ag.agwrite("EventName:SMS_RECEIVED,SMS_RECEIVED");

    ag.agwrite("Filters:S_Name,S_Ph_No,Text,Location");

    ag.agwrite("EventName:SMS_SENT,SENT SMS");

    ag.agwrite("Filters:R_Name,R_Ph_no,Text"); 

    ag.agwrite("ActionName: SMS_SEND,SEND SMS");

    ag.agwrite("URIFields:R_NAME,R_Ph_No,Text"); 

    ag.agwrite("ContentMap:"); 

    ag.agwrite("S_Name,SENDER NAME,STRING"); 

    ag.agwrite("R_Name,RECEIVER NAME,STRING"); 

    ag.agwrite("S_Ph_No,SENDER PHONE NUMBER,INT"); 

    ag.agwrite("R_Ph_No,RECEIVER PHONE NUMBER,INT"); 

    ag.agwrite("Text,Text,STRING"); 

    ag.agwrite("Location,SMS Number,INT");     

    Toast.makeText(getApplicationContext(),"Population Test App Config File",5).show();
    this.finish();
    
  }

  /* (non-Javadoc)
   * @see android.app.Activity#onDestroy()
   */
  @Override
  protected void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
  }
  
  
 

  

}

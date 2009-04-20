package edu.nyu.cs.omnidroid.external.catcherapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.core.CP;
public class CatcherAppActivity extends Activity {
    /** Called when the activity is first created.*/ 
  private String uri;
  Intent intent;  
  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main2);
        this.intent=getIntent();
        uri = getURI(intent);
     //  Uri DummyURI = Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP/2");
     displayAction(uri.toString());
     //  String action_data = displayAction(DummyURI.toString());
       // Toast.makeText(getBaseContext(), action_data, 5);
     this.finish();
  } 
       
       public String getURI(Intent intent1)
  {
    Bundle b = intent1.getExtras();
    Object c = b.get("uri");
    String uri=c.toString();
    return uri;
    
  }

   
public void displayAction(String uri) {
  String[] cols = null;
  String str_uri = uri;
    String[] temp = null;
    temp = str_uri.split("/");
    String num = temp[temp.length - 1];
    String final_uri=str_uri.substring(0, str_uri.length()-num.length()-1);
    int new_id = Integer.parseInt(num);
    
    Cursor cur = managedQuery(Uri.parse(final_uri), null, null, null, null);
    if (cur.moveToFirst()) {

      
      do {

        int id = Integer.parseInt(cur.getString(cur.getColumnIndex("_id")));

          
         if (new_id==id)
         {
           
           Toast.makeText(
               getBaseContext(),
               /*cur.getString(cur.getColumnIndex(CP._ID)) + ","
               +*/ cur.getString(cur.getColumnIndex(CP.ACTION_DATA)), Toast.LENGTH_LONG).show();
         }
                    
          
      } while (cur.moveToNext());

      }
    
     /* 
      String str_uri = uri.toString();
      String[] temp = null;
      temp = str_uri.split("/");
      String num = temp[temp.length - 1];
      String final_uri=str_uri.substring(0, str_uri.length()-num.length());
      int new_id = Integer.parseInt(num);
      Cursor cur = managedQuery(Uri.parse(final_uri), null, null, null, null);
     
  
      if (cur.moveToPosition(new_id)) {
        Toast.makeText(
            getBaseContext(),
            cur.getString(cur.getColumnIndex(CP._ID)) + ","
            + cur.getString(cur.getColumnIndex(CP.ACTION_DATA)), Toast.LENGTH_LONG).show();
      }
      String action = cur.getColumnName(cur.getColumnIndex(CP.ACTION_DATA));
      return action;
      */
    }

  }
    



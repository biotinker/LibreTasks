package edu.nyu.cs.omnidroid.tests;

import edu.nyu.cs.omnidroid.contprovider.*;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.core.OmniCP;

public class TestCP extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);
        Button Store;
    	final EditText Desc;
    	final EditText ID;
    	Button Retrieve;
    	Button GetAll;
    	
    	Store=(Button) findViewById(R.id.Store);
        Desc=(EditText) findViewById(R.id.Desc);
        Retrieve=(Button) findViewById(R.id.Retrieve);
        ID=(EditText) findViewById(R.id.ID);
        GetAll=(Button) findViewById(R.id.GetAll);
        Store.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		String desc = Desc.getText().toString();
                if (desc.length()>0) 
                {
                    ContentValues values = new ContentValues();
                    values.put("description", desc);
                    Uri uri = getContentResolver().insert(
                            Uri.parse(
                                "content://edu.nyu.cs.omnidroid.core.cp/CP"), 
                               values);
                    Toast.makeText(getBaseContext(),uri.toString(),Toast.LENGTH_SHORT).show();
                            Log.d("Insert Complete","This is a log");
                	Toast.makeText(getBaseContext(),"Good Job",Toast.LENGTH_SHORT).show();
                	
                  	//SENDING THE INTENT
                   	Intent intent = new Intent(desc, uri);
                   	intent.putExtra("uri", uri.toString());
        	       	sendBroadcast(intent);
                    
                }
                	else
                	  		
              Toast.makeText(getBaseContext(),"No Value",Toast.LENGTH_SHORT).show();
            
          }
        });  
        
        Retrieve.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		String id = ID.getText().toString();
        		if (id.length()>0) 
                {
        			Uri OmniURI = Uri.parse(
                    "content://edu.nyu.cs.omnidroid.core.cp/CP");
                    Cursor c = managedQuery(OmniURI, null, null, null, null);
                    int new_id =Integer.parseInt(id);
                    
                           if (c.moveToPosition(new_id))
                           {
                    		Toast.makeText(getBaseContext(), 
                              c.getString(c.getColumnIndex(
                              OmniCP._ID)) + ", " +                     
                              c.getString(c.getColumnIndex(
                                 OmniCP.DESCRIPTION)), 
                              Toast.LENGTH_LONG).show();
                           }
                     Toast.makeText(getBaseContext(),"Good Job 2",Toast.LENGTH_SHORT).show();
                }
                	else
                	
              Toast.makeText(getBaseContext(),"No Value",Toast.LENGTH_SHORT).show();
            
          }
        });
        
        GetAll.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		/*CProvider cp = new CProvider();
        		cp.displayRecords("content://com.omnidroid.provider.CP/CP");*/
        	  		Uri OmniURI = Uri.parse(
                        "content://edu.nyu.cs.omnidroid.core.cp/CP");
                     Cursor c = managedQuery(OmniURI, null, null, null, null);
                     if (c.moveToFirst()) {
                        do{
                           Toast.makeText(getBaseContext(), 
                              c.getString(c.getColumnIndex(
                              OmniCP._ID)) + ", " +                     
                              c.getString(c.getColumnIndex(
                                 OmniCP.DESCRIPTION)), 
                              Toast.LENGTH_LONG).show();               
                        } while (c.moveToNext());
                     } 
              
            }
          
        });    
       /*ContentValues values = new ContentValues();
        values.put("description", "This is a test");
        values.put("description", "This is the second test");
        values.put("description", "This is the third test");
        values.put("description", "This is the fourth test");
        values.put("description", "This is the final test");
        Uri uri = getContentResolver().insert(
        Uri.parse(
           "content://com.omnidroid.provider.CP/CP"), 
           values);
        Log.d("Insert Complete","This is a log");
        */
      /*  Uri OmniURI = Uri.parse(
        "content://com.omnidroid.provider.CP/CP");
     Cursor c = managedQuery(OmniURI, null, null, null, "description desc");
     if (c.moveToFirst()) {
        do{
           Toast.makeText(this, 
              c.getString(c.getColumnIndex(
              OmniCP._ID)) + ", " +                     
              c.getString(c.getColumnIndex(
                 OmniCP.DESCRIPTION)), 
              Toast.LENGTH_LONG).show();               
        } while (c.moveToNext());
     }

        */
    }
    
}
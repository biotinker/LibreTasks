package edu.nyu.cs.omnidroid.contprovider;



import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import edu.nyu.cs.omnidroid.util.*;
import edu.nyu.cs.omnidroid.R;

import android.widget.Toast;



public class CProvider {
	
	private Activity a;
	private Context context;




	public CProvider(Activity a)
	{
		this.a=a;		
	}
	
	

	
	public String[] displayRecords(String uri) {

// put the uri in the content:// format in the managedQuery
	Cursor cur = a.managedQuery(Uri.parse(uri), null, 

	null, 

	null, 

	null 

	);
	String[] cols = null;

	if (cur.moveToFirst()) {

		
	do {

			 cols = cur.getColumnNames();
			
			
		
	} while (cur.moveToNext());

	}
	
	
	AGParser ag = new AGParser(this.context);
	boolean ap;
	ap=ag.agwrite("Application:SMS");
    ag.agwrite("EventName:SMS_RECEIVED,RECEIVED SMS");
    ag.agwrite("Filters:S_Name,S_Ph_No,Text,Location");
    ag.agwrite("EventName:SMS_SENT,SENT SMS");
    ag.agwrite("Filters:R_Name,R_Ph_no,Text");
    ag.agwrite("ActionName:SMS_SEND,SEND SMS");
    ag.agwrite("URIFields:R_NAME,R_Ph_No,Text");
    ag.agwrite("ContentMap:");
    ag.agwrite("S_Name,SENDER NAME,STRING");
    ag.agwrite("R_Name,RECEIVER NAME,STRING ");
    ag.agwrite("S_Ph_No,SENDER PHONE NUMBER,INT");
    ag.agwrite("R_Ph_No,RECEIVER PHONE NUMBER,INT");
    ag.agwrite("Text,Text,STRING");
    ag.agwrite("Location,SMS Number,INT");
    
    ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
    al = ag.readEvents("SMS");
		/*for(int i = 0; i < cols.length; i++)
				{
			ag.agwrite(cols[i]);
		
			//Toast.makeText(this, cols[i], Toast.LENGTH_LONG).show();
				}	*/
		
		/*
		for(int j = 0; j < cols.length; j++)
		{

	Toast.makeText(context, line[j], Toast.LENGTH_LONG).show();
		}*/
return cols;
	}
}
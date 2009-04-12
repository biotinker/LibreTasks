package edu.nyu.cs.omnidroid.contprovider;



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
	ag.write("Application:SMS");
    ag.write("EventName:SMS_RECEIVED,RECEIVED SMS");
    ag.write("Filters:S_Name,S_Ph_No,Text,Location");
    ag.write("EventName:SMS_SENT,SENT SMS");
    ag.write("Filters:R_Name,R_Ph_no,Text");
    ag.write("ActionName:SMS_SEND,SEND SMS");
    ag.write("URIFields:R_NAME,R_Ph_No,Text");
    ag.write("ContentMap:");
    ag.write("S_Name,SENDER NAME,STRING");
    ag.write("R_Name,RECEIVER NAME,STRING ");
    ag.write("S_Ph_No,SENDER PHONE NUMBER,INT");
    ag.write("R_Ph_No,RECEIVER PHONE NUMBER,INT");
    ag.write("Text,Text,STRING");
    ag.write("Location,SMS Number,INT");
		/*for(int i = 0; i < cols.length; i++)
				{
			ag.write(cols[i]);
		
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
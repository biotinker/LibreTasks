package edu.nyu.cs.omnidroid.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import edu.nyu.cs.omnidroid.util.*;

public class ProcessIntent {
	private Intent intent;
	private Bundle bundle;
	private Uri uri;
	private static final String EVENT_NAME = "SMS_RECEIVED";
	private Context context;
	//Activity a;
	String filterdata = null;

	
	
	public ProcessIntent(Intent intent,Context context)
	{
		this.intent=intent;
		this.uri = Uri.parse("content://com.external.cp/CP");
		intent.putExtra("uri", this.uri.toString());

		this.context=context;
	}
	
	public String getURI()
	{
		/*bundle=intent.getExtras();
		Set<String> keys = bundle.keySet();*/
		Bundle b = intent.getExtras();
		Object c = b.get("uri");
		String uri=c.toString();
		return uri;
		
	}
	
	public String matchEventName()
	{
		 UGParser ug = new UGParser(context);
		ArrayList<HashMap<String, String>> recs = ug.readbyEventName(intent.getAction());
		//ArrayList<HashMap<String, String>> UCRecords = ug.readRecords();
        Iterator<HashMap<String, String>> i = recs.iterator();
        while (i.hasNext()) {
          HashMap<String, String> HM1 = i.next();
          // Configure the Intent Filter with the Events if Instance in enabled
          if (HM1.get("EnableInstance").equalsIgnoreCase("True"))
            filterdata = HM1.get("FilterData");
        }
		return filterdata;
	}
	
	/*public String[] getActionData()
	{
		Cursor cur = context.getApplicationContext().managedQuery(uri, null, null, null, null);
				String[] cols = null;

				if (cur.moveToFirst()) {

					
				do {

						 cols = cur.getColumnNames();
						
				} while (cur.moveToNext());

				}
		return cols;
	}
	*/

}

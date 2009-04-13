package edu.nyu.cs.omnidroid.contprovider;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

public class CProvider {
	Activity a;
	

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
	
return cols;
	}
}
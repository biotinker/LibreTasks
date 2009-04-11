package edu.nyu.cs.omnidroid.contentprovider;


import edu.nyu.cs.omnidroid.util.*;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Toast;



public class ContentProvider extends Activity {
	@Override

	public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	//setContentView(R.layout.main);

	String[] cols=displayRecords();
	
	
	AGParser ag = new AGParser(this);
	ag.OpenFileWrite(2);
		for(int i = 0; i < cols.length; i++)
				{
			ag.write(cols[i]);
		
			//Toast.makeText(this, cols[i], Toast.LENGTH_LONG).show();
				}	
		
		String line[] = new String[100];
		line = ag.allRead();
		
		
		for(int j = 0; j < cols.length; j++)
		{

	Toast.makeText(this, line[j], Toast.LENGTH_LONG).show();
		}
	
	}

	
	
	
	

	
	
	
	private String[] displayRecords() {

// put the uri in the content:// format in the managedQuery
	Cursor cur = managedQuery(Uri.parse("content://call_log/calls"), null, 

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
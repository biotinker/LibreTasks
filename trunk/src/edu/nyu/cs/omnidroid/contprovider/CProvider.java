package edu.nyu.cs.omnidroid.contprovider;



import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import edu.nyu.cs.omnidroid.util.*;
import edu.nyu.cs.omnidroid.R;

import android.widget.Toast;



public class CProvider extends Activity {
	@Override

	public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setContentView(R.layout.main);

	
	
	}

	
	public void displayRecords(String uri) {

// put the uri in the content:// format in the managedQuery
	Cursor cur = managedQuery(Uri.parse(uri), null, 

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
}
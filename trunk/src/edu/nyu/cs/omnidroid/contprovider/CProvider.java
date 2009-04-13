package edu.nyu.cs.omnidroid.contprovider;



import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import edu.nyu.cs.omnidroid.util.AGParser;




public class CProvider {
	Activity a;
	

	public CProvider(Activity a)
	{
		this.a=a;	
		
	}
	
	

	
	public String[] displayRecords(String uri) {
		AGParser ag = new AGParser(a.getApplicationContext());
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
	
	
	
    ag.delete_all();
    
    ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
    
    boolean ap=ag.agwrite("SMS");
    //al = ag.readEvents("SMS");
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
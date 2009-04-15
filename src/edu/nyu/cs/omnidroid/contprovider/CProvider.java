package edu.nyu.cs.omnidroid.contprovider;

import java.util.ArrayList;

import edu.nyu.cs.omnidroid.core.CP;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

// TODO: Please add documentation for this class.
public class CProvider {
	Activity a;
	

	public CProvider(Activity a)
	{
		this.a=a;	
		
	}
	
	public ArrayList<String> displayRecords(String uri) {
// put the uri in the content:// format in the managedQuery
	Cursor cur = a.managedQuery(Uri.parse(uri), null, null, null, null);
	String a_data = "22222";
	ArrayList<String> al = new ArrayList<String>();
	/*
	String[] temp = null;
    temp = uri.split("/");
    String num = temp[temp.length - 1];
    int new_id = Integer.parseInt(num);
    
    if (cur.moveToPosition(new_id)) {
    	String c = cur.getString(cur.getColumnIndex("a_data"));
    	if(a_data.equalsIgnoreCase(cur.getString(cur.getColumnIndex("a_data"))))
		 {
	 Toast.makeText(
               a.getApplicationContext(),
               cur.getString(cur.getColumnIndex("_id")) + ", "
               + cur.getString(cur.getColumnIndex("a_data")) , Toast.LENGTH_LONG).show();
		 al.add(cur.getString(cur.getColumnIndex("_id")));
		 al.add(cur.getString(cur.getColumnIndex("a_data")));
		 al.add(cur.getString(cur.getColumnIndex("i_name")));

		 }
      
    }
    String action = cur.getColumnName(cur.getColumnIndex(CP.ACTION_DATA));
	*/
   // String[] cols = null;
	
	if (cur.moveToFirst()) {

		
	do {


	//		 cols = cur.getColumnNames();
			 if(a_data.equalsIgnoreCase(cur.getString(cur.getColumnIndex("a_data"))))
					 {
				 Toast.makeText(
			                a.getApplicationContext(),
			                cur.getString(cur.getColumnIndex("_id")) + ", "
		                    + cur.getString(cur.getColumnIndex("a_data")) , Toast.LENGTH_LONG).show();
					 al.add(cur.getString(cur.getColumnIndex("_id")));
					 al.add(cur.getString(cur.getColumnIndex("a_data")));
					 al.add(cur.getString(cur.getColumnIndex("i_name")));
		
					 }
			
	} while (cur.moveToNext());

	}
	
return al;
	}
}
package edu.nyu.cs.omnidroid.contprovider;



import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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
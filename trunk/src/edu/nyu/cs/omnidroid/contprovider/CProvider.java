package edu.nyu.cs.omnidroid.contprovider;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;
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
    ag.agwrite("Application:SMS");
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
    
    ArrayList<HashMap<String, String>> eArrayList = ag.readEvents("SMS");
    Iterator<HashMap<String, String>> i1 = eArrayList.iterator();
    while (i1.hasNext()) {
      HashMap<String, String> HM1 = i1.next();
      Toast.makeText(a.getBaseContext(), HM1.toString(), 5).show();
    }

    ArrayList<HashMap<String, String>> aArrayList = ag.readActions("SMS");
    Iterator<HashMap<String, String>> i2 = aArrayList.iterator();
    while (i2.hasNext()) {
      HashMap<String, String> HM1 = i2.next();
       Toast.makeText(a.getBaseContext(), HM1.toString(), 5).show();
    }

    ArrayList<String> FilterList = ag.readFilters("SMS", "SMS_RECEIVED");
    Iterator<String> i3 = FilterList.iterator();
    while (i3.hasNext()) {
      String filter = i3.next();
       Toast.makeText(a.getBaseContext(), filter.toString(), 5).show();
    }

    ArrayList<String[]> contentmap = ag.readContentMap("SMS");
    Iterator<String[]> i4 = contentmap.iterator();
    while (i4.hasNext()) {
      String[] fieldmap = i4.next();
      // Toast.makeText(getBaseContext(),
      // fieldmap[0].toString()+fieldmap[1].toString()+fieldmap[2].toString(), 5).show();
    }
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
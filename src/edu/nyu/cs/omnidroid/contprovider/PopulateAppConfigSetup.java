package edu.nyu.cs.omnidroid.contprovider;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.util.AGParser;



public class PopulateAppConfigSetup {
	private Context context;
	AGParser ag;

	
	public PopulateAppConfigSetup(Context a)
	{
			
		this.context=a;	
        ag = new AGParser(a);
	}
	
	public void populate()
	{
		boolean ap;
		ag.delete_all();
	    ag.agwrite("Application:SMS");
	    ag.agwrite("EventName:SMS_RECEIVED,RECEIVED SMS");
	    ag.agwrite("Filters:S_Name,S_Ph_No,Text");
	    ag.agwrite("ActionName:SMS_SEND,SEND SMS");
	    ag.agwrite("ContentMap:");
	    ag.agwrite("S_Name,SENDER NAME,STRING");
	    ag.agwrite("S_Ph_No,SENDER PHONE NUMBER,INT");
	    ag.agwrite("Text,Text,STRING");
	}
	
	

	
	public void retrieveAppCfg() {
	
    
    ArrayList<HashMap<String, String>> eArrayList = ag.readEvents("SMS");
    Iterator<HashMap<String, String>> i1 = eArrayList.iterator();
    while (i1.hasNext()) {
      HashMap<String, String> HM1 = i1.next();
      Toast.makeText(context, HM1.toString(), 5).show();
    }

    ArrayList<HashMap<String, String>> aArrayList = ag.readActions("SMS");
    Iterator<HashMap<String, String>> i2 = aArrayList.iterator();
    while (i2.hasNext()) {
      HashMap<String, String> HM1 = i2.next();
       Toast.makeText(context, HM1.toString(), 5).show();
    }

    ArrayList<String> FilterList = ag.readFilters("SMS", "SMS_RECEIVED");
    Iterator<String> i3 = FilterList.iterator();
    while (i3.hasNext()) {
      String filter = i3.next();
       Toast.makeText(context, filter.toString(), 5).show();
    }

    ArrayList<String[]> contentmap = ag.readContentMap("SMS");
    Iterator<String[]> i4 = contentmap.iterator();
    while (i4.hasNext()) {
      String[] fieldmap = i4.next();
     // Toast.makeText(a.getBaseContext(), fieldmap.toString(), 5).show();
	}
}
}
package edu.nyu.cs.omnidroid.uiresearchsimple;

import java.util.ArrayList;

import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelEvent;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelFilter;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelFilterContact;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelFilterDateTime;


/**
 * This class is supposed to represent all events and
 * filters loaded from the database so we can render
 * them quickly in our different UI screens.
 */
public class DatabaseMemCache
{
	private static DatabaseMemCache mInstance;
	private ArrayList<ModelEvent> mEvents;
	private ArrayList<ModelFilter> mFilters;
	
	
	private DatabaseMemCache() {
		mEvents = new ArrayList<ModelEvent>();
		mFilters = new ArrayList<ModelFilter>();
	}
	
	public static DatabaseMemCache instance() {
		if (mInstance == null) {
			mInstance = new DatabaseMemCache();
		}
		return mInstance;
	}
	
	/**
	 * For now, simulates loading all supported events and filters and
	 * tasks from the database. We just make some dummy ones for now.
	 */
	public void reloadFromDatabase() {
		mEvents.clear();
		mFilters.clear();
		
		// TODO: Load actual list of events.
		mEvents.add(new ModelEvent("SMS recv", R.drawable.icon_event_sms_received, "An SMS receive event."));
		mEvents.add(new ModelEvent("Phone call recv", R.drawable.icon_event_phone_received, "An phone call receive event."));
		mEvents.add(new ModelEvent("email recv", R.drawable.icon_event_email_received, "An email receive event."));
    	
		// TODO: Load actual list of filters.
		mFilters.add(new ModelFilterContact(""));
		mFilters.add(new ModelFilterDateTime(""));
	}
	
	public ArrayList<ModelEvent> getEvents() {
		return mEvents;
	}
	
	public ArrayList<ModelFilter> getFilters() {
		return mFilters;
	}
}
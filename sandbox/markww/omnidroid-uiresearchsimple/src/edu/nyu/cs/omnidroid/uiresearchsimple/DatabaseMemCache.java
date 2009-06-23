package edu.nyu.cs.omnidroid.uiresearchsimple;

import java.util.ArrayList;

import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelEvent;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelFilter;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelFilterContact;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelFilterDateTime;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelTask;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelTaskSms;


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
	private ArrayList<ModelTask> mTasks;
	
	
	private DatabaseMemCache() {
		mEvents = new ArrayList<ModelEvent>();
		mFilters = new ArrayList<ModelFilter>();
		mTasks = new ArrayList<ModelTask>();
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
		mTasks.clear();
		
		// TODO: Load actual list of events.
		mEvents.add(new ModelEvent("SMS recv", R.drawable.icon_event_sms_received, "An SMS receive event."));
		mEvents.add(new ModelEvent("Phone call recv", R.drawable.icon_event_phone_received, "An phone call receive event."));
		mEvents.add(new ModelEvent("email recv", R.drawable.icon_event_email_received, "An email receive event."));
    	
		// TODO: Load actual list of filters.
		mFilters.add(new ModelFilterContact(""));
		mFilters.add(new ModelFilterDateTime(""));
		
		// TODO: Load actual list of filters.
		mTasks.add(new ModelTaskSms("", ""));
	}
	
	public ArrayList<ModelEvent> getEvents() {
		return mEvents;
	}
	
	public ArrayList<ModelFilter> getFilters() {
		return mFilters;
	}
	
	public ArrayList<ModelTask> getTasks() {
		return mTasks;
	}
}
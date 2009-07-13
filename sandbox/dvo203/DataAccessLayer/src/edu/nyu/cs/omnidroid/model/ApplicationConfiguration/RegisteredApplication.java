/*
  Notes:   
  public static final String KEY_APPLICATION = "Application";
  public static final String KEY_PACKAGE_NAME = "PkgName";
  public static final String KEY_LISTENER_CLASS = "ListenerClass";
  public static final String KEY_EVENT_TYPE = "EventName";
  public static final String KEY_FILTERS = "Filters";
  public static final String KEY_ACTION_TYPE = "ActionName";
  public static final String KEY_URI_FIELDS = "URIFields";
  public static final String KEY_CONTENT_MAP = "ContentMap";
 */
package edu.nyu.cs.omnidroid.model.ApplicationConfiguration;

import java.util.*;

/**
 * Application is an immutable class that represents an application that OmniDroid can interface with, 
 * as well as provides static methods to obtain and manipulate application information.
 * @author Dmitriy Ofenbakh
 *
 */
public class RegisteredApplication {
	private String appName = null;
	private String packageName = null;
	private int appID = 0;
	private boolean enabled = false;
	
	private static HashMap<String, RegisteredApplication> registeredApps = new HashMap<String, RegisteredApplication>();
	private HashMap<String, RegisteredAction> registeredActions = new HashMap<String, RegisteredAction>();
	private HashMap<String, RegisteredEvent> registeredEvents = new HashMap<String, RegisteredEvent>();
		
	public static void LoadRegisteredApplication() {
		register("SMS", "SMSPackage", true);
		register("Email", "EmailPackage", true);
		register("Phone", "PhonePackage", true);
		
		getByName("SMS").registerAction("Send");
		getByName("SMS").registerEvent("Sent");
		getByName("SMS").registerEvent("Received");

		getByName("Email").registerAction("Send");
		getByName("Email").registerEvent("Sent");
		getByName("Email").registerEvent("Received");

		getByName("Phone").registerAction("Dial");
		getByName("Phone").registerAction("IgnoreCall");
		getByName("Phone").registerEvent("CallReceived");
		getByName("Phone").registerEvent("CallDialed");
}
	
	private static int getNextAppID() {
		// TODO: This is a really bad implementation of it. I will change it later
		int maxID = 0;
		
		List<RegisteredApplication> apps = getAll();
		if(apps != null) { 
			for(int i=0; i<apps.size(); i++) {
				RegisteredApplication nextApp = apps.get(i);
				if(nextApp.getAppID() > maxID) maxID = nextApp.getAppID();
			}
		}
		return (maxID + 1);
	}
	
	private int getNextActionID() {
		// TODO: This is a really bad implementation of it. I will change it later
		int maxID = 0;
		
		List<RegisteredAction> actions = getActions();
		for(int i=0; i<actions.size(); i++) {
			RegisteredAction nextAction = actions.get(i);
			if(nextAction.getAppID() > maxID) maxID = nextAction.getAppID();
		}
		
		return (maxID + 1);
	}
	
	private int getNextEventID() {
		// TODO: This is a really bad implementation of it. I will change it later
		int maxID = 0;
		
		List<RegisteredEvent> events = getEvents();
		for(int i=0; i<events.size(); i++) {
			RegisteredEvent nextEvent = events.get(i);
			if(nextEvent.getAppID() > maxID) maxID = nextEvent.getAppID();
		}
		
		return (maxID + 1);
	}
	
	public RegisteredApplication(int appID, String appName, String packageName, boolean enabled) {
		this.appName = appName;
		this.packageName = packageName;
		this.enabled = enabled;
		this.appID = appID;
	}
	
	/**
	 * Static method that allows to obtain application object by searching on application name.
	 * Application names are unique.  
	 * @param appName - name of the application to return. Application names are unique.
	 * @return Application - application object represents information about application or null if no matching application is found.
`	 */
	public static RegisteredApplication getByName(String appName) {
		return registeredApps.get(appName);
	}
	
	/**
	 * Static method that allows to obtain application object by searching on application ID.
	 * Application IDs are unique.  
	 * @param appID - id of the application to return. Application IDs are unique.
	 * @return Application - application object represents information about application or null if no matching application is found.
`	 */
	public static RegisteredApplication getByID(int appID) {
		List<RegisteredApplication> apps = getAll();
		
		for(int i=0; i<apps.size(); i++) {
			RegisteredApplication nextApp = apps.get(i);
			if(nextApp.getAppID() == appID) return nextApp;
		}
		return null;
	}
	
	/**
	 * Static method that allows to obtain all registered application objects.
	 * @return List<Application> - list of application objects that represent information from all registered applications or null if no applications are registered.
`	 */
	public static List<RegisteredApplication> getAll() {
		if(registeredApps.size() <= 0) return null;
		return new ArrayList<RegisteredApplication>(registeredApps.values());
	}
	
	/**
	 * Static method that allows to obtain names of all registered application objects. Application names are unique.
	 * @return List<String> - list of registered application names or null if no applications are registered.
`	 */
	public static List<String> getAllNames() {
		return new ArrayList<String>(registeredApps.keySet());
	}
	
	/**
	 * Deregisters all applications and associated events
	 */
	public static void deregisterAll() {
		List<RegisteredApplication> apps = getAll();

		for(int i=0; i<apps.size(); i++) {
			apps.get(i).deregister();
		}
	}

	/**
	 * Registers application
	 * @param appName - application name (must be unique)
	 * @param appID - application ID (must be unique)
	 * @param packageName - application package name
	 * @param enabled - enabled/disabled (true - enabled, false - disabled) 
	 * @return boolean - true if registration completed successfully, false if registration failed.
	 */
	public static boolean register(String appName, String packageName, boolean enabled) {
		registeredApps.put(appName, new RegisteredApplication(getNextAppID(), appName, packageName, enabled));
		//System.out.println("There are " + registeredApps.size() + " registered apps.");
		return true;
	}

	/**
	 * Deregisters the application and all associated events.
	 * Will disable all of the associated rules.
	 */
	public void deregister() {
		registeredApps.remove(getAppName());
		// TODO: Deregister events.
	}

	/**
	 * Get application ID.
	 * @return int - application ID
	 */
	public int getAppID() {
		return appID;
	}
	
	/**
	 * Get application name.
	 * @return String - application name
	 */
	public String getAppName() {
		return appName;
	}
	
	/**
	 * Get application package name.
	 * @return String - application package name
	 */
	public String getPackageName() {
		return packageName;
	}
	
	/**
	 * Checks if application is active and enabled to send events and receive actions.
	 * @return boolean - application package name (true - enabled, false - disabled)
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Enable the application.
	 */
	public void enable() {
		enabled = true;
	}

	/**
	 * Disable the application.
	 */
	public void disable() {
		enabled = false;
	}

	/**
	 * Get list of events supported by the application.
	 * @return List<RegisteredEvents> - list of supported events
	 */
	public List<RegisteredEvent> getEvents() {
		return new ArrayList<RegisteredEvent>(registeredEvents.values());
	}
	
	public List<String> getEventNames() {
		return new ArrayList<String>(registeredEvents.keySet());
	}

	/**
	 * Get list of actions supported by the application.
	 * @return List<Intent> - list of supported actions
	 */
	public List<RegisteredAction> getActions() {
		return new ArrayList<RegisteredAction>(registeredActions.values());
	}
	
	public List<String> getActionNames() {
		return new ArrayList<String>(registeredActions.keySet());
	}

	/**
	 * Deletes application event and associated event parameters
	 * @param eventName - name of the event to remove.
	 * @return boolean - true if operation completed successfully, false if operation failed.
	 */
	public boolean deleteEventByName(String eventName) {
		return true;
	}
	
	/**
	 * Deletes application event and associated event parameters
	 * @param eventID - ID of the event to remove.
	 * @return boolean - true if operation completed successfully, false if operation failed.
	 */
	public boolean deleteEventByID(int eventID) {
		return true;
	}
	
	/**
	 * Deletes application action and associated action parameters
	 * @param actionName - name of the action to remove.
	 * @return boolean - true if operation completed successfully, false if operation failed.
	 */
	public boolean deleteActionByName(String actionName) {
		return true;
	}
	
	/**
	 * Deletes application action and associated action parameters
	 * @param actionID - ID of the action to remove.
	 * @return boolean - true if operation completed successfully, false if operation failed.
	 */
	public boolean deleteActionByID(int actionID) {
		return true;
	}
	
	public boolean registerAction(String actionName) {
		registeredActions.put(actionName, new RegisteredAction(getNextActionID(), actionName, appID));
		return true;
	}
	
	public boolean registerEvent(String eventName) {
		registeredEvents.put(eventName, new RegisteredEvent(getNextEventID(), eventName, appID));
		return true;
	}
}

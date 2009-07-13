package edu.nyu.cs.omnidroid.model.ApplicationConfiguration;

import java.util.*;

import edu.nyu.cs.omnidroid.model.ApplicationConfiguration.IntentParameter;

/**
 * Occurrence is an immutable class that represents an OmniDroid action or event. 
 * @author Dmitriy Ofenbakh
 *
 */
public class Intent {
	private int id = 0;
	private String name = null;
	private int appID = 0;
	
	public Intent(int id, String name, int appID) {
		this.id = id;
		this.name = name;
		this.appID = appID;
	}
	
	/**
	 * Method used to access occurrence ID.
	 * Returns the unique occurrence ID. 
	 * The ID is unique across all occurrences and all applications.
	 * @return int - unique occurrence ID
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Method used to obtain occurrence name.
	 * Returns the name of the occurrence.
	 * @return String - Name of the occurrence. (Occurrence name is unique within an application)
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Method used to obtain unique parent application ID.
	 * Returns the ID of the parent application the occurrence belongs to.
	 * @return int - unique application ID
	 */
	public int getAppID() {
		return appID;
	}
	
	/**
	 * Method used to obtain occurrence's parameter list.
	 * Returns a list of OccurenceParameter objects that represent occurrence parameters.
	 * @return List<OccurrenceParameter> - list of occurrence parameters
	 */
	public List<IntentParameter> getParameters() {
		return null;
	}
}

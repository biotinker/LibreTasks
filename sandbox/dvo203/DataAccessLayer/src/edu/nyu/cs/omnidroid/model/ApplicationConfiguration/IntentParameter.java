package edu.nyu.cs.omnidroid.model.ApplicationConfiguration;

/**
 * ActionParameter is an immutable class that represents parameters of OmniDroid actions. 
 * @author Dmitriy Ofenbakh
 *
 */public class IntentParameter {
	private int eventID; 
	 
	/**
	 * @return the eventID
	 */
	public int getEventID() {
		return eventID;
	}

	/**
	 * Method used to access action parameter ID.
	 * Returns the unique action parameter ID. 
	 * The ID is unique across all action parameters, all actions and all applications.
	 * @return int - unique action parameter ID
	 */
	public int getID() {
		return 0;
	}
	
	/**
	 * Method used to obtain action parameter name. The parameter's name will be used for display purposes UI.
	 * Returns the name of the action parameter. The parameter name is unique within an action. 
	 * @return String - Name of the action parameter.
	 */
	public String getName() {
		return null;
	}
	
	/**
	 * Method used to obtain action parameter type. The parameter's type will be used to determine the values that are allowed to be entered.
	 * Returns the name of the action parameter. The parameter name is unique within an action. 
	 * @return int - Action parameter type.
	 */
	public int getDataType() {
		return 0;
	}
}

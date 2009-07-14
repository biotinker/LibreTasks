package edu.nyu.cs.omnidroid.model.ApplicationConfiguration;

import edu.nyu.cs.omnidroid.model.DataTypes.DataType;

/**
 * IntentParameter is an immutable class that represents parameters of OmniDroid actions, events, and external parameters. 
 * @author Dmitriy Ofenbakh
 *
 */public class IntentParameter {
	private int intentID; 
	 
	/**
	 * @return the eventID
	 */
	public int getIntentID() {
		return intentID;
	}

	/**
	 * Method used to access intent parameter ID.
	 * Returns the unique intent parameter ID. 
	 * The ID is unique across all intent parameters, all intents and all applications.
	 * @return int - unique intent parameter ID
	 */
	public int getID() {
		return 0;
	}
	
	/**
	 * Method used to obtain intent parameter name. The parameter's name will be used for display purposes UI.
	 * Returns the name of the intent parameter. The parameter name is unique within an intent. 
	 * @return String - name of the intent parameter.
	 */
	public String getName() {
		return null;
	}
	
	/**
	 * Method used to obtain intent parameter data type. The parameter's data type will be used to determine the values that are allowed to be entered.
	 * Returns intent parameter data type object.
	 * @return DataType - intent parameter data type.
	 */
	public DataType getDataType() {
		return null;
	}
}

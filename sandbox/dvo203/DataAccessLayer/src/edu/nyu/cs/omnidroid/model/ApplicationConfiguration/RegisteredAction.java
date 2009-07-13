package edu.nyu.cs.omnidroid.model.ApplicationConfiguration;


/**
 * Action is an immutable class that represents OmniDroid actions. 
 * @author Dmitriy Ofenbakh
 *
 */public class RegisteredAction extends Intent{

	public RegisteredAction(int actionID, String actionName, int appID) {
		super(actionID, actionName, appID);
	}
}

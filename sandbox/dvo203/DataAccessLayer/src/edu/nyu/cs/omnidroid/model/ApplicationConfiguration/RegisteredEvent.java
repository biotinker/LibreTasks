package edu.nyu.cs.omnidroid.model.ApplicationConfiguration;


/**
 * Event is an immutable class that represents OmniDroid events. 
 * @author Dmitriy Ofenbakh
 *
 */public class RegisteredEvent extends Intent{

	public RegisteredEvent(int eventID, String eventName, int appID) {
		super(eventID, eventName, appID);
	}
}

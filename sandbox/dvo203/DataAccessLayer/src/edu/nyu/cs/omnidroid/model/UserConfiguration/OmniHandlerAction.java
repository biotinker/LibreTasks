/**
 * 
 */
package edu.nyu.cs.omnidroid.model.UserConfiguration;

import java.util.List;

/**
 * @author user
 *
 */
public class OmniHandlerAction {
	private int id;
	private int handlerID;
	private int actionID;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the handlerID
	 */
	public int getHandlerID() {
		return handlerID;
	}
	
	/**
	 * @return the actionID
	 */
	public int getActionID() {
		return actionID;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<OmniHandlerActionParameters> getParameters() {
		return null;
	}
}

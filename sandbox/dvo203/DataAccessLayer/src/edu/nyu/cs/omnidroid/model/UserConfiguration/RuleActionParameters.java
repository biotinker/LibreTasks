/**
 * 
 */
package edu.nyu.cs.omnidroid.model.UserConfiguration;

/**
 * @author user
 *
 */
public class RuleActionParameters {
	private int id;
	private int handerActionID;
	private int registeredActionParameterID;
	private String data;
	
	/**
	 * @return the handerActionID
	 */
	public int getHanderActionID() {
		return handerActionID;
	}
	
	/**
	 * @param handerActionID the handerActionID to set
	 */
	public void setHanderActionID(int handerActionID) {
		this.handerActionID = handerActionID;
	}
	
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the registeredActionParameterID
	 */
	public int getRegisteredActionParameterID() {
		return registeredActionParameterID;
	}
}

/**
 * 
 */
package edu.nyu.cs.omnidroid.model.UserConfiguration;

/**
 * @author user
 *
 */
public class OmniHandlerFilter {
	private int id;
	private int handlerID;
	private int eventAttributeID;
	private int dataFilterID;
	private String data;
	
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
	 * @return the handlerID
	 */
	public int getHandlerID() {
		return handlerID;
	}
	
	/**
	 * @return the eventAttributeID
	 */
	public int getEventAttributeID() {
		return eventAttributeID;
	}
	
	/**
	 * @return the dataFilterID
	 */
	public int getDataFilterID() {
		return dataFilterID;
	}
}

/**
 * 
 */
package edu.nyu.cs.omnidroid.model.UserConfiguration;

import java.util.Date;
import java.util.List;

/**
 * @author user
 *
 */
public class OmniHandler {
	private int id;
	private String name;
	private int eventID;
	private Date dateCreated;
	private Date dateUpdated;
	private boolean enabled;
	
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the eventID
	 */
	public int getEventID() {
		return eventID;
	}
	
	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}
	
	/**
	 * @return the dateUpdated
	 */
	public Date getDateUpdated() {
		return dateUpdated;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<OmniHandlerFilter> getFilters() {
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public List<OmniHandlerAction> getActions() {
		return null;
	}
}

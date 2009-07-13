/**
 * 
 */
package Tester;

import java.util.List;

import edu.nyu.cs.omnidroid.model.ApplicationConfiguration.RegisteredAction;
import edu.nyu.cs.omnidroid.model.ApplicationConfiguration.RegisteredApplication;
import edu.nyu.cs.omnidroid.model.ApplicationConfiguration.RegisteredEvent;


/**
 * @author user
 *
 */
public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        RegisteredApplication.LoadRegisteredApplication();
        List<String> appNames = RegisteredApplication.getAllNames();
        String temp = new String();
   	
        for(int i=0; i<appNames.size(); i++) {
        	if (temp.length() > 0) temp+=("\n");
        	temp+=(appNames.get(i));
        	
        	RegisteredApplication nextApp = RegisteredApplication.getByName(appNames.get(i));
        	temp+="\n"+nextApp.getAppID()+" "+nextApp.getAppName()+" "+nextApp.getPackageName()+" "+nextApp.isEnabled();
        	
        	temp+="\n    Actions";
        	List<RegisteredAction> actions = nextApp.getActions();
        	
        	for(int j=0; j<actions.size(); j++) {
            	temp+="\n    "+actions.get(j).getAppID()+" "+actions.get(j).getID()+" "+actions.get(j).getName();        		
        	}

        	temp+="\n    Events";
        	List<RegisteredEvent> events = nextApp.getEvents();
        	
        	for(int j=0; j<events.size(); j++) {
            	temp+="\n    "+events.get(j).getAppID()+" "+events.get(j).getID()+" "+events.get(j).getName();
        	}
        }
        
        System.out.println(temp);
        System.out.println("OK");
	}

}

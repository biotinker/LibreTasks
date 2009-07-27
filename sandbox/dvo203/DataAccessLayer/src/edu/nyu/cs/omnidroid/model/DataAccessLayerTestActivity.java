package edu.nyu.cs.omnidroid.model;

import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import edu.nyu.cs.omnidroid.model.ApplicationConfiguration.RegisteredAction;
import edu.nyu.cs.omnidroid.model.ApplicationConfiguration.RegisteredApplication;
import edu.nyu.cs.omnidroid.model.ApplicationConfiguration.RegisteredEvent;

public class DataAccessLayerTestActivity extends Activity {
	
	private static int execID = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String temp = new String();

		/*
		 * // test my own wrappers temp = runTest();
		 */

		/*
		 * // Create instance by name. String className =
		 * "edu.nyu.cs.omnidroid.model.DataTypes.DataFilter"; try { Class
		 * theClass = Class.forName(className);
		 * 
		 * Method[] methods = theClass.getMethods();
		 * 
		 * for(int i=0; i<methods.length; i++) { if (temp.length() > 0)
		 * temp+=("\n"); temp+=(methods[i].getName()); }
		 * 
		 * 
		 * } catch ( ClassNotFoundException ex ){ temp = ( ex +
		 * " Interpreter class must be in class path."); }
		 * 
		 * temp+=this.getClass().getName();
		 */

		/*
		 * // Test FactoryDataType class DataType someDT =
		 * FactoryDataType.createObject("OmniTextn", "blah blah blah");
		 * 
		 * if(someDT == null) { temp = "No such Type"; } else { temp =
		 * someDT.getValue(); }
		 */

		/*
		// Manually create visuals
		TextView tv = new TextView(this);
		tv.setText(temp);
		setContentView(tv);
		*/
        setContentView(R.layout.main);		 
		
        Button button = null;

        // Exit click
        button = (Button) findViewById(R.id.exit);

        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	// On Click Action
            	finish();	
            }
        });

        // Update click
        button = (Button) findViewById(R.id.update);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	// On Click Action
            	
        		String temp = new String();

        		temp = gpsTest();
        		temp += "\n" + execID;
        		execID++;
        		
        		EditText location = null;
        		location = (EditText) findViewById(R.id.coordinates);
        		location.setText(temp);
            }
        });

        // Get Coordinates click
        button = (Button) findViewById(R.id.getCoordinates);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	// On Click Action
            	
        		String temp = null;
        		
        		EditText address = null;
        		EditText location = null;
        		address = (EditText) findViewById(R.id.address);
        		temp = address.getText().toString();            		
        		
        		location = (EditText) findViewById(R.id.addressCoordinates);
        		location.setText(temp);
            }
        });
        
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

/*
		String temp = gpsTest();
		temp += "\n" + execID;

		TextView tv = new TextView(this);
		tv.setText(temp);
		setContentView(tv);
		*/
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onPostResume()
	 */
	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
/*
		String temp = gpsTest();
		temp += "\n" + execID;

		TextView tv = new TextView(this);
		tv.setText(temp);
		setContentView(tv);
*/
}


	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
/*
		String temp = gpsTest();
		temp += "\n" + execID;

		TextView tv = new TextView(this);
		tv.setText(temp);
		setContentView(tv);
		*/
}


	public String gpsTest() {
		String temp = "";
		LocationManager lm = null;
		List<String> locationProviders = null;

		try {
			lm = (LocationManager) getSystemService(LOCATION_SERVICE);
			locationProviders = lm.getAllProviders();

			for (int i = 0; i < locationProviders.size(); i++) {
				if (temp.length() > 0)
					temp += ("\n");
				temp += (locationProviders.get(i));
			}
			
			LocationProvider lp = lm.getProvider(lm.GPS_PROVIDER);
			
			Location lastLocation = lm.getLastKnownLocation(lm.GPS_PROVIDER);
			temp += "\n";
			if(lastLocation == null) temp += "No Location";
			else temp += lastLocation.getLatitude() + " " + lastLocation.getLongitude();
			
			
		} catch (Exception ex) {
			temp = (ex + " - Location Error.");
		}
		
		return temp;
	}
	
	public static String runTest() {
		// TODO Auto-generated method stub
		RegisteredApplication.LoadRegisteredApplication();
		List<String> appNames = RegisteredApplication.getAllNames();
		String temp = new String();

		for (int i = 0; i < appNames.size(); i++) {
			if (temp.length() > 0)
				temp += ("\n");
			temp += (appNames.get(i));

			RegisteredApplication nextApp = RegisteredApplication
					.getByName(appNames.get(i));
			temp += "\n" + nextApp.getAppID() + " " + nextApp.getAppName()
					+ " " + nextApp.getPackageName() + " "
					+ nextApp.isEnabled();

			temp += "\n    Actions";
			List<RegisteredAction> actions = nextApp.getActions();

			for (int j = 0; j < actions.size(); j++) {
				temp += "\n    " + actions.get(j).getAppID() + " "
						+ actions.get(j).getID() + " "
						+ actions.get(j).getName();
			}

			temp += "\n    Events";
			List<RegisteredEvent> events = nextApp.getEvents();

			for (int j = 0; j < events.size(); j++) {
				temp += "\n    " + events.get(j).getAppID() + " "
						+ events.get(j).getID() + " " + events.get(j).getName();
			}
		}

		temp += "OK";

		return temp;
	}

}
package edu.nyu.cs.omnidroid.model;

import java.util.List;
import java.util.Locale;
import android.location.Geocoder;
import android.location.Address;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import edu.nyu.cs.omnidroid.ExternalParameters.ExternalParameterAccessException;
import edu.nyu.cs.omnidroid.ExternalParameters.LocationService;
import edu.nyu.cs.omnidroid.model.ApplicationConfiguration.RegisteredAction;
import edu.nyu.cs.omnidroid.model.ApplicationConfiguration.RegisteredApplication;
import edu.nyu.cs.omnidroid.model.ApplicationConfiguration.RegisteredEvent;
import edu.nyu.cs.omnidroid.model.DataTypes.DataTypeValidationException;
import edu.nyu.cs.omnidroid.model.DataTypes.OmniArea;

public class DataAccessLayerTestActivity extends Activity {

  private static int execID = 0;

  private LocationService locationService;

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
     * "edu.nyu.cs.omnidroid.model.DataTypes.DataFilter"; try { Class theClass =
     * Class.forName(className);
     * 
     * Method[] methods = theClass.getMethods();
     * 
     * for(int i=0; i<methods.length; i++) { if (temp.length() > 0) temp+=("\n");
     * temp+=(methods[i].getName()); }
     * 
     * 
     * } catch ( ClassNotFoundException ex ){ temp = ( ex +
     * " Interpreter class must be in class path."); }
     * 
     * temp+=this.getClass().getName();
     */

    /*
     * // Test FactoryDataType class DataType someDT = FactoryDataType.createObject("OmniTextn",
     * "blah blah blah");
     * 
     * if(someDT == null) { temp = "No such Type"; } else { temp = someDT.getValue(); }
     */

    /*
     * // Manually create visuals TextView tv = new TextView(this); tv.setText(temp);
     * setContentView(tv);
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

        // temp = gpsTest();
        // temp += testDistance();

        OmniArea myLocation;
        try {
          myLocation = (OmniArea) LocationService.getAttributeValue();
        } catch (ExternalParameterAccessException e) {
          myLocation = null;;
        }
        if (myLocation != null)
          temp = myLocation.getLatitude() + " " + myLocation.getLongitude();
        else
          temp = "No Location";

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

        String address = null;
        String location = null;

        EditText addressTextBox = null;
        EditText locationTextBox = null;
        addressTextBox = (EditText) findViewById(R.id.address);
        address = addressTextBox.getText().toString();

        try {
          OmniArea loc = new OmniArea(OmniArea.getOmniArea(v.getContext(), address, 0));
          location = loc.getLatitude() + ", " + loc.getLongitude();
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        /*
         * // Reverse geocode the postal address to get a location. if (address != null) { try {
         * Geocoder geocoder = new Geocoder(v.getContext(), Locale.getDefault()); List<Address>
         * addressResult = geocoder.getFromLocationName(address, 1); if (!addressResult.isEmpty()) {
         * Address resultAddress = addressResult.get(0); location = resultAddress.getLatitude() +
         * ", " + resultAddress.getLongitude(); } else { location = "No Location Found"; } } catch
         * (Exception e) { location = e.getMessage(); } }
         */
        locationTextBox = (EditText) findViewById(R.id.addressCoordinates);
        locationTextBox.setText(location);
      }
    });

    // Watch for button clicks.
    button = (Button) findViewById(R.id.startService);
    button.setOnClickListener(mStartListener);
    button = (Button) findViewById(R.id.stopService);
    button.setOnClickListener(mStopListener);
  }

  private OnClickListener mStartListener = new OnClickListener() {
    public void onClick(View v) {
      // Make sure the service is started. It will continue running
      // until someone calls stopService(). The Intent we use to find
      // the service explicitly specifies our service component, because
      // we want it running in our own process and don't want other
      // applications to replace it.
      startService(new Intent(DataAccessLayerTestActivity.this, LocationService.class));
    }
  };

  private OnClickListener mStopListener = new OnClickListener() {
    public void onClick(View v) {
      // Cancel a previous call to startService(). Note that the
      // service will not actually stop at this point if there are
      // still bound clients.
      stopService(new Intent(DataAccessLayerTestActivity.this, LocationService.class));
    }
  };

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
      if (lastLocation == null)
        temp += "No Location";
      else
        temp += lastLocation.getLatitude() + " " + lastLocation.getLongitude() + " "
            + lastLocation.getAccuracy();

      lm.requestLocationUpdates(lm.GPS_PROVIDER, 60000, // 1min
          100, // 1km
          locationListener);

    } catch (Exception ex) {
      temp = (ex + " - Location Error.");
    }

    return temp;
  }

  private void updateWithNewLocation(Location newLocation) {
    /*
     * String temp = new String(); OmniArea location = GPSService.getLastLocation();
     * 
     * //temp = newLocation.getLatitude() + " " + newLocation.getLongitude(); if(location != null)
     * temp = location.getLatitude() + " " + location.getLongitude(); else temp = "No Location";
     * temp += "\n" + execID; execID++;
     * 
     * EditText locationTextBox = null; locationTextBox = (EditText) findViewById(R.id.coordinates);
     * locationTextBox.setText(temp);
     */
  }

  private final LocationListener locationListener = new LocationListener() {
    public void onLocationChanged(Location location) {
      updateWithNewLocation(location);
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
  };

  private double testDistance() {
    final int DISTANCE = 0;
    final int RETURN_VALUES = 1;

    float[] results = new float[RETURN_VALUES];

    Location.distanceBetween(40.7279793, -73.9957865, 40.756073, -73.9906634, results);

    // convert distance from meters to miles
    return results[DISTANCE];
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

      RegisteredApplication nextApp = RegisteredApplication.getByName(appNames.get(i));
      temp += "\n" + nextApp.getAppID() + " " + nextApp.getAppName() + " "
          + nextApp.getPackageName() + " " + nextApp.isEnabled();

      temp += "\n    Actions";
      List<RegisteredAction> actions = nextApp.getActions();

      for (int j = 0; j < actions.size(); j++) {
        temp += "\n    " + actions.get(j).getAppID() + " " + actions.get(j).getID() + " "
            + actions.get(j).getName();
      }

      temp += "\n    Events";
      List<RegisteredEvent> events = nextApp.getEvents();

      for (int j = 0; j < events.size(); j++) {
        temp += "\n    " + events.get(j).getAppID() + " " + events.get(j).getID() + " "
            + events.get(j).getName();
      }
    }

    temp += "OK";

    return temp;
  }

}
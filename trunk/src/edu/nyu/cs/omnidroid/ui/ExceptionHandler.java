package edu.nyu.cs.omnidroid.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.OmLogger;

/**
 * Exception Handler UI. When an Exception is caught by our app it should send an intent for this
 * activity to catch it.
 * 
 * @author - acase
 * 
 */
public class ExceptionHandler extends Activity {

  /** Called when the activity is first created. */
  // TODO (acase): Create a user interface to handle events that are caught.
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(this.getLocalClassName(), "onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.linear_default);
  }

  public void onReceive(Context context, Intent intent) {
    Log.i("Omnidroid Exception Found!", intent.getAction());
    try {
      Toast.makeText(context, intent.getAction(), 5).show();

    } catch (Exception e) {
      Log.i("Exception in Intent", e.getLocalizedMessage());
      OmLogger.write(context, "Unable to execute required action");
    }
  }

}
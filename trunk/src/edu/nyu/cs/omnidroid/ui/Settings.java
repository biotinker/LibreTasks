package edu.nyu.cs.omnidroid.ui;

import android.app.Activity;
import android.os.Bundle;
import edu.nyu.cs.omnidroid.R;

/**
 * Display and allow a user to change the settings/preferences for OmniDroid
 * 
 * @author acase
 */
public class Settings extends Activity {

  /**
   * Build our activity
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Create a base layout to present our settings
    setContentView(R.layout.settings);
    
  }

}
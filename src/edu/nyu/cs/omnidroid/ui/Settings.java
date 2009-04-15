package edu.nyu.cs.omnidroid.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
    ScrollView scrollPane = new ScrollView(this);
    LinearLayout layout = (LinearLayout) findViewById(R.layout.settings);
    scrollPane.addView(layout);
    setContentView(scrollPane);

  }

}
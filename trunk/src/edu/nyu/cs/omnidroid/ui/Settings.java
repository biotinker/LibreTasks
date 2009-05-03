package edu.nyu.cs.omnidroid.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;
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

    final ToggleButton omniabled_toggle = (ToggleButton) findViewById(R.id.settings_omniabled);
    omniabled_toggle.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
            if (omniabled_toggle.isChecked()) {
            	//FIXME(acase): Enable OmniDroid
            } else {
            	//FIXME(acase): Disable OmniDroid
            }
        }
    });

    final ToggleButton on_boot_toggle = (ToggleButton) findViewById(R.id.settings_onboot);
    on_boot_toggle.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
            if (on_boot_toggle.isChecked()) {
            	//FIXME(acase): Set OmniDroid to start on boot
            } else {
            	//FIXME(acase): Turn off OmniDroid on boot
            }
        }
    });

  }

}
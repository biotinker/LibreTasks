/*******************************************************************************
 * Copyright 2009, 2010 Omnidroid - http://code.google.com/p/omnidroid 
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 *******************************************************************************/
package edu.nyu.cs.omnidroid.app.view.simple;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.OmnidroidManager;

/**
 * This is the main entry point of the application. Here the user will see a main menu where they
 * can choose to create a new rule, view existing rules, or see help items.
 */
public class ActivityMain extends Activity {

  // Options Menu IDs
  private static final int MENU_SETTINGS_ID = 0;
  private static final int MENU_ABOUT_ID = 1;

  // Disclaimer and background service prefs are stored in SharedPreferences
  private SharedPreferences prefs;
  private static final String SETTING_ACCEPTED_DISCLAIMER = "SettingDisclaimerAccepted";

  /** request code for ChooseRootEventActivity */
  private static final int REQUEST_ACTIVITY_CREATE_RULE = 0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Initialize singleton instance of UIDbHelperStore, which is
    // our connection to the omnidroid database.
    UIDbHelperStore.init(this);

    // Link up click handlers with their buttons.
    Button btnCreateRule = (Button) findViewById(R.id.activity_main_btnCreateRule);
    btnCreateRule.setOnClickListener(listenerBtnClickCreateRule);

    Button btnViewRules = (Button) findViewById(R.id.activity_main_btnViewRules);
    btnViewRules.setOnClickListener(listenerBtnClickViewRules);

    Button btnViewLogs = (Button) findViewById(R.id.activity_main_btnLogs);
    btnViewLogs.setOnClickListener(listenerBtnClickViewLogs);

    Button btnHelp = (Button) findViewById(R.id.activity_main_btnHelp);
    btnHelp.setOnClickListener(listenerBtnClickHelp);

    Button btnResetDB = (Button) findViewById(R.id.activity_main_btnResetDB);
    btnResetDB.setOnClickListener(listenerBtnClickResetDb);

    // Show disclaimer if it hasn't been accepted yet
    prefs = UIDbHelperStore.instance().db().getSharedPreferences();
    if (prefs.getBoolean(SETTING_ACCEPTED_DISCLAIMER, false) == false) {
      showDisclaimer();
    }

    /*
     * Start Omnidroid service if it's not already running and it should be. This addresses two key
     * times: 1) On first run when the preference for running has not yet been set (since it
     * defaults to "true"), and 2) After upgrade the service will no longer be running, so this will
     * need to be start it again.
     * 
     * It's okay to try and start it multiple times, Android is smart enough to not create multiple
     * instances.
     */
    if (prefs.getBoolean(getString(R.string.pref_key_omnidroid_enabled), true)) {
      OmnidroidManager.enable(this, true);
    }

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
    case REQUEST_ACTIVITY_CREATE_RULE:
      if (resultCode == ActivityChooseRootEvent.RESULT_RULE_CREATED) {
        Intent intent = new Intent();
        intent.setClass(this, ActivitySavedRules.class);
        startActivity(intent);
      }
    default:
      // do nothing
    }
  }

  /**
   * Display our disclaimer dialog and require acceptance.
   */
  private void showDisclaimer() {
    Builder welcome = new AlertDialog.Builder(this);
    welcome.setTitle(R.string.disclaimer_title);
    welcome.setIcon(R.drawable.icon);
    welcome.setMessage(Html.fromHtml(getString(R.string.disclaimer_msg)));
    welcome.setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        setDisclaimerAccepted(true);
      }
    });
    welcome.setNegativeButton(R.string.disagree, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        setDisclaimerAccepted(false);
        // Exit immediately if user refused disclaimer
        finish();
      }
    });
    welcome.show();
  }

  private void setDisclaimerAccepted(boolean accepted) {
    SharedPreferences.Editor editor = prefs.edit();
    editor.putBoolean(SETTING_ACCEPTED_DISCLAIMER, accepted);
    editor.commit();
  }

  /**
   * Display our about dialog.
   */
  private void showAbout() {
    // Get package information
    String version;
    try {
      PackageInfo pkgInfo;
      pkgInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);
      version = pkgInfo.versionName;
    } catch (NameNotFoundException e) {
      version = getString(R.string.unknown);
    }

    StringBuilder message = new StringBuilder();
    message.append(getString(R.string.about_desc)).append("<br /><br />");
    message.append(getString(R.string.copyright)).append("<br /><br />");
    message.append(getString(R.string.about_version)).append(" ").append(version);
    message.append("<br /><br />");
    message.append(getString(R.string.about_license)).append("<br /><br />");
    message.append(getString(R.string.about_website));

    Builder about = new AlertDialog.Builder(this);
    about.setTitle(R.string.about_title);
    about.setIcon(R.drawable.icon);
    about.setMessage(Html.fromHtml(message.toString()));
    about.setPositiveButton(R.string.ok, null);
    about.show();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    UIDbHelperStore.instance().releaseResources();
  }

  /** Create a options menu for the main screen */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(Menu.NONE, MENU_SETTINGS_ID, Menu.NONE, getString(R.string.settings_label)).setIcon(
        android.R.drawable.ic_menu_preferences).setAlphabeticShortcut('s');
    menu.add(Menu.NONE, MENU_ABOUT_ID, Menu.NONE, getString(R.string.about)).setAlphabeticShortcut(
        'a').setIcon(android.R.drawable.ic_menu_info_details);

    return super.onCreateOptionsMenu(menu);
  }

  /** Called when an item of options menu is clicked */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_SETTINGS_ID:
      startActivity(new Intent(this, ActivitySettings.class));
      return true;
    case MENU_ABOUT_ID:
      showAbout();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Launch the create-a-new-rule activity.
   */
  private OnClickListener listenerBtnClickCreateRule = new OnClickListener() {
    public void onClick(View v) {
      Intent intent = new Intent();
      intent.setClass(getApplicationContext(), ActivityChooseRootEvent.class);
      startActivityForResult(intent, REQUEST_ACTIVITY_CREATE_RULE);
    }
  };

  /**
   * View saved rules.
   */
  private OnClickListener listenerBtnClickViewRules = new OnClickListener() {
    public void onClick(View v) {
      Intent intent = new Intent();
      intent.setClass(getApplicationContext(), ActivitySavedRules.class);
      startActivity(intent);
    }
  };

  /**
   * View logs.
   */
  private OnClickListener listenerBtnClickViewLogs = new OnClickListener() {
    public void onClick(View v) {
      ActivityLogs.resetUI(v.getContext());
      Intent intent = new Intent();
      intent.setClass(getApplicationContext(), ActivityLogs.class);
      startActivity(intent);
    }
  };

  /**
   * Help info.
   */
  private OnClickListener listenerBtnClickHelp = new OnClickListener() {
    public void onClick(View v) {
      Builder help = new AlertDialog.Builder(v.getContext());
      help.setTitle(R.string.help_title);
      help.setIcon(R.drawable.icon);
      help.setMessage(Html.fromHtml(getString(R.string.help_activitymain)));
      help.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        }
      });
      help.show();
    }
  };

  /**
   * Cleanup the Database, all info will be reset, user set rules will be lost
   */
  private OnClickListener listenerBtnClickResetDb = new OnClickListener() {
    public void onClick(View v) {
      // Show a dialog to ask users if they're sure they want to cleanup the Database
      new AlertDialog.Builder(v.getContext()).setIcon(android.R.drawable.ic_dialog_alert).setTitle(
          getString(R.string.reset_settings)).setPositiveButton(getString(R.string.ok),
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
              UIDbHelperStore.instance().db().resetDB();
            }
          }).setNegativeButton(getString(R.string.cancel), null).show();
    }
  };
}
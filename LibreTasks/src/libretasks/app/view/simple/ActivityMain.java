/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
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
package libretasks.app.view.simple;

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
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.Button;
import libretasks.app.R;
import libretasks.app.controller.OmnidroidManager;

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

    prefs = UIDbHelperStore.instance().db().getSharedPreferences();
    if (prefs.getBoolean(ActivitySettings.PREF_KEY_ACCEPTED_DISCAIMER, false)) {
      // Startup our background services
      startOmnidroid();
    } else {
      showDisclaimer();
    }
  }

  /*
   * Start Omnidroid service if it's not already running and it should be. This addresses two key
   * times: 1) On first run when the preference for running has not yet been set (since it defaults
   * to "true"), but only if the disclaimer was accepted, and 2) After upgrade the service will no
   * longer be running, so it needs to start it again.
   * 
   * It's okay to try and start it multiple times, it's smart enough to not create multiple
   * instances.
   */
  protected void startOmnidroid() {
    if (prefs.getBoolean(getString(R.string.pref_key_libretasks_enabled), true)) {
      OmnidroidManager.enable(this, true);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
   * 
   */
  private void showDisclaimer() {
    Builder welcome = new AlertDialog.Builder(this);
    welcome.setTitle(R.string.disclaimer_title);
    welcome.setCancelable(false);
    welcome.setIcon(R.drawable.icon);
    welcome.setMessage(Html.fromHtml(getString(R.string.disclaimer_msg)));
    welcome.setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        // Store acceptance of disclaimer
        setDisclaimerAccepted(true);
        
        // Startup our background services
        startOmnidroid();
      }
    });
    welcome.setNegativeButton(R.string.disagree, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        // Store refusal of disclaimer
        setDisclaimerAccepted(false);
        
        // Exit immediately if user refused disclaimer
        finish();
      }
    });
    welcome.show();
  }

  private void setDisclaimerAccepted(boolean accepted) {
    SharedPreferences.Editor editor = prefs.edit();
    editor.putBoolean(ActivitySettings.PREF_KEY_ACCEPTED_DISCAIMER, accepted);
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

  @Override
  public void onResume() {
    super.onResume();
    LinearLayout ll = (LinearLayout) findViewById(R.id.activity_main_omnidroidDisabled);
    UIDbHelperStore.init(this);
    prefs = UIDbHelperStore.instance().db().getSharedPreferences();
    if (prefs.getBoolean(getString(R.string.pref_key_libretasks_enabled), true)) {
      ll.setVisibility(LinearLayout.INVISIBLE);
    } else {
      ll.setVisibility(LinearLayout.VISIBLE);
    }
  }

  /** Create a options menu for the main screen */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(Menu.NONE, MENU_SETTINGS_ID, Menu.NONE, getString(R.string.settings_label))
        .setIcon(android.R.drawable.ic_menu_preferences).setAlphabeticShortcut('s');
    menu.add(Menu.NONE, MENU_ABOUT_ID, Menu.NONE, getString(R.string.about))
        .setAlphabeticShortcut('a').setIcon(android.R.drawable.ic_menu_info_details);

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

}

/*******************************************************************************
 * Copyright 2010 Omnidroid - http://code.google.com/p/omnidroid 
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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.OmnidroidManager;
import edu.nyu.cs.omnidroid.app.model.db.RuleDbAdapter;

/**
 * This activity shows all the settings/preferences
 */
public class ActivitySettings extends PreferenceActivity implements
    OnSharedPreferenceChangeListener {

  // Access to our preference settings
  protected SharedPreferences sharedPreferences;

  // The key for the disclaimer preference  
  public static final String PREF_KEY_ACCEPTED_DISCAIMER = "SettingDisclaimerAccepted";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Layout of this activity is loaded from resources
    addPreferencesFromResource(R.layout.activity_settings);

    // Get access to our preferences
    sharedPreferences = getPreferenceScreen().getSharedPreferences();

    // Add a listener for Enable/Disable click
    findPreference(getString(R.string.pref_key_omnidroid_enabled)).setOnPreferenceClickListener(
        new OnPreferenceClickListener() {
          // @Override
          public boolean onPreferenceClick(Preference preference) {
            enableOmnidroid(!sharedPreferences.getBoolean(
                getString(R.string.pref_key_omnidroid_enabled), false));
            return true;

          }
        });

    // Add a listener for DB Reset click
    findPreference(getString(R.string.pref_key_reset_db)).setOnPreferenceClickListener(
        new OnPreferenceClickListener() {
          // @Override
          public boolean onPreferenceClick(Preference preference) {
            resetDb();
            return true;
          }
        });

    // Add a listener for Settings Reset click
    findPreference(getString(R.string.pref_key_reset_settings)).setOnPreferenceClickListener(
        new OnPreferenceClickListener() {
          // @Override
          public boolean onPreferenceClick(Preference preference) {
            confirmResetSettings();
            return true;
          }
        });
  }

  @Override
  public void onResume() {
    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    setOmnidroidEnabledPrefName();
    super.onResume();
  }

  @Override
  public void onPause() {
    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    super.onPause();
  }

  private void enableOmnidroid(final boolean enable) {
    String dialogTitle;
    if (enable) {
      dialogTitle = getString(R.string.enable_omnidroid_dialog_msg);
    } else {
      dialogTitle = getString(R.string.disable_omnidroid_dialog_msg);
    }
    new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(dialogTitle)
        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            OmnidroidManager.enable(getApplicationContext(), enable);
            setOmnidroidEnabledPrefName();
          }
        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            // Do nothing
          }
        }).show();
  }

  /**
   * Update the displayed name/description to reflect the current status of Omnidroid
   * (Enabled/Disabled)
   */
  private void setOmnidroidEnabledPrefName() {
    final String prefTitle;
    final String prefSummary;
    if (sharedPreferences.getBoolean(getString(R.string.pref_key_omnidroid_enabled), false)) {
      prefTitle = getString(R.string.disable_omnidroid);
      prefSummary = getString(R.string.disable_omnidroid_desc);
    } else {
      prefTitle = getString(R.string.enable_omnidroid);
      prefSummary = getString(R.string.enable_omnidroid_desc);
    }
    findPreference(getString(R.string.pref_key_omnidroid_enabled)).setTitle(prefTitle);
    findPreference(getString(R.string.pref_key_omnidroid_enabled)).setSummary(prefSummary);
  }

  /**
   * Restore the Database to it's initial install state
   */
  private void resetDb() {
    new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(
        getString(R.string.reset_db_dialog_msg)).setPositiveButton(getString(R.string.ok),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            UIDbHelperStore.instance().db().resetDB();
          }
        }).setNegativeButton(getString(R.string.cancel), null).show();

  }

  /**
   * Delete all user specified preferences.
   */
  private void resetSettings() {
    /* Clear all user settings */
    sharedPreferences.edit().clear().commit();
    sharedPreferences.edit().putBoolean(PREF_KEY_ACCEPTED_DISCAIMER, true);
    sharedPreferences.edit().commit();

    /*
     * Restart this activity to get updates.
     * 
     * There may be a better way to handle this, but I couldn't figure out how to update all the
     * settings since they are originally pulled on startup)
     */
    startActivity(new Intent(this, ActivitySettings.class));
    finish();
  }

  /**
   * Present a dialog to confirm settings reset
   */
  private void confirmResetSettings() {
    new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(
        getString(R.string.reset_settings_dialog_msg)).setPositiveButton(getString(R.string.ok),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            resetSettings();
          }
        }).setNegativeButton(getString(R.string.cancel), null).show();
  }

  /**
   * Run any updates needed on the databases given a change on notification preferences.
   * 
   * @param defaultNotification
   *          - if omnidroid will send notifications on rule triggers
   */
  private void setNotification(boolean defaultNotification) {
    RuleDbAdapter.setDefaultNotificationValue(defaultNotification);
  }

  /**
   * General change monitor that can be used to call updates based on the preference that was
   * updated.
   */
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals(getString(R.string.pref_key_notification))) {
      setNotification(sharedPreferences.getBoolean(key, true));          
    } else if (key.equals(getString(R.string.pref_key_gmail_signature))){
      setGmailSignaturePrefDescription(sharedPreferences.getBoolean(key, false));
    } else if (key.equals(getString(R.string.pref_key_sms_signature))){
      setSmsSignaturePrefDescription(sharedPreferences.getBoolean(key, false));
    } 
  }

  private void setSmsSignaturePrefDescription(boolean prefValue) {
    if (prefValue) {
      findPreference(getString(R.string.pref_key_sms_signature))
          .setSummary(getString(R.string.sms_signature_desc_off));
    } else {
      findPreference(getString(R.string.pref_key_sms_signature))
          .setSummary(getString(R.string.sms_signature_desc_on));
    }    
  }

  private void setGmailSignaturePrefDescription(boolean prefValue) {
    if (prefValue) {
      findPreference(getString(R.string.pref_key_gmail_signature))
          .setSummary(getString(R.string.gmail_signature_desc_off));
    } else {
      findPreference(getString(R.string.pref_key_gmail_signature))
          .setSummary(getString(R.string.gmail_signature_desc_on));
    }    
  }
  
}

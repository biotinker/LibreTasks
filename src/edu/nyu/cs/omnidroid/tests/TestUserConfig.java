package edu.nyu.cs.omnidroid.tests;

import java.io.IOException;

import android.util.Log;

import edu.nyu.cs.omnidroid.util.UserConfig;

/**
 * Test Application to Verify Configuration File Functionality
 * 
 */
public class TestUserConfig {

  // Configuration file locations for the TM/RM.
  private static String userConfigFile;

  public TestUserConfig() {
    initConfig();
  }

  public void TestPut() {
    UserConfig.getInstance().put(UserConfig.EVENT_APP_KEY, "TestApp");
    UserConfig.getInstance().put(UserConfig.EVENT_NAME_KEY, "TestEventName");
    UserConfig.getInstance().put(UserConfig.FILTER_TYPE_KEY, "TestFilterType");
    UserConfig.getInstance().put(UserConfig.FILTER_DATA_KEY, "TestFilterData");
    UserConfig.getInstance().put(UserConfig.ACTION_APP_BASE, "TestActionAppBase");
    UserConfig.getInstance().put(UserConfig.ACTION_APP_DATA_BASE, "TestActionAppDataBase");
  }

  public void TestGet() {
    Log.i(UserConfig.EVENT_APP_KEY, UserConfig.getInstance().getValue(UserConfig.EVENT_APP_KEY)); 
    Log.i(UserConfig.EVENT_NAME_KEY, UserConfig.getInstance().getValue(UserConfig.EVENT_NAME_KEY));
    Log.i(UserConfig.FILTER_TYPE_KEY, UserConfig.getInstance().getValue(UserConfig.FILTER_TYPE_KEY));
    Log.i(UserConfig.FILTER_DATA_KEY, UserConfig.getInstance().getValue(UserConfig.FILTER_DATA_KEY));
    Log.i(UserConfig.ACTION_APP_BASE, UserConfig.getInstance().getValue(UserConfig.ACTION_APP_BASE));
    Log.i(UserConfig.ACTION_APP_DATA_BASE, UserConfig.getInstance().getValue(UserConfig.ACTION_APP_DATA_BASE));
  }
  
  /**
   * Initialize the server configuration objects
   */
  private static void initConfig() {
    try {
      UserConfig.getInstance().init(userConfigFile);
    } catch (IOException e) {
      // TODO (acase): Use OmnidroidExceptions
      System.err.println("Unable to load user config properties file.");
      System.err.println("  " + e.getMessage());
    }
  }

  
}
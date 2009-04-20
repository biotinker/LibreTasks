package edu.nyu.cs.omnidroid.tests;

import java.io.IOException;

import android.util.Log;

import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Test Application to Verify Configuration File Functionality
 * 
 * @author acase
 */
public class TestUserConfig {

  // Configuration file locations for the TM/RM.
  private static String userConfigFile;

  public TestUserConfig() {
    initConfig();
  }

  public void TestPut() {
    /*
    UGParser.getInstance().put(UGParser.KEY_EventApp, "TestApp");
    UGParser.getInstance().put(UGParser.KEY_EventName, "TestEventName");
    UGParser.getInstance().put(UGParser.KEY_FilterType, "TestFilterType");
    UGParser.getInstance().put(UGParser.KEY_FilterData, "TestFilterData");
    UGParser.getInstance().put(UGParser.ACTION_APP_BASE, "TestActionAppBase");
    UGParser.getInstance().put(UGParser.ACTION_APP_DATA_BASE, "TestActionAppDataBase");
    */
  }

  public void TestGet() {
    /*
    Log.i(UGParser.KEY_EventApp, UGParser.getInstance().getValue(UGParser.KEY_EventApp)); 
    Log.i(UGParser.KEY_EventName, UGParser.getInstance().getValue(UGParser.KEY_EventName));
    Log.i(UGParser.KEY_FilterType, UGParser.getInstance().getValue(UGParser.KEY_FilterType));
    Log.i(UGParser.KEY_FilterData, UGParser.getInstance().getValue(UGParser.KEY_FilterData));
    */
  }
  
  /**
   * Initialize the server configuration objects
   */
  private static void initConfig() {
/*
    try {
      UGParser.getInstance().init(userConfigFile);
    } catch (IOException e) {
      // TODO (acase): Use OmnidroidExceptions
      System.err.println("Unable to load user config properties file.");
      System.err.println("  " + e.getMessage());
    }
*/
  }

  
}
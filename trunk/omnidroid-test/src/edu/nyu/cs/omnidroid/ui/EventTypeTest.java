/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid 
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
package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;
import edu.nyu.cs.omnidroid.tests.TestAppConfig;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.StringMap;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Tests for EventType Activity, which displays the types of events that the application specified
 * in the Intent can handle.
 */
public class EventTypeTest extends ActivityUnitTestCase<EventType> {

  public EventTypeTest() {
    super(EventType.class);
  }

  private static final String SAVED_EVENT_NAME = "Contact_Saved";
  private static final String UPDATED_EVENT_DESC = "Contact Updated";
  private static final String UPDATED_EVENT_NAME = "Contact_Updated";
  private static final String TEST_APPLICATION_NAME = "Test Application";
  private EventType activity;
  private AGParser appConfigParser;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    // Create an Intent from which to choose events
    Intent i = new Intent();
    i.putExtra(AGParser.KEY_APPLICATION, TEST_APPLICATION_NAME);
    activity = startActivity(i, null, null);
    
    appConfigParser = new AGParser(activity.getApplicationContext());
    TestAppConfig.writeConfig(appConfigParser);
  }

  /** Tests that, given an AGParser format event list, parseEventsList returns an ArrayAdapter
   * format event list.
   */
  public void testParseEventsList() {
    // Sample map for Test Application:
    // [{Contact_Saved=Contact Saved, Contact_Updated=Contact Updated}]
    HashMap<String, String> savedMap = new HashMap<String, String>();
    String savedEventDesc = "Contact Saved";
    savedMap.put(SAVED_EVENT_NAME, savedEventDesc);
    savedMap.put(UPDATED_EVENT_NAME, UPDATED_EVENT_DESC);
    ArrayList<HashMap<String, String>> eventIdAndStringMap =
        new ArrayList<HashMap<String, String>>();
    eventIdAndStringMap.add(savedMap);
    assertEquals(eventIdAndStringMap, appConfigParser.readEvents(TEST_APPLICATION_NAME));

    StringMap savedStringMap = new StringMap(SAVED_EVENT_NAME, savedEventDesc);
    StringMap updatedStringMap = new StringMap(UPDATED_EVENT_NAME, UPDATED_EVENT_DESC);
    ArrayList<StringMap> stringMapList = new ArrayList<StringMap>(2);
    stringMapList.add(savedStringMap);
    stringMapList.add(updatedStringMap);

    assertEquals(stringMapList, EventType.transformEventsList(eventIdAndStringMap));
  }

  /** Tests that creating an intent adds the activity's eventApp and the given eventName. */
  public void testCreateIntent() {
    Intent actualIntent = activity.createIntent(SAVED_EVENT_NAME);
    Bundle extras = actualIntent.getExtras();
    assertEquals(TEST_APPLICATION_NAME, extras.get(AGParser.KEY_APPLICATION));
    assertEquals(SAVED_EVENT_NAME, extras.get(UGParser.KEY_EVENT_TYPE));
  }

  /** Tests that onCreate leaves the activity in the proper initialized state. */
  public void testOnCreate() {
    // There are 2 events for the test app
    assertEquals(2, activity.getListView().getAdapter().getCount());
  }

  /** Tests that only the expected request and result code cause the Activity to call finish(). */
  public void testOnActivityResult() {
    assertFalse(isFinishCalled());
    Intent i = new Intent();
    activity.onActivityResult(Constants.RESULT_ADD_OMNIHANDLER, Constants.RESULT_FAILURE, i);
    assertFalse(isFinishCalled());
    activity.onActivityResult(Constants.RESULT_ADD_DATUM, Constants.RESULT_SUCCESS, i);
    assertFalse(isFinishCalled());
    activity.onActivityResult(Constants.RESULT_ADD_OMNIHANDLER, Constants.RESULT_SUCCESS, i);
    assertTrue(isFinishCalled());
  }
}

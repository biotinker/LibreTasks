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
package edu.nyu.cs.omnidroid.core;

import junit.framework.TestCase;

/**
 * Unit tests for {@link RegisteredApp} class.
 */
public class RegisteredAppTest extends TestCase {
  private RegisteredApp app;

  @Override
  public void setUp() {
    app = new RegisteredApp();
  }

  /** Tests that default {@code appName} is null. */
  public void testDefaultGetAppName() {
    assertNull(app.getAppName());
  }

  /** Tests that {@code appName} is stored and retrieved correctly. */
  public void testAppName() {
    String name = "foo";
    app.setAppName(name);
    assertEquals(name, app.getAppName());
  }

  // TODO(ksjohnson3): Add tests for the rest of the methods on RegisteredApp when it is actually
  // used.
}

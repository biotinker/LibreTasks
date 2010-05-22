package edu.nyu.cs.omnidroid.app.core;

import edu.nyu.cs.omnidroid.app.core.RegisteredApp;
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
package edu.nyu.cs.omnidroid.core;

import junit.framework.TestCase;

/**
 * Unit tests for {@link OmniHandler} class.
 */
public class OmniHandlerTest extends TestCase {
  private OmniHandler handler;

  @Override
  public void setUp() {
    handler = new OmniHandler();
  }

  /** Tests that default {@code name} is an empty string. */
  public void testDefaultGetName() {
    assertEquals("", handler.getName());
  }

  /** Tests that {@code name} is stored and retrieved correctly. */
  public void testName() {
    String name = "foo";
    handler.setName(name);
    assertEquals(name, handler.getName());
  }

  /** Tests that default {@code enabled} is true. */
  public void testDefaultGetEnabled() {
    assertTrue(handler.isEnabled());
  }

  /** Tests that {@code enabled} is stored and retrieved correctly. */
  public void testEnabled() {
    handler.setEnabled(false);
    assertFalse(handler.isEnabled());
  }

  /** Tests that default {@code eventApp} is an empty string. */
  public void testDefaultGetEventApp() {
    assertEquals("", handler.getEventApp());
  }

  /** Tests that {@code eventApp} is stored and retrieved correctly. */
  public void testEventApp() {
    String name = "foo";
    handler.setEventApp(name);
    assertEquals(name, handler.getEventApp());
  }

  /** Tests that default {@code eventType} is an empty string. */
  public void testDefaultGetEventType() {
    assertEquals("", handler.getEventType());
  }

  /** Tests that {@code eventType} is stored and retrieved correctly. */
  public void testEventType() {
    String name = "foo";
    handler.setEventType(name);
    assertEquals(name, handler.getEventType());
  }

  // TODO(ksjohnson3): Add tests for filterType, filterData, actionApp, actionType, actionData if
  // OmniHandler will actually be used.
}

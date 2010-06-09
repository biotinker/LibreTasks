/*******************************************************************************
 * Copyright 2010 OmniDroid - http://code.google.com/p/omnidroid
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
package edu.nyu.cs.omnidroid.app.controller.external.helper.telephony;

import junit.framework.TestCase;

/**
 * Test for {@link PhoneStateMachine}.
 * 
 * @see PhoneStateMachine
 */
public class TestPhoneStateMachine extends TestCase {
  private PhoneStateMachine phoneStateMachine;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    phoneStateMachine = new PhoneStateMachine();
  }

  public void testInitialState() {
    assertTrue(phoneStateMachine.isIdle());
  }

  public void testIdleState() throws IllegalStateException {
    phoneStateMachine.onIdle();
    assertTrue(phoneStateMachine.isIdle());
  }

  public void testRinging() throws IllegalStateException {
    phoneStateMachine.onRing();
    assertTrue(phoneStateMachine.isRinging());

    /*
     * Although it is very unlikely that the phone will ring twice in a row, make sure that it still
     * stays on the same state.
     */
    phoneStateMachine.onRing();
    assertTrue(phoneStateMachine.isRinging());

    phoneStateMachine.onIdle();
    assertTrue(phoneStateMachine.isIdle());
  }

  public void testInboundOffHook() throws IllegalStateException {
    phoneStateMachine.onRing();

    phoneStateMachine.onOffhook();
    assertTrue(phoneStateMachine.isInboundOffhook());

    /*
     * Although it is very unlikely that an off-hooked phone will be off-hooked again, make sure
     * that the phone stays in the same state.
     */
    phoneStateMachine.onOffhook();
    assertTrue(phoneStateMachine.isInboundOffhook());

    boolean exceptionOccured = false;
    try {
      phoneStateMachine.onRing();
    } catch (IllegalStateException e) {
      // The phone cannot ring while off-hook!
      exceptionOccured = true;
    }

    assertTrue(exceptionOccured);
    assertTrue(phoneStateMachine.isInboundOffhook());

    phoneStateMachine.onIdle();
    assertTrue(phoneStateMachine.isIdle());
  }

  public void testOutboundOffHook() throws IllegalStateException {
    phoneStateMachine.onOffhook();
    assertTrue(phoneStateMachine.isOutboundOffhook());

    /*
     * Although it is very unlikely that an off-hooked phone will be off-hooked again, make sure
     * that the phone stays in the same state.
     */
    phoneStateMachine.onOffhook();
    assertTrue(phoneStateMachine.isOutboundOffhook());

    boolean exceptionOccured = false;
    try {
      phoneStateMachine.onRing();
    } catch (IllegalStateException e) {
      // The phone cannot ring while off-hook!
      exceptionOccured = true;
    }

    assertTrue(exceptionOccured);
    assertTrue(phoneStateMachine.isOutboundOffhook());

    phoneStateMachine.onIdle();
    assertTrue(phoneStateMachine.isIdle());
  }

  /**
   * Test whether isIdle returns true only on cases where it should be true
   */
  public void testIsIdle() {
    phoneStateMachine.onRing();
    assertFalse(phoneStateMachine.isIdle());

    phoneStateMachine.onOffhook();
    assertFalse(phoneStateMachine.isIdle());

    phoneStateMachine.onIdle();
    assertTrue(phoneStateMachine.isIdle());
    phoneStateMachine.onOffhook();
    assertFalse(phoneStateMachine.isIdle());
  }

  /**
   * Test whether isRinging returns true only on cases where it should be true
   */
  public void testIsRinging() {
    assertFalse(phoneStateMachine.isRinging());

    phoneStateMachine.onRing();
    assertTrue(phoneStateMachine.isRinging());

    phoneStateMachine.onOffhook();
    assertFalse(phoneStateMachine.isRinging());

    phoneStateMachine.onIdle();
    assertFalse(phoneStateMachine.isRinging());

    phoneStateMachine.onOffhook();
    assertFalse(phoneStateMachine.isRinging());
  }

  /**
   * Test whether isInboundOffhook returns true only on cases where it should be true
   */
  public void testIsInboundOffhook() {
    assertFalse(phoneStateMachine.isInboundOffhook());

    phoneStateMachine.onRing();
    assertFalse(phoneStateMachine.isInboundOffhook());

    phoneStateMachine.onOffhook();
    assertTrue(phoneStateMachine.isInboundOffhook());

    phoneStateMachine.onIdle();
    assertFalse(phoneStateMachine.isInboundOffhook());

    phoneStateMachine.onOffhook();
    assertFalse(phoneStateMachine.isInboundOffhook());
  }

  /**
   * Test whether isOutboundOffhook returns true only on cases where it should be true
   */
  public void testIsOutboundOffhook() {
    assertFalse(phoneStateMachine.isOutboundOffhook());

    phoneStateMachine.onRing();
    assertFalse(phoneStateMachine.isOutboundOffhook());

    phoneStateMachine.onOffhook();
    assertFalse(phoneStateMachine.isOutboundOffhook());

    phoneStateMachine.onIdle();
    assertFalse(phoneStateMachine.isOutboundOffhook());

    phoneStateMachine.onOffhook();
    assertTrue(phoneStateMachine.isOutboundOffhook());
  }
}

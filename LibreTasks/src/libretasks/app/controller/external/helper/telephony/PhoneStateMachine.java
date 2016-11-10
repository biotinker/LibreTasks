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
package libretasks.app.controller.external.helper.telephony;

/**
 * Class for representing the different states of a phone. Please also refer to the state diagram on
 * {@link http://code.google.com/p/omnidroid/wiki/PhoneCallStateTransition} for an overview of the
 * different transitions.
 */
public class PhoneStateMachine {
  private final IdlePhoneState idleState;
  private final RingingPhoneState ringingState;
  private final InboundOffHookState inboundOffHookState;
  private final OutboundOffHookState outboundOffHookState;
  private PhoneState currentState;

  /**
   * Class Consturctor
   */
  public PhoneStateMachine() {
    idleState = new IdlePhoneState(this);
    ringingState = new RingingPhoneState(this);
    inboundOffHookState = new InboundOffHookState(this);
    outboundOffHookState = new OutboundOffHookState(this);
    currentState = idleState;
  }

  /**
   * Package private method for setting states
   * 
   * @param currentState
   *          the currentState to set
   */
  void setCurrentState(PhoneState currentState) {
    this.currentState = currentState;
  }

  /**
   * Package private method for getting the idle state
   * 
   * @return the idleState
   */
  IdlePhoneState getIdleState() {
    return idleState;
  }

  /**
   * Package private method for getting the ringing state
   * 
   * @return the ringingState
   */
  RingingPhoneState getRingingState() {
    return ringingState;
  }

  /**
   * Package private method for getting the inbound off-hook state
   * 
   * @return the inboundOffHookState
   */
  InboundOffHookState getInboundOffHookState() {
    return inboundOffHookState;
  }

  /**
   * Package private method for getting the outbound off-hook state
   * 
   * @return the outboundOffHookState
   */
  OutboundOffHookState getOutboundOffHookState() {
    return outboundOffHookState;
  }

  /**
   * Check if phone is idle.
   * 
   * @return true if idle.
   */
  public boolean isIdle() {
    return (currentState == idleState);
  }

  /**
   * Check if phone is ringing.
   * 
   * @return true if ringing.
   */
  public boolean isRinging() {
    return (currentState == ringingState);
  }

  /**
   * Check if phone is off-hook during an inbound call.
   * 
   * @return true if is off-hook and during an inbound call.
   */
  public boolean isInboundOffhook() {
    return (currentState == inboundOffHookState);
  }

  /**
   * Check if phone is off-hook during an outbound call.
   * 
   * @return true if is off-hook and during an outbound call.
   */
  public boolean isOutboundOffhook() {
    return (currentState == outboundOffHookState);
  }

  /**
   * Action for phone on idle.
   * 
   * @throws IllegalAction
   *           when action is invalid
   */
  public void onIdle() throws IllegalStateException {
    currentState.idle();
  }

  /**
   * Action for phone on off-hook.
   * 
   * @throws IllegalAction
   *           when action is invalid
   */
  public void onOffhook() throws IllegalStateException {
    currentState.offhook();
  }

  /**
   * Action for phone rings.
   * 
   * @throws IllegalAction
   *           when action is invalid
   */
  public void onRing() throws IllegalStateException {
    currentState.ring();
  }
}
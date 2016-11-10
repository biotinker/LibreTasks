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
 * Class for representing idle phone state.
 */
public class IdlePhoneState implements PhoneState {
  private final PhoneStateMachine phone;

  /**
   * Class constructor
   */
  public IdlePhoneState(PhoneStateMachine phone) {
    this.phone = phone;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.nyu.cs.omnidroid.app.controller.external.attributes.helper.PhoneState#idle()
   */
  public void idle() throws IllegalStateException {
    // Do nothing, stays in the same state
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.nyu.cs.omnidroid.app.controller.external.attributes.helper.PhoneState#offhook()
   */
  public void offhook() throws IllegalStateException {
    phone.setCurrentState(phone.getOutboundOffHookState());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.nyu.cs.omnidroid.app.controller.external.attributes.helper.PhoneState#ring()
   */
  public void ring() throws IllegalStateException {
    phone.setCurrentState(phone.getRingingState());
  }
}

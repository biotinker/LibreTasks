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
 * Interface for representing a phone state.
 */
public interface PhoneState {
  /**
   * Action for phone rings.
   * 
   * @throws IllegalStateException
   *           when action is invalid for the current state
   */
  public void ring() throws IllegalStateException;

  /**
   * Action for phone on off hook.
   * 
   * @throws IllegalStateException
   *           when action is invalid for the current state
   */
  public void offhook() throws IllegalStateException;

  /**
   * Action for phone on idle.
   * 
   * @throws IllegalStateException
   *           when action is invalid for the current state
   */
  public void idle() throws IllegalStateException;
}

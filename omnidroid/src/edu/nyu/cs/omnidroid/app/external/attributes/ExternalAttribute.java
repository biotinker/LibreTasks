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
package edu.nyu.cs.omnidroid.app.external.attributes;

import edu.nyu.cs.omnidroid.app.core.datatypes.DataType;

/**
 * This class encapsulates a generic external attribute. External Attributes allow us to access
 * properties like, phone ring tone setting, location, sensor access, etc. None of this are event
 * driven, but simply represent the current state.
 */
public interface ExternalAttribute {
  /**
   * Looks up attribute value of external attribute.
   * 
   * @return attribute value
   */
  public DataType getAttributeValue() throws ExternalAttributeAccessException;

  /**
   * Returns the name of the external attribute
   * 
   * @return name of the event
   */
  public String getName();
}
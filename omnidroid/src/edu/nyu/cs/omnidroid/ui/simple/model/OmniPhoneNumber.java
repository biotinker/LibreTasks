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
package edu.nyu.cs.omnidroid.ui.simple.model;

/**
 * Dummy class for OmniPhonenumber. This is to be removed once the real implementation is added to
 * the core package.
 */
public class OmniPhoneNumber extends OmniDataType {

  private String mPhoneNumber;

  public OmniPhoneNumber(String phoneNumber) throws Exception {
    super();
    if (phoneNumber == null || phoneNumber.length() < 1) {
      throw new Exception("Phone numbers must have at least 1 character!");
    }
    mPhoneNumber = phoneNumber;
  }

  public String getDescription() {
    return mPhoneNumber == null ? "" : mPhoneNumber;
  }

  public String getPhoneNumber() {
    return mPhoneNumber;
  }
}
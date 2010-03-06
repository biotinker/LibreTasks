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

import android.content.Intent;

/**
 * This class encapsulates an Gmail received event. It wraps the intent that triggered this event
 * and provides access to any attribute data associated with it.
 */
public class GmailReceivedEvent extends Event {
  /** Event name (to match record in database) */
  public static final String APPLICATION_NAME = "Gmail";
  public static final String EVENT_NAME = "Gmail received";
  public static final String ACTION_NAME = "GMAIL_RECEIVED";
  public static final String ATTRIBUTE_FROM = "from";
  public static final String ATTRIBUTE_SUBJECT = "subject";
  public static final String ATTRIBUTE_BODY = "body";
  public static final String ATTRIBUTE_CC = "cc";
  public static final String ATTRIBUTE_BCC = "bcc";
  public static final String ATTRIBUTE_ATTACHMENT = "attachment";

  /** Cache any values that are requested because it is likely they will be asked for again */
  protected String from;
  protected String subject;
  protected String body;
  protected String cc;
  protected String bcc;
  protected String attachment;

  /**
   * Constructs a new GmailReceivedEvent object that holds an GmailReceived event fired intent. 
   * This intent holds the data needed to check the event against user defined rules.
   * 
   * @param intent
   *          the intent received when the PhoneRinging event was fired
   */
  public GmailReceivedEvent(Intent intent) {
    super(APPLICATION_NAME, EVENT_NAME, intent);
  }

  /**
   * Looks up attributes associated with this event.
   * 
   * @param attributeName
   *          the name of the attribute associated with this event
   * @return the data associated with the attribute
   * @throws IllegalArgumentException
   *           if the attribute is not of a type supported by this event
   */
  @Override
  public String getAttribute(String attributeName) {
  // TODO (dvo203): Replace by a generic method in a super class.
  if (attributeName.equals(ATTRIBUTE_FROM)) {
    if (from == null) {
      from = intent.getStringExtra(ATTRIBUTE_FROM);
    }
    return from;
  } else if (attributeName.equals(ATTRIBUTE_SUBJECT)) {
    if (subject == null) {
      subject = intent.getStringExtra(ATTRIBUTE_SUBJECT);
    }
    return subject;
  } else if (attributeName.equals(ATTRIBUTE_CC)) {
    if (cc == null) {
      cc = intent.getStringExtra(ATTRIBUTE_CC);
    }
    return cc;
  } else if (attributeName.equals(ATTRIBUTE_BCC)) {
    if (bcc == null) {
      bcc = intent.getStringExtra(ATTRIBUTE_BCC);
    }
    return bcc;
  } else if (attributeName.equals(ATTRIBUTE_BODY)) {
    if (body == null) {
      body = intent.getStringExtra(ATTRIBUTE_BODY);
    }
    return body;
  } else if (attributeName.equals(ATTRIBUTE_ATTACHMENT)) {
    if (attachment == null) {
      attachment = intent.getStringExtra(ATTRIBUTE_ATTACHMENT);
    }
    return attachment;
  } else {
    throw (new IllegalArgumentException());
  }
}

}

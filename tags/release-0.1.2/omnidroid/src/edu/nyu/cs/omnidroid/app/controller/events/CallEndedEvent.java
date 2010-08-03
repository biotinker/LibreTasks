package edu.nyu.cs.omnidroid.app.controller.events;

import android.content.Intent;
import android.util.Log;

/**
 * This class encapsulates an phone call ended event. It wraps the intent that triggered this event
 * and provides access to any attribute data associated with it. A phone call is considered ended
 * when the user hangs up the phone. This means that this event is not triggered when an unanswered
 * incoming call ended. There may be a need to split this class into 2 different classes in the
 * future to differentiate end of an incoming or outgoing calls.
 * 
 * TODO: (renctan) Support the phone number attribute and refactor it to the base class if possible.
 * As of API level 4, {@code incomingNumber} passed to {@code onCallStateChanged()} is empty. We
 * could work around this by remembering the number during CALL_STATE_RINGING, but that only covers
 * the case for incoming calls.
 */
public class CallEndedEvent extends PhoneCallEvent {
  /** Event name (to match record in database) */
  //TODO: (renctan) Move the string to be displayed on the UI to string.xml 
  public static final String EVENT_NAME = "Phone Call Ended";
  public static final String ACTION_NAME = "PHONE_CALL_ENDED";

  private static final String LOG_TAG = CallEndedEvent.class.getSimpleName();

  /**
   * Constructs a new CallEndedEvent object that holds an PHONE_CALL_ENDED event fired intent. This
   * intent holds the data needed to check the event against user defined rules.
   * 
   * @param intent
   *          the intent received when the PHONE_CALL_ENDED event was fired
   */
  public CallEndedEvent(Intent intent) {
    super(EVENT_NAME, intent);
    Log.d(LOG_TAG, EVENT_NAME);
  }
}

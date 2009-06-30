package edu.nyu.cs.omnidroid.external.catcherapp;

import android.content.ComponentName;
import android.content.Intent;
import android.test.AndroidTestCase;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Tests for the SMSCatcher class, which handles intents with SMS_SEND as their action.
 */
public class SMSCatcherTest extends AndroidTestCase {
  private static final String SMS_SEND = "SMS_SEND";
  private SMSCatcher receiver;
  private Intent intent;

  public void setUp() {
    receiver = new SMSCatcher();
    intent = new Intent();
    intent.setAction(SMS_SEND);
    intent.setComponent(new ComponentName("edu.nyu.cs.omnidroid", SMSCatcher.class.getName()));
  }

  /**
   * Tests that onReceive properly transforms the received Intent in order to launch the
   * SMSCatcherActivity which actually sends the SMS. Also checks that the user is notified through
   * the on-screen Toasts.
   */
  public void testOnReceive() {
    receiver.onReceive(getContext(), intent);
    assertEquals(new ComponentName("edu.nyu.cs.omnidroid", SMSCatcherActivity.class.getName()),
        intent.getComponent());
    assertEquals(Intent.FLAG_ACTIVITY_NEW_TASK, intent.getFlags());
    assertEquals(Toast.LENGTH_LONG, receiver.actionToast.getDuration());
    LinearLayout toastLayout = (LinearLayout) receiver.actionToast.getView();
    assertEquals(SMS_SEND, ((TextView) toastLayout.getChildAt(0)).getText());
    assertEquals(Toast.LENGTH_LONG, receiver.actionToast.getDuration());
  }

  /** Tests that an Intent with a different action does not show the "Caught!" Toast. */
  public void testOnReceive_wrongAction() {
    intent = new Intent();
    intent.setAction("FOO");
    receiver.onReceive(getContext(), intent);
    assertNull(receiver.smsToast);
  }
}

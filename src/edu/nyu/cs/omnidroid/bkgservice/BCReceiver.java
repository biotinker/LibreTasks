package edu.nyu.cs.omnidroid.bkgservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.nyu.cs.omnidroid.util.OmLogger;

/**
 * 
 * @author sucharita
 *
 */
/**
 * The Broadcast receiver receives any intent that is broadcast either by the system or by any other
 * application. If it is a system broadcast, the intent checks whether the receiver has the
 * permission to receive the specific intent, in the applications Manifest.xml file.
 * <p>
 * Presently the receiver has permissions to receive following intents: SMS_RECEIVED, PHONE_STATE
 * and any custom intent that is not broadcast by the system.
 */
public class BCReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    try {
      intent.setClass(context, edu.nyu.cs.omnidroid.core.DummyActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
      Log.i("Received Intent", intent.getAction());
    } catch (Exception e) {
      Log.i("Exception in Intent", e.getLocalizedMessage());
      OmLogger.write(context, "Unable to execute required action");
    }
  }

}

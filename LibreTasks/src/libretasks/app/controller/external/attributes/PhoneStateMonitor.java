/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
 /*******************************************************************************
 * Copyright 2009 Omnidroid - http://code.google.com/p/omnidroid
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
package libretasks.app.controller.external.attributes;

import libretasks.app.controller.events.MissedCallEvent;
import libretasks.app.controller.events.PhoneRingingEvent;
import libretasks.app.controller.events.CallEndedEvent;
import libretasks.app.controller.events.ServiceAvailableEvent;
import libretasks.app.controller.external.helper.telephony.PhoneStateMachine;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Monitors phone state and broadcasts Phone Ringing Intent when phone rings.
 */
public class PhoneStateMonitor implements SystemServiceEventMonitor {
  private static final String SYSTEM_SERVICE_NAME = "TELEPHONY_SERVICE";
  private static final String MONITOR_NAME = PhoneStateMonitor.class.getSimpleName();

  private static volatile String phoneNumber = null; 

  private static volatile boolean serviceAvailable = false; 
  
  /**
   * Since there is currently no direct way to determine if a phone call has ended (the system only
   * sends ringing, off-hook and idle events and the API does not provide an accessor to determine
   * if a phone call just ended), this object basically keeps track of the events (or rather
   * intents) from the system to know the current state of the telephone.
   * 
   * It is also used to differentiate whether CALL_STATE_IDLE received on {@code onCallStateChanged}
   * is during device initialization or transition from CALL_STATE_RINGING or CALL_STATE_OFFHOOK.
   * CALL_STATE_IDLE is used to determine that a phone call ended and can also be used to
   * differentiate whether the phone call event was an incoming or an outgoing phone call.
   * 
   * However, the code right now does not support Android phones that has multiple lines. To support
   * multiple lines, each line should need its own {@code PhoneStateMachine}, and the {@code
   * PhoneStateMachine} also needs to add something passed to the constructor a unique identifier
   * for each line.
   */
  private final PhoneStateMachine phoneStateMachine;

  private Context context;

  public PhoneStateMonitor(Context context) {
    this.context = context;
    this.phoneStateMachine = new PhoneStateMachine();
  }

  public void init() {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    if (tm == null) {
      Log.i(MONITOR_NAME, "Could not obtain TELEPHONY_SERVICE from the system.");
      return;
    }
    tm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    tm.listen(phoneStateListener1, PhoneStateListener. LISTEN_SERVICE_STATE);
  }

  public void stop() {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    tm.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
  }

  private final PhoneStateListener phoneStateListener = new PhoneStateListener() {
    public void onCallStateChanged(int state, String incomingNumber) {
      /**
       * Summary of known cases where these events are triggered:
       * 
       * CALL_STATE_IDLE
       * <ul>
       * <li>Once, during Android device startup</li>
       * <li>Whenever ring from an incoming call stops</li>
       * <li>Whenever the user hangs up the phone.</li>
       * </ul>
       * 
       * CALL_STATE_RINGING
       * <ul>
       * <li>When the Android device rings during incoming calls</li>
       * </ul>
       * 
       * CALL_STATE_OFFHOOK
       * <ul>
       * <li>When the user accepts an incoming call</li>
       * <li>When the user tries to commence and outgoing call (at moment when the device actually
       * dials)</li>
       * </ul>
       */
      try {
        if (state == TelephonyManager.CALL_STATE_RINGING) {
          Intent intent = new Intent(PhoneRingingEvent.ACTION_NAME);
          intent.putExtra(PhoneRingingEvent.ATTRIBUTE_PHONE_NUMBER, incomingNumber);
          context.sendBroadcast(intent);
          
          phoneNumber = incomingNumber;
          Log.d(MONITOR_NAME, "RINGING");
          phoneStateMachine.onRing();
        } else if (state == TelephonyManager.CALL_STATE_IDLE) {
          if (phoneStateMachine.isInboundOffhook() || phoneStateMachine.isOutboundOffhook()) {
            /**
             * This block covers transitions from these cases:
             * <ol>
             * <li>{@literal idle > off-hook > idle (outgoing call end)}</li>
             * <li>{@literal idle > ringing > off-hook > idle (incoming call end)}</li>
             * </ol>
             */
            Log.d(MONITOR_NAME, "inbound? " + phoneStateMachine.isInboundOffhook());
            Intent intent = new Intent(CallEndedEvent.ACTION_NAME);
            context.sendBroadcast(intent);
           } else if (phoneStateMachine.isRinging()) {
             Intent intent = new Intent(MissedCallEvent.ACTION_NAME);
             if (phoneNumber != null) {
               intent.putExtra(MissedCallEvent.ATTRIBUTE_PHONE_NUMBER, phoneNumber);
             }
             context.sendBroadcast(intent);             
           }

          Log.d(MONITOR_NAME, "IDLE");
          phoneStateMachine.onIdle();
        } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
          Log.d(MONITOR_NAME, "OFFHOOK");
          phoneStateMachine.onOffhook();
        }
      } catch (IllegalStateException e) {
        Log.e(MONITOR_NAME, "Invalid state", e);
      }
    }
  };

  private final PhoneStateListener phoneStateListener1 = new PhoneStateListener() {    
    @Override
    public void onServiceStateChanged(ServiceState serviceState){      
      if (serviceState.getState() == ServiceState.STATE_IN_SERVICE ) {
        Intent intent = new Intent(ServiceAvailableEvent.ACTION_NAME);
        context.sendBroadcast(intent);
        serviceAvailable = true;
      } else {
        serviceAvailable = false;
      }
    }
  };

  public String getMonitorName() {
    return MONITOR_NAME;
  }

  public String getSystemServiceName() {
    return SYSTEM_SERVICE_NAME;
  }
  
  public static boolean isServiceAvailable() {
    return serviceAvailable;
  }
}

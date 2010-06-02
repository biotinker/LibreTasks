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
package edu.nyu.cs.omnidroid.app.controller.events;
/**
 * represent only system broadcasted events, and only for those with no parameters. 
 *
 */
public enum SystemEvent {
  //TODO(Roger): pass in a context to use resource for internationalization.
  PowerConnectedEvent("Android", "Power connected", 
  "android.intent.action.ACTION_POWER_CONNECTED"),
  PowerDisconnectedEvent("Android", "Power Disconnected", 
  "android.intent.action.ACTION_POWER_DISCONNECTED"),
  BatteryLowEvent("Android", "Battery is low", 
  "android.intent.action.BATTERY_LOW"),
  BatteryOkayEvent("Android", "Battery is ok after low", 
  "android.intent.action.BATTERY_OKAY"),
  CameraButtonPressedEvent("Android", "Camera button was pressed", 
  "android.intent.action.CAMERA_BUTTON"),

  //TODO(Roger): think about use cases for the following events.
  //The rest of the events is not interest for now (i.e. no use case)
//  HeadsetPlugEvent("Android", "Wired Headset plugged in or unplugged", 
//  "android.intent.action.HEADSET_PLUG"),
//  ScreenOffEvent("Android", "Screen turns off", 
//  "android.intent.action.SCREEN_OFF"),
//  ScreenOnEvent("Android", "Screen turns on", 
//  "android.intent.action.SCREEN_ON"),
//  DockEvent("Android", "The device is docked or undocked", 
//  "android.intent.action.DOCK_EVENT"),
//  DeviceStorageLowEvent("Android", "storage is low", 
//  "android.intent.action.DEVICE_STORAGE_LOW"),
//  DeviceStorageOkayEvent("Android", "storage is ok after low", 
//  "android.intent.action.DEVICE_STORAGE_OK"),
//  GtalkConnectedEvent("Android", "GTalk connection has been established", 
//  "android.intent.action.GTALK_CONNECTED"),
//  GtalkDisconnectedEvent("Android", "GTalk connection has been disconnected", 
//  "android.intent.action.GTALK_DISCONNECTED"),
//  TimezoneChangedEvent("Android", "The timezone has changed", 
//  "android.intent.action.TIMEZONE_CHANGED"),
//  UserPresentEvent("Android", "user is present", 
//  "android.intent.action.USER_PRESENT"),
//  WallPaperChangedEvent("Android", "Wallpaper has changed", 
//  "android.intent.action.WALLPAPER_CHANGED"),
//  InputMethodChangedEvent("Android", "Input Method Changed", 
//  "android.intent.action.INPUT_METHOD_CHANGED")
  
  ;

  public final String APPLICATION_NAME;
  public final String EVENT_NAME;
  public final String ACTION_NAME;

  SystemEvent(String APPLICATION_NAME, String EVENT_NAME, String ACTION_NAME) {
    this.APPLICATION_NAME = APPLICATION_NAME;
    this.EVENT_NAME = EVENT_NAME;
    this.ACTION_NAME = ACTION_NAME;
  }
}
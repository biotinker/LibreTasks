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
package libretasks.app.controller.events;
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

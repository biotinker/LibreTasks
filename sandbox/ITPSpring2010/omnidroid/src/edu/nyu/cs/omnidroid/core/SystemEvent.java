package edu.nyu.cs.omnidroid.core;
/**
 * represent only system broadcasted events, and only for those with no parameters. 
 *
 */
public enum SystemEvent {
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
  DeviceStorageLowEvent("Android", "storage is low", 
  "android.intent.action.DEVICE_STORAGE_LOW"),
  DeviceStorageOkayEvent("Android", "storage is ok after low", 
  "android.intent.action.DEVICE_STORAGE_OK"),
  DockEvent("Android", "The device is docked or undocked", 
  "android.intent.action.DOCK_EVENT"),
  GtalkConnectedEvent("Android", "GTalk connection has been established", 
  "android.intent.action.GTALK_CONNECTED"),
  GtalkDisconnectedEvent("Android", "GTalk connection has been disconnected", 
  "android.intent.action.GTALK_DISCONNECTED"),
  HeadsetPlugEvent("Android", "Wired Headset plugged in or unplugged", 
  "android.intent.action.HEADSET_PLUG"),
  ScreenOffEvent("Android", "Screen turns off", 
  "android.intent.action.SCREEN_OFF"),
  ScreenOnEvent("Android", "Screen turns on", 
  "android.intent.action.SCREEN_ON"),
  TimezoneChangedEvent("Android", "The timezone has changed", 
  "android.intent.action.TIMEZONE_CHANGED"),
  UserPresentEvent("Android", "user is present", 
  "android.intent.action.USER_PRESENT"),
  WallPaperChangedEvent("Android", "Wallpaper has changed", 
  "android.intent.action.WALLPAPER_CHANGED"),
  InputMethodChangedEvent("Android", "Input Method Changed", 
  "android.intent.action.INPUT_METHOD_CHANGED");

  public final String APPLICATION_NAME;
  public final String EVENT_NAME;
  public final String ACTION_NAME;

  SystemEvent(String APPLICATION_NAME, String EVENT_NAME, String ACTION_NAME) {
    this.APPLICATION_NAME = APPLICATION_NAME;
    this.EVENT_NAME = EVENT_NAME;
    this.ACTION_NAME = ACTION_NAME;
  }
}
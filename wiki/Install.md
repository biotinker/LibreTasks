# Install by Searching the Android Market #

Search for "Omnidroid" on the Market application.

# Install by using a QR code #

If you have a barcode scanner, just scan this QR code:

<a href='market://search?q=pname:edu.nyu.cs.omnidroid.app'><img src='http://omnidroid.googlecode.com/svn/docs/qr_code.png' /></a>


# Install using the android package file #

<a href='http://andrewcase.net/files/Omnidroid-release.apk'>Omnidroid-release.apk</a>


# Install from Source Code #
## Requirements ##
  * Ant
  * Android SDK 1.6

## Building/Installing with Ant ##
  1. Start an [emulator](http://developer.android.com/guide/developing/tools/emulator.html) to install onto.
    * `/path/to/android-sdk/tools/emulator`
  1. Checkout the omnidroid project from the trunk of the [repository](http://code.google.com/p/omnidroid/source/checkout).
    * `svn checkout http://omnidroid.googlecode.com/svn/trunk/omnidroid`
  1. Edit the build.properties file to point to your Android SDK.
  1. Build, Install, or Update
    * To build an android package run: `ant build`
    * To install on the running emulator: `ant install`
    * To update an install on the emulator: `ant reinstall`
> > Specify SDK location by using "-Dsdk-location=" argument, for example:
> > ant install -Dsdk-location=/Library/android-sdk-mac\_x86/
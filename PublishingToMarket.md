How to publish to the android market.

# Information Needed #
Keystore password: Ask Maintainers

Android Market account info: Ask Maintainers


# Steps To Build, Sign, and Publish Omnidroid #

  1. Checkout latest code from SCM
```
  svn co http://omnidroid.googlecode.com/svn/trunk/omnidroid
```
  1. Change to project directory
```
  cd omnidroid
```
  1. Bump the two version strings in AndroidManifest.xml and commit changes
  1. Set key.store and key.alias in build.properties.  Example:
```
  echo "sdk.dir=/path/to/android-sdk" >> build.properties
  echo "key.store=/path/to/key/omnidroid-release.keystore" >> build.properties
  echo "key.alias=omnidroid" >> build.properties
```
  1. Compile for release (ant does the signing)
```
  ant clean
  ant release
```
  1. Publish to market
> Upload bin/omnidroid-release.apk update to http://market.android.com

# Generating a Keystore #

  * THIS SHOULD NEVER HAVE TO BE DONE AGAIN - ASK A MAINTAINER FOR THE KEY

> Example:
```
  keytool -genkey -v -dname \
  "CN=Omnidroid Maintainer, OU=Omnidroid Developers, O=Omnidroid, L=New York, ST=New York, C=US" \
  -alias omnidroid \
  -keystore ~/path/to/releasekey/omnidroid-release.keystore \
  -validity 10000
```
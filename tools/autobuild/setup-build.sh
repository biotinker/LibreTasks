#!/bin/sh
BUILD_DIR="/home/$USER/build"
ANDROID_DIR="$BUILD_DIR/tools/android-sdk-linux_x86-1.5_r2"
ANDROID_BIN_DIR="$ANDROID_DIR/tools"

#
# This file sets up the environment so the autobuilder can run. It creates
# an Android AVD named omnidroid-autobuild
#

createavd() {
  # Create an avd, overwriting the existing one if it exists
  $ANDROID_BIN_DIR/android create avd -n omnidroid-autobuild -t 2 -f
}

createavd
exit $?

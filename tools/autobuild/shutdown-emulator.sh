#!/bin/sh
BUILD_DIR="/home/$USER/build"
ANDROID_DIR="$BUILD_DIR/tools/android-sdk-linux_x86-1.5_r2"
ANDROID_BIN_DIR="$ANDROID_DIR/tools"

#
# This file shuts down the only running android emulator. If more than one 
# emulator is running, it fails.
#
# This script should run occasionally, followed by run-emulator.sh to start a 
# new emulator to ensure a clean build.
#
# TODO(kaijohnson): figure out how to connect to the emulator running the
# omnidroid-autobuild avd so we don't have to worry about other emulators.
#

shutdownemulator() {
  adb -e emu kill
}

shutdownemulator
exit $?

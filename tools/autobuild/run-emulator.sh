#!/bin/sh
BUILD_DIR="/home/$USER/build"
ANDROID_DIR="$BUILD_DIR/tools/android-sdk-linux_x86-1.5_r2"
ANDROID_BIN_DIR="$ANDROID_DIR/tools"

#
# This file runs an android emulator for use in testing in ant-build.sh.
# It will not return until the emulator has been closed:
#   $ telnet localhost 5554 # telnet to the port of the running emulator
#   $ kill # shut down the running emulator
# Or:
#   $ adb emu kill # kills first emulator it sees running
#
# shutdown_emulator.sh should run occasionally to start a new emulator to ensure
# a clean build.
#

startemulator() {
  $ANDROID_BIN_DIR/emulator -avd omnidroid-autobuild # TODO(kaijohnson): consider -wipe-data
}

startemulator
exit $?

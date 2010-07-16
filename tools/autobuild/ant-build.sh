#!/bin/sh
# This script will checkout the latest code from the reposiotry
# compile and run tests on the emulator.
#
# Notes: This script requires a graphical interface to run.
#
# TODO(acase): Sign app and produce a downloadable binary.
# TODO(acase): Make runs on post-commit and support parallel compilation

#############
# CONSTANTS #
#############
#PROJECT_HOME=`dirname "$0"`
BUILD_DIR="/home/$USER/projects/omnidroid/build"
REPO_BASE="http://omnidroid.googlecode.com/svn"
CO_DIR="$BUILD_DIR/work/checkout"
TRUNK_DIR="$CO_DIR/trunk"
APP_DIR="$TRUNK_DIR/omnidroid"
TEST_DIR="$TRUNK_DIR/omnidroid-test"
ANDROID_VER="android-4" # From 'android list targets'
ANDROID_HOME="$BUILD_DIR/tools/android-sdk-linux_86"
AVD_DIR="/home/$USER/.android/avd/autobuild-avd.avd/"
AVD_INI="/home/$USER/.android/avd/autobuild-avd.ini"
AVD_NAME="autobuild-avd"
AVD_PORT="5580"
AVD_SERIAL="emulator-$AVD_PORT"
JAVA_HOME="/opt/java"
ANDROID_BIN_DIR="$ANDROID_HOME/tools"
PATH="$ANDROID_BIN_DIR:$ANT_BIN_DIR:/usr/local/bin:/opt/bin:/opt/java/bin:/usr/bin:/bin:/usr/sbin:/sbin"
OMNIDROID_APK="$APP_DIR/bin/omnidroid-debug.apk"
OMNIDROID_TEST_APK="$TEST_DIR/bin/omnidroid-test-debug.apk"
TEST_CLASS="edu.nyu.cs.omnidroid.app.AllTests"
TEST_RUNNER="edu.nyu.cs.omnidroid.app.tests/android.test.InstrumentationTestRunner"
DISPLAY=":0" # Required for emulator to grab graphical interface
#XAUTH="/tmp/.gdmZALRWU" # Required for emulator to grab graphical interface

export PATH
export JAVA_HOME

#############
# FUNCTIONS #
#############
# Makes sure we have a standard setup
setup() {
    # Freshen up our starting system
    echo "  + Setup our build environment" && \
    rm -rf $CO_DIR && \
    rm -rf $AVD_DIR && \
    rm -rf $AVD_INI && \
    mkdir -p $CO_DIR && \
    mkdir -p $TRUNK_DIR && \
    echo "  + Creating test AVD" && \
    echo "no" | android create avd --force -n $AVD_NAME -t $ANDROID_VER > /dev/null && \
    echo "  + Starting android server" && \
    adb start-server && \
    echo "  + Starting android emulator" && \
    #(DISPLAY=$DISPLAY XAUTHORITY=$XAUTH emulator -avd $AVD_NAME -port $AVD_PORT &) 
    (DISPLAY=$DISPLAY emulator -avd $AVD_NAME -port $AVD_PORT &) 
    return $?
}

# Cleans up all the left overs from our current build
cleanup() {
    echo "  + Reseting build environment" && \
    adb -s $AVD_SERIAL emu kill >/dev/null 2>&1
    echo "  + Deleting test AVD"
    android delete avd -n $AVD_NAME >/dev/null 2>&1
}

# Fresh checkout from source code management
checkout() {
    # Checkout the source
    echo "  + Checking out source from SVN" && \
    cd $CO_DIR && \
    echo "  +    svn co ${REPO_BASE}/trunk" && \
    svn co ${REPO_BASE}/trunk >/dev/null
    return $?
}

# Build from scratch
build() {
    cd $APP_DIR && \
    echo "  + Using android tools to build omnidroid ant file." && \
    android update project -p . -t $ANDROID_VER && \
    echo "  + Building omnidroid from ant build file" && \
    ant debug && \
    echo "  + Copying package to $TEST_DIR/libs to test with." && \
    mkdir $TEST_DIR/libs && \
    cd bin/classes && \
    jar cf omnidroid.jar . && \
    mv omnidroid.jar $TEST_DIR/libs
    return $?
}

# Run Android/J-UNIT Tests
run_tests() {
    cd $TEST_DIR
    echo "  + Using android tools to build omnidroid ant file." && \
    android update project -p . -t $ANDROID_VER && \
    echo "  + Building omnidroid-test from ant build file" && \
    ant debug && \
    echo "  + Waiting a few minutes for the emulator to come up" && \
    sleep 120 && \
    echo "  + Installing omnidroid onto running emulator" && \
    adb -s $AVD_SERIAL install -r $OMNIDROID_APK && \
    echo "  + Installing omnidroid-test onto running emulator" && \
    adb -s $AVD_SERIAL install -r $OMNIDROID_TEST_APK && \
    echo "  + Running Junit Tests on project" && \
    adb -s $AVD_SERIAL shell am instrument -w -e class $TEST_CLASS $TEST_RUNNER
    return $?
}


########
# MAIN #
########
echo "*** Pre-setup ***" && \
cleanup
echo "*** Setup ***" && \
setup && \
echo "*** Checkout ***" && \
checkout && \
echo "*** Build ***" && \
build && \
echo "*** Test ***" && \
run_tests
success=$?
echo "*** Cleanup ***" && \
cleanup
exit $success

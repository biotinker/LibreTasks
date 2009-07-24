#!/bin/sh
#PROJECT_HOME=`dirname "$0"`
BUILD_DIR="/home/$USER/build"
REPO_BASE="http://omnidroid.googlecode.com/svn"
REPO_VENDOR="$REPO_BASE/vendor"
CO_DIR="$BUILD_DIR/work/checkout"
TRUNK_DIR="$CO_DIR/trunk"
APP_DIR="$TRUNK_DIR/omnidroid"
TEST_DIR="$TRUNK_DIR/omnidroid-test"
ANDROID_DIR="$BUILD_DIR/tools/android-sdk-linux_x86-1.1_r1"
ANDROID_BIN_DIR="$ANDROID_DIR/tools"
MANIFEST="AndroidManifest.xml"
LOG_DIR="$BUILD_DIR/logs"
export JAVA_HOME="/opt/java"
export PATH="/usr/local/bin:/opt/bin:/opt/java/bin:/usr/bin:/bin:/usr/sbin:/sbin"
export CLASSPATH="${CLASSPATH}"

cleanup() {
    # Cleanup
    echo "  + Removing $CO_DIR" && \
    rm -rf $CO_DIR && \
    mkdir -p $CO_DIR && \
    mkdir -p $TRUNK_DIR && \
    chmod 755 $LOG_DIR && \
    chmod 644 $LOG_DIR/* && \
    echo "  + Environment set to:" && \
    env && \
    return $?
}

checkout() {
    # Checkout the source
    echo "  + Checking out source from SVN." && \
    cd $CO_DIR && \
    svn co ${REPO_BASE}/trunk
    return $?
}

build() {
    # Build it
    echo "  + Using android tools to build an ant file" && \
    cd $APP_DIR && \
    $ANDROID_BIN_DIR/activitycreator --out $APP_DIR $MANIFEST && \
    echo "  + Building from ant build file" && \
    ant && \
    echo "  + Copying omnidroid.jar to $TEST_DIR/libs" && \
    # copy jar to omnidroid-test to run against
    mkdir $TEST_DIR/libs && \
    cd bin/classes
    jar cf omnidroid.jar .
    mv omnidroid.jar $TEST_DIR/libs
    return $?
}

run_tests() {
    # Run J-unit Tests
    cd $TEST_DIR
    # copy activitycreator command from above for omnidroid-test
    $ANDROID_BIN_DIR/activitycreator --out $TEST_DIR $MANIFEST && \
    echo "  + Building omnidroid-test from ant build file" && \
    ant && \
    echo "  + Installing omnidroid onto running emulator" && \
    $ANDROID_BIN_DIR/adb install -r $APP_DIR/bin/Overview-debug.apk && \
    echo "  + Installing omnidroid-test onto running emulator" && \
    $ANDROID_BIN_DIR/adb install -r $TEST_DIR/bin/ACTIVITY_NAME-debug.apk && \
    echo "  + Running Junit Tests on project" && \
    $ANDROID_BIN_DIR/adb shell am instrument -w edu.nyu.cs.omnidroid.tests/android.test.InstrumentationTestRunner
}

cleanup && \
checkout && \
build && \
run_tests
exit $?

#!/bin/sh
#PROJECT_HOME=`dirname "$0"`
BUILD_DIR="/home/acase/classes/itp/build"
REPO_URL="https://subversive.cims.nyu.edu/itp/sp09-google"
CO_DIR="$BUILD_DIR/work/checkout"
TEST_DIR="$BUILD_DIR/work/test"
ANDROID_DIR="$BUILD_DIR/tools/android-sdk-linux_x86-1.1_r1"
ANDROID_BIN_DIR="$ANDROID_DIR/tools"
PKG_NAME="edu.nyu.cs.omnidroid"
LOG_DIR="$BUILD_DIR/logs"
export JAVA_HOME="/opt/java"
export PATH="/usr/local/bin:/opt/bin:/opt/java/bin:/usr/bin:/bin:/usr/sbin:/sbin"

cleanup() {
    # Cleanup
    echo "  + Removing $TEST_DIR" && \
    rm -rf $TEST_DIR && \
    echo "  + Removing $CO_DIR" && \
    rm -rf $CO_DIR && \
    mkdir -p $CO_DIR && \
    mkdir -p $TEST_DIR && \
    chmod 755 $LOG_DIR && \
    chmod 644 $LOG_DIR/* && \
    return $?
}

checkout() {
    # Checkout the source
    echo "  + Checking out source from SVN." && \
    cd $CO_DIR && \
    svn co $REPO_URL/trunk
    return $?
    #svn co $REPO_URL/tools/cruisecontrol && \
}

build() {
    # Build it
    echo "  + Using android tools to build an ant file" && \
    cd $CO_DIR/trunk && \
    env && \
    $ANDROID_BIN_DIR/activitycreator --out $TEST_DIR $PKG_NAME && \
    cd $TEST_DIR && \
    echo "  + Building from ant build file" && \
    ant
    return $?
}

cleanup && \
checkout && \
build
exit $?

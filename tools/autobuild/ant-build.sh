#!/bin/sh
#PROJECT_HOME=`dirname "$0"`
BUILD_DIR="/home/acase/projects/omnidroid/build"
REPO_URL="http://omnidroid.googlecode.com/svn/"
CO_DIR="$BUILD_DIR/work/checkout"
TEST_DIR="$CO_DIR/trunk"
ANDROID_DIR="$BUILD_DIR/tools/android-sdk-linux_x86-1.1_r1"
ANDROID_BIN_DIR="$ANDROID_DIR/tools"
MANIFEST="AndroidManifest.xml"
LOG_DIR="$BUILD_DIR/logs"
export JAVA_HOME="/opt/java"
export PATH="/usr/local/bin:/opt/bin:/opt/java/bin:/usr/bin:/bin:/usr/sbin:/sbin"

cleanup() {
    # Cleanup
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
}

build() {
    # Build it
    echo "  + Using android tools to build an ant file" && \
    cd $TEST_DIR && \
    env && \
    $ANDROID_BIN_DIR/activitycreator --out $TEST_DIR $MANIFEST && \
    echo "  + Building from ant build file" && \
    ant
    return $?
}

cleanup && \
checkout && \
build
exit $?

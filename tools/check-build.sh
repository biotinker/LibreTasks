#!/bin/sh

# This script checks to see if a new build is waiting to be built.
# If so, it builds it and sends an email based on that build's
# success or failure

DATE=`date +%Y.%m.%d_%T`
BUILD_DIR="$HOME/classes/itp/build"
BUILD_SCRIPT="$BUILD_DIR/bin/ant-build.sh"
BUILD_FLAG="$BUILD_DIR/.NEEDS_REBUILD"
TMPFILE="$BUILD_DIR/var/tmp/check-build"
EMAIL_HEAD="$BUILD_DIR/share/templates/email_header"
EMAIL_FOOT="$BUILD_DIR/share/templates/email_footer"
LOG="$BUILD_DIR/logs/omnidroid.build.log.$DATE"
#TO="itp-sp09-google-private@cims.nyu.edu"
TO="acase@cims.nyu.edu"
SUBJECT="[Build] Rev#"

initialize() {
    if [ -f $TMPFILE ]; then
        # If this script is already running, then don't build
        exit 1;
    elif [ ! -f $BUILD_FLAG ]; then
        # If no build flag has been generated, then don't build
        exit 2;
    else
        # Otherwise, it's off to the races!
        /bin/touch ${TMPFILE}
        REV=`grep "^New Revision" ${BUILD_FLAG} | awk '{print $3}'`
    fi
}

# Build our software
build() {
    $BUILD_SCRIPT >$LOG 2>&1 
    if [ $? == "0" ]; then
        build_result=0
        SUBJECT="$SUBJECT $REV SUCCESSFUL"
    else
        build_result=1
        SUBJECT="$SUBJECT $REV FAILURE"
    fi
}

# Email success/failure email
email_results() {
    echo "to: $TO" >> ${TMPFILE}
    echo "subject: $SUBJECT" >> ${TMPFILE}
    cat ${EMAIL_HEAD} >> ${TMPFILE}
    echo "== REVISION COMMIT LOG =============================" >> ${TMPFILE}
    cat $BUILD_FLAG >> ${TMPFILE}
    echo "== /REVISION COMMIT LOG ============================" >> ${TMPFILE}
    echo "== BUILD LOG =======================================" >> ${TMPFILE}
    cat $LOG >> ${TMPFILE}
    echo "== /BUILD LOG ======================================" >> ${TMPFILE}
    cat ${EMAIL_FOOT} >> ${TMPFILE}
    /usr/lib/sendmail -t < ${TMPFILE}
}

# Remove lock file
cleanup() {
    /bin/rm -f $BUILD_FLAG
    /bin/rm -f ${TMPFILE}
}

initialize
build
email_results
cleanup

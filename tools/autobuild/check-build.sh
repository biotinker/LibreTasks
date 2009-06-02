#!/bin/sh

# This script checks to see if a new build is waiting to be built.
# If so, it builds it and sends an email based on that build's
# success or failure

DATE=`date +%Y.%m.%d_%T`
BUILD_DIR="$HOME/projects/omnidroid/build"
BUILD_SCRIPT="$BUILD_DIR/bin/ant-build.sh"
BUILD_FLAG="$BUILD_DIR/.NEEDS_REBUILD"
REPO_URL="http://omnidroid.googlecode.com/svn/trunk"
TMPFILE="$BUILD_DIR/var/tmp/check-build"
EMAIL_HEAD="$BUILD_DIR/share/templates/email_header"
EMAIL_FOOT="$BUILD_DIR/share/templates/email_footer"
LOG="$BUILD_DIR/logs/omnidroid.build.log.$DATE"
TO="omnidroid-build@googlegroups.com"
#TO="acase@cims.nyu.edu"
REV_OLD_FILE="$BUILD_DIR/.REV_OLD"
REV_NEW_FILE="$BUILD_DIR/.REV_NEW"
REV_OLD=""
REV_NEW=""
SUBJECT="Rev#"

update_version() {
    mv $REV_NEW_FILE $REV_OLD_FILE
    svn info $REPO_URL | grep ^Rev | awk '{print $2}' > $REV_NEW_FILE
}

initialize() {
    if [ -f $TMPFILE ]; then
        # If this script is already running, then don't build
        exit 1;
    fi
    update_version
    if [ ! `cat ${REV_NEW_FILE}` -gt `cat ${REV_OLD_FILE}` ]; then
        # If no update has been made, then don't build
        exit 2;
    else
        # Otherwise, it's off to the races!
        /bin/touch ${TMPFILE}
        REV_NEW=`cat ${REV_NEW_FILE}`
        REV_OLD=`cat ${REV_OLD_FILE}`

    fi
    touch $LOG
}

# Build our software
build() {
    echo "Running $BUILD_SCRIPT" >> $LOG
    $BUILD_SCRIPT >>$LOG 2>&1 
    if [ $? == "0" ]; then
        build_result=0
        SUBJECT="$SUBJECT $REV_NEW SUCCESSFUL"
    else
        build_result=1
        SUBJECT="$SUBJECT $REV_NEW FAILURE"
    fi
}

# Email success/failure email
email_results() {
    echo "to: $TO" >> ${TMPFILE}
    echo "subject: $SUBJECT" >> ${TMPFILE}
    cat ${EMAIL_HEAD} >> ${TMPFILE}
    echo "== OLD REVISION: ${REV_OLD}" >> ${TMPFILE}
    echo "== NEW REVISION: ${REV_NEW}" >> ${TMPFILE}
    echo "== REVISION COMMIT LOG =============================" >> ${TMPFILE}
    svn log ${REPO_URL} -v -r ${REV_OLD}:${REV_NEW} >> ${TMPFILE}
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

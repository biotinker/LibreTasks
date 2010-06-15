#!/bin/sh

# This script checks to see if a new build is waiting to be built.
# If so, it builds it and sends an email based on that build's
# success or failure

DATE=`date +%Y.%m.%d_%T`
BUILD_DIR="$HOME/projects/omnidroid/build"
BUILD_SCRIPT="$BUILD_DIR/bin/ant-build.sh"
BUILD_FLAG="$BUILD_DIR/.NEEDS_REBUILD"
REPO_URL="http://omnidroid.googlecode.com/svn"
TMPFILE="$BUILD_DIR/var/tmp/check-build"
EMAIL_HEAD="$BUILD_DIR/share/templates/email_header"
EMAIL_FOOT="$BUILD_DIR/share/templates/email_footer"
LOG_DIR="$BUILD_DIR/logs"
LOG="$LOG_DIR/omnidroid.build.log.$DATE"
TO="omnidroid-build@googlegroups.com"
REV_OLD_FILE="$BUILD_DIR/.REV_OLD"
REV_NEW_FILE="$BUILD_DIR/.REV_NEW"
REV_OLD=""
REV_NEW=""
SUBJECT="rev"

update_version() {
    new_version=`svn info $REPO_URL | grep ^Rev | awk '{print $2}'`
    if [ "$new_version" -gt 0 ]; then
        mv $REV_NEW_FILE $REV_OLD_FILE
        echo "$new_version" > $REV_NEW_FILE
    else
        exit "Failed to pull from $REPO_URL"
    fi
}

initialize() {
    mkdir -p $LOG_DIR && \
    chmod 755 $LOG_DIR && \
    touch $LOG && \
    chmod 644 $LOG
    # If this script is already running, then don't build
    if [ -f $TMPFILE ]; then
        echo "$TMPFILE found, build probably already in progress." >> $LOG
        exit 1;
    fi

    # Upon exit, cleanup locks
    trap "{ rm -f $TMPFILE ; exit 255; }" EXIT
    /bin/touch ${TMPFILE}

    # See if we have a new repo version
    update_version
    REV_NEW=`cat ${REV_NEW_FILE}`
    REV_OLD=`cat ${REV_OLD_FILE}`
    if [ ! $REV_NEW -gt $REV_OLD ]; then
        # If no update has been made, then don't build
        echo "Old Revision=$REV_OLD, New Revision=$REV_NEW." >> $LOG
        echo "Old >= New.  Skipping build." >> $LOG
        exit 2;
    fi
}

# Build our software
build() {
    echo "Running $BUILD_SCRIPT" >> $LOG
    $BUILD_SCRIPT >>$LOG 2>&1 
    if [ $? == "0" ]; then
        build_result=0
        SUBJECT="$SUBJECT $REV_NEW Successful"
    else
        build_result=1
        SUBJECT="$SUBJECT $REV_NEW Failure"
    fi
}

# Email success/failure email
email_results() {
    # Some SVN commands take oldrevision+1 as a param
    # while others take oldrevision as a param.
    let REV_OLD_PP="$REV_OLD"+1
    echo "to: $TO" >> ${TMPFILE}
    echo "subject: $SUBJECT" >> ${TMPFILE}
    cat ${EMAIL_HEAD} >> ${TMPFILE}
    echo "== OLD REVISION: ${REV_OLD}" >> ${TMPFILE}
    echo "== NEW REVISION: ${REV_NEW}" >> ${TMPFILE}
    echo "== REVISION COMMIT LOG =============================" >> ${TMPFILE}
    svn log ${REPO_URL} -v -r ${REV_OLD_PP}:${REV_NEW} >> ${TMPFILE}
    svn diff ${REPO_URL} -r ${REV_OLD}:${REV_NEW} >> ${TMPFILE}
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

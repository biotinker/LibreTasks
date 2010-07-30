#!/bin/bash
SVNROOT=/home/acase/projects/omnidroid/svn
cd $SVNROOT && \
svn log --xml -v > docs/statsvn/svn.log && \
cd docs/statsvn && \
java -jar ../../tools/statsvn/statsvn.jar svn.log ../../trunk/ && \
svn add * && \
svn commit -m "Updating svn stats"

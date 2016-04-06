This is a simple document to help you add an Event/Action to Omnidroid

## Additional steps after [r744](https://code.google.com/p/omnidroid/source/detail?r=744) ##
Please refer to http://code.google.com/p/omnidroid/wiki/MakingChangesOnTheDatabase for more details on how to modify the database (previously done on DbData.java).

## Add Event ##

To add an event to Omnidroid, first one need to create an Monitor class to capture the intent. If the intent is broadcast directly to Omnidroid (or to all apps), no need to create monitor. Secondly create an Event class, as a wrapper class for the intent. The third step is to let Omnidroid aware of this event, this including make changes to AndroidManifest.xml, IntentParser.java, DbDate.java(this class should eventually replaced by a static file) and FactoryDynamicUI.java.


here's an example code review for add a new event:
http://codereview.appspot.com/890043/show

In this code review, the following is the changed/added classes and their purpose.

M  AndroidManifest.xml -- add support to service, activity etc.

M src/edu/nyu/cs/omnidroid/core/IntentParser.java -- add the support to the new intent

A src/edu/nyu/cs/omnidroid/core/SomeEvent.java -- the wrapper for the new event.

M src/edu/nyu/cs/omnidroid/external/attributes/EventMonitoringService.java -- add more monitor if you need.

A src/edu/nyu/cs/omnidroid/external/attributes/SomeMonitor.java -- the new monitor.

M src/edu/nyu/cs/omnidroid/model/DbData.java -- prepopulate the db.

M src/edu/nyu/cs/omnidroid/ui/simple/FactoryDynamicUI.java -- create UI support for the new event.



## Add Action ##

To add an Action to Omnidroid is relatively easier, the basic idea is to create an intent and fire corresponding application on that intent. So the first step is to create the Action, which contains the intent waiting for fire. And the other step is to let Omnidroid know how and when to use the Action. This including modify the following classes: AndroidManifest.xml, CoreActionDbHelper.java and DbData.java. And in order to fire the Action, you need to create a service for that, (or added it to an existing service).


Here's an example code review of adding a new Action.
http://codereview.appspot.com/822041/show

In this code review, the following is the changed/added classes and their purpose.

M AndroidManifest.xml
A src/edu/nyu/cs/omnidroid/core/SomeAction.java -- the action

A src/edu/nyu/cs/omnidroid/external/actions/SomeService.java -- create a service to do the job if necessary.

M src/edu/nyu/cs/omnidroid/model/CoreActionsDbHelper.java -- add support to the action

M src/edu/nyu/cs/omnidroid/model/DbData.java -- prepopulate Db
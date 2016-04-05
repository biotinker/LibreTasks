General Design Overview for Omnidroid

# ARCHITECTURE #

Omnidroid presents a simple event handler system.  Omnidroid processes data on three levels:
  * Application Registration
  * User Setup/Configuration
  * Event Handling

## Application Registration ##
In order for Omnidroid to handle events, it needs to have applications register with it.  This can either be a passive registration where Omnidroid has built-in support for a specific application, or active registration where an application is OmniAware and provides the necessary information to Omnidroid.

For OmniAware applications, registration occurs at two times.
  * TODO: First when Omnidroid is first installed it will query the system for packages and try to register (both passively and actively) any applications which are OmniCompatible.
  * TODO: Secondly, when a new package is installed, Omnidroid will check to see if newly installed package is OmniCompatible and register it if possible.

When applications are registered with Omnidroid it populates the Omnidroid database with the information that the application provides Event wise and the information it can receive on Actions through intents.


## User Setup/Configuration ##
The user can customize Omnidroid by providing customized rules and also by customizing the Omnidroid application settings.  Users can: create, edit, delete, enable, and disable Rules as the need arises.  Also there are many application settings that can be tweaked based on user preference.  When users make changes to rules or in the settings, it populates and updates the database.


## Event Handling ##
Event handling is the core of Omnidroid functionality.  The data flow for event handling is seen below:
```
   ------------     --------------------     ----------------      -----------
  | App Events |-->| Broadcast Receiver |-->| Handler Service |-->|App Actions|
   ------------     --------------------     ----------------      -----------
```
Applications cause events.  Omnidroid catches these events using a Broadcast Receiver.  The Broadcast Receiver then hands this event off to the Handler Service.  The Handler Service then checks to see if the event matches a given rule set in the user configuration.  If it does, it then gathers the data needed and hands this data over to the Action application.

# DEFINITIONS #

See [Glossary](Glossary.md)
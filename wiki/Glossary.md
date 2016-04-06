### Action ###
An intent that Omnidroid initiates as a result of an Omnidroid rule.

### Action Parameters ###
Data that must be sent along with the action.

### Application Configuration File ###
Lists of applications that are Omni-aware which includes a list of events that application triggers and a list of actions that application can perform along with what filters can be applied for it and how to map data for each of these items.

### Event ###
An intent that is being received by Omnidroid Intent Listener.

### Event Attributes ###
Data that is received along with the event.

### Event Processor ###
Refers to component that processes events and performs required action, based on given rules.

### Global Attributes ###
A system or third party attribute that provides additional data.

### Intent (_or Android Intent_) ###
Refers to a Android Intent.

### Intent Listener ###
Refers to component that listens for Android Intents that we have active rules for.

### Intent Parser ###
Parses the intent according to it's type and creates properly formated Event.

### OmniAware ###
Applications which provide an Omnidroid? Application Configuration File for their app.

### OmniCompatible ###
Applications which are either Omni-aware or have built-in Omnidroid support.

### OmniHandler ###
An event handler that is configured and stored for use in Omnidroid.  It represents a user specified correlation between events and actions.

### Rule ###
See OmniHandler.

### User Configuration File ###
> List of Omnidroid global settings as well as the set of OmniHandlers currently configured.
# Setting up Your environment to Develop & Test Omnidroid #
<br />
## I - Setup Android development environment ##
  1. [How to install the Android SDK](http://developer.android.com/sdk/installing.html)<br />
  1. [Hello World Tutorial](http://developer.android.com/guide/tutorials/hello-world.html) (To verify your environment)<br /><br />

## II - Understanding Android ##
  1. [Android Fundamentals](http://developer.android.com/guide/topics/fundamentals.html) (Very good article, should be read first.)<br /><br />

## III - Getting omnidroid to run in emulator ##
### Prerequisite ###
  * Make sure you have Android SDK, Eclipse(with Android plugin) installed.
### Create a Android Virtual Device (If you already create a 1.6 AVD (android-4), skip this step.) ###
  * In Eclipse: Windows -> Android AVD Manager <br />
  * Name = Whatever you like<br />
  * Target = Android 1.6 (android-4)<br />
  * Skin = Default<br />
  * Click on "Create AVD"<br />
### Checking out the Code ###
#### Manually checkout source codes (If you would like to use subclipse, skip to next step.) ####
    * Make sure you have SVN command tools installed.<br />
    * Go to the [project site](http://code.google.com/p/omnidroid/source/checkout) and checkout the source code.<br />
    * In Eclipse: File -> New -> Other -> Android Project<br />
    * Select "Create project from existing code", with the local svn folder you just checked out.<br />
> > > Project name = Whatever you like<br />
> > > Build Target = "Android 1.6 (android-4)"<br />
> > > Application name = Whatever you like<br />
> > > Min SDK Version = 4<br />
#### checkout source code with subclipse (This is not necessary if you use svn command manually) ####
    * Install [subclipse](http://subclipse.tigris.org/)<br />
    * In Eclipse: Window -> Show View -> other-> SVN Repositories<br />
    * Click on the "+" sign<br />
    * url: http://omnidroid.googlecode.com/svn -> Finish<br />
    * Right click url -> Checkout.... Use the defaults and click Finish.<br />
### Run the application ###
#### In Eclipse ####
    * Project -> Build All
    * Run -> Android Application
#### In Emulator: Launch omnidroid ####
    * Start using the app!
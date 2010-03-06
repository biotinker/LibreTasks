package edu.nyu.cs.omnidroid.core;

import android.content.Intent;

/**
 * This is a test event for simple "hello world" app
 */
public class HelloWorldEvent extends Event {
	public static final String APPLICATION_NAME = ".HelloAndroid";
	//The name showed from root event
	public static final String EVENT_NAME = "HelloWorld is called";
	public static final String ACTION_NAME = ".HelloAndroid";

	public HelloWorldEvent(Intent intent) {
		super(APPLICATION_NAME, EVENT_NAME, intent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAttribute(String attributeName)
	throws IllegalArgumentException {
		//TODO: in this test case, it's hard coded.
		if (attributeName.equals("NAME")) {
			return "HELLO OMNIDROID.";
		} else {
			throw new IllegalArgumentException("No attrbute named " + attributeName);
		}
	}

}

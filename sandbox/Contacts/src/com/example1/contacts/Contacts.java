package com.example1.contacts;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.widget.Toast;

public class Contacts extends Activity {
	@Override

	public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setContentView(R.layout.main);

	displayRecords();

	}

	private void displayRecords() {

	

	String columns[] = new String[] { People.NAME, People.NUMBER };

	Uri mContacts = People.CONTENT_URI;

	Cursor cur = managedQuery(mContacts, columns, 

	null, 

	null, 

	null 

	);

	if (cur.moveToFirst()) {

	String name = null;

	String phoneNo = null;

	do {

	

	name = cur.getString(cur.getColumnIndex(People.NAME));

	phoneNo = cur.getString(cur.getColumnIndex(People.NUMBER));

	Toast.makeText(this, name + "" + phoneNo, Toast.LENGTH_LONG).show();

	} while (cur.moveToNext());

	}

	}
}
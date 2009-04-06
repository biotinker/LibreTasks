package com.example1.contacts;

import java.net.URI;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Toast;

public class Contacts extends Activity {
	@Override

	public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setContentView(R.layout.main);

	displayRecords();

	}

	private void displayRecords() {

	


	
// put the uri in the content:// format in the managedQuery
	Cursor cur = managedQuery(Uri.parse("content://call_log/calls"), null, 

	null, 

	null, 

	null 

	);

	if (cur.moveToFirst()) {

		
	do {

			String[] cols = cur.getColumnNames(); 
		//for(int i = 0; i < cols.length; i++) { Log.d(TAG, "Column: " + cols[i]); } while(c.moveToNext()) { Log.d(TAG, c.getString(c.getColumnIndex("address")) + ":" + c.getString(c.getColumnIndex("person")) + ":" +c.getString(c.getColumnIndex("date")) + ":" +c.getString(c.getColumnIndex("body"))); } 
		for(int i = 0; i < cols.length; i++)
		{
		Toast.makeText(this, cols[i], Toast.LENGTH_LONG).show();
		}

	} while (cur.moveToNext());

	}

	}
}